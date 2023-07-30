package mixology

import grails.gorm.services.Service

import javax.transaction.Transactional

@Transactional
@Service(Ingredient)
class IngredientService {

    Ingredient get(Long id) {
        return Ingredient.findById(id)
    }

    List<Ingredient> list(Map args) {
        return Ingredient.list(args)
    }

    Long count() {
        return Ingredient.all.size()
    }

    void delete(Long id) {
        Drink drink = Drink.findById(id)
        if (!drink) return
        else drink.delete(flush:true)
    }

    Ingredient save(Ingredient ingredient) {
        ingredient.save(flush:true)
    }
}
