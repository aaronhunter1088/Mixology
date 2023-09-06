package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.transaction.Transactional

@Service(Ingredient)
class IngredientService {

    private static Logger logger = LogManager.getLogger(IngredientService.class)
    def springSecurityService

    /**
     * This method takes in an ID of type Long
     * and finds an Ingredient object. If one is found,
     * it returns it. Otherwise it will return null
     * @param id
     * @return
     */
    Ingredient get(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        ingredient
    }

    /**
     * Returns all the Ingredients as a List
     * @param args
     * @return
     */
    List<Ingredient> list(Map args) {
        def returnList = Ingredient.list(args)
        returnList
    }

    /**
     * Returns all total count of all ingredients
     * @return
     */
    Long count() {
        Ingredient.all.size()
    }

    /**
     * This save method is used for testing method purposes only!
     * It will save the Ingredient. It returns the ingredient
     * @param ingredient
     * @param validate
     * @return
     */
    Ingredient save(Ingredient ingredient, boolean validate = false) {
        Ingredient.withNewTransaction {
            try {
                ingredient.save(validate: validate, flush: true, failOnError: validate)
            } catch (Exception e) {
                logger.error("Could not save ingredient:: $ingredient", e)
            }
        }
        ingredient
    }

    /**
     * This save method is used for saving a real Ingredient.
     * This saves the Ingredient onto the User first, then saves
     * the user. Then it saves the Ingredient itself. It returns
     * the ingredient.
     * An Ingredient, other than a default ingredient, will always
     * belong to a user, of type Role Admin or Role User. Therefore,
     * a user is required when saving an Ingredient.
     * @param ingredient
     * @param user
     * @param validate
     * @return
     */
    Ingredient save(Ingredient ingredient, User user, boolean validate = false) {
        if (!ingredient || !user) null
        try {
            Ingredient.withNewTransaction {
                ingredient.save(validate:validate, flush:true, failOnError:validate)
                // user is not validated here. we are saving drink (mainly)
                user.addToIngredients(ingredient).save(flush: true, failOnError: false, validate: false)
            }
            logger.info("Ingredient saved!")
            ingredient
        } catch (Exception e) {
            logger.error("Could not save ingredient:: $ingredient", e)
            null
        }
    }

    /**
     * This delete method takes in an ID of type Long.
     * It uses it to find an Ingredient object. If an
     * Ingredient is found, the drinks are detached from
     * the ingredient.
     * Likewise, each ingredient is detached from the Drink.
     * This method does not return anything.
     * @param id
     */
    void delete(Long id) {
        Ingredient ingredient = get(id)
        if (!ingredient) {
            logger.error("Could not delete ingredient:: $ingredient")
        }
        else {
            def iDrinks = ingredient.drinks as List<Drink>
            def user = getCurrentUser()
            if (!user?.ingredients?.contains(ingredient)) {
                logger.warn("A user, id:: ${user.id} is deleting an ingredient they did not create.")
                logger.warn("Ingredient belongs to user, id:: ${ingredient?.user?.id}")
            }
            try {
                iDrinks.each { drink ->
                    drink.removeFromIngredients(ingredient)
                    ingredient.removeFromDrinks(drink)
                }
                if (iDrinks.size() > 0) {
                    iDrinks.eachWithIndex { Drink drink, int i ->
                        logger.warn("Deleting this ingredient will affect this drink, id:: ${drink.id}")
                    }
                }
                Ingredient.withNewTransaction {
                    ingredient.delete(flush: true)
                }
                logger.info("Ingredient '${ingredient.name}' deleted!")
            } catch (Exception e) {
                logger.error("Could not delete drink:: $ingredient", e)
            }
        }
    }

    private User getCurrentUser() {
        User.findByUsername(springSecurityService.getPrincipal().username as String)
    }
}