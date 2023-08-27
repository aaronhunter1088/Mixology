package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Service(Drink)
class DrinkService {

    Drink get(Long id) {
        Drink drink = Drink.findById(id)
        def ingredients = drink.ingredients
        ingredients.each {
            it.addToDrinks(drink)
        }
        drink
    }

    List<Drink> list(Map args) {
        Drink.list(args)
    }

    Long count() {
        Drink.all.size()
    }

    @Deprecated
    @Transactional
    Drink save(Drink drink, boolean validate = false) {
        if (validate) {
            drink.save(validate:validate, flush:true, failOnError:true)
        }
        else drink.save(validate:false, flush:true, failOnError:false)
        def ingredients = drink.ingredients
        ingredients.each {
            it.addToDrinks(drink)
        }
        drink
    }

    @Transactional
    Drink save(Drink drink, User user, boolean validate = false) {
        if (!drink || !user) return null
        if (validate) {
            if (drink.validate()) {
                try {
                    Drink.withTransaction {
                        User.withTransaction {
                            user.addToDrinks(drink)
                            user.save(flush:true, deepValidate:false)
                        }
                        drink.save(flush:true)
                    }
                } catch (Exception e) {
                    println "could not save drink"
                    return null
                }
            }
            else return null
        } else {
            try {
                Drink.withTransaction {
                    User.withTransaction {
                        user.addToDrinks(drink)
                        user.save(flush:true, deepValidate:false)
                    }
                    drink.save(flush:true)
                }
            } catch (Exception e) {
                println "could not save drink"
                return null
            }
        }
        drink
    }

    @Transactional
    void delete(Long id) {
        Drink drink = Drink.findById(id)
        List<Ingredient> ingredients = drink.ingredients as List<Ingredient>
        // detach fully ingredient from drink
        ingredients.each { ingredient ->
            drink.removeFromIngredients(ingredient)
            ingredient.removeFromDrinks(drink)
        }
        drink.delete(flush:true)
    }
}
