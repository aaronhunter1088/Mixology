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


    Ingredient save(Ingredient ingredient, boolean validate = false) {
        if (ingredient.validate()) ingredient.save(flush:true)
    }

    void delete(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient) ingredient.delete(flush:true)
    }
}
