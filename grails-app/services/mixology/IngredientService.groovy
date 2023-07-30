package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Transactional
@Service(Ingredient)
class IngredientService {

    Ingredient get(Long id) {
        Ingredient.findById(id)
    }

    List<Ingredient> list(Map args) {
        Ingredient.list(args)
    }

    Long count() {
        Ingredient.all.size()
    }


    Ingredient save(Ingredient ingredient) {
        if (ingredient.validate()) ingredient.save(flush:true)
    }

    void delete(Long id) {
        Drink drink = Drink.findById(id)
        if (drink) drink.delete(flush:true)
    }
}
