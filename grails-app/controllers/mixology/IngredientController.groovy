package mixology

import grails.validation.ValidationErrors
import grails.validation.ValidationException
import io.micronaut.http.annotation.Error
import org.springframework.validation.ObjectError

import java.util.function.Predicate

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class IngredientController {

    IngredientService ingredientService
    DrinkService drinkService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ingredientService.list(params), model:[ingredientCount: ingredientService.count()]
    }

    def show(Long id) {
        respond ingredientService.get(id)
    }

    def create() {
        Ingredient ingredient = new Ingredient(params)
        respond ingredient
    }

    def save() { //(Ingredient ingredient) {
        if (!params) {
            notFound()
            return
        }
        Ingredient errorI = new Ingredient()
        List<Ingredient> ingredients = createIngredientsFromParams(params)
        int savedCount = 0
        try {
            ingredients.each { ingredient ->
                if (alreadyExists(ingredient)) {
                    errorI.name = ingredient.name
                    errorI.unit = ingredient.unit
                    errorI.amount = ingredient.amount
                    errorI.errors.reject('default.invalid.ingredient.instance',
                            [ingredient.name, ingredient.unit, ingredient.amount] as Object[],
                            '[Ingredient has already been created]')
                    //respond ingredient.errors, view:'create'
                    //return
                }
                else {
                    ingredientService.save(ingredient)
                    savedCount++
                }
            }
            //ingredientService.save(ingredient)
        }
        catch (ValidationException e) {
            redirect(controller: "ingredient", action: "create") //respond ingredient.errors, view:'create'
            return
        }
//        if (savedCount == ingredients.size()) {
//
//        }
        if (ingredients.size() == 1) {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), ingredients.get(0).id])
                    //redirect ingredients.get(0)
                }
                '*' { respond ingredients.get(0), [status: CREATED] }
            }
        }
        else {
            Predicate<Long> isNull = id -> id == null as Predicate<Long>
            List<Long> ingredientIds = ingredients*.id
            ingredientIds.removeAll([null])
            request.withFormat {
                form multipartForm {
                    if (ingredientIds.size() == 1) {
                        flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), ingredientIds.get(0)])
                    } else {
                        flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredients'), ingredientIds])
                    }
                    //redirect(controller: "ingredient", action: "index")
                }
                '*' { respond ingredientIds, [status: CREATED] }
            }
        }
        respond errorI.errors, view:'create'
    }

    def edit(Long id) {
        respond ingredientService.get(id)
    }

    def update(Ingredient ingredient) {
        if (!ingredient) {
            notFound()
            return
        }

        try {
            if (alreadyExists(ingredient)) {
                throw new ValidationException("Cannot create ingredient because it already exists",
                        new ValidationErrors(ingredient))
            }
            ingredientService.save(ingredient)
        } catch (ValidationException e) {
            respond ingredient.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ingredient.label', default: 'ingredient'), ingredient.id])
                redirect ingredient
            }
            '*'{ respond ingredient, [status: OK] }
        }
    }

    def delete(Long id) {
        if (!id) {
            notFound()
            return
        }

        ingredientService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ingredient.label', default: 'ingredient'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ingredient.label', default: 'ingredient'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def createIngredientsFromParams(params) {
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
}
