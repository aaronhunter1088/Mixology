package mixology.unit

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import mixology.Drink
import mixology.DrinkController
import mixology.DrinkService
import mixology.Ingredient
import mixology.IngredientService
import mixology.Role
import mixology.User
import mixology.UserRole
import org.junit.Test
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
class DrinkControllerSpec extends Specification implements ControllerUnitTest<DrinkController>, DataTest {

    Class<?>[] getDomainClassesToMock(){ return [Drink, Ingredient, User] as Class[] }

    Drink drink1, drink2
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)

    Set<Ingredient> createIngredientsWithAAndBAndC() {
        Set<Ingredient> ingredients = []
        ingredientA = createAIngredient()
        ingredientB = createBIngredient()
        ingredientC = createCIngredient()
        return ingredients << ingredientA << ingredientB << ingredientC
    }
    Set<Ingredient> createIngredientsWithVodkaAndDAndE() {
        Set<Ingredient> ingredients = []
        vodka = createVodkaIngredient()
        ingredientD = createDIngredient()
        ingredientE = createEIngredient()
        return ingredients << vodka << ingredientD << ingredientE
    }

    def vodka
    def createVodkaIngredient = {
        if ( !vodka ) {
            vodka = new Ingredient([
                    name: 'Vodka',
                    unit: Unit.OZ,
                    amount: 1.5
            ])
        }
        else if (!vodka.idIsNull()) {
            vodka
        }
    }
    def ingredientA
    def createAIngredient = {
        if ( !ingredientA ) {
            ingredientA = new Ingredient([
                    name: 'IngredientA',
                    unit: Unit.OZ,
                    amount: 1.5
            ])
        }
        else if (!ingredientA.idIsNull()) {
            ingredientA
        }
    }
    def ingredientB
    def createBIngredient = {
        if ( !ingredientB ) {
            ingredientB = new Ingredient([
                    name: 'IngredientB',
                    unit: Unit.OZ,
                    amount: 1.5
            ])
        }
        else if ( !ingredientB.idIsNull()) {
            ingredientB
        }
    }
    def ingredientC
    def createCIngredient = {
        if ( !ingredientC ) {
            ingredientC = new Ingredient([
                    name: 'IngredientC',
                    unit: Unit.OZ,
                    amount: 1.5
            ])
        }
        else if ( !ingredientC.idIsNull() ) {
            ingredientC
        }
    }
    def ingredientD
    def createDIngredient = {
        if ( !ingredientD ) {
            ingredientD = new Ingredient([
                    name: 'IngredientD',
                    unit: Unit.OZ,
                    amount: 1.5
            ])
        }
        else if ( !ingredientD.idIsNull() ) {
            ingredientD
        }
    }
    def ingredientE
    def createEIngredient = {
        if ( !ingredientE ) {
            ingredientE = new Ingredient([
                    name: 'IngredientE',
                    unit: Unit.OZ,
                    amount: 1.5
            ])
        }
        else if ( !ingredientE.idIsNull() ) {
            ingredientE
        }
    }

    def setup() {
        mockDomain Drink
        mockDomain Ingredient
        mockDomain User
        drink1 = new Drink([
                drinkName: 'Drink1',
                drinkNumber: 1,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.HURRICANE,
                alcoholType: Alcohol.TEQUILA,
                drinkSymbol: 'D1',
                ingredients: createIngredientsWithAAndBAndC(),
                canBeDeleted: true,
                custom: true
        ])
        drink2 = new Drink([
                drinkName: 'Drink2',
                drinkNumber: 2,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.SHOT,
                alcoholType: Alcohol.TEQUILA,
                drinkSymbol: 'D2',
                ingredients: createIngredientsWithVodkaAndDAndE(),
                canBeDeleted: true,
                custom: true
        ])
        // save all ingredients
        drink1.ingredients.each { ingredientService.save(it) }
        drink2.ingredients.each { ingredientService.save(it) }
        drinkService.save(drink1)
        drinkService.save(drink2)
        controller.drinkService = drinkService
    }

    def cleanup() {
        Ingredient.all.each { it.delete()}
        println "Total ingredients after cleanup: ${Ingredient.count()}"
    }

    @Test
    void "setup created two drinks with three ingredients"() {
        expect:"Drink1.id is not null"
            drink1.id != null
        println "DrinkID: ${drink1.id}"
        and:"Drink1 has three ingredients"
            drink1.ingredients.size() == 3
        println "Drink1 Ingredients Size: ${drink1.ingredients.size()}"
        and:"Drink2.id is not null"
            drink2.id != null
        println "DrinkID: ${drink2.id}"
        and:"Drink2 has three ingredients"
        drink2.ingredients.size() == 3
        println "Drink2 Ingredients Size: ${drink2.ingredients.size()}"
    }

    @Test
    void "delete one drink doesn't delete other drink"() {
        given:"Two drinks exist"
            println "# of drinks: ${drinkService.count()}"
            drinkService.count() == 2
        when:
            request.method = 'DELETE'
            controller.delete(drink1.id)
        then:"A single drink should still exist"
//            controller.drinkService = Stub(DrinkService) {
//                delete(_) >> drink1.delete(flush:true)
//            }
            println "# of drinks: ${drinkService.count()}"
            drinkService.count() == 1
        and:"Drink1 no longer exists"
            !Drink.exists(drink1.id)
        and:"Drink2 exists"
            Drink.findById(2) instanceof Drink
            println "${drink2}"
        and:"6 ingredients still exists"
            ingredientService.count() == 6
            println "# of ingredients: ${ingredientService.count()}"
    }

    @Test
    void "test index action"() {
        given:
        List<Drink> drinks = [drink1, drink2]
        controller.drinkService = Stub(DrinkService) {
            list(_) >> drinks
            count() >> drinks.size()
        }

        when: 'call controller.index'
            controller.index()

        then:
            model.drinkList == drinks
            model.drinkCount == drinks.size()
    }

    @Test
    void "test customIndex action"() {
        given:
        Set<Drink> drinks = [drink1, drink2]
        def user = new User([
            username: "testusername@gmail.com",
            firstName: "test",
            lastName: "user"
        ]).save(validate:false)
        user.drinks = drinks

        controller.drinkService = Stub(DrinkService) {
            list(_) >> drinks
            count() >> drinks.size()
        }
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
//            getAuthentication() >> Stub(Authentication) {
//
//            }
        }

        when: 'call controller.index'
        controller.customIndex()

        then:
        User.findByUsername(user.username) >> user
        model.drinkList == drinks as List
        model.drinkCount == drinks.size()
        user.drinks.size() == 2
    }

    @Test
    void "test show action"() {
        given:
        def drink = Stub(Drink) {
            id >> 1
        }
        controller.drinkService = Stub(DrinkService) {
            get(_) >> drink
        }

        when:
        controller.show(1)

        then:
        response.status == 200
    }

    @Test
    void "test showCustomDrinks action"() {
        when:
        controller.showCustomDrinks()

        then:
        view == '/drink/customDrinks'
    }

    @Test
    void "test create action"() {
        when:
        controller.create()

        then:
        response.status == 200
    }

    @Test
    void "test save fails because method is GET"() {
        when:
        request.method = 'GET'
        controller.save()

        then:
        response.status == 405
    }

    @Test
    void "test save fails because method is PUT"() {
        when:
        request.method = 'PUT'
        controller.save()

        then:
        response.status == 405
    }

    @Test
    void "test save fails because method is DELETE"() {
        when:
        request.method = 'DELETE'
        controller.save()

        then:
        response.status == 405
    }

    @Test
    void "test save fails because it is a default drink"(){
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        user.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = drinkService

        when:
        request.method = 'POST'
        controller.params.drinkName = 'drinkNameTest'
        controller.params.drinkNumber = '1'
        controller.params.alcoholType = 'VODKA'
        controller.params.drinkSymbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        //controller.params.ingredients = "100 Proof Vodka : 1.5 : OZ"
        controller.params.ingredientName = '100 Proof Vodka'
        controller.params.ingredientAmount = 1.5
        controller.params.ingredientUnit = 'OZ'
        controller.params.custom = false
        controller.params.canBeDeleted = false
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 403
    }

    @Test
    void "test save drink fails validation"() {
        //NEW
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        user.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = drinkService
//        controller.metaClass.createDrinkFromParams { params, aRole ->
//            Drink drink = new Drink()
//            Errors error = new BeanPropertyBindingResult(drink, "Test drink")
//            drink.errors << error
//            return new ValidationException("test save drink fails validation", error)
//        }

        when:
        request.method = 'POST'
        controller.params.drinkName = ''
        controller.params.drinkNumber = 1
        controller.params.alcoholType = Alcohol.VODKA.name()
        controller.params.drinkSymbol = ''
        controller.params.instructions = ''
        controller.params.glass = GlassType.GOBLET.name()
        //controller.params.ingredients = ''
        controller.params.ingredientName = '100 Proof Vodka'
        controller.params.ingredientAmount = 1.5
        controller.params.ingredientUnit = 'OZ'
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 400
    }

    @Test
    void "test save action with one ingredient"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        user.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = drinkService

        when:
        request.method = 'POST'
        controller.params.drinkName = 'drinkNameTest'
        controller.params.drinkNumber = '1'
        controller.params.alcoholType = 'VODKA'
        controller.params.drinkSymbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        //controller.params.ingredients = "100 Proof Vodka : 1.5 : OZ"
        controller.params.ingredientName = '100 Proof Vodka'
        controller.params.ingredientAmount = 1.5
        controller.params.ingredientUnit = 'OZ'
        controller.params.custom = true
        controller.params.canBeDeleted = true
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 201
    }

    @Test
    void "test save action with multiple ingredients"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        user.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = drinkService

        when:
        request.method = 'POST'
        controller.params.drinkName = 'vodkaOrangeJuice'
        controller.params.drinkNumber = '1'
        controller.params.alcoholType = 'VODKA'
        controller.params.drinkSymbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        //controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
        controller.params.ingredientName = ['100 Proof Vodka', 'Orange Juice']
        controller.params.ingredientAmount = [1.5, 1.5]
        controller.params.ingredientUnit = ['OZ', 'SPLASH']
        controller.params.custom = true
        controller.params.canBeDeleted = true
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 201
    }

    @Test
    void "test edit action"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user",
                password: 'p@ssword1',
                email: "testusername@gmail.com"
        ]).save(validate:false)
        def drink = Stub(Drink) {
            id >> 1
        }
        controller.drinkService = Stub(DrinkService) {
            get(_) >> drink
        }
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
        controller.edit(1)

        then:
        response.status == 200
    }

    @Test
    void "test update fails because method is GET"() {
        when:
        request.method = 'GET'
        controller.update(drink1)

        then:
        response.status == 405
    }

    @Test
    void "test update fails because method is POST"() {
        when:
        request.method = 'POST'
        controller.update(drink1)

        then:
        response.status == 405
    }

    @Test
    void "test update fails because method is DELETE"() {
        when:
        request.method = 'DELETE'
        controller.update(drink1)

        then:
        response.status == 405
    }

    @Test
    void "test updating a default drink"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user",
                password: 'p@ssword1',
                email: "testusername@gmail.com"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        user.drinks = new HashSet<Drink>()
        user.addToDrinks(drink1 as Drink)
        drink1.custom = false
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = Stub(DrinkService) { save(_) >> drink1}
        User.findByUsername(user.username) >> user
        Role.findByAuthority('ROLE_ADMIN') >> role

        when:
        request.method = 'PUT'
        controller.params.drinkName = 'updatedName'
        controller.params.drinkNumber = '11'
        controller.params.alcoholType = 'VODKA'
        controller.params.drinkSymbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        //controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
        controller.params.ingredientName = ['100 Proof Vodka', 'Orange Juice']
        controller.params.ingredientAmount = [1.5, 1.5]
        controller.params.ingredientUnit = ['OZ', 'SPLASH']
        controller.params.version = 2l
        controller.update(drink1 as Drink)

        then:
        drink1.drinkName == 'updatedName'
        drink1.drinkNumber == 11
        drink1.alcoholType == Alcohol.VODKA
        drink1.drinkSymbol == 'TD'
        drink1.mixingInstructions == 'Test instructions'
        drink1.suggestedGlass == GlassType.HIGHBALL
        drink1.ingredients.size() == 5
        response.status == 200
    }

    @Test
    void "test updating a custom drink"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user",
                password: 'p@ssword1',
                email: "testusername@gmail.com"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        user.drinks = new HashSet<Drink>()
        user.addToDrinks(drink1 as Drink)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = Stub(DrinkService) { save(_) >> drink1}
        User.findByUsername(user.username) >> user
        Role.findByAuthority('ROLE_USER') >> role

        when:
        request.method = 'PUT'
        controller.params.drinkName = 'updatedName'
        controller.params.drinkNumber = '11'
        controller.params.alcoholType = 'VODKA'
        controller.params.drinkSymbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        //controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
        controller.params.ingredientName = ['100 Proof Vodka', 'Orange Juice']
        controller.params.ingredientAmount = [1.5, 1.5]
        controller.params.ingredientUnit = ['OZ', 'SPLASH']
        controller.params.version = 2l
        controller.update(drink1 as Drink)

        then:
        drink1.drinkName == 'updatedName'
        drink1.drinkNumber == 11
        drink1.alcoholType == Alcohol.VODKA
        drink1.drinkSymbol == 'TD'
        drink1.mixingInstructions == 'Test instructions'
        drink1.suggestedGlass == GlassType.HIGHBALL
        drink1.ingredients.size() == 5
        response.status == 200
    }

    @Test
    void "test copy drink duplicates exactly"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user",
                password: 'p@ssword1',
                email: "testusername@gmail.com"
        ]).save(validate:false)
        user.drinks = new HashSet<Drink>()
        //User.findByUsername(user.username) >> user
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.drinkService = drinkService
        controller.ingredientService = ingredientService
        user.drinks.size() == 0

        when:
        controller.copy(drink1)

        then:
        user.drinks.size() == 1
        def copied = user.drinks[0]
        copied.drinkName == drink1.drinkName
        copied.drinkNumber == drink1.drinkNumber
        copied.alcoholType == drink1.alcoholType
        copied.drinkSymbol == drink1.drinkSymbol
        copied.mixingInstructions == drink1.mixingInstructions
        copied.suggestedGlass == drink1.suggestedGlass
        copied.id != drink1.id
        copied.ingredients.size() == drink1.ingredients.size()
        copied.ingredients.each {Ingredient copiedIng ->
            drink1.ingredients.each {Ingredient originalIng ->
                copiedIng.name == originalIng.name
                copiedIng.unit == originalIng.unit
                copiedIng.amount == originalIng.amount
                copiedIng.id != originalIng.id
            }
        }
    }

    @Test
    void "test delete drink deletes only drink"() {
        given:
        def numOfDrinks = drinkService.count()
        def drinkIngredients = drink1.ingredients as List<Ingredient>
        request.method = 'DELETE'

        when:
        controller.delete(drink1.id)

        then:
        assert drinkService.count() == numOfDrinks - 1
        drinkIngredients?.each {
            assert it.id
        }
        response.status == 204
    }

    @Test
    void "test already existing ingredient returns true"() {
        boolean result
        when:
            result = controller.alreadyExists(ingredientA)

        then:
            assert result
    }

    @Test
    void "test validateIngredients returns bad request"() {
        controller.params.apiCallCount = 1
        controller.metaClass.createNewIngredientsFromParams { params ->
            return [ingredientA]
        }
        when:
            controller.validateIngredients(params)

        then:
            response.status == 400
    }

    @Test
    void "test validateIngredients returns ok"() {
        controller.params.apiCallCount = 1
        controller.metaClass.createNewIngredientsFromParams { params ->
            return [new Ingredient([
                    name: 'New Ingredient',
                    unit: 'WEDGE',
                    amount: 2.5
            ])]
        }
        when:
        controller.validateIngredients(params)

        then:
        response.status == 200
    }

    @Test
    void "test not found returns 404"() {
        when:
            controller.notFound('','')

        then:
            response.status == 404
    }
}
