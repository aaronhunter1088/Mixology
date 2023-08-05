package mixology

import grails.gorm.services.Service
import groovy.sql.Sql

import javax.transaction.Transactional

@Service(Ingredient)
class IngredientService {

    def dataSource

    Ingredient get(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        def sql = Sql.newInstance(dataSource)
        def results = sql.rows("select drink_id from drink_ingredients" +
                " where ingredient_id = ${ingredient.id};")
        results = results.collect { (it.drink_id as Long) }
        def drinks  = [] as Set<Drink>
        results.each { drinks << Drink.findById(it as Long) }
        drinks.each {
            it.addToIngredients(ingredient)
        }
        ingredient
    }

    List<Ingredient> list(Map args) {
        Ingredient.list(args)
    }

    Long count() {
        Ingredient.all.size()
    }

    @Transactional
    Ingredient save(Ingredient ingredient, boolean validate = false) {
        if (ingredient.validate()) ingredient.save(flush:true)
        ingredient
    }

    @Transactional
    void delete(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient) ingredient.delete(flush:true)
    }
}
