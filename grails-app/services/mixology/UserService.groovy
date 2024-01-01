package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import javax.transaction.Transactional

@Service(User)
class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class)

    User get(Long id) {
        User user = User.findById(id)
        logger.info("get($id) ==> ${user?'found':'not found'}")
        user
    }

    User getByUsername(String username) {
        User user = User.findWhere(username: username)
        logger.info("getByUsername($username) ==> ${user?'found':'not found'}")
        user
    }

    List<User> list(User user = null) {
        List<User> users = user ? User.findAll("from User where id=:id", [id:user.id]) : User.findAll()
        users
    }

    Long count() {
        User.all.size()
    }

    User save(User user, boolean validate = false) {
        if (!user) return null
        try {
            User.withNewTransaction {
                user.save(validate:validate, flush:true, failOnError:true)
                logger.info("user saved")
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
            logger.info("User '${user.firstName}' deleted!")
        }
    }

}