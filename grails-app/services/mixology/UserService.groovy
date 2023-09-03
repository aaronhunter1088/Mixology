package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.core.parameters.P

import javax.transaction.Transactional
import java.beans.Transient

@Service(User)
class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class)
    def springSecurityService

    User get(Long id) {
        User user = User.findById(id as Long)
        user
    }

    List<User> list(Map args) {
        User.list(args)
    }

    Long count() {
        User.all.size()
    }

    User save(User user, boolean validate = false) {
        if (!user) return null
        try {
            User.withNewTransaction {
                user.save(validate:validate, flush:true, failOnError:true)
            }
        } catch (Exception e) {
            logger.error("Failed to save user:: $user", e)
        }
        user
    }

    @Transactional
    void delete(Long id) {
        User user = User.findById(id)
        if (!user) {
            logger.error("Could not delete user:: $user")
        } else {
            User.withTransaction {
                user.delete(flush:true)
            }
            logger.info("User '${user.name}' deleted!")
        }
    }

}