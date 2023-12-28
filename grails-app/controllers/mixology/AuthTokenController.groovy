package mixology

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class AuthTokenController {

    AuthTokenService authTokenService
    def userService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        params.max = Math.min(max ?: 10, 100)
        render view:'index',
            model: [
            authTokenList:authTokenService.list(params),
            authTokenCount:authTokenService.count(),
            user:user
        ]
    }

    def show(Long id) {
        respond authTokenService.get(id)
    }

    def create() {
        respond new AuthToken(params)
    }

    def save(AuthToken authToken) {
        if (authToken == null) {
            notFound()
            return
        }

        try {
            authTokenService.save(authToken)
        } catch (ValidationException e) {
            respond authToken.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'authToken.label', default: 'AuthToken'), authToken.id])
                redirect authToken
            }
            '*' { respond authToken, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond authTokenService.get(id)
    }

    def update(AuthToken authToken) {
        if (authToken == null) {
            notFound()
            return
        }

        try {
            authTokenService.save(authToken)
        } catch (ValidationException e) {
            respond authToken.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'authToken.label', default: 'AuthToken'), authToken.id])
                redirect authToken
            }
            '*'{ respond authToken, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        authTokenService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'authToken.label', default: 'AuthToken'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'authToken.label', default: 'AuthToken'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
