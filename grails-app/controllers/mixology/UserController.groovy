package mixology

import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import static org.springframework.http.HttpStatus.*

class UserController extends BaseController {

    private static Logger logger = LogManager.getLogger(UserController.class)

    RoleService roleService
    UserService userService
    UserRoleService userRoleService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Override
    void badRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: BAD_REQUEST
            }
            '*'{ render status: BAD_REQUEST }
        }
    }
    @Override
    void notFound(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: message(code: 'default.not.found.message', args: [message(code: 'drink.label', default: 'Drink'), params.id])
                redirect action: "index", method: method ?: "GET", status: NOT_FOUND
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    @Override
    void okRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'OK 200'
                redirect action: "index", method: method ?: "create", status: OK
            }
            '*'{ render status: OK }
        }
    }
    @Override
    void createdRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: CREATED
            }
            '*'{ render status: CREATED }
        }
    }
    @Override
    void noContentRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No content'
                redirect action: "index", method: method ?: "create", status: NO_CONTENT
            }
            '*'{ render status: NO_CONTENT }
        }
    }
    @Override
    void unauthorized(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'You are not authorized for the previous request'
                redirect action: "index", method:method, status: UNAUTHORIZED
            }
            '*'{ render status: UNAUTHORIZED }
        }
    }
    @Override
    void methodNotAllowed(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'Check your request method!'
                redirect action: "index", method: method ?: "create", status: METHOD_NOT_ALLOWED
            }
            '*'{ render status: METHOD_NOT_ALLOWED }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        respond userService.list(params), model:[userCount: userService.count()]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def show(Long id) {
        def user = userService.get(id)
        respond user
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def create() {
        User user = new User(params)
        respond user
    }

    def save(User user) {
        if (!user) {
            badRequest('','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed('','')
            return
        }
        if (!Boolean.valueOf(params.passwordsMatch)) {
            user.clearErrors()
            user.errors.reject('default.invalid.user.password.instance',
                    [params.password, params.passwordConfirm] as Object[],
                    '[Password and PasswordConfirm do not match]')
            println "Password and PasswordConfirm do not match"
            badRequest('create', 'Passwords do not match')
            return
        } else {
            MultipartRequest multipartRequest = request as MultipartRequest
            MultipartFile file = multipartRequest.getFile('photo')
            def userRole = roleService.findByAuthority(enums.Role.USER.name)
            user = createUserFromParams(user, params, file)
            user.validate()
            if (!user.errors.hasErrors()) {
                user = userService.save(user, true)
                userRoleService.save(user, userRole, true)
                request.withFormat {
                    form multipartForm {
                        flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                        respond user, view:'show', status:OK
                    }
                    '*'{ respond user, [status: OK] }
                }
            } else {
                request.withFormat {
                    form multipartForm {
                        respond user.errors, view:'create', status:BAD_REQUEST
                    }
                    '*'{ respond user.errors, [status:BAD_REQUEST] }
                }
            }
        }
    }

    def createUserFromParams(user, params, file) {
        String reduced = reduceImageSize(file)
        user = new User([
                firstName: params.firstName,
                lastName: params.lastName,
                username: params.email,
                email: params.email,
                password: params.password,
                mobileNumber: params.cellphone,
                photo: reduced ?: ''
        ])
        user
    }

    def reduceImageSize(file) {
        int size = 200// size of the new image.
        //take the file as inputstream.
        String encodedString = ''
        if (!file?.filename) return encodedString
        encodedString = Base64.getEncoder().encodeToString(file.getBytes() as byte[])
        InputStream imageStream = new ByteArrayInputStream(file.getBytes() as byte[])
        //read the image as a BufferedImage.
        BufferedImage image = ImageIO.read(imageStream)
        //cal the scaleImage method.
        BufferedImage newImage = scaleImage(image, size)
        File outputfile = new File("image.jpg")
        ImageIO.write(newImage, "png", outputfile)
        byte[] fileContent = FileUtils.readFileToByteArray(outputfile)
        encodedString = Base64.getEncoder().encodeToString(fileContent)
        // remove file before returning
        outputfile.delete();
        encodedString
    }

    private BufferedImage scaleImage(BufferedImage bufferedImage, int size) {
        double boundSize = size
        int origWidth = bufferedImage.getWidth()
        int origHeight = bufferedImage.getHeight()
        double scale
        if (origHeight > origWidth)
            scale = boundSize / origHeight
        else
            scale = boundSize / origWidth
        //* Don't scale up small images.
        if (scale > 1.0)
            return (bufferedImage)
        int scaledWidth = (int) (scale * origWidth)
        int scaledHeight = (int) (scale * origHeight)
        Image scaledImage = bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB)
        Graphics2D g = scaledBI.createGraphics()
        g.drawImage(scaledImage, 0, 0, null)
        g.dispose()
        return (scaledBI)
    }

    def edit(Long id) {
        User user = userService.get(id)
        respond user
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def update(User user) {
        if (!user) {
            badRequest(null, 'No user passed in')
            return
        }
        MultipartRequest multipartRequest =  request as MultipartRequest
        MultipartFile file = multipartRequest.getFile('photo')
        String encodedString = null
        // if file is present and they cleared the current image
        // only encode if file is present. will set to empty string
        // if photo was cleared.
        if (file && Boolean.valueOf(params.clearedImage as String)) encodedString = Base64.getEncoder().encodeToString(file.getBytes())
        user.firstName = params?.firstName ?: user.firstName
        user.lastName = params?.lastName ?: user.lastName
        user.email = params?.email ?: user.email
        params?.drinks?.each {
            Drink drink = Drink.read(it as Long)
            user.drinks.add(drink)
        }
        // only update password if both values are set
        if (params?.passwordConfirm && params?.password == params?.passwordConfirm) {
            user.password = params.password
            user.passwordConfirm = params.passwordConfirm
        }
        // update photo if photo was cleared. photo may not exist anymore
        // and so user photo may be set to empty string
        if (Boolean.valueOf(params.clearedImage as String)) user.photo = encodedString
        user.org_grails_datastore_gorm_GormValidateable__errors = null
        userService.save(user, false)
        logger.info("user saved!")
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                redirect (id:user.id, action:'show', status:OK)
            }
            '*'{ redirect (id:user.id, action:'show', status:OK) }
        }
    }

}
