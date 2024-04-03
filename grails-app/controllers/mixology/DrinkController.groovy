package mixology

import enums.*
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import groovy.text.GStringTemplateEngine

import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import static org.springframework.http.HttpStatus.*
import javax.servlet.http.HttpServletResponse

class DrinkController extends BaseController {

    private static Logger logger = LogManager.getLogger(DrinkController.class)
    Set<Ingredient> validIngredients = new HashSet<Ingredient>()

    def drinkService
    def ingredientService
    def userService
    def roleService
    def userRoleService
    def springSecurityService

    static allowedMethods = [sendADrinkEmail: "POST",
                             save: "POST",
                             update: "PUT",
                             delete: "DELETE"]

    @Secured(['ROLE_ADMIN','IS_AUTHENTICATED_FULLY'])
    def index() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def role = roleService.findByAuthority(enums.Role.ADMIN.name)
        def adminUserRole = userRoleService.getUserRoleIfExists(user as User, role as Role)
        def drinks = null
        def args = [
                max: params.max ?: 5,
                offset: params.offset ?: 0,
                sort: params.sort ?: 'id',
                order: params.order ?: 'asc'
        ]
        def criteria = Drink.createCriteria()
        drinks = criteria.list(args, {
            if (params.id) {
                eq('id', params.id as Long)
            } else {
                if (params.name) eq ('name', params.name as String)
                if (params.number) eq ( 'number', params.number as int)
                if (params.alcohol) eq ( 'alcoholType', Alcohol.valueOf(params.alcohol as String))
                if (params.glass) eq ( 'suggestedGlass', GlassType.valueOf(params.glass as String))
                if (params.defaultDrink && isOn(params.defaultDrink as String)) eq ( 'custom', false)
                else params.defaultDrink = false
            }
        })
        logger.info("drinks size: ${drinks.totalCount}")
        withFormat {
            html {
                render view:'index',
                       model: [drinkList: drinks,
                               drinkCount: drinks.totalCount,
                               adminIsLoggedIn:(adminUserRole?true:false),
                               customDrinks:false,
                               params:params
                       ]
            }
            json {
                render drinks as JSON
            }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def customIndex() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = roleService.findByAuthority(enums.Role.ADMIN.name)
        def userRole = roleService.findByAuthority(enums.Role.USER.name)
        def adminUser = userRoleService.getUserRoleIfExists(user as User, adminRole as Role)
        def regularUser = userRoleService.getUserRoleIfExists(user as User, userRole as Role)
        def args = [
                max: params.max ?: 5,
                offset: params.offset ?: 0,
                sort: params.sort ?: 'id',
                order: params.order ?: 'asc'
        ]
        def criteria = Drink.createCriteria()
        def userDrinks = criteria.list(args, {
            'in'('id', user.drinks*.id)
            and {
                if (params.id) {
                    eq('id', params.id as Long)
                } else {
                    if (params.name) eq ('name', params.name as String)
                    if (params.number) eq ( 'number', params.number as int)
                    if (params.alcohol) eq ( 'alcoholType', Alcohol.valueOf((params.alcohol as String).toUpperCase()))
                    if (params.glass) eq ( 'suggestedGlass', GlassType.valueOf((params.glass as String).toUpperCase()))
                }
            }
        })
        logger.info("custom drinks size: ${userDrinks?.totalCount ?: userDrinks.size()}")
        withFormat {
            html {
                render view:'index',
                        model:[drinkList:userDrinks,
                               drinkCount:userDrinks.totalCount,
                               adminIsLoggedIn:(adminUser?true:false),
                               customDrinks:true,
                               user:user,
                               params:params
                        ]
            }
            json { render userDrinks as JSON }
        }

    }

