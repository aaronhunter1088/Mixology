package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.transaction.Transactional

@Service(Ingredient)
class IngredientService {

    private static Logger logger = LogManager.getLogger(IngredientService.class)

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
     * This method is similar to Ingredient.list(params)
     * but it uses a user's ingredients specifically.
     * If you are sorting the list, that action will occur
     * first. Then offset is applied, followed by max.
     * @param args
     *        max, affects number of Ingredients returns
     *        offset, affects how many Ingredients to
     *          initially skip
     *        sort, a property to sort by, and used with order
     *        order, asc or desc used with sort
     * @param user
     * @return
     */
    def listFromUser(Map args, User user) {
        //TODO: Fix me
        return user.ingredients
//        def returnList = []
//        def userIngredientsIds = user.ingredients.collect { it.id }
//        def icrit = Ingredient.createCriteria()
//        def results = icrit.list (max: 100) {
//            order("${args.sort}", "${args.order}")
//        } as List<Ingredient>
//        results.each { Ingredient allI ->
//            while (returnList.size() < args.max as int ?: 10) {
//                if (userIngredientsIds.contains(allI.id)) {
//                    returnList << allI
//                }
//            }
//            return
//        }
//        results.each { result -> userIngredients << result }
//        if (args.sort && args.order) {
//            user.ingredients.collect { it."${args.sort}"}
//        } else {
//            userIngredients = user.ingredients.drop(args?.offset as int ?: 0)
//        }

//        returnList
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
    @Transactional
    Ingredient save(Ingredient ingredient, boolean validate = false) {
        if (validate) { if (ingredient.validate()) { ingredient.save(flush:true) } }
        else ingredient.save(flush:true)
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
        if (!ingredient || !user) return null
        if (validate) {
            if (ingredient.validate()) {
                try {
                    Ingredient.withTransaction {
                        ingredient.save(flush:true)
                        //User.withTransaction {
                        user.addToIngredients(ingredient)
                        user.save(flush:true)
                        //}
                    }
                } catch (Exception e) {
                    logger.error("Could not save ingredient:: $ingredient")
                    return null
                }
            }
            else return null
        } else ingredient.save(flush:true)
        ingredient
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
    @Transactional
    void delete(Long id) {
        Ingredient ingredient = get(id)
        def drinks = ingredient.drinks
        drinks.each { drink ->
            drink.removeFromIngredients(ingredient)
            ingredient.removeFromDrinks(drink)
        }
        ingredient.delete(flush:true)
    }
}
