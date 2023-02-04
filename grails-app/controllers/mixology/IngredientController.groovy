package mixology

import grails.validation.ValidationErrors
import grails.validation.ValidationException

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

    def save(Ingredient ingredient) {
        if (!ingredient) {
            notFound()
            return
        }
        try {
            if (alreadyExists(ingredient)) {
                ingredient.errors.reject('default.invalid.ingredient.instance',
                                        [ingredient.name, ingredient.unit, ingredient.amount] as Object[],
                                        '[Ingredient has already been created]')
                respond ingredient.errors, view:'create'
                return
            }

            ingredientService.save(ingredient)
        } catch (ValidationException e) {
            respond ingredient.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'ingredient'), ingredient.id])
                redirect ingredient
            }
            '*' { respond ingredient, [status: CREATED] }
        }
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

    def alreadyExists(ingredient) {
        boolean exists = false;
        List<Ingredient> ingredients = ingredient.list()
        ingredients.each {
            if (ingredient.compareTo(it) == 0) {
                if (it.drinks.size() == 0) {
                    exists = false
                } else if (it.drinks.size() > 0) {
                   println "Drinks size is more than 0: Determine what to do!"
                } else {
                    exists = true
                }
            }
        }
        return exists;
    }
}
