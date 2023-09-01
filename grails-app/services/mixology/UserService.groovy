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

    boolean update(User user) {

    }

    User saveUser(User user, boolean validate = false) {
        if (!user) return null
        if (validate) {
            if (user.validate()) {
                User.withNewTransaction {
                    try {
                        user.save(flush:true, failOnError:true)
                    } catch (Exception e) {
                        logger.error("Failed to save user:: $user", e)
                    }
                }
            }
            else return null
        } else {
            try {
                user.save(flush:true, failOnError:false)
            } catch (Exception e) {
                logger.error("Failed to save user:: $user", e)
            }
        }
        user
    }

    @Transactional
    void delete(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient) ingredient.delete(flush:true)
    }

}