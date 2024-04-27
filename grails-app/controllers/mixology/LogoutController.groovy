package mixology

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LogoutController {

    private static Logger logger = LogManager.getLogger(LogoutController.class)

    def springSecurityService

    def index = {
        User user = User.findByUsername(springSecurityService.principal.username as String)
        logger.info("${user.firstName} ${user.lastName} is now logged out!")
        session.invalidate()
        request.logout()
        redirect(uri:'/')
    }
}
