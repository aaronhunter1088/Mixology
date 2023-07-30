package mixology

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import validators.PasswordValidator

import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.security.SecureRandom

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class)

    RoleService roleService
    UserService userService
    UserRoleService userRoleService

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

    def create() {
        //render view:'create'
        User user = new User(params)
        respond user
    }

    def save(User user) {
        if (!user) {
            notFound()
            return
        }
        if (!Boolean.valueOf(params.passwordsMatch)) {
            user.clearErrors()
            user.errors.reject('default.invalid.user.password.instance',
                    [params.password, params.passwordConfirm] as Object[],
                    '[Password and PasswordConfirm do not match]')
            println "Password and PasswordConfirm do not match"
            request.withFormat {
                form multipartForm {
                      respond user.errors, view:'create'
                }
                '*'{ respond user.errors, view:'create' }
            }
            return
        } else {
            MultipartRequest multipartRequest = request as MultipartRequest
            MultipartFile file = multipartRequest.getFile('photo')
            def userRole = roleService.findByAuthority(enums.Role.USER.name)
            user = createUserFromParams(user, params, file)
            try {
                user.validate()
                if (!user.errors.hasErrors()) {
                    user = userService.save(user)
                    def roleOfUser = UserRole.create user, userRole
                    userRoleService.save(roleOfUser)
                }
            } catch (ValidationException e) {
                println "exception ${e.getMessage()}"
                respond user.errors, view:'create'
                return
            }
        }

        if (user.errors.hasErrors()) {
            request.withFormat {
                form multipartForm {
                    respond user.errors, view:'create'
                }
                '*'{ respond user.errors, [status: 400] }
            }
        }
        else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                    respond user, view:'show'
                }
                '*'{ respond user, [status: OK] }
            }
        }
    }

    def createUserFromParams(user, params, file) {
        if (params.password != params.passwordConfirm) {
            user.errors.reject('default.invalid.user.password.instance',
                    [params.password, params.passwordConfirm] as Object[],
                    '[Password and PasswordConfirm do not match]')
            println "Password and PasswordConfirm do not match"
            //respond user.errors, view: 'create'
            return user
        }
//        byte[] fileContent = FileUtils.readFileToByteArray(new File(params.photo as String))
//        String encodedString = Base64.getEncoder().encodeToString(fileContent)
        try {
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
            return user
        } catch (Exception e) {
            println "Exception creating new user ${e.getMessage()}"
            notFound()
        }
    }

//    def createUsername(params) {
//        int count = User.withCriteria {
//            eq('username', params.firstName.toString()[0].toLowerCase() + params.lastName.toString().toLowerCase())
//        }.collect().size()
//        if (count == 0) params.firstName.toString()[0].toLowerCase() + params.lastName.toString().toLowerCase()
//        else {
//            params.firstName.toString()[0].toLowerCase() +
//                    params.lastName.toString().toLowerCase() + '_' + params.mobileNumber
//        }
//    }

    def reduceImageSize(file) {
        int size = 200// size of the new image.
        //take the file as inputstream.
        //MultipartFile file = request.getFile('photo')
        String encodedString = ''
        if (!file.filename) return encodedString
        encodedString = Base64.getEncoder().encodeToString(file.getBytes() as byte[])
        //byte[] fileContent = FileUtils.readFileToByteArray(photo)
        //String encodedString = Base64.getEncoder().encodeToString(fileContent)
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
        return encodedString
        //String path = getServletContext().getRealPath("/image");
        //write file.
        //File file = new File(path, "testimage.jpg");
        //ImageIO.write(newImage, "JPG", file);
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
        // new ImageIcon(image); // load image
        // scaledWidth = scaledImage.getWidth(null);
        // scaledHeight = scaledImage.getHeight(null);
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB)
        Graphics2D g = scaledBI.createGraphics()
        //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR)
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
            notFound()
        }
        MultipartRequest multipartRequest =  request as MultipartRequest
        MultipartFile file = multipartRequest.getFile('photo')
        String encodedString = null
        // if file is present and they cleared the current image
        // only encode if file is present. will set to empty string
        // if photo was cleared.
        if (file && Boolean.valueOf(params.clearedImage as String)) encodedString = Base64.getEncoder().encodeToString(file.getBytes())
        try {
            user.firstName = params.firstName
            user.lastName = params.lastName
            user.email = params.email
            params.drinks.each {
                Drink drink = Drink.read(it as Long)
                user.drinks.add(drink)
            }
            // only update password if both values are set
            if (params.passwordConfirm && params.password == params.passwordConfirm) {
                user.password = params.password
                user.passwordConfirm = params.passwordConfirm
            }
            // update photo if photo was cleared. photo may not exist anymore
            // and so user photo may be set to empty string
            if (Boolean.valueOf(params.clearedImage as String)) user.photo = encodedString
            user.org_grails_datastore_gorm_GormValidateable__errors = null
            User.withTransaction {
                user.save(validate:false,flush:true)
                logger.info("user saved!")
            }
        } catch (ValidationException e) {
            println "exception ${e.getMessage()}"
            respond user.errors, view:'edit'
            return
        }
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                redirect (id:user.id, action:'show')
            }
            '*'{ redirect (id:user.id, action:'show') }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "create"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    protected void badRequest() {
        request.withFormat {
            form multipartForm {
                flash.message = 'No request parameters found!'
                redirect action: "index", method: "create", status: BAD_REQUEST
            }
            '*'{ render status: BAD_REQUEST }
        }
    }

}
