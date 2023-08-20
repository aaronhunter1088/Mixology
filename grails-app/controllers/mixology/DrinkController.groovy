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
import javax.servlet.http.HttpServletResponse

import static org.springframework.http.HttpStatus.*

class DrinkController extends BaseController {

    private static Logger logger = LogManager.getLogger(DrinkController.class)
    Set<Ingredient> validIngredients = new HashSet<Ingredient>()

    DrinkService drinkService
    IngredientService ingredientService
    def springSecurityService

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

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN','IS_AUTHENTICATED_ANONYMOUSLY'])
    def index(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        withFormat {
            html {
                respond drinkService.list(params), model:[drinkCount: drinkService.count()]
            }
            json { drinkService.list(params) as JSON }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def customIndex(Integer max) {
        params.max = Math.min(max ?: 5, 100)
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        List<Drink> customDrinks = user.drinks.collect()
        render view:'index', model:[drinkList:customDrinks,drinkCount:customDrinks.size()]
    }

    def show(Long id) {
        respond drinkService.get(id)
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','IS_AUTHENTICATED_FULLY'])
    def showCustomDrinks() {
        render(view:'customDrinks')
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def create() {
        Drink drink = new Drink(params)
        respond drink
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def save() {
        if (!params) {
            notFound('','')
            return
        }
        if (request.method != 'POST') {
            methodNotAllowed('','')
            return
        }
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        UserRole ur = UserRole.findByUserAndRole(user, Role.findByAuthority(enums.Role.ADMIN.name))
        Drink drink
        try {
            drink = createDrinkFromParams(params, ur)
            if (drink.isCustom() || ur?.role == enums.Role.ADMIN) {
                user.addToDrinks(drink)
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
                    flash.message = message(code: 'default.created.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName])
                    redirect drink
                }
                '*' { respond drink, [status: CREATED] }
            }
        }
        // clear the list
        validIngredients.clear()
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def edit(Long id) {
        Drink drink = drinkService.get(id)
        def user = User.findByUsername(springSecurityService.getPrincipal().username as String)
        def drinkIngredients = drink.ingredients
        def customIngredients = user.ingredients
        render view:'edit', model:[
                drink:drink,
                drinkIngredients:drinkIngredients,
                ingredients:customIngredients
        ]
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
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
        UserRole adminRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.getPrincipal().username as String), Role.findByAuthority(enums.Role.ADMIN.name))
        UserRole userRole = UserRole.findByUserAndRole(User.findByUsername(springSecurityService.getPrincipal().username as String), Role.findByAuthority(enums.Role.USER.name))
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
                validIngredients = createNewIngredientsFromParams(params)
                validIngredients.each{
                    drink.ingredients.add(it as Ingredient)
                }
                drink.ingredients.each { Ingredient i ->
                    if (!i.drinks) i.drinks = new HashSet<Drink>()
                    if (!i.drinks.contains(drink)) i.addToDrinks(drink as Drink)
                    //i.save()
                }
                // Find all ingredients currently associated with drink
                List<Long> associatedIngredientIds = drink.ingredients*.id as List<Long>
                logger.info("${associatedIngredientIds}")
                for(Long id : associatedIngredientIds) {
                    if ( !(id in drink.ingredients*.id)) {
                        logger.info("id ${id} not found in drink.ingredients. removing ${id} from drink: ${drink.drinkName}")
                        Ingredient i = Ingredient.findById(id)
                        i.removeFromDrinks(drink)
                    }
                }
                if (drink.validate()) {
                    drinkService.save(drink)
                    logger.info("default drink saved")
                }
            }
            // if is a custom drink and user has either role
            else if (drink.isCustom() && (!adminRole || !userRole)) {
                drink.drinkName = params.drinkName
                drink.drinkNumber = Integer.valueOf(params.drinkNumber as String)
                drink.alcoholType = Alcohol.valueOf(params.alcoholType as String)
                drink.drinkSymbol = params.drinkSymbol
                drink.suggestedGlass = GlassType.valueOf(params.glass as String)
                drink.mixingInstructions = params.instructions
                drink.version = (params.version as Long)
                validIngredients = createNewIngredientsFromParams(params)
                validIngredients.each{
                    drink.ingredients.add(it as Ingredient)
                }
                drink.ingredients.each { Ingredient i ->
                    if (!i.drinks) i.drinks = new HashSet<Drink>()
                    if (!i.drinks.contains(drink)) i.addToDrinks(drink)
                    //i.save()
                }
                List<Long> associatedIngredientIds = drink.ingredients*.id as List<Long>
                logger.info("${associatedIngredientIds}")
                for(Long id : associatedIngredientIds) {
                    if ( !(id in drink.ingredients*.id)) {
                        logger.info("id ${id} not found in drink.ingredients. removing ${id} from drink: ${drink.drinkName}")
                        Ingredient i = Ingredient.findById(id)
                        i.removeFromDrinks(drink as Drink)
                    }
                }
                if (drink.validate()) {
                    drinkService.save(drink)
                    logger.info("custom drink saved!")
                }
            }
            else {
                drink.errors.reject('default.updated.error.message', [drink.drinkName] as Object[], '')
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
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName])
                    respond drink, view:'edit'
                }
                '*'{ respond drink, [status: OK] }
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
    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def copy(Drink drink) {
        if (!drink) {
            notFound('','')
            return
        }
        def user = User.findByUsername(springSecurityService.getPrincipal()?.username as String)
        if (user) {
            logger.info("we have a user logged in ${user.firstName} ${user.lastName}")
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
            copied.ingredients.each { newIngredient ->
                ingredientService.save(newIngredient, user, true)
            }
            Drink.withSession {
                Drink.withTransaction {
                    logger.info("drink has been copied")
                    //copied.ingredients.each { it.save(flush:true) }
                    //copied.ingredients.each { ingredientService.save(it, user, true) }
                    //copied.save(flush:true)
                    drinkService.save(copied, user)
                    logger.info("drink has been saved")
//                    User.withSession {
//                        User.withTransaction {
//                            logger.info("adding $drink to user")
//                            user.addToDrinks(copied)
//                            user.save(flush:true, validate:false)
//                        }
//                    }
                }
            }
            if (!user.hasErrors()) {
                flash.message = message(code: 'default.copied.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName, user], default: "Copied $copied to $user. You can edit your version as you see fit.") as Object
                request.withFormat {
                    form multipartForm {
                        respond drink, view:'show'
                    }
                    '*'{ respond drink, view:'show' }
                }
            }
            else {
                logger.error("There was validation errors [{}] on the user", user.errors.allErrors.size())
                request.withFormat {
                    form multipartForm {
                        respond drink.errors, view:'show'
                        //redirect(action:'show', params:[id:drink.id])
                    }
                    '*' { respond drink.errors, view:'show' }
                }
            }
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_USER'])
    def delete(Long id) {
        if (!id) {
            notFound('','')
            return
        }
        Drink drink = drinkService.get(id)
        if (drink.canBeDeleted) {
            // do not remove ingredient here
            // ingredient removed when it is called to be
            // we are deleting drink, not ingredient
            try {
                Drink.withNewTransaction { drinkService.delete(id) }
            } catch (Exception e) {
                drink.errors.reject('default.deleted.error2.message',
                    [drink.drinkName] as Object[],
                    "There was an exception deleting the drink: ${e.message}"
                )
            }
        } else {
            drink.errors.reject('default.deleted.error.message', [drink.drinkName] as Object[], '')
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
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'drink.label', default: 'Drink'), drink.drinkName])
                    redirect action:"index", method:"GET"
                }
                '*'{ render status: NO_CONTENT }
            }
        }
    }

