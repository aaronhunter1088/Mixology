package mixology

import com.fasterxml.jackson.annotation.JsonAlias
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

    def save(Drink drink) {
        if (!drink) {
            notFound()
            return
        }

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
}
