package mixology

import enums.*
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import groovy.sql.Sql
import org.grails.datastore.mapping.collection.PersistentSet

import java.sql.Connection

import static org.apache.commons.lang3.StringUtils.isNotEmpty
import javax.servlet.http.HttpServletResponse

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class DrinkController {

    DrinkService drinkService
    //IngredientService ingredientService
    def springSecurityService

    Set<Ingredient> validIngredients = new HashSet<Ingredient>()

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        respond drinkService.list(params), model:[drinkCount: drinkService.count()]
    }

    def show(Long id) {
        respond drinkService.get(id)
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomDrinks() {
        //render(view: 'customDrinks', model: [drinks: Drink.withCriteria {eq('custom', true)}])
        render(view:'customDrinks')
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
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
        UserRole ur = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.ADMIN.name))
        try {
            if (drink.isCustom() || ur) drinkService.save(drink)
            else {
                drink.errors.reject("You cannot save a default drink!")
            }
        }
        catch (ValidationException e) {
            respond drink.errors, view:'create'
            return
        }

        if (drink.errors.hasErrors()) {
            request.withFormat {
                form multipartForm {
                    respond drink.errors, [status: FORBIDDEN]
                }
                '*' { respond drink, [status: FORBIDDEN] }
            }
        } else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName])
                    redirect drink
                }
                '*' { respond drink, [status: CREATED] }
            }
        }
        // clear the list
        validIngredients.clear()
    }

    def edit(Long id) {
        Drink drink = drinkService.get(id)
        respond drink
    }

    def update(Drink drink) {
        if (!drink) {
            notFound()
            return
        }
        // Get UR based on current user
        UserRole adminRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.ADMIN.name))
        UserRole userRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.USER.name))
        try {
            // TODO: update logic
            drink.org_grails_datastore_gorm_GormValidateable__errors = null
            // if not a custom drink and user has adminRole
            if (!drink.isCustom() && adminRole) {
                drink.drinkName = params.drinkName
                drink.drinkNumber = Integer.valueOf(params.drinkNumber as String)
                drink.alcoholType = Alcohol.valueOf(params.alcoholType as String)
                drink.drinkSymbol = params.drinkSymbol
                drink.suggestedGlass = GlassType.valueOf(params.glass as String)
                drink.mixingInstructions = params.instructions
                drink.version = (params.version as Long)
                extractIngredientsFromParams(params)
                validIngredients.each{
                    drink.ingredients.add(it as Ingredient)
                }
                drink.ingredients.each { Ingredient i ->
                    if (!i.drinks) i.drinks = new HashSet<Drink>()
                    if (!i.drinks.contains(drink)) i.addToDrinks(drink)
                    i.save()
                }
                // Find all ingredients currently associated with drink
                //def sql = new Sql(grailsApplication.mainContext.getBean('dataSource') as Connection)
                List<Ingredient> associatedIngredientIds = Ingredient.withCriteria {
                    eq('drinks.id', drink.id)
                } as List<Ingredient> // 1,2,3,4,38
                println "${associatedIngredientIds}"
                for(Ingredient i : associatedIngredientIds) {
                    if ( !(i.id in drink.ingredients*.id)) {
                        println "id ${i.id} not found in drink.ingredients. removing ${i} from drink: ${drink.drinkName}"
                        i.removeFromDrinks(drink)
                    }
                }
                if (drink.validate()) drinkService.save(drink)
            }
            // if is a custom drink and user has either role
            else if (drink.isCustom() && (!adminRole || !userRole)) {
                // TODO: Implement
            }
            else {
                drink.errors.reject('default.updated.error.message', [drink.drinkName] as Object[], '')
            }
            validIngredients.clear()
        } catch (ValidationException e) {
            println "exception ${e.getMessage()}"
            respond drink.errors, view:'edit'
            return
        }

        if (drink.errors.hasErrors()) {
            Set<Ingredient> ingredients = Ingredient.withCriteria {
                eq('drinks.id', drink.id)
            } as List<Ingredient>
            drink.ingredients = ingredients
            request.withFormat {
                form multipartForm {
                    respond drink.errors, view:'edit'
                }
                '*' { respond drink.errors, view:'edit' }
            }
        } else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName])
                    respond drink, view:'edit'
                }
                '*'{ respond drink, [status: OK] }
            }
        }
    }

    def delete(Long id) {
        if (!id) {
            notFound()
            return
        }
        Drink drink = Drink.findById(id)
        if (drink.canBeDeleted) {
            List<Ingredient> ingredients = drink.ingredients.toArray() as List<Ingredient>
            ingredients.each { ingredient ->
                ingredient.removeFromDrinks(drink)
                drink.removeFromIngredients(ingredient)
            }
            drinkService.delete(id)
        } else {
            drink.errors.reject('default.deleted.error.message', [drink.drinkName] as Object[], '')
        }

        if (drink.errors.hasErrors()) {
            request.withFormat {
                form multipartForm {
                    respond drink.errors, view:'show'
                }
                '*'{ respond drink.errors, view:'show' }
            }
        } else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName])
                    redirect action:"index", method:"GET"
                }
                '*'{ render status: NO_CONTENT }
            }
        }
    }

    /**
     * By default, a drink will be copied with all ingredients
     * By clicking on the action button which states that you
     * don't want the ingredient's copied, then the drink will
     * not contain the given ingredients, or instructions.
     * @param drink
     */
    def copy(Drink drink) {
        def user = User.findByUsername(springSecurityService?.authentication?.getPrincipal()?.username as String)
        if (user) {
            log.info("we have a user logged in ${user}")
            Drink copied = new Drink([
                drinkName : drink.drinkName,
                drinkSymbol : drink.drinkSymbol,
                drinkNumber : drink.drinkNumber,
                alcoholType : drink.alcoholType,
                ingredients : Ingredient.copyAll(drink.ingredients),
                mixingInstructions : drink.mixingInstructions,
                suggestedGlass : drink.suggestedGlass,
                canBeDeleted : true,
                custom : true
            ])
            Drink.withTransaction {
                copied.ingredients.each { it.save(flush:true) }
                log.info("adding $drink to user")
                copied.save(flush:true)
            }
            User.withTransaction {
                user.addToDrinks(copied)
                user.save(flush:true)
            }
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.copied.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName], default: "Copied $copied to $user. You can edit your version as you see fit.")
                    redirect(drink:drink, view:'show')
                }
                '*'{ redirect(drink:drink, view:'show') }
            }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'drink.label', default: 'Drink'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def extractIngredientsFromParams(params) {
        //validIngredients = new HashSet<Ingredient>()
        List<Ingredient> allIngredients = Ingredient.list()
        Ingredient newIngredientFromParams
        String ingredients = params.ingredients
        if (isNotEmpty(ingredients)) {
            ingredients = ingredients.replace('[','').replace(']','')
            if (ingredients.indexOf(',') >= 0) {
                println "has a comma"
                String[] list = ingredients.split(',')
                println list
                for (it in list) {
                    String[] parts = it.split ':'
                    newIngredientFromParams = new Ingredient([
                            name: parts[0].trim(),
                            amount: parts[1].trim(),
                            unit  : Unit.valueOf(parts[2].trim().toUpperCase().replace(' ','_'))
                    ])
                    for (ingredient in allIngredients) {
                        if (ingredient == newIngredientFromParams) {
                            newIngredientFromParams = ingredient // set the id for referencing
                            break
                        }
                    }
                    validIngredients.add(newIngredientFromParams)
                }
            }
            else {
                String[] parts = ingredients.split ':'
                newIngredientFromParams = new Ingredient([
                        name  : parts[0].trim(),
                        amount: parts[1].trim(),
                        unit  : Unit.valueOf(parts[2].trim().toUpperCase().replace(' ','_'))
                ])
                for (ingredient in allIngredients) {
                    if (ingredient == newIngredientFromParams) {
                        newIngredientFromParams = ingredient // set the id for referencing
                        break
                    }
                }
                validIngredients.add(newIngredientFromParams)
            }
        }
    }

    def createDrinkFromParams(params) {
        extractIngredientsFromParams(params)

        Drink drink = new Drink([
                drinkName: params.drinkName,
                drinkNumber: params.drinkNumber as Integer,
                alcoholType: Alcohol.valueOf(params.alcoholType),
                drinkSymbol: params.drinkSymbol,
                suggestedGlass: GlassType.valueOf(params.glass),
                mixingInstructions: params.instructions,
                ingredients: validIngredients,
                canBeDeleted: true,
                custom: true
        ])
        // Associate all ingredients with this drink
        validIngredients.each { Ingredient it ->
            if (!it.drinks) it.drinks = new HashSet<Drink>()
            if (!it.drinks.contains(drink)) it.addToDrinks(drink)
            it.save()
        }
        return drink
    }

    def createNewIngredientsFromParams(params) {
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

    def validateIngredients(params) {
        println "API Call # ${params.apiCallCount}"
        Ingredient ingredient = createNewIngredientsFromParams(params).get(0)
        boolean result = false
        if (alreadyExists(ingredient)) { result = true }
        //response.setContentType("text/plain")
        response.setContentType("text/json")
        if (result) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ingredient: "+ingredient+" has already been created")
        }
        else {
            validIngredients.add(ingredient)
            response.getWriter().append("Ingredient: "+ingredient+" is valid")
            response.getWriter().flush()
            response.setStatus(HttpServletResponse.SC_OK) // 200
        }
    }

}
