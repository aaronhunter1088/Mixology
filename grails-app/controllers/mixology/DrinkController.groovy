package mixology

import enums.*
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
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
    def userRoleService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN','IS_AUTHENTICATED_ANONYMOUSLY'])
    def index() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
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
                               adminIsLoggedIn:(adminRole?true:false),
                               customDrinks:false,
                               params:params
                       ]
            }
            json { drinks as JSON }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def customIndex() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
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
        logger.info("custom drinks size: ${userDrinks.totalCount}")
        withFormat {
            html {
                render view:'index',
                        model:[drinkList:userDrinks,
                               drinkCount:userDrinks.totalCount,
                               adminIsLoggedIn:(adminRole?true:false),
                               customDrinks:true,
                               params:params
                        ]
            }
            json { userDrinks as JSON }
        }

    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def show(Long id) {
        Drink drink = drinkService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def role = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        respond drink, model:[adminIsLoggedIn:(role?true:false)]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showDrinks() {
        redirect(uri:'/')
    }
    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomDrinks() {
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        render view:'customDrinks', model:[drinkList:user.drinks,params:params]
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
            notFound('','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed('','')
            return
        }
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def ur = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        Drink drink
        try {
            drink = createDrinkFromParams(params, user, ur)
            if (drink.isCustom() || ur?.role == enums.Role.ADMIN) {
                drinkService.save(drink, user, false)
            }
            else {
                drink.errors.reject("You cannot save a default drink!")
            }
        }
        catch (ValidationException e) {
            respond drink?.errors, view:'create', status: BAD_REQUEST
            return
        }

        if (drink.errors.hasErrors()) {
            request.withFormat {
                form multipartForm {
                    respond drink?.errors, [status: FORBIDDEN]
                }
                '*' { respond drink, [status: FORBIDDEN] }
            }
        } else {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'drink.label', default: 'Drink'), drink.name])
                    redirect drink
                }
                '*' { respond drink, [status: CREATED] }
            }
        }
        // clear the list
        validIngredients.clear()
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def edit(Long id) {
        Drink drink = drinkService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
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
    def update(Drink drink) {
        if (!drink) {
            notFound('','')
            return
        }
        if (request.method != 'PUT') {
            methodNotAllowed('','')
            return
        }
        // Get UR based on current user
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
        try {
            drink.clearErrors()
            // if not a custom drink and user has adminRole
            if (!drink.isCustom() && adminRole) {
                drink.name = params?.name ?: drink.name
                drink.number = params?.number ? Integer.valueOf(params.number as String) : drink.number
                drink.alcoholType = params.alcoholType ? Alcohol.valueOf(params.alcoholType as String) : drink.alcoholType
                drink.symbol = params?.symbol ?: drink.symbol
                drink.suggestedGlass = params.glass ? GlassType.valueOf(params.glass as String) : drink.suggestedGlass
                drink.mixingInstructions = params?.mixingInstructions ?: drink.mixingInstructions
                drink.version = (params?.version as Long) ?: drink.version
                //validIngredients = createNewIngredientsFromParams(params)
                validIngredients = obtainFromParams(params)
                validIngredients.each{
                    drink.ingredients.add(it as Ingredient)
                }
                drink.ingredients.each { Ingredient i ->
                    if (!i.drinks) i.drinks = new HashSet<Drink>()
                    if (!i.drinks.contains(drink)) i.addToDrinks(drink as Drink)
                }
                // Find all ingredients currently associated with drink
                List<Long> associatedIngredientIds = drink.ingredients*.id as List<Long>
                logger.info("${associatedIngredientIds}")
                for(Long id : associatedIngredientIds) {
                    if ( !(id in drink.ingredients*.id)) {
                        logger.info("id ${id} not found in drink.ingredients. removing ${id} from drink: ${drink.name}")
                        Ingredient i = Ingredient.findById(id)
                        i.removeFromDrinks(drink)
                    }
                }
                drinkService.save(drink, user, false)
                logger.info("default drink saved")
            }
            // if is a custom drink and user has either role
            else if (drink.isCustom() && (!adminRole || !userRole)) {
                drink.name = params?.drinkName ?: drink.name
                drink.number = params.drinkNumber ? Integer.valueOf(params.drinkNumber as String) : drink.number
                drink.alcoholType = params.alcoholType ? Alcohol.valueOf(params?.alcoholType as String) : drink.alcoholType
                drink.symbol = params?.drinkSymbol ?: drink.symbol
                drink.suggestedGlass = params.glass ? GlassType.valueOf(params.glass as String) : drink.suggestedGlass
                drink.mixingInstructions = params?.instructions ?: drink.mixingInstructions
                drink.version = (params?.version as Long) ?: drink.version
                validIngredients = obtainFromParams(params)
                validIngredients.each{
                    drink.ingredients.add(it as Ingredient)
                }
                drink.ingredients.each { Ingredient i ->
                    if (!i.drinks) i.drinks = new HashSet<Drink>()
                    if (!i.drinks.contains(drink)) i.addToDrinks(drink)
                }
                List<Long> associatedIngredientIds = drink.ingredients*.id as List<Long>
                logger.info("${associatedIngredientIds}")
                for(Long id : associatedIngredientIds) {
                    if ( !(id in drink.ingredients*.id)) {
                        logger.info("id ${id} not found in drink.ingredients. removing ${id} from drink: ${drink.name}")
                        Ingredient i = Ingredient.findById(id)
                        i.removeFromDrinks(drink as Drink)
                    }
                }
                drinkService.save(drink, user, true)
                logger.info("custom drink saved!")
            }
            else {
                drink.errors.reject('default.updated.error.message', [drink.name] as Object[], '')
            }
            validIngredients.clear()
        } catch (ValidationException e) {
            logger.error("exception ${e.getMessage()}")
            respond drink.errors, view:'edit'
            return
        }

        if (drink.errors.hasErrors()) {
            Set<Ingredient> ingredients = Ingredient.findAllByDrinksInList(drink.ingredients as List)
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
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'drink.label', default: 'Drink'), drink.name])
                    respond drink, view:'show'
                }
                '*'{ respond drink, view:'show', status: OK }
            }
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
            Drink copiedDrink = new Drink([
                    name : drink.name,
                    symbol : drink.symbol,
                    number : drink.number,
                    alcoholType : drink.alcoholType,
                    //ingredients : copiedIngredients,
                    mixingInstructions : drink.mixingInstructions,
                    suggestedGlass : drink.suggestedGlass
                    //,canBeDeleted : true
                    //,custom : true
            ])
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
                flash.message = message(code: 'default.copied.message', args: [message(code: 'drink.label', default: 'Drink'), drink.name, user], default: "Copied $copiedDrink to $user. You can edit your version as you see fit.") as Object
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

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def delete(Long id) {
        if (!id) {
            notFound('','')
            return
        }
        Drink drink = drinkService.get(id)
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def adminRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.ADMIN.name))
        def userRole = userRoleService.getRoleIfExists(user as User, Role.findByAuthority(enums.Role.USER.name))
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
        def validIds = saveValidIngredients(user)
        def validStringIds = validIds.collect { it as String }
        /* TODO: If params.ingredients is one ( "435" ),
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
        Drink drink = new Drink([
                name: params.name,
                number: params.number as Integer,
                alcoholType: Alcohol.valueOf(params.alcoholType as String),
                symbol: params.symbol,
                suggestedGlass: GlassType.valueOf(params.glass as String),
                mixingInstructions: params.mixingInstructions,
                ingredients: validIngredients
                //,custom: Boolean.valueOf(params.custom as String)
                //,canBeDeleted: ( Boolean.valueOf(params.canBeDeleted as String) && Boolean.valueOf(params.custom as String) )
        ])
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
    def createNewIngredientsFromParams(params) {
        List<String> ingredientNames = new ArrayList<>()
        List<Unit> ingredientUnits = new ArrayList<>()
        List<Double> ingredientAmounts = new ArrayList<>()
        List<Ingredient> ingredients = new ArrayList<>()
        if (params.ingredients?.size() > 1 || params.ingredients instanceof String) {
            Ingredient foundI
            params.ingredients.each { String id ->
                foundI = Ingredient.findById ( id as Long )
                if (foundI?.id) ingredients << foundI
            }
            return ingredients
        } else {
            def options = (params.ingredients as String)?.split(':')
            if (options && options.size() > 2) {
                ingredientNames.add(options[0] as String)
                ingredientAmounts.add(Double.parseDouble(options[1]))
                ingredientUnits.add(Unit.valueOf((options[2] as String).trim()))
            } else {
                ingredientNames.add(params.ingredientName as String)
                ingredientUnits.add(Unit.valueOf((params.ingredientUnit as String).trim()))
                ingredientAmounts.add(Double.parseDouble(params.ingredientAmount as String))
            }
        }
        int createNum = ingredientNames.size()
        for (int i=0; i<createNum; i++) {
            Ingredient ingredient = new Ingredient([
                    name: ingredientNames.get(i),
                    unit: ingredientUnits.get(i),
                    amount: ingredientAmounts.get(i)
            ])
            if (alreadyExists(ingredient)) {
                ingredient = getExisting(ingredient)
                if (ingredient?.id) ingredients.add(ingredient)
            } else {
                ingredients.add(ingredient)
            }
        }
        return ingredients
    }

    /**
     * Tests if an ingredient already exists overall
     * If you pass in returnExists as true, it will return
     * the already existing ingredient, instead of a boolean
     * @param ingredient
     * @return
     */
    def alreadyExists(ingredient) {
        boolean exists = false
        List<Ingredient> ingredients = Ingredient.list()
        ingredients?.each {
            if (ingredient.compareTo(it) == 0) {
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
        Ingredient ingredient = createNewIngredientsFromParams(params).get(0)
        boolean result = false
        if (alreadyExists(ingredient)) { result = true }
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

    def saveValidIngredients(user) {
        if (!validIngredients) {
            validIngredients = createNewIngredientsFromParams(params)
        }
        def validIngredientIds = []
        validIngredients.eachWithIndex{ Ingredient i, int idx ->
            logger.info("${idx+1}) Ingredient ${i.name} being saved...")
            i = ingredientService.save(i, user, true)
            def result = user.ingredients.contains( i )
            validIngredientIds << i.id
            logger.info("Ingredient saved to user:: $result")
        }
        validIngredients.clear()
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
        switch (checkbox.toLowerCase()) {
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

    // TODO: move into its own service
    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def sendADrinkEmail() {
        if (!params) {
            badRequest()
        }
        if (request.method != 'GET') {
            badRequest()
        }
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        // Create email
        // Save email
        // Send email
        println "SimpleEmail Start"
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
        def bindMap = ['user':user, 'drink': Drink.findById(params.id as Long)]
        def emailTemplate = new File('grails-app/views/email/drinkEmail.gsp')
        def emailText = new GStringTemplateEngine().createTemplate(emailTemplate).make(bindMap)

        boolean sent = sendEmail(session, "aaronhunter@live.com", "Test Email", emailText.toString())
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
            redirect action:'show', params:[id:params.id], method: "GET", status: OK
            //redirect (view:'show', model:['id', params.id])
            //redirect show(params.id as Long)
        } else {
            withFormat {
                flash.message = 'There was an error sending the email'
                form multipartForm {
                    redirect show(params.id as Long)
                }
            }
        }
    }

    static boolean sendEmail(Session session, String toEmail, String subject, String body){
        try {
            MimeMessage msg = new MimeMessage(session);
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
            println "Message is ready"
            Transport.send(msg)

            println "Email Sent Successfully!!"
            return true
        }
        catch (Exception e) {
            e.printStackTrace()
            return false
        }
    }
}
