package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.transaction.Transactional

@Service(Drink)
class DrinkService {

    private static Logger logger = LogManager.getLogger(DrinkService.class)
    def springSecurityService

    /**
     * This method takes in an ID of type Long
     * and finds a Drink object. If one is found,
     * it returns it. Otherwise it will return null
     * @param id
     * @return
     */
    Drink get(Long id) {
        Drink drink = Drink.findById(id) ?: null
        drink
    }

    /**
     * Returns all the Drinks as a List
     * @param args
     * @return
     */
    List<Drink> list(Map args) {
        def returnList = Drink.list(args)
        returnList
    }

    /**
     * Returns the total count of all drinks
     * @return
     */
    Long count() {
        Drink.all.size()
    }

    /**
     * This save method is used for testing method purposes only!
     * It will save the Drink and then attach each ingredient
     * to the drink. It returns the Drink regardless of exceptions
     * @param drink
     * @param validate
     * @return
     */
    Drink save(Drink drink, boolean validate = false) {
        try {
            drink.save(validate:validate, flush:true, failOnError:validate)
        } catch (Exception e) {
            logger.error("Could not save drink:: $drink", e)
        }
        drink
    }

    /**
     * This save method is used for saving a real Drink.
     * This saves the Drink itself. Then it adds the Drink
     * to the Users drinks, then saves the user.It returns
     * the drink.
     * A drink, other than a default drink, will always belong
     * to a user.
     * Therefore, a user is required when saving a Drink.
     * @param drink
     * @param user
     * @param validate
     * @return
     */
    Drink save(Drink drink, User user, boolean validate = false) {
        if (!drink || !user) null
        try {
            Drink.withNewTransaction {
                drink.save(validate:validate, flush:true, failOnError:validate)
                // user is not validated here. we are saving drink (mainly)
                user.addToDrinks(drink).save(flush:true, failOnError:false, validate:false)
            }
            logger.info("Drink saved!")
            drink
        } catch (Exception e) {
            logger.error("Could not save drink:: $drink", e)
            null
        }
    }

    /**
     * This delete method takes in an ID of type Long.
     * It uses it to find a Drink object. If a Drink is found,
     * then the drink is detached from each ingredient.
     * Likewise, each ingredient is detached from the Drink.
     * This method does not return anything.
     * @param id
     */
    void delete(Long id) {
        Drink drink = get(id)
        if (!drink) {
            logger.error("Could not delete drink:: $drink")
        }
        else {
            def user = getCurrentUser()
            if (!user?.drinks?.contains(drink)) {
                logger.warn("A user, id:: ${user.id} is deleting a drink they did not create.")
                logger.warn("Drink belongs to user, id:: ${drink?.user?.id}")
            }
            try {
                def dIngredients = drink.ingredients as List<Ingredient>
                dIngredients.each { ingredient ->
                    drink.removeFromIngredients(ingredient)
                    ingredient.removeFromDrinks(drink)
                }
                if (dIngredients.size() > 0) {
                    dIngredients.eachWithIndex{ Ingredient ingredient, int i ->
                        logger.warn("Deleting this drink will affect this ingredient, id:: ${ingredient.id}")
                    }
                }
                Drink.withNewTransaction {
                    drink.delete(flush: true)
                }
                logger.info("Drink '${drink.name}' deleted!")
            } catch (Exception e) {
                logger.error("Could not delete drink:: $drink", e)
            }
        }
    }

    private User getCurrentUser() {
        User.findByUsername(springSecurityService.getPrincipal().username as String)
    }
}
