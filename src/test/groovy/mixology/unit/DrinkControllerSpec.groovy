package mixology.unit

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import mixology.Drink
import mixology.DrinkController
import mixology.DrinkService
import mixology.Ingredient
import mixology.IngredientService
import mixology.Role
import mixology.RoleService
import mixology.User
import mixology.UserRole
import mixology.UserRoleService
import mixology.UserService
import org.grails.orm.hibernate.query.PagedResultList
import org.junit.Test
import org.springframework.test.context.ContextConfiguration
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import spock.lang.Specification

@ContextConfiguration
class DrinkControllerSpec extends Specification implements ControllerUnitTest<DrinkController>, DataTest {

    Class<?>[] getDomainClassesToMock(){ return [Drink, Ingredient, User] as Class[] }

    Drink drink1, drink2
    User adminUser, testUser
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)
    def userService = getDatastore().getService(UserService)
    def roleService = getDatastore().getService(RoleService)
    def userRoleService = getDatastore().getService(UserRoleService)

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
        drink1 = new Drink([
                name: 'Drink1',
                number: 1,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.HURRICANE,
                alcoholType: Alcohol.TEQUILA,
                symbol: 'D1',
                ingredients: createIngredientsWithAAndBAndC(),
                canBeDeleted: true,
                custom: true
        ])
        drink2 = new Drink([
                name: 'Drink2',
                number: 2,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.SHOT,
                alcoholType: Alcohol.TEQUILA,
                symbol: 'D2',
                ingredients: createIngredientsWithVodkaAndDAndE(),
                canBeDeleted: true,
                custom: true
        ])
        // save all ingredients
        drink1.ingredients.each { ingredientService.save(it, false) }
        drink2.ingredients.each { ingredientService.save(it, false) }
        //ingredientService.save(createDefaultIngredient(), false)
        drinkService.save(drink1, false)
        drinkService.save(drink2, false)

        def roleAdmin = roleService.save(enums.Role.ADMIN.name)
        adminUser = new User([
                username: "adminuser@gmail.com",
                firstName: "admin",
                lastName: "user",
                password: 'p@ssword1',
                email: "adminuser@gmail.com"
        ]).save(validate:false)
        userRoleService.save(adminUser, roleAdmin)

        def roleUser = roleService.save(enums.Role.USER.name)
        testUser = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        userRoleService.save(testUser, roleUser)

        controller.drinkService = drinkService
        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.roleService = roleService
        controller.userRoleService = userRoleService
    }

    def cleanup() {
        //Ingredient.all.each { it.delete()}
        //println "Total ingredients after cleanup: ${Ingredient.count()}"
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
        println "# of drinks: ${controller.drinkService.count()}"
        assert controller.drinkService.count() == 2
        drink1.user = testUser
        testUser.addToDrinks(drink1).save(flush:true)

        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}

        when:
        request.method = 'DELETE'
        controller.delete(drink1.id)

        then:"Drink1 no longer exists"
        assert !Drink.exists(drink1.id)
        and:"Drink2 exists"
        println "${drink2}"
        assert Drink.exists(2)
        and:"A single drink should still exist"
        println "# of drinks: ${controller.drinkService.count()}"
        assert controller.drinkService.count() == 1
        and:"6 ingredients still exists"
        println "# of ingredients: ${ingredientService.count()}"
        assert ingredientService.count() == 6
    }

    @Test
    void "test index action"() {
        given:
        List<Drink> drinks = [drink1, drink2]
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

        when: 'call controller.index'
        controller.index()

        then:
        assert model.drinkList == drinks
        assert model.drinkCount == drinks.size()
    }

    @Test
    void "test customIndex action"() {
        given:
        testUser.addToDrinks(drink1).addToDrinks(drink2).save(flush:true)

        def myCriteria = [
                //list : {Map args, Closure  cls -> testUser*.drinks as PagedResultList}
                list : {Map args, Closure cls ->
                    [
                            resultList: testUser.drinks,
                            totalCount: testUser.drinks.size()
                    ]
                }
        ]
        Drink.metaClass.'static'.createCriteria = { myCriteria }
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}

        when: 'call controller.index'
        controller.customIndex()

        then:
        assert model.drinkCount == testUser.drinks.size()
        assert (model.drinkList).resultList as ArrayList == (testUser.drinks as List)
    }

    @Test
    void "test show action"() {
        given:
        drink1.user = testUser
        drink1.save()
        testUser.addToDrinks(drink1).save(flush:true)

        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

        when:
        controller.show(1)

        then:
        response.status == 200
    }

    @Test
    void "test showCustomDrinks action"() {
        given:
        testUser.addToDrinks([drink1:drink1, drink2:drink2])
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

        when:
        controller.showCustomDrinks()

        then:
        view == '/drink/customDrinks'
    }

    @Test
    void "test create action"() {
        given:
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

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
        Drink defaultDrink = new Drink([custom:false,canBeDeleted:false])
        controller.metaClass.createDrinkFromParams = {p1,p2,p3 -> defaultDrink}
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        request.method = 'POST'

        when:
        controller.params.drink = defaultDrink
        controller.save()

        then:
        response.status == 403
    }

    @Test
    void "test save drink fails for validation errors"() {
        given:
        testUser.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        controller.metaClass.createDrinkFromParams { params, uzr, aRole ->
            Drink drink = new Drink()
            Errors error = new BeanPropertyBindingResult(drink, "Test drink")
            drink.errors = error
            throw new ValidationException("test save drink fails validation", error)
        }

        when:
        request.method = 'POST'
        controller.params.something = "must_be_here"
        controller.save()

        then:
        response.status == 400
    }

    @Test
    void "test save action with one ingredient"() {
        given:
        testUser.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

        when:
        request.method = 'POST'
        controller.params.name = 'drinkNameTest'
        controller.params.number = '1'
        controller.params.alcoholType = 'VODKA'
        controller.params.symbol = 'TD'
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
        response.status == 201
    }

    @Test
    void "test save action with multiple ingredients"() {
        given:
        testUser.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        controller.drinkService = Stub(DrinkService) {save(_,_,_)>> null}
        controller.metaClass.saveValidIngredients = { uzr -> [1, 2]}

        when:
        request.method = 'POST'
        controller.params.name = 'vodkaOrangeJuice'
        controller.params.number = '1'
        controller.params.alcoholType = 'VODKA'
        controller.params.symbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        controller.params.ingredients = ["1", "2"] // Long IDs of ingredients
        controller.save()

        then:
        response.status == 201
    }

    @Test
    void "test edit action"() {
        given:
        testUser.ingredients = createIngredientsWithAAndBAndC()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
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
        drink1.custom = false
        drink1.canBeDeleted = false
        drink1.save(flush:true)
        adminUser.drinks = new HashSet<Drink>()
        adminUser.addToDrinks(drink1)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> adminUser
        }
        controller.drinkService = Stub(DrinkService) { save(_) >> drink1}

        when:
        request.method = 'PUT'
        controller.params.name = 'updatedName'
