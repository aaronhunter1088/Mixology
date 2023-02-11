package mixology

import grails.gorm.services.Service

@Service(Ingredient)
interface IngredientService {

    Ingredient get(Serializable id)

    List<Ingredient> list(Map args)

    Long count()

    void delete(Serializable id)



    Ingredient save(Ingredient ingredient)
}
