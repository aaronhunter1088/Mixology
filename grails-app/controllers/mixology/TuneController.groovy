package mixology

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TuneController {

    TuneService tuneService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond tuneService.list(params), model:[songCount: tuneService.count()]
    }

    def show(Long id) {
        respond tuneService.get(id)
    }

    def create() {
        respond new Tune(params)
    }

    def save(Tune tune) {
        if (tune == null) {
            notFound()
            return
        }

        try {
            tuneService.save(tune)
        } catch (ValidationException e) {
            respond tune.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tune.label', default: 'tune'), tune.id])
                redirect tune
            }
            '*' { respond tune, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond tuneService.get(id)
    }

    def update(Tune tune) {
        if (tune == null) {
            notFound()
            return
        }

        try {
            tuneService.save(tune)
        } catch (ValidationException e) {
            respond tune.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'tune.label', default: 'tune'), tune.id])
                redirect tune
            }
            '*'{ respond tune, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        tuneService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'tune.label', default: 'tune'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tune.label', default: 'tune'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
