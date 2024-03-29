package mixology.unit

import enums.Alcohol
import enums.GlassType
import enums.Role
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import mixology.Drink
import mixology.DrinkController
import mixology.DrinkService
import mixology.Ingredient
import mixology.User
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.Test
import org.springframework.test.context.ContextConfiguration
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import spock.lang.Unroll

import static org.springframework.http.HttpStatus.*

@ContextConfiguration
class DrinkControllerSpec extends BaseController implements ControllerUnitTest<DrinkController> {

    private static final Logger logger = LogManager.getLogger(DrinkControllerSpec.class)

    Class<?>[] getDomainClassesToMock(){ return [Drink, Ingredient, User] as Class[] }

    Drink drink1, drink2
    User adminUser, testUser

    def vodka = createIngredient('Vodka')
    def ingredientA = createIngredient('A')
    def ingredientB = createIngredient('B')
    def ingredientC = createIngredient('C')
    def ingredientD = createIngredient('D')
    def ingredientE = createIngredient('E')

    Set<Ingredient> ingredientsAAndBAndC() {
        Set<Ingredient> ingredients = []
        return ingredients << ingredientA << ingredientB << ingredientC
    }
    Set<Ingredient> ingredientsVodkaAndDAndE() {
        Set<Ingredient> ingredients = []
        return ingredients << vodka << ingredientD << ingredientE
    }

    def setup() {
        drink1 = new Drink([
                name: 'Drink1',
                number: 1,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.HURRICANE,
                alcohol: Alcohol.TEQUILA,
                symbol: 'D1',
                ingredients: ingredientsAAndBAndC(),
                canBeDeleted: true,
                custom: true
        ])
        drink2 = new Drink([
                name: 'Drink2',
                number: 2,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.SHOT,
                alcohol: Alcohol.TEQUILA,
                symbol: 'D2',
                ingredients: ingredientsVodkaAndDAndE(),
                canBeDeleted: false,
                custom: false
        ])
        // save all ingredients
        drink1.ingredients.each { ingredientService.save(it, false) }
        drink2.ingredients.each { ingredientService.save(it, false) }
        //ingredientService.save(createDefaultIngredient(), false)
        drinkService.save(drink1, false)
        drinkService.save(drink2, false)

        def roleAdmin = roleService.save(Role.ADMIN.name)
        adminUser = createUser('admin')
        userRoleService.save(adminUser, roleAdmin)

        def roleUser = roleService.save(Role.USER.name)
        testUser = createUser('regular')
        userRoleService.save(testUser, roleUser)

        controller.drinkService = drinkService
        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.roleService = roleService
        controller.userRoleService = userRoleService
        controller.validIngredients = new HashSet<>()
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
        logger.info("# of drinks: ${controller.drinkService.count()}")
        assert controller.drinkService.count() == 2
        drink1.user = testUser
        testUser.addToDrinks(drink1).save(flush:true)
        request.method = 'DELETE'
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
        controller.params.something = 'hasToBeHere'

        when:
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

    // Test Index
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

    // Test Custom Index
    @Test
    void "test customIndex action"() {
        given:
        testUser.addToDrinks(drink1).addToDrinks(drink2).save(flush:true)

        def myCriteria = [
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

    // Test Show Drink
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

    // Test Show all Drinks
    @Test
    void "test show all drinks"() {
        when:
        controller.showDrinks()

        then:
        response.status == FOUND.value()
    }

    // Test Show Custom Drinks
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

    // Test Create
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

    // Test Saving
    @Unroll('controller.save(): #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test save fails because wrong request method"() {
        when:
        if (value != 'POST') {
            controller.params.something = 'hasToBeHere'
        }
        request.method = value
        controller.save()
        then:
        expected.value() == response.status
        where:
        value   | expected              | expectedErrorCode
        'GET'   | METHOD_NOT_ALLOWED    | ''
        'PUT'   | METHOD_NOT_ALLOWED    | ''
        'DELETE'| METHOD_NOT_ALLOWED    | ''
        'POST'  | NOT_FOUND             | ''
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
        response.status == 400
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
            return drink //throw new ValidationException("test save drink fails validation", error)
        }

        when:
        request.method = 'POST'
        controller.params.something = "hasToBeHere"
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
        controller.params.alcohol = 'VODKA'
        controller.params.symbol = 'TD'
        controller.params.mixingInstructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        //controller.params.ingredients = "100 Proof Vodka : 1.5 : OZ"
        controller.params.ingredientName = '100 Proof Vodka'
        controller.params.ingredientAmount = 1.5
        controller.params.ingredientUnit = 'OZ'
        controller.params.custom = true
        controller.params.canBeDeleted = true
        controller.save()

        then:
        response.status == 302
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
        controller.params.alcohol = 'VODKA'
        controller.params.symbol = 'TD'
        controller.params.instructions = 'Test instructions'
        controller.params.glass = 'HIGHBALL'
        controller.params.ingredients = ["1", "2"] // Long IDs of ingredients
        controller.save()

        then:
        response.status == 302
    }

    // Test Editing
    @Test
    void "test edit action"() {
        given:
        testUser.ingredients = ingredientsAAndBAndC()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }

        when:
        controller.edit(1)

        then:
        response.status == 200
    }

    // Test Updating
    @Unroll('controller.update(): #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test update fails because wrong request method or params not supplied"() {
        when:
        if (value != 'PUT') {
            controller.params.something = 'hasToBeHere'
        }
        request.method = value
        controller.update()
        then:
        expected.value() == response.status
        where:
        value   | expected              | expectedErrorCode
        'GET'   | METHOD_NOT_ALLOWED    | ''
        'POST'  | METHOD_NOT_ALLOWED    | ''
        'DELETE'| METHOD_NOT_ALLOWED    | ''
        'PUT'   | NOT_FOUND             | ''
    }

    @Test
    void "test update fails because of validation errors"() {
        given:
        testUser.drinks = new HashSet<Drink>()
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        controller.drinkService = Stub(DrinkService) {save(_,_,_) >> {
            Errors error = new BeanPropertyBindingResult(new Drink(), "Test drink")
            return new ValidationException("test update drink fails validation", error)
        }}
        request.method = 'PUT'
        controller.params.id = drink1.id
        controller.params.something = "hasToBeHere"

        when:
        controller.update()

        then:
        response.status == 400
    }

    @Test
    void "test updating a default drink"() {
        given:
        adminUser.addToDrinks(drink2)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> adminUser
        }
        controller.drinkService = Stub(DrinkService) { get(_) >> drink2; save(_) >> drink2}
        request.method = 'PUT'
        controller.params.id = drink2.id
        controller.params.drinkName = 'updatedName'
        controller.params.ingredients = ["4", "5"]

        when:
        controller.update()

        then:
        drink2.name == 'updatedName'
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
        controller.params.mixingInstructions = 'Test instructions'
        controller.params.suggestedGlass = 'HIGHBALL'
        controller.params.ingredients = ["1", "2", "3", "4", "5"]
        controller.update()

        then:
        drink1.name == 'updatedName'
        drink1.number == 11
        drink1.alcohol == Alcohol.VODKA
        drink1.symbol == 'TD'
        drink1.mixingInstructions == 'Test instructions'
        drink1.suggestedGlass == GlassType.HIGHBALL
        drink1.ingredients.size() == 5
        response.status == 200
    }

