package mixology

import com.fasterxml.jackson.annotation.JsonAlias
import enums.Alcohol
import enums.GlassType
import grails.converters.JSON
import grails.validation.ValidationException

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class DrinkController {

    DrinkService drinkService

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
        } catch (ValidationException e) {
            respond drink.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'drink.label', default: 'drink'), drink.id])
                redirect drink
            }
            '*' { respond drink, [status: CREATED] }
        }
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
        List<Ingredient> ingredientsList = []
        List<Ingredient> allIngredients = Ingredient.list()
        params.option.each {
            String[] o = it.split(":")
            // ":" comes in at o[1], so ignore it
            Ingredient i = new Ingredient([
                    name: o[0].trim(),
                    amount: o[1].toDouble(),
                    unit: o[2].trim()
            ])
            allIngredients.each { ingredient ->
                if (ingredient.compareTo(i) == 0) {
                    i = ingredient // set the id for referencing
                }
            }
            ingredientsList.add(i)
        }
        Drink drink = new Drink([
                drinkName: params.drinkName,
                drinkNumber: params.drinkNumber as Integer,
                drinkType: Alcohol.valueOf(params.drinkType),
                drinkSymbol: params.drinkSymbol,
                suggestedGlass: GlassType.valueOf(params.glass),
                mixingInstructions: params.instructions,
                ingredients: ingredientsList
        ])
        return drink
    }
}
