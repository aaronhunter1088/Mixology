package mixology

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import enums.Unit
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import static org.springframework.http.HttpStatus.*
import javax.servlet.http.HttpServletResponse

class IngredientController extends BaseController {

    private static Logger logger = LogManager.getLogger(IngredientController.class)

    IngredientService ingredientService
    DrinkService drinkService
    UserService userService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN','IS_AUTHENTICATED_ANONYMOUSLY'])
    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        withFormat {
            html {
                respond ingredientService.list(params), model:[ingredientCount: ingredientService.count()]
            }
            json { ingredientService.list(params) as JSON }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_ANONYMOUSLY'])
    def customIndex() {
        if (!params.max) params.max = 10
        if (!params.offset) params.offset = 0
        if (!params.sort) { params.sort = 'id'; params.order = 'desc' }
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        def customIngredients = user.ingredients
        def maxSize = customIngredients.size()
        // TODO: work out logic better
        customIngredients = customIngredients.findAll{ it.custom }.sort{it.id }
        if (params.max && !params.offset) {
            customIngredients = customIngredients.take( params.max as int )
        }
        if (!params.max || params.offset) {
            def offsetIngredients = []
            customIngredients.eachWithIndex { Ingredient i, int idx ->
                if ( (params.offset as int) > idx ) {
                    logger.info("Skipping index $idx")
                } else {
                    logger.info("Adding ingredient: [$i]")
                    offsetIngredients << i
                }
            }
            customIngredients = offsetIngredients.take( params.max as int )
        }
        render view:'index',
               model:[ingredientList: customIngredients,
                      ingredientCount: maxSize,
                      max:10
               ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def show(Long id) {
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        def role = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        respond ingredientService.get(id), model:[user:user, role:role]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomIngredients() {
          customIndex()
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def create() {
        Ingredient ingredient = new Ingredient(params)
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        render view:'create',
                model:[user:user,
                       ingredient:ingredient
                ]
        //respond ingredient
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def save() {
        if (!params) {
            notFound('','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed('','')
            return
        }
        Ingredient errorI = new Ingredient()
        List<Ingredient> ingredientErrors = []
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        UserRole userRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.USER.name))
        List<Ingredient> ingredients
        int savedCount = 0
        try {
            ingredients = createIngredientsFromParams(params, adminRole)
            ingredients.each { ingredient ->
                Ingredient saved = null
                if (
                     // if ingredient is default and adminRole is present
                     ( !(ingredient.isCustom()) && adminRole )
                     || ( ingredient.isCustom() && (adminRole || userRole) )
                     // OR if ingredient is custom and either role is present
                ) {
                    if (!alreadyExists(ingredient)) {
                        saved = ingredientService.save(ingredient, user, true)
                    }
                    if (saved) {
                        logger.info("ingredient.id ${saved.id} saved")
                        savedCount++
                    } else if (saved == null) {
                        logger.info("ingredient not saved. it already exists")
                        errorI.errors.reject('default.invalid.ingredient.instance',
                                [ingredient.name, ingredient.unit, ingredient.amount] as Object[],
                                "[The ingredient, ${ingredient.name}, already exists.]")
                        ingredientErrors << errorI
                    }
                }
                else {
                    errorI.errors.reject('default.invalid.ingredient.instance',
                            [ingredient.name, ingredient.unit, ingredient.amount] as Object[],
                            '[There was an error saving the ingredient]')
                    ingredientErrors << errorI
                }
            }
        }
        catch (ValidationException e) {
            respond errorI?.errors, view:'create', status: BAD_REQUEST
            return
        }

        List<Long> ingredientIds = ingredients*.id
        ingredientIds.removeAll([null])
        if (!ingredientIds.isEmpty()) {
            request.withFormat {
                form multipartForm {
                    if (ingredientIds.size() == 1) {
                        flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), ingredientIds.get(0)])
                    } else {
                        flash.message = ''
                    }
                    redirect(controller:'ingredient',view:'show')
                }
                '*' { respond ingredients.get(0), view:'show', status:CREATED }
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

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def edit(Long id) {
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.USER.name))

        def ingredient = ingredientService.get(id)
        def drinks = adminRole ? Drink.all : user.drinks as List
        render view:'edit', model:[ingredient:ingredient, user:user, drinks:drinks]
        //respond ingredientService.get(id)
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def update() { //(Ingredient ingredientToUpdate) {
        if (!params) {
            badRequest(flash, request, 'show', 'No ingredientToUpdate was sent in to update')
            return
        }
        if (request.method != 'PUT') {
            methodNotAllowed('','')
            return
        }
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        Ingredient ingredientToUpdate = ingredientService.get(params.id as Long)
        def drinksBefore = ingredientToUpdate?.drinks*.id ?: []
        ingredientToUpdate.clearErrors()
        if ( (!ingredientToUpdate.isCustom() && adminRole) ||
                (ingredientToUpdate.isCustom()) ){
            ingredientToUpdate.name = params?.name ?: ingredientToUpdate.name
            ingredientToUpdate.amount = params?.amount ? Double.valueOf(params.amount as double) : ingredientToUpdate.amount
            ingredientToUpdate.unit = params?.unit ? Unit.valueOf(params.unit as String) : ingredientToUpdate.unit
            ingredientToUpdate = updateIngredientDrinks(ingredientToUpdate, user, params)
        }
        else {
            ingredientToUpdate.errors.reject('default.updated.error.message', [ingredientToUpdate.name] as Object[], '')
        }

        if (ingredientToUpdate.errors.hasErrors()) {
            Set<Drink> drinks = Drink.findAllByIdInList(drinksBefore)
            ingredientToUpdate.drinks = drinks
            request.withFormat {
                form multipartForm {
                    respond ingredientToUpdate.errors, view:'edit', status:BAD_REQUEST
                }
                '*'{ respond ingredientToUpdate.errors, [status:BAD_REQUEST] }
            }
        } else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'ingredient.label', default: 'ingredientToUpdate'), ingredientToUpdate.toString()])
                    redirect ingredientToUpdate
                }
                '*'{ respond ingredientToUpdate, [status: OK] }
            }
        }
    }

    /**
     * TODO: Update copy doc
     */
    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def copy(Ingredient ingredient) {
        if (!ingredient) {
            notFound('','')
            return
        }
        def user = User.findByUsername(springSecurityService.getPrincipal()?.username as String)
        if (user) {
            logger.info("we have a user logged in ${user}")
            Ingredient copied = new Ingredient([
                    name: ingredient.name,
                    unit: ingredient.unit,
                    amount: ingredient.amount,
                    canBeDeleted: true,
                    custom: true
            ])
            Ingredient.withSession {
                Ingredient.withTransaction {
                    ingredientService.save(copied, user, false)
                    logger.info("ingredient.id:${copied.id} has been copied")
                    request.withFormat {
                        form multipartForm {
                            if (copied) {
                                flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), copied.id])
                            } else {
                                flash.message = ''
                            }
                            redirect(controller:"ingredient", action:"show", params:[id:copied.id])
                        }
                        '*' { redirect(controller:"ingredient", action:"show", params:[id:copied.id]) }
                    }
                }
            }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def delete(Long id) {
        if (!id) {
            badRequest(flash, request, 'show', 'No ingredient ID was sent in to delete')
            return
        }
        Ingredient ingredient = ingredientService.get(id)
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        if ( (!ingredient.canBeDeleted && adminRole) ||
             (ingredient.canBeDeleted) ){
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
            if (adminRole) {
                ingredient.canBeDeleted = false
                ingredient.custom = false
            }
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

    /**
     * Called from the UI which validates an
     * ingredient, created while creating a new ingredient
     * @param params
     * @return
     */
    def validateIngredient(params) {
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

    def updateIngredientDrinks(Ingredient ingredientToUpdate, User user, Map params) {
        def drinksBefore = ingredientToUpdate.drinks*.id ?: []
        def drinksAfter = []
        params?.drinks?.each{ String id -> drinksAfter << Long.valueOf(id) }
        def drinksToRemove = drinksBefore - drinksAfter
        if (drinksToRemove?.size() > 0) {
            drinksToRemove.each { Long id ->
                Drink drink = drinkService.get(id)
                if (drink) {
                    drink.removeFromIngredients(ingredientToUpdate)
                    drinkService.save(drink, user, true)
                    logger.info("Updated ingredient. Removed from drink and vise versa")
                }
            }
        }
        if (drinksAfter) {
            drinksAfter.each { Long id ->
                Drink drink = drinkService.get(id)
                if (drink) {
                    if (!drink.ingredients.contains(ingredientToUpdate)) {
                        drink.addToIngredients(ingredientToUpdate)
                        drinkService.save(drink, user, true)
                        logger.info("Updated drink to show ingredient (${ingredientToUpdate.id}) in list")
                    }
                    if (!ingredientToUpdate.drinks.contains(drink)) {
                        ingredientToUpdate.addToDrinks(drink)
                        ingredientService.save(ingredientToUpdate, user, true)
                        logger.info("Updated ingredient to show Drink (${drink.id}) in list")
                    }
                }
            }
        }

        ingredientToUpdate
    }
}
