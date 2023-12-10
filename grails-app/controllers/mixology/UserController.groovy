package mixology


import grails.converters.JSON
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

    def roleService
    def userService
    def userRoleService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN','IS_AUTHENTICATED_FULLY'])
    def index() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def args = [
                max: params.max ?: 5,
                offset: params.offset ?: 0,
                sort: params.sort ?: 'id',
                order: params.order ?: 'asc'
        ]
        def criteria = User.createCriteria()
        def users = criteria.list(args, {
            if (params.id) {
                eq('id', params.id as Long)
            } else {
                if (params.name) {
                    eq ('firstName', params.firstName as String)
                    or {
                        eq ( 'lastName', params.lastName as String)
                    }
                }
                if (params.username) eq ( 'username', params.username as String)
                if (params.email) eq ( 'email', params.email as String)
            }
        })
        withFormat {
            html {
                render view:'index',
                       model:[userList:users,
                              userCount:users.totalCount,
                              user:user as User,
                              params:params
                       ]
            }
            json { users as JSON }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def show(Long id) {
        def userToDisplay = userService.get(id)
        def currentUser = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        boolean showPassword = currentUser.id == id
        withFormat{
            html {
                if (!userToDisplay) render view:'/notFound', model:[object:'User']
                else {
                    render view:'show',
                            model:[user:userToDisplay,
                                   currentUser:currentUser,
                                   showPassword:showPassword
                            ]
                }
            }
            json {
                if (userToDisplay) render (userToDisplay as JSON)
                else {
                    render(status:204, text:'No user found')
                }
            }
        }
    }

    def create() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        User newUser = new User(params)
        render view:'create', model:[newUser:newUser,currentUser:user]
    }

    def save(User user) {
        if (!user) {
            badRequest(flash, request, '','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed(request,'','')
            return
        }
        if (!Boolean.valueOf(params.passwordsMatch)) {
            user.clearErrors()
            user.errors.reject('default.invalid.user.password.instance',
                    [params.password, params.passwordConfirm] as Object[],
                    '[Password and PasswordConfirm do not match]')
            logger.error("Password and PasswordConfirm do not match")
            badRequest(flash, request,'create', 'Passwords do not match')
            return
        } else {
            MultipartRequest multipartRequest = request as MultipartRequest
            MultipartFile file = multipartRequest.getFile('photo')
            def userRole = roleService.findByAuthority(enums.Role.USER.name)
            user = createUserFromParams(user, params, file)
            if (user.validate()) {
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

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def edit(Long id) {
        User user = userService.get(id)
        render view:'edit',
               model:[user:user,
                   darkMode:user.darkMode
               ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def update() {
        User user
        if (!params) {
            badRequest(flash, request, null, 'No user passed in')
            return
        } else {
            user = userService.get(params?.id as Long)
        }
        if (!user) {
            badRequest(flash, request, '','')
            return
        }
        MultipartRequest multipartRequest =  request as MultipartRequest
        MultipartFile file
        try { file = multipartRequest?.getFile('photo') }
        catch (MissingMethodException mme) { logger.error('Request is not MultipartRequest. Fine. No file') }
        String reduced
        // if file is present and they cleared the current image
        // only encode if file is present. will set to empty string
        // if photo was cleared.
        if (file) {
            reduced = reduceImageSize(file)
            user.photo = reduced
        } else if  (Boolean.valueOf(params.clearedImage as String)){
            user.photo = ''
        }

        user.firstName = params?.firstName ?: user.firstName
        user.lastName = params?.lastName ?: user.lastName
        if (params?.cellphone) {
            def cellphone = params.cellphone as String
            cellphone = cellphone.trim().replaceAll('\\D','')
            user.mobileNumber = cellphone
        }
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
        user.language = params?.language ?: 'en'
        // update photo if photo was cleared. photo may not exist anymore
        // and so user photo may be set to empty string
        user.clearErrors()
        userService.save(user, false)
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.toString()])
                redirect (id:user.id, action:'show', status:OK)
            }
            '*'{ redirect (id:user.id, action:'show', status:OK) }
        }
    }

    def forgotPassword = {
        logger.info("Implement method User.forgotPassword")
        redirect(uri:'/')
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def darkMode() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        user.darkMode = !user.darkMode
        userService.save(user, false)
        logger.info("user.enableDarkMode from (${!user.darkMode}) to (${user.darkMode})")
        redirect (uri:"${params.uri}")
    }

    def createUserFromParams(user, params, file) {
        String reduced = reduceImageSize(file)
        user = new User([
                firstName: params.firstName,
                lastName: params.lastName,
                username: params.email,
                email: params.email,
                password: params.password,
                passwordConfirm: params.passwordConfirm,
                mobileNumber: params.cellphone,
                photo: reduced ?: '',
                language: params.chosenLanguage ?: 'en'
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

    def scaleImage(bufferedImage, size) {
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

}
