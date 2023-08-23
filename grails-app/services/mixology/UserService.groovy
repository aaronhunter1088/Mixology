package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

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

    @Transactional
    User saveIngredientToUser(User user, boolean validate = false) {
        user.save(validate:validate, flush:true, failOnError:true)
        user
    }

    @Transactional
    User saveIngredientToUser(User user, Ingredient ingredient) {
        try {
            User.withTransaction {
                user.addToIngredients(ingredient)
                user.save(flush:true, validate:false)
            }
            logger.info ("User updated to have ingredient")
        }
        catch (Exception e) {
            logger.error ("Could not update user with ingredient")
        }
        user
    }

    @Transactional
    void delete(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient) ingredient.delete(flush:true)
    }

}