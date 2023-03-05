package mixology

import enums.*
import grails.validation.ValidationException

import javax.servlet.http.HttpServletResponse

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class DrinkController {

    DrinkService drinkService
    IngredientService ingredientService

    Set<Ingredient> validIngredients = []

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond drinkService.list(params), model:[drinkCount: drinkService.count()]
    }

    def show(Long id) {
        respond drinkService.get(id)
    }

    def create() {
        Drink drink = new Drink(params)
        respond drink
    }

    def save() { // (Drink drink)
        if (!params) {//if (!drink) {
            notFound()
            return
        }
        Drink drink = createDrinkFromParams(params)
        try {
            drinkService.save(drink)
        }
        catch (ValidationException e) {
            respond drink.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'drink.label', default: 'drink'), drink.drinkName])
                redirect drink
            }
            '*' { respond drink, [status: CREATED] }
        }
        // clear the list
        validIngredients = []
    }

    def edit(Long id) {
        respond drinkService.get(id)
    }

    def update(Drink drink) {
        if (!drink) {
            notFound()
            return
        }

        try {
            drinkService.save(drink)
        } catch (ValidationException e) {
            respond drink.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'drink.label', default: 'drink'), drink.id])
                redirect drink
            }
            '*'{ respond drink, [status: OK] }
        }
    }

    def delete(Long id) {
        if (!id) {
            notFound()
            return
        }

        drinkService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'drink.label', default: 'drink'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'drink.label', default: 'drink'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def createDrinkFromParams(params) {
        List<Ingredient> allIngredients = Ingredient.list()

        Ingredient newIngredientFromParams
        String ingredients = params.ingredients
        ingredients = ingredients.replace('[','').replace(']','')
        if (ingredients.indexOf(',') >= 0) {
            println "has a comma"
            String[] list = ingredients.split(',')
            println list
            for (it in list) {
                String[] parts = it.split ':'
                newIngredientFromParams = new Ingredient([
                        name: parts[0].trim(),
                        amount: parts[1].trim(),
                        unit  : Unit.valueOf(parts[2].trim().toUpperCase())
                ])
                for (ingredient in allIngredients) {
                    if (ingredient == newIngredientFromParams) {
                        newIngredientFromParams = ingredient // set the id for referencing
                        break
                    }
                }
                validIngredients.add(newIngredientFromParams)
            }
        }
        else {
            String[] parts = ingredients.split ':'
            newIngredientFromParams = new Ingredient([
                    name  : parts[0].trim(),
                    amount: parts[1].trim(),
                    unit  : Unit.valueOf(parts[2].trim().toUpperCase())
            ])
            for (ingredient in allIngredients) {
                if (ingredient == newIngredientFromParams) {
                    newIngredientFromParams = ingredient // set the id for referencing
                    break
                }
            }
            validIngredients.add(newIngredientFromParams)
        }

        Drink drink = new Drink([
                drinkName: params.drinkName,
                drinkNumber: params.drinkNumber as Integer,
                drinkType: Alcohol.valueOf(params.drinkType),
                drinkSymbol: params.drinkSymbol,
                suggestedGlass: GlassType.valueOf(params.glass),
                mixingInstructions: params.instructions,
                ingredients: validIngredients
        ])
        // Associate all ingredients with this drink
        validIngredients.each {
            if (!it.drinks) it.drinks = new HashSet<Drink>()
            if (!it.drinks.contains(drink)) it.drinks.add(drink)
        }
        return drink
    }

    def createNewIngredientsFromParams(params) {
        List<String> ingredientNames = new ArrayList<>()
        List<String> units = new ArrayList<>()
        List<Double> ingredientAmounts = new ArrayList<>()
        List<Ingredient> ingredients = new ArrayList<>()
        if (params.ingredientName.size() > 1 && !(params.ingredientName instanceof String)) {
            params.ingredientName.each {
                ingredientNames.add(it as String)
            }
            params.ingredientUnit.each {
                units.add(it as String)
            }
            params.ingredientAmount.each {
                ingredientAmounts.add(Double.parseDouble(it as String))
            }
        } else {
            ingredientNames.add(params.ingredientName as String)
            units.add(params.ingredientUnit as String)
            ingredientAmounts.add(Double.parseDouble(params.ingredientAmount as String))
        }
        int createNum = ingredientNames.size()
        for (int i=0; i<createNum; i++) {
            Ingredient ingredient = new Ingredient([
                    name: ingredientNames.get(i),
                    unit: units.get(i),
                    amount: ingredientAmounts.get(i)
            ])
            ingredients.add(ingredient)
        }
        return ingredients
    }

    def alreadyExists(ingredient) {
        boolean exists = false
        List<Ingredient> ingredients = ingredient.list()
        ingredients.each {
            if (ingredient.compareTo(it) == 0) {
                return (exists = true)
            }
        }
        return exists
    }

    def validateIngredients(params) {
        println "API Call # ${params.apiCallCount}"
        Ingredient ingredient = createNewIngredientsFromParams(params).get(0)
        boolean result = false
        if (alreadyExists(ingredient)) { result = true }
        response.setContentType("text/plain")
        if (result) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ingredient has already been created")
        }
        else {
            validIngredients.add(ingredient)
            response.getWriter().append("Ingredient is valid")
            response.getWriter().flush()
            response.setStatus(HttpServletResponse.SC_OK) // 200
        }
    }

}