//        controller.params.drinkNumber = '11'
//        controller.params.alcoholType = 'VODKA'
//        controller.params.drinkSymbol = 'TD'
//        controller.params.instructions = 'Test instructions'
//        controller.params.glass = 'HIGHBALL'
        controller.params.ingredients = ["1", "2"]
//        controller.params.ingredientName = ['100 Proof Vodka', 'Orange Juice']
//        controller.params.ingredientAmount = [1.5, 1.5]
//        controller.params.ingredientUnit = ['OZ', 'SPLASH']
//        controller.params.version = 2l
        controller.update(drink1 as Drink)

        then:
        drink1.name == 'updatedName'
        response.status == 200
    }

    @Test
    void "test updating a custom drink"() {
        given:
        testUser.drinks = new HashSet<Drink>()
        testUser.addToDrinks(drink1)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

        when:
        request.method = 'PUT'
        controller.params.drinkName = 'updatedName'
        controller.params.drinkNumber = '11'
        controller.params.alcoholType = 'VODKA'
        controller.params.drinkSymbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        controller.params.ingredients = ["1", "2", "3", "4", "5"]
//        controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
//        controller.params.ingredientName = ['100 Proof Vodka', 'Orange Juice']
//        controller.params.ingredientAmount = [1.5, 1.5]
//        controller.params.ingredientUnit = ['OZ', 'SPLASH']
//        controller.params.version = 2l
        controller.update(drink1 as Drink)

        then:
        drink1.name == 'updatedName'
        drink1.number == 11
        drink1.alcoholType == Alcohol.VODKA
        drink1.symbol == 'TD'
        drink1.mixingInstructions == 'Test instructions'
        drink1.suggestedGlass == GlassType.HIGHBALL
        drink1.ingredients.size() == 5
        response.status == 200
    }

    @Test
    void "test copy drink duplicates exactly"() {
        given:
        testUser.drinks = new HashSet<Drink>()
        //User.findByUsername(user.username) >> user
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        assert testUser.drinks.size() == 0

        when:
        controller.copy(drink1)

        then:
        assert testUser.drinks.size() == 1
        def copied = testUser.drinks[0]
        assert copied.name == drink1.name
        assert copied.number == drink1.number
        assert copied.alcoholType == drink1.alcoholType
        assert copied.symbol == drink1.symbol
        assert copied.mixingInstructions == drink1.mixingInstructions
        assert copied.suggestedGlass == drink1.suggestedGlass
        assert copied.id != drink1.id
        assert copied.ingredients.size() == drink1.ingredients.size()
        copied.ingredients.each {Ingredient copiedIng ->
            drink1.ingredients.each {Ingredient originalIng ->
                if (copiedIng.name == originalIng.name) {
                    assert copiedIng.name == originalIng.name
                    assert copiedIng.unit == originalIng.unit
                    assert copiedIng.amount == originalIng.amount
                    assert copiedIng.id != originalIng.id
                }
            }
        }
    }

//    @Test
//    void "test delete drink deletes only drink"() {
//        given:
//        def numOfDrinks = drinkService.count()
//        def drinkIngredients = drink1.ingredients as List<Ingredient>
//        def user = new User([
//                username: "testusername@gmail.com",
//                firstName: "test",
//                lastName: "user"
//        ]).save(validate:false)
//        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> user}
//        controller.drinkService.springSecurityService = Stub(SpringSecurityService) {
//            getPrincipal() >> user
//        }
//
//        when:
//        request.method = 'DELETE'
//        controller.delete(drink1.id)
//
//        then:
//        assert drinkService.count() == numOfDrinks - 1
//        drinkIngredients?.each {
//            assert it.id
//        }
//        response.status == 204
//    }

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
