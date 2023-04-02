package mixology

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.apache.commons.io.FileUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest

import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.security.SecureRandom

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class UserController {

    RoleService roleService
    UserService userService
    UserRoleService userRoleService

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        respond userService.list(params), model:[userCount: userService.count()]
    }

    @Secured(['ROLE_ADMIN, ROLE_USER'])
    def show(Long id) {
        respond userService.get(id)
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
        MultipartRequest multipartRequest =  request as MultipartRequest
        MultipartFile file = multipartRequest.getFile('photo')
        def role = roleService.findByAuthority(enums.Role.USER.name)
        user = createUserFromParams(user, params, file)
        try {
            user.validate()
            if (!user.errors.hasErrors()) {
                user = userService.save(user)
                def userRole = UserRole.create user, role
                userRoleService.save(userRole)
            }
        } catch (ValidationException e) {
            println "exception ${e.getMessage()}"
            respond user.errors, view:'create'
            return
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

    def update(User user) {
        if (!user) {
            notFound()
        }
        MultipartRequest multipartRequest =  request as MultipartRequest
        MultipartFile file = multipartRequest.getFile('photo')
        //byte[] fileContent = FileUtils.readFileToByteArray(new File(file.toString()))
        String encodedString = ''
        if (file) encodedString = Base64.getEncoder().encodeToString(file.getBytes())
        try {
            user.firstName = params.firstName
            user.lastName = params.lastName
            user.email = params.email
            user.password = params.password
            user.passwordConfirm = params.passwordConfirm
            //String reduced = reduceImageSize(file.toString())
            if (file) user.photo = encodedString as String
            user.org_grails_datastore_gorm_GormValidateable__errors = null
            userService.save(user)
        } catch (ValidationException e) {
            println "exception ${e.getMessage()}"
            respond user.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                respond user, view:'show'
            }
            '*'{ respond user, [status: OK] }
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

//    def uploadImage() {
//        imageFile = request.getFile('uploadImage')
//        flash.message = 'File uploaded!'
//    }

}
