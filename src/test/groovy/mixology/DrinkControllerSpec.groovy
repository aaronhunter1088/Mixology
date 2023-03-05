package mixology

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class DrinkControllerSpec extends Specification implements ControllerUnitTest<DrinkController>, DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [Drink,Ingredient] as Class[]
    }

    def drink1, drink2
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)

    void setup() {
        mockDomain Drink
        mockDomain Ingredient
        drink1 = new Drink([
                drinkName: 'Drink1',
                drinkNumber: 1,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.HURRICANE,
                drinkType: Alcohol.TEQUILA,
                drinkSymbol: 'D1',
                ingredients: createIngredientsWithVodkaAndBAndC()
        ])
        drink2 = new Drink([
                drinkName: 'Drink2',
                drinkNumber: 2,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.SHOT,
                drinkType: Alcohol.TEQUILA,
                drinkSymbol: 'D2',
                ingredients: createIngredientsWithVodkaAndDAndE()
        ])
        // save all ingredients
        drink1.ingredients.each {
            ingredientService.save(it)
        }
        drink2.ingredients.each {
            ingredientService.save(it)
        }
        drinkService.save(drink1)
        drinkService.save(drink2)
    }

    def cleanup() {
    }

    void "setup created two drinks with three ingredients"() {
        expect:"Drink1.id is null"
            drink1.id != null
        println "DrinkID: ${drink1.id}"
        and:"Drink1 doesn't have three ingredients"
            drink1.ingredients.size() == 3
        println "Drink1 Ingredients Size: ${drink1.ingredients.size()}"
        and:"Drink2.id is null"
            drink2.id != null
        println "DrinkID: ${drink2.id}"
        and:"Drink2 doesn't have three ingredients"
        drink2.ingredients.size() == 3
        println "Drink2 Ingredients Size: ${drink2.ingredients.size()}"
    }

    void "confirm ingredient service saved all ingredients"() {
        expect:"Ingredients didn't add 6 ingredients"
            ingredientService.count() == 6
    }

    void "delete one drink doesn't delete other drink"() {
        expect:"Two drinks exist"
            drinkService.count() == 2
        println "# of drinks: ${drinkService.count()}"
        List<Ingredient> ingredients = drink1.ingredients.toArray()
        ingredients.each { ingredient ->
            ingredient.removeFromDrinks(drink1)
        }
        drinkService.delete(drink1.id as Long)
        and:"A single drink should still exist"
            drinkService.count() == 1
        println "# of drinks: ${drinkService.count()}"
        and:"Drink2 exists"
            Drink.findById(2) instanceof Drink
        println "${drink2}"
        and:"6 ingredients still exists"
            ingredientService.count() == 6
        println "# of ingredients: ${ingredientService.count()}"
    }

    // Kept for memory
    void "dummy test"() {
        expect:"fix me"
            true
    }

    Set<Ingredient> createIngredientsWithVodkaAndBAndC() {
        Set<Ingredient> ingredients = []
        Ingredient x = new Ingredient([
                name: 'Vodka',
                unit: Unit.OZ,
                amount: 1.5
        ])
        Ingredient y = new Ingredient([
                name: 'IngredientB',
                unit: Unit.OZ,
                amount: 1.5
        ])
        Ingredient z = new Ingredient([
                name: 'IngredientC',
                unit: Unit.OZ,
                amount: 1.5
        ])
        ingredients.add(x)
        ingredients.add(y)
        ingredients.add(z)
        return ingredients
    }

    Set<Ingredient> createIngredientsWithVodkaAndDAndE() {
        Set<Ingredient> ingredients = []
        Ingredient x = new Ingredient([
                name: 'Vodka',
                unit: Unit.OZ,
                amount: 1.5
        ])
        Ingredient y = new Ingredient([
                name: 'IngredientD',
                unit: Unit.OZ,
                amount: 1.5
        ])
        Ingredient z = new Ingredient([
                name: 'IngredientE',
                unit: Unit.OZ,
                amount: 1.5
        ])
        ingredients.add(x)
        ingredients.add(y)
        ingredients.add(z)
        return ingredients
    }
}
