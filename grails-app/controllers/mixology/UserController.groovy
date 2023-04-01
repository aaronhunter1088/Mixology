package mixology

import grails.converters.JSON
import grails.validation.ValidationException
import org.apache.commons.fileupload.FileItem
import org.apache.commons.io.FileUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.imageio.ImageIO
import javax.swing.ImageIcon
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class UserController {

    UserService userService

//    def imageFile

    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        respond userService.list(params), model:[userCount: userService.count()]
    }

    def show(Long id) {
        respond userService.get(id)
    }

    def create() {
        //render view:'create'
        User user = new User(params)
        respond user
    }

    def save(params) {
        if (!params) {
            notFound()
            return
        }
        User user = createUserFromParams(params)
        try {
            userService.save(user)
        } catch (ValidationException e) {
            println "exception ${e.getMessage()}"
            respond user.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.saved.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                respond user, view:'show'
            }
            '*'{ respond user, [status: OK] }
        }
    }

    def createUserFromParams(params) {
//        byte[] fileContent = FileUtils.readFileToByteArray(new File(params.photo as String))
//        String encodedString = Base64.getEncoder().encodeToString(fileContent)
        try {
            String reduced = reduceImageSize(params.photo as String)
            return new User([
                    firstName: params.firstName,
                    lastName: params.lastName,
                    email: params.email,
                    mobileNumber: params.cellphone,
                    photo: reduced ?: ''
            ])
        } catch (Exception e) {
            println "Exception creating new user ${e.getMessage()}"
            notFound()
        }
    }

    def reduceImageSize(photo) {
        if (!photo) return ''
        int size = 200// size of the new image.
        //take the file as inputstream.
        //byte[] fileContent = FileUtils.readFileToByteArray(photo)
        //String encodedString = Base64.getEncoder().encodeToString(fileContent)
        InputStream imageStream = new FileInputStream(photo)
        //read the image as a BufferedImage.
        BufferedImage image = ImageIO.read(imageStream)
        //cal the scaleImage method.
        BufferedImage newImage = scaleImage(image, size)
        byte[] fileContent = FileUtils.readFileToByteArray(new File(newImage.toString()))
        String encodedString = Base64.getEncoder().encodeToString(fileContent)

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
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR)
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
        MultipartFile file = request.getFile('photo')
        //byte[] fileContent = FileUtils.readFileToByteArray(new File(file.toString()))
        String encodedString = Base64.getEncoder().encodeToString(file.getBytes())
        try {
            user.firstName = params.firstName
            user.lastName = params.lastName
            user.email = params.email
            user.password = params.password
            user.passwordConfirm = params.passwordConfirm
            //String reduced = reduceImageSize(file.toString())
            user.photo = encodedString as String
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
