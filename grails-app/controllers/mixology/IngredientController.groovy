package mixology

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import enums.Unit
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import static org.springframework.http.HttpStatus.*
import static mixology.DrinkController.isOn
import javax.servlet.http.HttpServletResponse

class IngredientController extends BaseController {

    private static Logger logger = LogManager.getLogger(IngredientController.class)

    def drinkService
    def ingredientService
    def userService
    def roleService
    def userRoleService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN','IS_AUTHENTICATED_ANONYMOUSLY'])
    def index() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def role = roleService.findByAuthority(enums.Role.ADMIN.name)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, role as Role)
        def args = [
                max: params.max ?: 5,
                offset: params.offset ?: 0,
                sort: params.sort ?: 'id',
                order: params.order ?: 'asc'
        ]
        def criteria = Ingredient.createCriteria()
        def ingredients = criteria.list(args, {
            if (params.id) {
                eq('id', params.id as Long)
            } else {
                if (params.name) eq ('name', params.name as String)
                if (params.unit) eq ( 'unit', Unit.valueOf(params.unit as String))
                if (params.amount) eq ( 'amount', params.amount as double)
                if (params.defaultIngredient && isOn(params.defaultIngredient as String)) eq ( 'custom', false)
                else params.defaultIngredient = false
            }
        })
        logger.info("ingredients size: ${ingredients.totalCount}")
        withFormat {
            html {
                render view:'index',
                       model:[ingredientList:ingredients,
                              ingredientCount: ingredients.totalCount,
                              adminIsLoggedIn:(adminRole?true:false),
                              user:user,
                              action:(adminRole?'index':'customIndex'),
                              params:params
                       ]
            }
            json { ingredients as JSON }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def customIndex() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
        def args = [
                max: params.max ?: 5,
                offset: params.offset ?: 0,
                sort: params.sort ?: 'id',
                order: params.order ?: 'asc'
        ]
        def criteria = Ingredient.createCriteria()
        def userIngredients = criteria.list(args, {
            'in'('id', user.ingredients*.id)
            and {
                if (params.id) {
                    eq('id', params.id as Long)
                }
                else {
                    if (params.name) eq ('name', params.name as String)
                    if (params.unit) eq ( 'unit', Unit.valueOf(params.unit as String))
                    if (params.amount) eq ( 'amount', params.alcohol as double)
                }
            }
        })
        logger.info("custom ingredients size: ${userIngredients?.totalCount}")
        withFormat {
            html {
                render view:'index',
                       model:[ingredientList:userIngredients,
                              ingredientCount: userIngredients.totalCount,
                              adminIsLoggedIn:(adminRole?true:false),
                              customIngredients:true,
                              user:user,
                              params:params
                       ]
            }
            json { userIngredients as JSON }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def show(Long id) {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def role = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def ingredient = ingredientService.get(id)
        withFormat{
            html {
                if (!ingredient) { render view:'/notFound', model:[object:'Ingredient']}
                else {
                    render view:'show',
                            model:[user:user,
                                   role:role,
                                   ingredient:ingredient
                            ]
                }
            }
            json {
                if (ingredient) render (ingredient as JSON)
                else {
                    render(status:204, text:'No ingredient found')
                }
            }
        }
    }

    def showIngredients() {
        redirect(action:'index')
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomIngredients() {
          customIndex()
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def create() {
        Ingredient ingredient = new Ingredient(params)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        render view:'create',
               model:[user:user,
                      ingredient:ingredient
               ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def save() {
        if (!params) {
            return notFound(flash,request,'','')
        }
        if (request.method != 'POST') {
            return methodNotAllowed(request,'','')
        }
        Ingredient errorI = new Ingredient()
        List<Ingredient> ingredientErrors = []
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = roleService.findByAuthority(enums.Role.ADMIN.name)
        def userRole = roleService.findByAuthority(enums.Role.USER.name)
        def adminUser = userRoleService.getUserRoleIfExists(user as User, adminRole as Role)
        def regularUser = userRoleService.getUserRoleIfExists(user as User, userRole as Role)
        List<Ingredient> ingredients
        int savedCount = 0
        try {
            ingredients = createIngredientsFromParams(params, adminUser)
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
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
        def ingredient = ingredientService.get(id)
        def drinks = adminRole ? Drink.all : user.drinks as List
        render view:'edit', model:[ingredient:ingredient, user:user, drinks:drinks]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def update() {
        if (!params) {
            badRequest(flash, request, 'show', 'No ingredientToUpdate was sent in to update')
            return
        }
        if (request.method != 'PUT') {
            methodNotAllowed('','')
            return
        }
        Ingredient ingredientToUpdate = ingredientService.get(params.id as Long)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def drinksBefore = ingredientToUpdate?.drinks*.id ?: []
        ingredientToUpdate.clearErrors()
        if ( (!ingredientToUpdate.isCustom() && adminRole) ||
                (ingredientToUpdate.isCustom()) ){
            ingredientToUpdate.name = params?.name ?: ingredientToUpdate.name
            ingredientToUpdate.amount = params?.amount ? Double.valueOf(params.amount as double) : ingredientToUpdate.amount
            ingredientToUpdate.unit = params?.unit ? Unit.valueOf(params.unit as String) : ingredientToUpdate.unit
            ingredientToUpdate = updateIngredientDrinks(ingredientToUpdate, user, params)
            ingredientService.save(ingredientToUpdate, user, true)
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
                    redirect action:'show', params: [id:ingredientToUpdate.id], method:'GET'
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
        Ingredient copied
        if (!ingredient) {
            return notFound('','')
        }
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        if (user) {
            logger.info("we have a user logged in ${user}")
            copied = new Ingredient([
                    name: ingredient.name,
                    unit: ingredient.unit,
                    amount: ingredient.amount
                    //,canBeDeleted: true,
                    //,custom: true
            ])
            ingredientService.save(copied, user, false)
            request.withFormat {
                html {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), copied.id])
                    redirect(controller:"ingredient", action:"show", params:[id:copied.id])
                }
                '*' { redirect(controller:"ingredient", action:"show", params:[id:copied.id]) }
            }
        } else {
            ingredient.errors.reject('default.copy.error.message', [ingredient.name] as Object[], 'Could not copy')
        }

        request.withFormat {
            html {
                flash.message = message(code: 'default.updated.error.message', args: [message(code: 'ingredient.label', default: 'Ingredient')])
                respond copied.errors, view:'show', status:BAD_REQUEST
            }
            '*'{ respond ingredient?.errors, view:'show', status:BAD_REQUEST }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def delete(Long id) {
        if (!id) {
            badRequest(flash, request, 'show', 'No ingredient ID was sent in to delete')
            return
        }
        Ingredient ingredient = ingredientService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def role = roleService.findByAuthority(enums.Role.ADMIN.name)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, role as Role)
        boolean adminIsLoggedIn = adminRole ? true : false
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
                    redirect action:adminIsLoggedIn?'index':'customIndex', status:NO_CONTENT
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
    def createIngredientsFromParams(def params, def adminUser) {
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
            if (adminUser) {
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

    def updateIngredientDrinks(def ingredientToUpdate, def user, def params) {
        def drinksBefore = ingredientToUpdate.drinks*.id ?: []
        def drinksAfter = []
        if (params.drinks instanceof String) {
            logger.info("only 1 drink")
            drinksAfter << Long.valueOf(params.drinks as Long)
        } else {
            logger.info("multiple drinks")
            params?.drinks?.each{ String id -> drinksAfter << Long.valueOf(id) }
        }
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
