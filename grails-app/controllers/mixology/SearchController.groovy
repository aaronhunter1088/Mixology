package mixology

import enums.*

import javax.servlet.http.HttpServletResponse

// Will do searching in specific controller. Delete
@Deprecated
class SearchController {

    DrinkService drinkService
    IngredientService ingredientService

    def index(params) {
        render(view: 'index', model: params)
    }

    /**
     * The search function takes in a value, Tequila for example
     * and will search matching values for both Drinks and
     * Ingredients. It will return all results which can be
     * viewed together or separate, depending on the user
     * @param params
     * @return
     */
    def search(params) {
        println "Searching for '${params.searchingFor}'"
        def value = params.searchingFor
        def input = null
        //def amount = null
        List<Drink> drinks = new ArrayList<>();
        List<Ingredient> ingredients  = new ArrayList<>();

        try {
            input = Double.valueOf(value as String)
            println 'Found an number value'
            respond view:'index', model:[:]
        }
        catch (NumberFormatException nfe) {
            input = value
            println 'Found a String input'
            // first search drinks
            List<Drink> matchingDrinkNames = Drink.withCriteria {
                eq('drinkName', input)
            } as List<Drink>
            drinks.addAll(matchingDrinkNames)
            GlassType glassType = null
            try {
                glassType = GlassType.valueOf(input.toUpperCase() as String)
                List<Drink> matchingGlassTypes = Drink.withCriteria {
                    eq('suggestedGlass', glassType)
                } as List<Drink>
                drinks.addAll(matchingGlassTypes)
            } catch (IllegalArgumentException iae) {
                println "${input} was not found as an available GlassType"
            }
            Alcohol alcoholType = null
            try {
                alcoholType = Alcohol.valueOf(input.toUpperCase() as String)
                List<Drink> matchingAlcoholTypes = Drink.withCriteria {
                    eq('alcoholType', Alcohol.valueOf(input.toUpperCase() as String))
                } as List<Drink>
                drinks.addAll(matchingAlcoholTypes)
            } catch (IllegalArgumentException iae) {
                println "${input} was not found as an available AlcoholType"
            }
            List<Drink> matchingDrinkSymbols = Drink.withCriteria {
                eq('drinkSymbol', input)
            }  as List<Drink>
            drinks.addAll(matchingDrinkSymbols)
            // second search Ingredients
            List<Ingredient> matchingIngredientNames = Ingredient.withCriteria {
                eq('name', input)
            } as List<Ingredient>
            ingredients.addAll(matchingIngredientNames)
            Unit unit = null
            try {
                unit = Unit.valueOf(input)
                List<Ingredient> matchingIngredientUnit = Ingredient.withCriteria {
                    eq('unit', unit)
                } as List<Ingredient>
                ingredients.addAll(matchingIngredientUnit)
            } catch (IllegalArgumentException iae) {
                println "${input} was not found as an available UnitType"
            }
        }
        int totalFound = drinks.size() + ingredients.size()

        println "Found ${totalFound} total matches"
        //response.setContentType("text/plain")
        //response.getWriter().append(totalFound ? "Matches found" : "No matches found")
        //response.getWriter().flush()
        response.setStatus(HttpServletResponse.SC_OK) // 200
//        render(contentType: "application/json") {
//            [total: totalFound, drinks: drinks, ingredients: ingredients]
//        }
//        render(view: "index", model: [total: totalFound, drinks: drinks, ingredients: ingredients])
        respond view: 'index',
                model: [
                    drinks: drinks,
                    ingredients: ingredients,
                    total: totalFound
                ]
    }
}
