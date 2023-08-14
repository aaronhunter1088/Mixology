package mixology

import grails.gorm.services.Service
import groovy.sql.Sql

import javax.transaction.Transactional

@Service(Ingredient)
class IngredientService {

    def dataSource

    Ingredient get(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
//        def sql = Sql.newInstance(dataSource)
//        def results = sql.rows("select drink_id from drink_ingredients" +
//                " where ingredient_id = ${ingredient.id};")
//        results = results.collect { (it.drink_id as Long) }
//        def drinks  = [] as Set<Drink>
//        results.each { drinks << Drink.findById(it as Long) }
//        drinks.each {
//            it.addToIngredients(ingredient)
//        }
        ingredient
    }

    List<Ingredient> list(Map args) {
        def returnList = []
        if (args.max) returnList = Ingredient.list(args)

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
    List<Ingredient> listFromUser(Map args, User user) {
        def returnList = []
        def userIngredientsIds = user.ingredients.collect { it.id }
        def icrit = Ingredient.createCriteria()
        def results = icrit.list (max: 100) {
            order("${args.sort}", "${args.order}")
        } as List<Ingredient>
        results.each { Ingredient allI ->
            while (returnList.size() < args.max as int ?: 10) {
                if (userIngredientsIds.contains(allI.id)) {
                    returnList << allI
                }
            }
            return
        }
//        results.each { result -> userIngredients << result }
//        if (args.sort && args.order) {
//            user.ingredients.collect { it."${args.sort}"}
//        } else {
//            userIngredients = user.ingredients.drop(args?.offset as int ?: 0)
//        }

        returnList
    }

    Long count() {
        Ingredient.all.size()
    }

    @Deprecated
    @Transactional
    Ingredient save(Ingredient ingredient, boolean validate = false) {
        if (ingredient.validate()) ingredient.save(flush:true)
        ingredient
    }

    @Transactional
    Ingredient save(Ingredient ingredient, User user, boolean validate = false) {
        if (!ingredient || !user) return null
        if (validate) {
            if (ingredient.validate()) ingredient.save(flush:true, validate:validate)
            else return null
        } else ingredient.save(flush:true)
        user.addToIngredients(ingredient)
        user.save(flush:true, deepValidate:validate)
        ingredient
    }

    @Transactional
    void delete(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient) ingredient.delete(flush:true)
    }
}
