package mixology

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Environment
import groovy.sql.Sql
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.security.SecureRandom
import java.sql.SQLException
import java.sql.Timestamp
import java.time.LocalDateTime

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

class EmailsController extends BaseController {

    private static Logger logger = LogManager.getLogger(EmailsController.class)

    def userService
    def springSecurityService
    def mailService

    static allowedMethods = [shareDrinkEmail: "POST",
                             forgotPasswordEmail: "POST"]

    def index() { }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def shareDrinkEmail() {
        logger.info("shareDrinkEmail start...")
        if (!params) {
            flash.message = 'No request parameters found!'
            redirect(controller: 'drink', action:'show', params:[id:drink.id], method: "GET", status: INTERNAL_SERVER_ERROR)
        }
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)


        User userExists = userService.getByUsername(params.recipientEmail as String)
        Drink drink = Drink.findById(params.drinkId as Long)
        def imageAsString = convertImageToString(drink.glassImage)

        def message = null
        try {
            message = mailService.sendMail {
                to params.recipientEmail as String
                from "mixology@noreply.com"
                subject "Test Email"
                body view:"/emails/drinkEmail",
                        model:[user:user,
                               userExists:userExists?true:false,
                               rName:params.recipientName as String,
                               rEmail:params.recipientEmail as String,
                               drink:drink,
                               image:imageAsString,
                               testing:true]
            }
        } catch (Exception e) {
            logger.error("An exception occurred while sending the email", e)
            logger.error("Exception: ${e.message}")
            flash.message = e.message
            redirect(controller: 'drink', action:'show', params:[id:drink.id], method: "GET", status: INTERNAL_SERVER_ERROR)
        }
        logger.info("shareDrinkEmail concluded")
        if (message) {
            flash.message = 'Email was sent!'
            redirect(controller: 'drink', action: 'show', params: [id:drink.id], method: 'GET', status: OK)
        }
    }

    String convertImageToString(String glassImage) {
        String encodedString = ''
        if (!glassImage) return encodedString
        encodedString = Base64.getEncoder().encodeToString(new File("grails-app/assets/images/$glassImage").getBytes() as byte[])
        encodedString
    }

    def forgotPasswordEmail() {
        logger.info("forgotPasswordEmail start...")
        if (!params) {
            flash.message = 'No request parameters found!'
            redirect uri:'/'
        }
        def user = userService.getByUsername("${params.username}" as String)
        if (user) {
            try {
                def token = generateToken("${params.username}")
                def message = mailService.sendMail {
                    to params.username as String
                    from "mixology@noreply.com"
                    subject "Reset Password"
                    body view:"/emails/resetPasswordEmail",
                            model:[user:user,
                                   token: token,
                                   testing:Environment.isDevelopmentMode() ?: false]
                }
                if (message) {
                    // add new entry to userPasswordReset table
                    def dataSource = grailsApplication.mainContext.dataSource
                    def db = new Sql(dataSource)
                    Timestamp datetime = Timestamp.valueOf(LocalDateTime.now().plusMinutes(10))
                    try {
                        def result = db.executeInsert("insert into userPasswordReset values ('${params.username}', '${token}', '${datetime}');")
                        if (result.size() != 0) println "Entry for ${user.username} successful"
                    } catch (SQLException sqle) {
                        try {
                            def result = db.executeUpdate("replace into userPasswordReset values ('${params.username}', '${token}', '${datetime}');")
                            if (result == 1) println "Updated entry for ${user.username} successful"
                        } catch (Exception e) {
                            logger.error("There was a problem with re-entering password reset")
                        }
                    }

                }
            } catch (Exception e) {
                logger.error("An exception occurred while sending the email", e)
                logger.error("Exception: ${e.message}")
                flash.message = e.message
            }
        }
        logger.info("resetPasswordEmail concluded")
        flash.message = 'If an account was found using the provided email, ' +
                'you will receive an email with a link allowing you to reset your password.'
        redirect controller:'login', action:'forgotPassword'
    }

    private String generateToken(String username) {
        def hash = new BCryptPasswordEncoder(10, new SecureRandom()).encode(username)
        println "$hash"
        return hash
    }
}
