package mixology


import grails.validation.ValidationException
import enums.Unit

import javax.servlet.http.HttpServletResponse

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class IngredientController {

    IngredientService ingredientService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        respond ingredientService.list(params), model:[ingredientCount: ingredientService.count()]
    }

    def show(Long id) {
        respond ingredientService.get(id)
    }

    def create() {
        Ingredient ingredient = new Ingredient(params)
        respond ingredient
    }

    def save() { //(Ingredient ingredient) {
        if (!params) {
            notFound()
            return
        }
        Ingredient errorI
        List<Ingredient> ingredientErrors = []
        List<Ingredient> ingredients = createIngredientsFromParams(params)
        int savedCount = 0
        UserRole adminRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.ADMIN.name))
        UserRole userRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.USER.name))
        try {
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
                // if ingredient is not custom and user is not admin user
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
                else if (ingredient.isCustom() && (!adminRole || !userRole)) {
                    // TODO: Implement
                }
                else {
                    ingredientService.save(ingredient)
                    println "ingredient.id ${ingredient.id} saved"
                    savedCount++
                }
            }
        }
        catch (ValidationException e) {
            redirect(controller: "ingredient", action: "create") //respond ingredient.errors, view:'create'
            return
        }

        //Predicate<Ingredient> isNull = id -> id == null
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
                respond ingredientErrors.get(0).errors, view:'create'
            } else {
                ingredientErrors.each { err ->
                    respond err.errors, view: 'create'
                }
            }
//            request.withFormat {
//                form multipartForm {
//                    if (ingredientIds.size() == 1) {
//                        flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredient'), ingredientIds.get(0)])
//                    } else if (ingredientIds.size() > 1) {
//                        flash.message = message(code: 'default.created.message', args: [message(code: 'ingredient.label', default: 'Ingredients'), ingredientIds.each { id ->
//                            id + " "
//                        }])
//                    } else {
//                        flash.message = ''
//                    }
//                    redirect(controller: "ingredient", action: "create")
//                }
//                '*' { respond ingredientIds, [status: CREATED] }
//            }
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
        UserRole adminRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.ADMIN.name))
        UserRole userRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.authentication.getPrincipal().username as String), Role.findByAuthority(enums.Role.USER.name))
        try {
            ingredient.org_grails_datastore_gorm_GormValidateable__errors = null
            if (!ingredient.isCustom() && adminRole) {
                Set<Drink> ingredientsDrinks = ingredient.drinks
                ingredientsDrinks.each { drink ->
                    drink.addToIngredients(ingredient)
                    drink.save()
                }
                ingredientService.save(ingredient)
            }
            else if (ingredient.isCustom() && (adminRole || userRole)) {
                Set<Drink> ingredientsDrinks = ingredient.drinks
                ingredientsDrinks.each { drink ->
                    drink.addToIngredients(ingredient)
                    drink.save()
                }
                ingredientService.save(ingredient)
            }
            else {
                ingredient.errors.reject('default.updated.error.message', [ingredient.name] as Object[], '')
            }
        } catch (ValidationException e) {
            respond ingredient.errors, view:'originalEdit'
            return
        }

        if (ingredient.errors.hasErrors()) {
            Set<Drink> drinks = Drink.withCriteria {eq('ingredients.id', ingredient.id)} as List<Drink>
            ingredient.drinks = drinks
            request.withFormat {
                form multipartForm {
                    respond ingredient.errors, view:'edit'
                }
                '*'{ respond ingredient.errors, view:'edit' }
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

    def delete(Long id) {
        if (!id) {
            notFound()
            return
        }
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient.canBeDeleted) {
            List<Drink> drinks = ingredient.drinks.toArray() as List<Drink>
            drinks.each { drink ->
                drink.removeFromIngredients(ingredient)
                ingredient.removeFromDrinks(drink)
            }
            ingredientService.delete(id)
        } else {
            ingredient.errors.reject('default.deleted.error.message', [ingredient.name] as Object[], '')
        }
        if (ingredient.errors.hasErrors()) {
            request.withFormat {
                form multipartForm {
                    respond ingredient.errors, view:'show'
                }
                '*'{ respond ingredient.errors, view:'show' }
            }
        }
        else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'ingredient.label', default: 'ingredient'), id])
                    redirect action:"index", method:"GET"
                }
                '*'{ render status: NO_CONTENT }
            }
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

    def createIngredientsFromParams(params) {
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

    def validate(params) {
        println "Ingredients: API call # ${params.apiCallCount}"
        Ingredient ingredient = createIngredientsFromParams(params).get(0)
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
