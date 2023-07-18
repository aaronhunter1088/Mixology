package mixology.unit

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.plugin.springsecurity.SpringSecurityService
import mixology.Role
import mixology.UserRole
import org.junit.validator.TestClassValidator
import org.springframework.test.context.ContextConfiguration

import grails.testing.web.controllers.ControllerUnitTest
import mixology.Drink
import mixology.Ingredient
import mixology.IngredientController
import mixology.IngredientService
import mixology.DrinkService
import mixology.User
import org.junit.Test
import spock.lang.Specification
import grails.testing.gorm.DataTest

import static org.mockito.Mockito.*

@ContextConfiguration
class IngredientControllerSpec extends Specification implements ControllerUnitTest<IngredientController>, DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [Drink, Ingredient, User] as Class[]
    }

    Drink drink1, drink2
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)
    //def userDetailsService = getDatastore().getService(CustomUserDetailsService)

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
                ingredients: createIngredientsWithVodkaAndBAndC(),
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
    }

    def cleanup() {
    }

    Set<Ingredient> createIngredientsWithVodkaAndBAndC() {
        Set<Ingredient> ingredients = []
        Ingredient x = new Ingredient([
                name: 'Vodka',
                unit: Unit.OZ,
                amount: 1.5
        ]).save(failOnError:true)
        Ingredient y = new Ingredient([
                name: 'IngredientB',
                unit: Unit.OZ,
                amount: 1.5
        ]).save(failOnError:true)
        Ingredient z = new Ingredient([
                name: 'IngredientC',
                unit: Unit.OZ,
                amount: 1.5
        ]).save(failOnError:true)
        ingredients.add(x)
        ingredients.add(y)
        ingredients.add(z)
        return ingredients
    }

    Set<Ingredient> createIngredientsWithVodkaAndDAndE() {
        Set<Ingredient> ingredients = []
        Ingredient x = new Ingredient([
                name: 'Vodka',
                unit: Unit.OZ,
                amount: 1.5
        ])
        Ingredient y = new Ingredient([
                name: 'IngredientD',
                unit: Unit.OZ,
                amount: 1.5
        ])
        Ingredient z = new Ingredient([
                name: 'IngredientE',
                unit: Unit.OZ,
                amount: 1.5
        ])
        ingredients.add(x)
        ingredients.add(y)
        ingredients.add(z)
        return ingredients
    }

    Set<Ingredient> createCustomIngredients(int numberToCreate) {
        Set<Ingredient> customIngredients = []
        (1..numberToCreate).each {
            def customIngredient = new Ingredient([
                    name: 'CustomIngredient'+"{$it}",
                    unit: Unit.getRandomUnit(),
                    amount: 1
            ])
            customIngredients << customIngredient
        } as Set<Ingredient>
        customIngredients
    }

    @Test
    void "test create custom ingredient"() {
        when:
        def aSet = createCustomIngredients(3)

        then:
        assert aSet.size() == 3
    }

    @Test
    void "test index action"() {
        given:
        List list1 = createIngredientsWithVodkaAndDAndE() as List
        List list2 = createIngredientsWithVodkaAndDAndE() as List
        def ingredients = []
        ingredients.addAll(list1)
        ingredients.addAll(list2)
        controller.ingredientService = Stub(IngredientService) {
            list(_) >> ingredients
            count() >> ingredients.size()
        }

        when: 'call controller.index'
        controller.index()

        then:
        model.ingredientList == ingredients
        model.ingredientCount == ingredients.size()
    }

    @Test
    void "test custom index"() {
        given:
        List<Ingredient> ingredients = []
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        user.drinks = [drink1, drink2]
        drink1.ingredients.each { ingredients << it}
        drink2.ingredients.each { ingredients << it}
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when: 'call controller.index'
        controller.customIndex()

        then:
        User.findByUsername(user.username) >> user
        model.ingredientCount == ingredients.size()
        ingredients.size() == 6
    }

    @Test
    void "test show ingredients"() {
        given:
        def ingredient = Stub(Ingredient) {
            id >> 1
        }
        controller.ingredientService = Stub(IngredientService) {
            get(_) >> ingredient
        }

        when:
        controller.show(1)

        then:
        response.status == 200
    }

    @Test
    void "test create action"() {
        when:
        controller.create()

        then:
        response.status == 200
    }

    // Test Saving
    @Test
    void "test save fails because method is GET"() {
        given:
        request.method = 'GET'
        controller.params.option = 'test'

        when:
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
        given:
        request.method = 'DELETE'

        when:
        controller.save()

        then:
        response.status == 405
    }

    @Test
    void "test saving one ingredient"() {
        given:
        request.method = 'POST'
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Ingredient testIngredient = new Ingredient([
                name: 'testIngredient',
                unit: Unit.randomUnit,
                amount: 1.0,
                canBeDeleted: true,
                custom: true
        ])
        controller.params.ingredientName = 'testIngredient'
        controller.params.ingredientUnit = Unit.randomUnit
        controller.params.ingredientAmount = 1.0
        controller.params.canBeDeleted = true
        controller.params.custom = true
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        controller.createIngredientsFromParams(params) >> testIngredient
        //controller.ingredientService = Stub(IngredientService) { save(_) >> testIngredient}
        controller.springSecurityService = Stub(SpringSecurityService) {
             getPrincipal() >> user
        }

        when:
        Role.findByAuthority('ROLE_ADMIN') >> role
        Role.findByAuthority('ROLE_USER') >> null
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 201
    }

    @Test
    void "test saving an ingredient fails because it already exists"() {
        given:
        request.method = 'POST'
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Ingredient testIngredient = new Ingredient([
                name: 'testIngredient',
                unit: Unit.randomUnit,
                amount: 1.0,
                canBeDeleted: true,
                custom: true
        ]).save()
        controller.params.ingredientName = 'Vodka'
        controller.params.ingredientUnit = Unit.OZ
        controller.params.ingredientAmount = 1.5
        controller.params.canBeDeleted = true
        controller.params.custom = true
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        controller.createIngredientsFromParams(params) >> testIngredient
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
        Role.findByAuthority('ROLE_ADMIN') >> role
        Role.findByAuthority('ROLE_USER') >> null
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 400
    }

    @Test
    void "test saving an ingredient fails because ingredient is not custom"() {
        given:
        request.method = 'POST'
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Ingredient testIngredient = new Ingredient([
                ingredientName: 'testIngredient',
                ingredientUnit: Unit.randomUnit,
                ingredientAmount: 1.0,
                custom: true
        ])
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        controller.ingredientService = Stub(IngredientService) { save(_) >> testIngredient}
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
        controller.params.ingredientName = 'testName'
        controller.params.ingredientUnit = Unit.randomUnit
        controller.params.ingredientAmount = '1.0'
        Role.findByAuthority('ROLE_ADMIN') >> null
        Role.findByAuthority('ROLE_USER') >> role
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 400
    }

    // Test Editing
}
