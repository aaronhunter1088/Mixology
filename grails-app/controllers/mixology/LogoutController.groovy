package mixology

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LogoutController {

    private static Logger logger = LogManager.getLogger(LogoutController.class)

    def index = {
        logger.info("${principal.fullname} is now logged out!")
        session.invalidate()
        redirect(uri:'/')
    }
}