    /**
     * Creates and returns a saved Drink
     * @param params
     * @param role
     * @return
     */
    def createDrinkFromParams(params, role) throws ValidationException {
        validIngredients = createNewIngredientsFromParams(params)
        //extractIngredientsFromParams(params)

        Drink drink = new Drink([
                drinkName: params.drinkName,
                drinkNumber: params.drinkNumber as Integer,
                alcoholType: Alcohol.valueOf(params.alcoholType as String),
                drinkSymbol: params.drinkSymbol,
                suggestedGlass: GlassType.valueOf(params.glass as String),
                mixingInstructions: params.instructions,
                ingredients: validIngredients
                ,custom: Boolean.valueOf(params.custom as String)
                ,canBeDeleted: ( Boolean.valueOf(params.canBeDeleted as String) && Boolean.valueOf(params.custom as String) )
        ])
        if ( !role && !role?.role == enums.Role.ADMIN) {
            drink.canBeDeleted = true
            drink.custom = true
        }
        if (!drink.save(failOnError:true)) {
            throw new ValidationException("Failed to save drink", drink.errors)
        }
        // Associate all ingredients with this drink
        validIngredients.each { Ingredient it ->
            if (!it.drinks) it.drinks = new HashSet<Drink>()
            if (!it.drinks.contains(drink)) it.addToDrinks(drink)
        }
        return drink
    }

    /**
     * Creates 1 or more ingredients
     * @param params
     * @return
     */
    def createNewIngredientsFromParams(params) {
        List<String> ingredientNames = new ArrayList<>()
        List<String> units = new ArrayList<>()
        List<Double> ingredientAmounts = new ArrayList<>()
        List<Ingredient> ingredients = new ArrayList<>()
        if (params.ingredientName?.size() > 1 && !(params.ingredientName instanceof String)) {
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
     * Called from the UI which validates each
     * ingredient, created while creating a new drink
     * @param params
     * @return
     */
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