    // Test Copying
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
        assert copied.alcohol == drink1.alcohol
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

    // Test Deleting
    @Unroll('controller.delete(): #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test delete fails because wrong request method or params not supplied"() {
        when:
        if (value != 'DELETE') {
            controller.params.something = 'hasToBeHere'
        }
        request.method = value
        controller.delete(1)
        then:
        expected.value() == response.status
        where:
        value   | expected              | expectedErrorCode
        'GET'   | METHOD_NOT_ALLOWED    | ''
        'POST'  | METHOD_NOT_ALLOWED    | ''
        'PUT'   | METHOD_NOT_ALLOWED    | ''
        'DELETE'| NOT_FOUND             | ''
    }

    @Test
    void "test delete drinks fails because drink is not deletable"() {
        given:
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
        controller.params.something = 'hasToBeHere'
        request.method = 'DELETE'

        when:
        controller.delete(drink2.id)

        then:
        response.status == BAD_REQUEST.value() // 400
    }

    @Test
    void "test delete drinks fails in service call"() {
        given:
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
        controller.params.something = 'hasToBeHere'
        request.method = 'DELETE'
        controller.drinkService = Stub(DrinkService) { delete(_,_,_) >> new Exception('Mocked')}

        when:
        controller.delete(drink1.id)

        then:
        response.status == BAD_REQUEST.value() // 400
    }

    @Test
    void "test delete drink deletes only drink"() {
        given:
        def numOfDrinks = drinkService.count()
        def drinkIngredients = drink1.ingredients as List<Ingredient>
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
        controller.drinkService.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
        request.method = 'DELETE'
        controller.params.something = 'hasToBeHere'

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
        given:
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        controller.params.apiCallCount = 1
        controller.metaClass.createNewIngredientsFromParams { params, adminUser ->
            return ingredientA
        }
        when:
            controller.validateIngredients(params)

        then:
            response.status == 400
    }

    @Test
    void "test validateIngredients returns ok"() {
        given:
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> testUser
        }
        controller.params.apiCallCount = 1
        controller.metaClass.createNewIngredientsFromParams { params, adminUser ->
            return new Ingredient([
                    name: 'New Ingredient',
                    unit: 'WEDGE',
                    amount: 2.5
            ])
        }
        when:
        controller.validateIngredients(params)

        then:
        response.status == 200
    }

    @Test
    void "test not found returns 404"() {
        when:
            controller.notFound(flash,request,'','')

        then:
            response.status == 404
    }
}