    //@Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def show(Long id) {
        Drink drink = drinkService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        if (drink.custom && user == null) drink = null // if not a default drink and no user
        else if (user && !user?.drinks?.contains(drink)) {
            render view:'/notFound', model:[object:'Drink']
            return
        }
        def roleAdmin = roleService.findByAuthority(enums.Role.ADMIN.name)
        def adminUser = userRoleService.getUserRoleIfExists(user as User, roleAdmin as Role)
        withFormat{
            html {
                if (!drink) { render view:'/notFound', model:[object:'Drink'] }
                else {
                    render view:'show',
                            model:[user:user,
                                   drink:drink,
                                   adminIsLoggedIn:(adminUser) ? true : false
                            ]
                }
            }
            json {
                if (drink) render (drink as JSON)
                else {
                    render(status:204, text:'No drink found')
                }
            }
        }
    }

    //@Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showDrinks() {
        redirect(uri:'/', params:[lang:params.lang,darkMode:params.darkMode])
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomDrinks() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        render view:'customDrinks',
               model:[drinkList:user.drinks.sort{a,b -> a.number <=> b.number ?: a.symbol <=> b.symbol ?: a.name <=> b.name},
                      darkMode:user.darkMode,
                      params:params
               ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def create() {
        Drink drink = new Drink(params)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        render view:'create',
               model:[user:user,
                      drink:drink
               ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def save() {
        if (!params) {
            notFound(flash, request,'','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed('','')
            return
        }
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def role_admin = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        Drink drink = new Drink()
        try {
            if (!params.ingredients) {
                if (validIngredients.isEmpty())
                    throw new Exception("no ingredients were selected")
            }
            drink = createDrinkFromParams(params, user, role_admin)
            if (drink.isCustom() || enums.Role.ADMIN.is(role_admin?.role)) {
                drinkService.save(drink, user, true)
            }
            else {
                drink.errors.reject("You cannot save a default drink!")
            }
        }
        catch (Exception e) {
            logger.error("ValidationError: ${e.getMessage()}")
            if (!(e instanceof grails.validation.ValidationException)) {
                drink.errors.reject('default.created.error.message',
                        ['Drink', e.message] as Object[], '')
            }
        }

        // clear the list
        validIngredients.clear()

        if (drink.errors.hasErrors()) {
            //respond drink?.errors, [status:BAD_REQUEST]
            respond drink.errors, view:'create', status:BAD_REQUEST
        }
        else {
            flash.message = message(code: 'default.created.message', args: [message(code: 'drink.label', default: 'Drink', args:' '), drink.name])
            redirect(controller:'drink',action:'show',params:[id:drink.id])
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def edit(Long id) {
        Drink drink = drinkService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
        def drinkIngredients = drink.ingredients
        def customIngredients = user.ingredients
        if (userRole) {
            customIngredients = customIngredients.findAll{ it.custom }
        }
        render view:'edit', model:[
                user:user,
                drink:drink,
                drinkIngredients:drinkIngredients,
                ingredients:customIngredients
        ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def update() {
        if (!params) {
            return notFound(flash,request,'','')
        }
        if (request.method != 'PUT') {
            return methodNotAllowed(request,'','')
        }
        Drink drinkToUpdate = drinkService.get(params.id as Long)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
        try {
            drinkToUpdate.clearErrors()
            // if is not a custom drink and user has adminRole OR
            // if is a custom drink and user has either role
            if ( (!drinkToUpdate.isCustom() && adminRole) ||
                   drinkToUpdate.isCustom() && (!adminRole || !userRole) ) {
                drinkToUpdate.name = params?.drinkName ?: drinkToUpdate.name
                drinkToUpdate.number = params.drinkNumber ? Integer.valueOf(params.drinkNumber as String) : drinkToUpdate.number
                drinkToUpdate.alcohol = params.alcohol ? Alcohol.valueOf(params?.alcohol as String) : drinkToUpdate.alcohol
                drinkToUpdate.symbol = params?.drinkSymbol ?: drinkToUpdate.symbol
                drinkToUpdate.suggestedGlass = params.glass ? GlassType.valueOf(params.glass as String) : drinkToUpdate.suggestedGlass
                drinkToUpdate.mixingInstructions = params?.mixingInstructions ?: drinkToUpdate.mixingInstructions
                //drinkToUpdate.version = (params?.version as Long) ?: drinkToUpdate.version
                validIngredients = obtainFromParams(params)
                validIngredients.each{
                    drinkToUpdate.addToIngredients(it as Ingredient)
                }
                drinkToUpdate.ingredients.each { Ingredient i ->
                    if (!i.drinks) i.drinks = new HashSet<Drink>()
                    if (!i.drinks.contains(drinkToUpdate)) i.addToDrinks(drinkToUpdate)
                }
                List<Long> associatedIngredientIds = drinkToUpdate.ingredients*.id as List<Long>
                logger.info("${associatedIngredientIds}")
                for(Long id : associatedIngredientIds) {
                    if ( !(id in drinkToUpdate.ingredients*.id)) {
                        logger.info("id ${id} not found in drink.ingredients. removing ${id} from drink: ${drinkToUpdate.name}")
                        Ingredient i = Ingredient.findById(id)
                        i.removeFromDrinks(drinkToUpdate as Drink)
                    }
                }
                drinkService.save(drinkToUpdate, user, true)
                logger.info("custom drink saved!")
            }
            else {
                drinkToUpdate.errors.reject('default.updated.error.message', [drinkToUpdate.name] as Object[], '')
            }
            validIngredients.clear()
        } catch (Exception e) {
            logger.error("exception ${e.getMessage()}")
            drinkToUpdate.errors.reject('default.updated.error.message', [drinkToUpdate.name] as Object[], '')
        }

        if (drinkToUpdate.errors.hasErrors()) {
            user.attach()
            Set<Ingredient> ingredients = Ingredient.findAllByIdInList(drinkToUpdate.ingredients*.id)
            drinkToUpdate.ingredients = ingredients
            def drinkIngredients = drinkToUpdate.ingredients ?: [] as Set<Ingredient>
            def customIngredients = user.ingredients ?: [] as Set<Ingredient>
            render view:'edit',
                    model:[errors:drinkToUpdate.errors,
                           drink:drinkToUpdate,
                           user:user,
                           drinkIngredients:drinkIngredients.sort{it.id},
                           ingredients:customIngredients.sort{it.id}],
                    status:BAD_REQUEST
        } else {
            flash.message = message(code: 'default.updated.message', args:[message(code: 'drink.label', default: 'Drink', args:" "), drinkToUpdate.name])
            show(drinkToUpdate.id)
        }
    }

    /**
     * By default, a drink will be copied with all ingredients
     * @param drink
     */
    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def copy(Drink drink) {
        if (!drink) {
            notFound('','')
            return
        }
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        if (user) {
            logger.info("we have a user logged in ${user.firstName} ${user.lastName}")
            def copiedIngredients = Ingredient.copyAll(drink.ingredients) as List<Ingredient>
            copiedIngredients.each {ingredient ->
                ingredient = ingredientService.save(ingredient, user, true)
                logger.info("$ingredient saved:: ${ingredient.id}")
            }
            def copiedDrink = Drink.copyDrink(drink)
            copiedIngredients.each{ci ->
                copiedDrink.addToIngredients(ci)
            }
            logger.info("drink has been copied")
            copiedDrink = drinkService.save(copiedDrink, user, true)
            logger.info("drink saved:: ${copiedDrink.id}")
            copiedIngredients.each {ingredient ->
                ingredient.addToDrinks(copiedDrink)
                ingredient = ingredientService.save(ingredient, user, false)
                copiedDrink.addToIngredients(ingredient)
                drinkService.save(copiedDrink, user, false)
            }
            copiedDrink = drinkService.save(copiedDrink, user, true)
            user.clearErrors()
            if (!user.hasErrors()) {
                flash.message = message(code:'default.copied.message', args:[drink.name, (user.firstName+' '+user.lastName)], default: "Copied ${copiedDrink.name} to ${user.firstName} ${user.lastName}. You can edit your version as you see fit.") as Object
                request.withFormat {
                    form multipartForm {
                        respond drink, view:'show'
                    }
                    '*'{ respond drink, view:'show' }
                }
            }
            else {
                logger.error("There was validation [{}] errors on the user", user.errors.allErrors.size())
                request.withFormat {
                    form multipartForm {
                        respond drink.errors, view:'show'
                    }
                    '*' { respond drink.errors, view:'show' }
                }
            }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def delete(Long id) {
        if (!id || !params) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'drink.label', default: 'Drink'), params.id])
            redirect action: "show", status: NOT_FOUND
            return
        }
        if (request.method != 'DELETE') {
            return methodNotAllowed(request,'','')
        }
        Drink drink = drinkService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getUserRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
        if (drink.canBeDeleted) {
            try {
                drinkService.delete(id, user, true)
            } catch (Exception e) {
                drink.errors.reject(
                    'default.deleted.error2.message',
                    [drink.name] as Object[],
                    "There was an exception deleting the drink:: ${e.message}"
                )
            }
        } else {
            drink.errors.reject('default.deleted.error.message', [drink.name] as Object[], '')
        }

        if (drink.errors.hasErrors()) {
            request.withFormat {
                form multipartForm {
                    respond drink.errors, view:'show', status:BAD_REQUEST
                }
                '*'{ respond drink.errors, view:'show', status:BAD_REQUEST }
            }
        }
        else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'drink.label', default: 'Drink'), drink.name])
                    if (adminRole) redirect action:"index", method:"GET"
                    else redirect action:'customIndex', method:'GET'
                }
                '*'{ render status: NO_CONTENT }
            }
        }
    }

    def obtainFromParams(params) throws Exception {
        def listOfIngredients = []
        def allIds = (params.ingredients as String[]).each{ it.trim()}
        allIds.each {
            Ingredient found = Ingredient.findById( it as Long )
            if (found?.id) listOfIngredients << found
        }
        listOfIngredients
    }

    /**
     * Creates and returns a saved Drink
     * @param params
     * @param role
     * @return
     */
    def createDrinkFromParams(params, user, role) throws ValidationException {
        def validIds = saveValidIngredients(user as User, params as Map)
        def validStringIds = validIds.collect { it as String }
        /* If params.ingredients is one ( "435" ),
            and not like String[2] ["435","436"] then we should not
            do .each, we should just add that value alone to the validStringIds
         */
        if (params?.ingredients instanceof String) {
            validStringIds.add( params.ingredients as String )
        } else {
            params.ingredients?.each { String id ->
                validStringIds.add(id)
            }
        }

        validStringIds.each { String id ->
            Ingredient ingredient = Ingredient.findById(id as Long)
            if (alreadyExists(ingredient)) {
                ingredient = getExisting(ingredient)
                if (ingredient?.id) validIngredients.add(ingredient)
            }
        }
        Drink drink = new Drink()
        drink.name = params.name
        drink.number = params.number as Integer
        drink.alcohol = Alcohol.valueOf(params.alcohol as String)
        drink.symbol = params.symbol
        drink.suggestedGlass = GlassType.valueOf(params.glass as String)
        drink.mixingInstructions = params.mixingInstructions
        drink.ingredients = validIngredients
                //,custom: Boolean.valueOf(params.custom as String)
                //,canBeDeleted: ( Boolean.valueOf(params.canBeDeleted as String) && Boolean.valueOf(params.custom as String) )
        if ( role && role?.role == enums.Role.ADMIN ) {
            drink.canBeDeleted = false
            drink.custom = false
        }
        return drink
    }

    /**
     * Creates 1 or more ingredients
     * params.ingredients = ["34", "289"]
     * or
     * "ingredientName": value, String
     * "ingredientUnit": value, String
     * "ingredientAmount": value, String
     * @param params
     * @return
     */
    def createNewIngredientsFromParams(def params, def adminUser) {
        Ingredient ingredient = new Ingredient([
                name: params.ingredientName as String,
                unit: params.ingredientUnit as Unit,
                amount: Double.parseDouble(params.ingredientAmount as String)
        ])
        if (adminUser) {
            ingredient.canBeDeleted = false
            ingredient.custom = false
        }

        return ingredient
//        List<String> ingredientNames = new ArrayList<>()
//        List<Unit> ingredientUnits = new ArrayList<>()
//        List<Double> ingredientAmounts = new ArrayList<>()
//        List<Ingredient> ingredients = new ArrayList<>()
//        if (Arrays.asList(params.ingredients).size() > 0) {
//            Ingredient foundI
//            Arrays.asList(params.ingredients).each { id ->
//                foundI = Ingredient.findById ( id as Long )
//                if (foundI?.id) ingredients << foundI
//            }
//            return ingredients
//        }
//        else {
//
//        }
    }

    /**
     * Tests if an ingredient already exists overall
     * If you pass in returnExists as true, it will return
     * the already existing ingredient, instead of a boolean
     * @param ingredient
     * @return
     */
    def alreadyExists(Ingredient ingredient) {
        boolean exists = false
        List<Ingredient> ingredients = Ingredient.list()
        ingredients?.each {
            if (ingredient == it) {
                exists = true
            }
        }
        exists
    }

    def getExisting(Ingredient noIdIngredient) {
        List<Ingredient> ingredients = Ingredient.list()
        Ingredient ingredient = null
        ingredients?.each {
            if (noIdIngredient == it) {
                ingredient = it as Ingredient
            }
        }
        ingredient
    }

    /**
     * Called from the UI which validates each
     * ingredient, created while creating a new drink
     * Single drink: ingredientName, ingredientUnit, ingredientAmount
     * as single String values.
     * Is called once for each new ingredient
     * @param params
     * @return
     */
    def validateIngredients(params) {
        println "API Call # ${params.apiCallCount}"
        User user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        Role role = Role.findByAuthority(enums.Role.ADMIN.name)
        def adminUser = UserRole.get(user.id, role.id)
        Ingredient ingredient = createNewIngredientsFromParams(params, adminUser)
        boolean exists = alreadyExists(ingredient)
        response.setContentType("text/json")
        if (exists) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ingredient: "+ingredient+" has already been created")
        }
        else {
            validIngredients.add(ingredient)
            response.getWriter().append("Ingredient: "+ingredient+" is valid")
            response.getWriter().flush()
            response.setStatus(HttpServletResponse.SC_OK) // 200
        }
    }

    def saveValidIngredients(def user, def params) {
        if (!validIngredients && !params.ingredients) {
            Role role = Role.findByAuthority(enums.Role.ADMIN.name)
            def adminUser = UserRole.get(user.id, role.id)
            def ingredient = createNewIngredientsFromParams(params, adminUser)
            validIngredients << ingredient
        }
        def validIngredientIds = []
        validIngredients.eachWithIndex{ Ingredient i, int idx ->
            logger.info("${idx+1}) Ingredient being saved...")
            i = ingredientService.save(i, user, true)
            def result = user.ingredients.contains( i )
            validIngredientIds << i.id
            logger.info("${idx+1}) Ingredient ${i.id}:${i.name} being saved...")
        }
        validIngredientIds
    }

    /**
     * Used to determine if the Show Default checkbox
     * is checked when admin user is looking at drinks
     * or ingredients
     * @param checkbox
     * @return
     */
    static boolean isOn(String checkbox) {
        boolean result = false
        switch (checkbox?.toLowerCase()) {
            case "checked":
            case "on":
            case "true": {
                result = true
                break
            }
            default: false
        }
        result
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def saveSharedDrink() {
        logger.info("Implement saving shared drink")
        if (!params) {
            logger.info("No parameters passed in")
            redirect(uri:'/')
        }
        logger.info "${params.userEmail}"
        logger.info "${params.drinkId}"
        // find the user. if no user found, for now do nothing
        def user = userService.getByUsername(params.userEmail as String)
        if (!user) {
            logger.info("Couldn't find a user with email: ${params.userEmail}")
            render "Couldn't find a user with email: ${params.userEmail}"
        }
        // if user found, find the drink. if no drink, for now, do nothing
        def drink = drinkService.get(params.drinkId as long)
        if (!drink) {
            logger.info("Couldn't find a drink with id: ${params.drinkId}")
            render "Couldn't find a drink with id: ${params.drinkId}"
        }
        // if drink found, save drink to user. return nothing for now
        def message = "You successfully saved the drink, and ingredients. Next time you log in, you'll find them in your lists."
        def copiedIngredients = Ingredient.copyAll(drink.ingredients) as List<Ingredient>
        copiedIngredients.each {ingredient ->
            if ( !user.ingredients.contains(ingredient) ) {
                ingredientService.save(ingredient, user, false)
            }
            else {
                message = "You already have some or all of these ingredients."
            }
        }
        drink = Drink.copyDrink(drink)
        copiedIngredients.each{ci ->
            drink.addToIngredients(ci)
        }
        if ( !(user.drinks.contains(drink)) ) {
            drinkService.save(drink, user, true)
            render message
        } else {
            if (message.contains('successfully')) {
                render "You already have this drink"
            } else {
                render message + " You already have this drink as well."
            }
        }
    }



    // These methods have been moved to EmailsController. Once fully tested we can remove these.
    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def sendADrinkEmail() {
        if (!params) {
            badRequest(flash, request,'', '')
        }
        if (request.method != 'POST') {
            badRequest(flash, request, '', '')
        }
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        // Create email
        // Save email
        // Send email
        logger.info("SimpleEmail Start")
        //String smtpHostServer = "smtp.example.com";
        //String emailID = "email_me@example.com";
        Properties props = new Properties()
        String host = 'sandbox.smtp.mailtrap.io'
        int port = 465
        String username = '12e26122e12ed7'
        String password = '3b4a64f536617e'
        props.put("mail.smtp.host", host)
        props.put("mail.smtp.user", username)
        props.put("mail.smtp.password", password)
        props.put('mail.smtp.port', port)
        props.put('mail.smtp.starttls.enable', 'true')
        props.put('mail.smtp.auth', 'true')
//        jakarta.mail.Authenticator authenticator = new jakarta.mail.Authenticator() {
//            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
//                new jakarta.mail.PasswordAuthentication(username, password)
//            }
//        }
        def auth = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        username, password);
            }
        }
        Session session = Session.getInstance(props, auth)

        User userExists = userService.getByUsername(params.recipientEmail as String)
        Drink drink = Drink.findById(params.drinkId as Long)
        def imageAsString = convertImageToString(drink.glassImage)
        def bindMap = [
                user:user,
                userExists:userExists?true:false,
                rName:params.recipientName as String,
                rEmail:params.recipientEmail as String,
                drink:drink,
                image:imageAsString,
                testing:true]
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(new File('grails-app/views/email/drinkEmail.gsp'))
        template = template.make(bindMap)
        def text = template.toString()
        boolean sent = sendEmail(session, params.recipientEmail as String, "Test Email", text)
