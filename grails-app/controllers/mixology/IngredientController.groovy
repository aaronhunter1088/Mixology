package mixology

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import enums.Unit

import javax.servlet.http.HttpServletResponse

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

class IngredientController extends BaseController {

    IngredientService ingredientService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Override
    void badRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: BAD_REQUEST
            }
            '*'{ render status: BAD_REQUEST }
        }
    }
    @Override
    void notFound(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: message(code: 'default.not.found.message', args: [message(code: 'drink.label', default: 'Drink'), params.id])
                redirect action: "index", method: method ?: "GET", status: NOT_FOUND
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    @Override
    void okRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'OK 200'
                redirect action: "index", method: method ?: "create", status: OK
            }
            '*'{ render status: OK }
        }
    }
    @Override
    void createdRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No request parameters found!'
                redirect action: "index", method: method ?: "create", status: CREATED
            }
            '*'{ render status: CREATED }
        }
    }
    @Override
    void noContentRequest(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'No content'
                redirect action: "index", method: method ?: "create", status: NO_CONTENT
            }
            '*'{ render status: NO_CONTENT }
        }
    }
    @Override
    void unauthorized(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'You are not authorized for the previous request'
                redirect action: "index", method:method, status: UNAUTHORIZED
            }
            '*'{ render status: UNAUTHORIZED }
        }
    }
    @Override
    void methodNotAllowed(method, message) {
        request.withFormat {
            form multipartForm {
                flash.message = message ?: 'Check your request method!'
                redirect action: "index", method: method ?: "create", status: METHOD_NOT_ALLOWED
            }
            '*'{ render status: METHOD_NOT_ALLOWED }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        respond ingredientService.list(params), model:[ingredientCount: ingredientService.count()]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def customIndex(Integer max) {
        params.max = 5//Math.min(max ?: 5, 100)
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        //List<Ingredient> customIngredients = Ingredient.findAllByCustom(true, params).collect()
        //customIngredients = customIngredients.each { it in user.drinks*.ingredients }
        List<Ingredient> customIngredients = []
        user.drinks*.ingredients.each { Set s -> s.collect().each { def it -> customIngredients << it} }
        respond customIngredients, model:[ingredientCount: customIngredients.size()]
    }

    def show(Long id) {
        respond ingredientService.get(id)
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomIngredients() {
        //TODO: here for later implementation
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def create() {
        Ingredient ingredient = new Ingredient(params)
        respond ingredient
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def save() { //(Ingredient ingredient) {
        if (!params) {
            notFound('','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed('','')
            return
        }
        Ingredient errorI
        List<Ingredient> ingredientErrors = []
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        UserRole userRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.USER.name))
        List<Ingredient> ingredients
        int savedCount = 0
        try {
            ingredients = createIngredientsFromParams(params, adminRole)
            ingredients.each { ingredient ->
                errorI = new Ingredient()
                // if ingredient already exists, should not save
                if (alreadyExists(ingredient)) {
                    errorI.name = ingredient.name
                    errorI.unit = ingredient.unit
                    errorI.amount = ingredient.amount
                    errorI.errors.reject('default.invalid.ingredient.instance',
                            [ingredient.name, ingredient.unit, ingredient.amount] as Object[],
                            '[Ingredient has already been created]')
                    println "ingredient.name ${ingredient.name} already exists"
                    ingredientErrors << errorI
                }
                // if ingredient is not custom (default ingredient) and user is not admin user
                else if (!ingredient.isCustom() && !adminRole)  {
                    errorI.name = ingredient.name
                    errorI.unit = ingredient.unit
                    errorI.amount = ingredient.amount
                    errorI.errors.reject('default.invalid.ingredient.instance',
                        [ingredient.name, ingredient.unit, ingredient.amount] as Object[],
                        '[You cannot save a default ingredient]')
                    println "You cannot save a default ingredient"
                    ingredientErrors << errorI
                }
                // if ingredient is custom and user has either role
                else { //if (ingredient.isCustom() && (!adminRole || !userRole)) {
                    ingredient.save(failOnError:true)
                    println "ingredient.id ${ingredient.id} saved"
                    savedCount++
                }
//                else {
//                    //ingredientService.save(ingredient)
//                    ingredient.save(failOnError:true)
//                    println "ingredient.id ${ingredient.id} saved"
//                    savedCount++
//                }
            }
        }
        catch (ValidationException e) {
            //redirect(controller: "ingredient", action: "create") //respond ingredient.errors, view:'create'
            respond errorI?.errors, view:'create', status: BAD_REQUEST
            return
        }

        List<Long> ingredientIds = ingredients*.id
        ingredientIds.removeAll([null])
        //ingredients.removeAll([isNull])
        if (!ingredientIds.isEmpty()) {
            request.withFormat {
                form multipartForm {
                    if (ingredientIds.size() == 1) {
                        flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), ingredientIds.get(0)])
                    } else {
                        flash.message = ''
                    }
                    redirect(controller: "ingredient", action: "create")
                }
                '*' { respond ingredients.get(0), [status: CREATED] }
            }
        }
        else {
            if (ingredientErrors.size() == 1) {
                respond ingredientErrors.get(0).errors, view:'create', status: BAD_REQUEST
            } else {
                ingredientErrors.each { err ->
                    respond err.errors, view: 'create', status: BAD_REQUEST
                }
            }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def edit(Long id) {
        respond ingredientService.get(id)
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def update(Ingredient ingredient) {
        if (!ingredient) {
            badRequest('show', 'No ingredient was sent in to update')
            return
        }
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
//        UserRole userRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.USER.name))
        ingredient.org_grails_datastore_gorm_GormValidateable__errors = null
        if ( (!ingredient.isCustom() && adminRole) ||
                (ingredient.isCustom()) ){
            Set<Drink> ingredientsDrinks = ingredient?.drinks
            ingredientsDrinks.each { drink ->
                drink.addToIngredients(ingredient)
                drink.save()
            }
            ingredientService.save(ingredient)
        }
        else {
            ingredient.errors.reject('default.updated.error.message', [ingredient.name] as Object[], '')
        }

        if (ingredient.errors.hasErrors()) {
            //Set<Drink> drinks = Drink.withCriteria {eq('ingredients.id', ingredient.id)} as List<Drink>
            Set<Drink> drinks = Drink.findAllByIngredientsInList(ingredient.drinks as List)
            ingredient.drinks = drinks
            request.withFormat {
                form multipartForm {
                    respond ingredient.errors, view:'edit', status:BAD_REQUEST
                }
                '*'{ respond ingredient.errors, [status:BAD_REQUEST] }
            }
        } else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'ingredient.label', default: 'ingredient'), ingredient.toString()])
                    redirect ingredient
                }
                '*'{ respond ingredient, [status: OK] }
            }
        }
    }

    /**
     * TODO: Update copy doc
     */
    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def copy(Ingredient ingredient) {

    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def delete(Long id) {
        if (!id) {
            badRequest('show', 'No ingredient ID was sent in to delete')
            return
        }
        Ingredient ingredient = Ingredient.findById(id)
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        if ( (!ingredient.canBeDeleted && adminRole) ||
             (ingredient.canBeDeleted) ){
            List<Drink> drinks = ingredient.drinks.collect()
            drinks.each { drink ->
                drink.removeFromIngredients(ingredient)
                ingredient.removeFromDrinks(drink)
            }
            ingredientService.delete(id)
        } else {
            ingredient.errors.reject('default.deleted.error.message', [ingredient.name] as Object[], '')
        }
        if (ingredient.errors.hasErrors()) {
            if (!adminRole) {
                unauthorized('show', 'You do not have the authority to delete this ingredient')
            } else {
                request.withFormat {
                    form multipartForm {
                        respond ingredient.errors, view:'show', status:BAD_REQUEST
                    }
                    '*'{ respond ingredient.errors, view:'show', status:BAD_REQUEST }
                }
            }
        }
        else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'ingredient.label', default: 'ingredient'), id])
                    redirect action:"index", status:NO_CONTENT
                }
                '*'{ render status:NO_CONTENT }
            }
        }
    }

    /**
     * Creates and returns a List of Ingredients
     * @param params
     * @param role
     * @return
     */
    def createIngredientsFromParams(def params, def adminRole) {
        List<String> ingredientNames = new ArrayList<>()
        List<Unit> units = new ArrayList<>()
        List<Double> ingredientAmounts = new ArrayList<>()
        List<Ingredient> ingredients = new ArrayList<>()
        if (params.ingredientName.size() > 1 && !(params.ingredientName instanceof String)) {
            params.ingredientName.each {
                ingredientNames.add(it as String)
            }
            params.ingredientUnit.each {units.add(it as Unit)} //{units.add(Unit.valueOf(it as String))}
            params.ingredientAmount.each {
                ingredientAmounts.add(Double.parseDouble(it as String))
            }
        } else {
            ingredientNames.add(params.ingredientName as String)
            units.add(params.ingredientUnit as Unit)
            ingredientAmounts.add(Double.parseDouble(params.ingredientAmount as String))
        }
        int createNum = ingredientNames.size()
        for (int i=0; i<createNum; i++) {
            Ingredient ingredient = new Ingredient([
                    name: ingredientNames.get(i),
                    unit: units.get(i),
                    amount: ingredientAmounts.get(i)
            ])
            if (!adminRole) {
                ingredient.canBeDeleted = true
                ingredient.custom = true
            }
            //ingredient.save(failOnError:true)
            ingredients.add(ingredient)
        }
        return ingredients
    }

    /**
     * Tests if an ingredient already exists overall
     * @param ingredient
     * @return
     */
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

    //TODO: Rename to validateIngredient(s)
    /**
     * Called from the UI which validates an
     * ingredient, created while creating a new ingredient
     * @param params
     * @return
     */
    def validate(params) {
        println "Ingredients: API call # ${params.apiCallCount}"
        Ingredient ingredient = createIngredientsFromParams(params, null).get(0)
        boolean result = alreadyExists(ingredient)
        response.setContentType("text/json")
        if (result) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ingredient has already been created")
        } else {
            response.getWriter().append("Ingredient is valid")
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_OK) // 200
        }
    }
}
