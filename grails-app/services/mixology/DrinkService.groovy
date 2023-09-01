package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.transaction.Transactional

@Service(Drink)
class DrinkService {

    private static Logger logger = LogManager.getLogger(DrinkService.class)

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
     * to the drink. It returns the Drink
     * @param drink
     * @param validate
     * @return
     */
    @Transactional
    Drink save(Drink drink, boolean validate = false) {
        if (validate) { if (drink.validate()) { drink.save(flush:true, failOnError:true) } }
        else drink.save(validate:false, flush:true, failOnError:false)
        def ingredients = drink.ingredients
        ingredients.each {
            it.addToDrinks(drink)
        }
        drink
    }

    /**
     * This save method is used for saving a real Drink.
     * This saves the Drink onto the User first, then saves
     * the user. Then it saves the Drink itself. It returns
     * the drink.
     * A drink, other than a default drink, will always belong
     * to a user, of type Role Admin or Role User. Therefore,
     * a user is required when saving a Drink.
     * @param drink
     * @param user
     * @param validate
     * @return
     */
    Drink save(Drink drink, User user, boolean validate = false) {
        if (!drink || !user) return null
        if (validate) {
            if (drink.validate()) {
                try {
                    Drink.withTransaction {
                        drink.save(flush:true)
                        user.addToDrinks(drink)
                        user.save(flush:true, failOnError:false, validate:false)
                    }
                } catch (Exception e) {
                    logger.error("Could not save drink")
                    return null
                }
            }
            else return null
        } else {
            try {
                Drink.withTransaction {
                    drink.save(flush:true)
                    user.addToDrinks(drink)
                    user.save(flush:true, failOnError:false, validate:false)
                }
            } catch (Exception e) {
                logger.error("Could not save drink")
                return null
            }
        }
        drink
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
        List<Ingredient> ingredients = drink.ingredients as List<Ingredient>
        ingredients.each { ingredient ->
            drink.removeFromIngredients(ingredient)
            ingredient.removeFromDrinks(drink)
        }
        Drink.withNewTransaction {
            drink.delete(flush: true)
        }
    }
}
