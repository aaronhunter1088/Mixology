package mixology

import enums.*

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
        def amount = null
        List<Object> results = new ArrayList<>();
        // check
        if (value instanceof String) {
            input = value;
            println 'Found a String input';
            // first search drinks
            List<Drink> matchingDrinkNames = Drink.withCriteria {
                eq('drinkName', input)
            }
            results.addAll(matchingDrinkNames)
            GlassType glassType = null
            try {
                glassType = GlassType.valueOf(input.toUpperCase() as String)
                List<Drink> matchingGlassTypes = Drink.withCriteria {
                    eq('suggestedGlass', glassType)
                }
                results.addAll(matchingGlassTypes)
            } catch (IllegalArgumentException iae) {
                println "${input} was not found as an available GlassType"
            }
            Alcohol alcoholType = null
            try {
                alcoholType = Alcohol.valueOf(input.toUpperCase() as String)
                List<Drink> matchingAlcoholTypes = Drink.withCriteria {
                    eq('alcoholType', Alcohol.valueOf(input.toUpperCase() as String))
                }
                results.addAll(matchingAlcoholTypes)
            } catch (IllegalArgumentException iae) {
                println "${input} was not found as an available AlcoholType"
            }
            List<Drink> matchingDrinkSymbols = Drink.withCriteria {
                eq('drinkSymbol', input)
            }
            results.addAll(matchingDrinkSymbols)
            // second search Ingredients
            List<Ingredient> matchingIngredientNames = Ingredient.withCriteria {
                eq('name', input)
            }
            results.addAll(matchingIngredientNames)
            Unit unit = null
            try {
                unit = Unit.valueOf(input)
                List<Ingredient> matchingIngredientUnit = Ingredient.withCriteria {
                    eq('unit', unit)
                }
                results.addAll(matchingIngredientUnit)
            } catch (IllegalArgumentException iae) {
                println "${input} was not found as an available UnitType"
            }

        }
        else {

            amount = value;
            println 'Found an integer value'

        }
        println "Found ${results.size()} total matches"
        respond view: 'index', model: [results: results]


    }
}
