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
    void delete(Long id) {
        Drink drink = Drink.findById(id)
        if (drink) drink.delete(flush:true)
    }
}
