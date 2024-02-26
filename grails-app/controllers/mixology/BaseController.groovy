package mixology

import org.apache.logging.log4j.Logger

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.UNAUTHORIZED

abstract class BaseController implements IController {

    protected DrinkService drinkService
    protected IngredientService ingredientService
    protected UserService userService
    protected RoleService roleService
    protected UserRoleService userRoleService
    protected AuthTokenService authTokenService

    protected static Logger logger = null

}

interface IController {

    default void okRequest(def method, def message) {// 200, ok
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: BAD_REQUEST
            }
            '*'{ render status: BAD_REQUEST }
        }
    } // 200, ok
    default void createdRequest(def method, def message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: CREATED
            }
            '*'{ render status: CREATED }
        }
    } // 201, created
    default void noContentRequest(def method, def message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No content'
                redirect action: "index", method: method ?: "create", status: NO_CONTENT
            }
            '*'{ render status: NO_CONTENT }
        }
    } // 204, no content

    default void badRequest(def flash, def request, def method, def message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: BAD_REQUEST
            }
            '*'{ render status: BAD_REQUEST }
        }
    } // 400, bad request
    default void unauthorized(def method, def message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'You are not authorized to view this content'
                redirect action: "index", status: UNAUTHORIZED
            }
            '*'{ render status: UNAUTHORIZED }
        }
    } // 401, unauthorized to view content
    default void forbidden(def method, def message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'You do not have permission to view this content'
                redirect controller:'drink', action: 'index', status: FORBIDDEN
            }
            '*'{ render status: FORBIDDEN }
        }
    } // 403, forbidden
    default void notFound(def flash, def request, def method, def message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: message(code: 'default.not.found.message', args: [message(code: 'drink.label', default: 'Drink'), params.id])
                redirect action: "index", method: method ?: "GET", status: NOT_FOUND
            }
            '*'{ render status: NOT_FOUND }
        }
    } // 404, not found
    default void methodNotAllowed(def request, def method, def message) {
        flash.message = message ?: 'Check your request method!'
        request.withFormat {
            form multipartForm {
                redirect action: "index", method: method ?: "create", status: METHOD_NOT_ALLOWED
                //render status: METHOD_NOT_ALLOWED
            }
            '*'{ render status: METHOD_NOT_ALLOWED }
        }
    } // 405, method not allowed

}