//        sendEmail(session, "aaronhunter@live.com",  "Test Email", """
//        Hi ${user.firstName + ' ' + user.lastName},
//        This is a test email which will soon be built
//        out to share this drink, ${Drink.findById(params.id as Long).drinkName}.
//
//        Stay tuned!
//        """.toString())
        if (sent) {
            flash.message = 'Email was sent'
            //redirect show(params.id as Long)
            redirect action:'show', params:[id:drink.id], method: "GET", status: OK
            //redirect (view:'show', model:['id', params.id])
            //redirect show(params.id as Long)
        } else {
            withFormat {
                flash.message = 'There was an error sending the email'
                redirect action:'show', params:[id:drink.id], method: "GET", status: INTERNAL_SERVER_ERROR
            }
        }
    }

    String convertImageToString(String glassImage) {
        String encodedString = ''
        if (!glassImage) return encodedString
        encodedString = Base64.getEncoder().encodeToString(new File("grails-app/assets/images/$glassImage").getBytes() as byte[])
        encodedString
    }

    static boolean sendEmail(Session session, String toEmail, String subject, String body){
        try {
            MimeMessage msg = new MimeMessage(session)
            //set message headers
            msg.setHeader("Content-Type", "text/html; charset=UTF-8")
            msg.setHeader("Content-Length", body.length() as String)
            msg.setHeader("format", "flowed")
            msg.setHeader("Content-Transfer-Encoding", "8bit")

            msg.setFrom(new InternetAddress("mixology@noreply.com", "mixology@noreply.com"))
            msg.setReplyTo(InternetAddress.parse("mixology@noreply.com", false))
            msg.setSubject(subject, "UTF-8")
            msg.setText(body, "UTF-8")
            msg.setContent(body, "text/html")
            msg.setSentDate(new Date())
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false))

            msg.saveChanges()
            logger.info("Message is ready")
            Transport.send(msg)

            logger.info("Email Sent Successfully!!")
            return true
        }
        catch (Exception e) {
            logger.error("An exception occurred while sending the email", e)
            logger.error("Exception: ${e.getMessage()}")
            return false
        }
    }
}
