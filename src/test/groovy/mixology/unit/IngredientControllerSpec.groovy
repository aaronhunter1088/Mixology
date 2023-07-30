package mixology.unit

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.ValidationException
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
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import spock.lang.Specification
import grails.testing.gorm.DataTest

import static org.mockito.Mockito.*
import static enums.Alcohol.*
import static enums.GlassType.*
import static enums.Unit.*

@ContextConfiguration
class IngredientControllerSpec extends Specification implements ControllerUnitTest<IngredientController>, DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [Drink, Ingredient, User] as Class[]
    }

    Drink drink1, drink2
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)

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
    def defaultIngredient
    def createDefaultIngredient = {
        if ( !defaultIngredient ) {
            defaultIngredient = new Ingredient([
                    name: 'DefaultIngredient',
                    unit: Unit.OZ,
                    amount: 1.5,
                    canBeDeleted: false,
                    custom: false
            ])
        }
        else if ( !defaultIngredient.idIsNull() ) {
            defaultIngredient
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
        ingredientService.save(createDefaultIngredient())
        drinkService.save(drink1)
        drinkService.save(drink2)
    }

    def cleanup() {
        Ingredient.all.each { it.delete()}
        println "Total ingredients after cleanup: ${Ingredient.count()}"
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
    void "test saving fails because of validation error"() {
        given:
        request.method = 'POST'
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        controller.params.prop = ''
        controller.metaClass.createIngredientsFromParams { params, aRole ->
            return [new Ingredient([
                    name:'',
                    unit: enums.Unit.WEDGE,
                    amount: 0
            ])]
        }
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
            controller.save()

        then:
            Role.findByAuthority('ROLE_ADMIN') >> role
            response.status == 400
    }

    @Test
    void "test saving fails because of multiple validation errors"() {
        given:
        request.method = 'POST'
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        controller.params.prop = ''
        controller.metaClass.createIngredientsFromParams { params, aRole ->
            return [
                    new Ingredient([name:'', unit: WEDGE, amount: 0, custom:false]),
                    new Ingredient([name:'', unit: FRUIT, amount: 0, custom:false])
            ]
        }
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
        controller.save()

        then:
        response.status == 400
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
                unit: randomUnit,
                amount: 1.0,
                canBeDeleted: true,
                custom: true
        ])
        controller.params.ingredientName = 'testIngredient'
        controller.params.ingredientUnit = randomUnit
        controller.params.ingredientAmount = 1.0
        controller.params.canBeDeleted = true
        controller.params.custom = true
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        controller.createIngredientsFromParams(params,role) >> testIngredient
        //controller.ingredientService = Stub(IngredientService) { save(_) >> testIngredient}
        controller.springSecurityService = Stub(SpringSecurityService) {
             getPrincipal() >> user
        }

        when:
        //Role.findByAuthority('ROLE_ADMIN') >> role
        //Role.findByAuthority('ROLE_USER') >> null
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 201
    }

    @Test
    void "test saving multiple ingredients"() {
        given:
        request.method = 'POST'
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        def unit1 = Unit.randomUnit
        def unit2 = Unit.randomUnit

        controller.params.ingredientName = ['testIngredient1', 'testIngredient2']
        controller.params.ingredientUnit = [unit1, unit2]
        controller.params.ingredientAmount = [1.0, 2.0]
        controller.params.canBeDeleted = true
        controller.params.custom = true
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        //controller.createIngredientsFromParams(params,role) >> [testIngredient1, testIngredient2]
        //controller.ingredientService = Stub(IngredientService) { save(_) >> testIngredient}
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
        Role.findByAuthority('ROLE_ADMIN') >> null
        Role.findByAuthority('ROLE_USER') >> role
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
        controller.params.ingredientName = 'Vodka'
        controller.params.ingredientUnit = Unit.OZ
        controller.params.ingredientAmount = 1.5
        Role role = new Role(authority: enums.Role.ADMIN.name).save()
        UserRole.create(user, role)
        //controller.createIngredientsFromParams(params) >> testIngredient
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
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        //controller.ingredientService = Stub(IngredientService) { save(_) >> testIngredient}
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
        controller.params.ingredientName = 'testIngredientX'
        controller.params.ingredientUnit = Unit.WEDGE
        controller.params.ingredientAmount = '2.2'
        controller.metaClass.createIngredientsFromParams = { def params, def role2 ->
            Ingredient test = new Ingredient([
                    name: 'testIngredientX',
                    unit: Unit.WEDGE,
                    amount: 2.2,
                    custom: false
            ])
            return [test]
        }

        Role.findByAuthority('ROLE_ADMIN') >> null
        Role.findByAuthority('ROLE_USER') >> role
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 400
    }

    @Test
    void "save ingredient returns 404"() {
        given:
        request.method = 'POST'

        when:
        controller.save()

        then:
        response.status == 404
    }

    // Test Editing
    @Test
    void "edit ingredient returns ingredient"() {
        given:
        Ingredient test = new Ingredient([
                name: 'testIngredientX',
                unit: Unit.WEDGE,
                amount: 2.2
        ])
        controller.ingredientService = Stub(IngredientService) { get(5L) >> test}

        when:
        controller.edit(5L)

        then:
        response.status == 200
    }

    // TODO: Test updating
    @Test
    void "update ingredient fails when no ingredient"() {
        when:
            request.method = 'PUT'
            controller.update(null)

        then:
            response.status == 400
    }

    @Test
    void "update ingredient fails because it is non admin updating default ingredient"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role userRole = new Role(authority: enums.Role.USER.name)
        UserRole.create(user, userRole)
        Ingredient ingredient = new Ingredient([
                name: 'testDefaultIngredient',
                unit: randomUnit,
                amount: 1.0,
                canBeDeleted: false,
                custom: false
        ])
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        User.findByUsername(user.username) >> user

        when:
            request.method = 'PUT'
            controller.update(ingredient)

        then:
            response.status == 400
    }

    @Test
    void "test updating an ingredient with admin user and is default"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role userRole = new Role(authority: enums.Role.ADMIN.name)
        UserRole.create(user, userRole)
        Ingredient ingredient = new Ingredient([
                name: 'testDefaultIngredient',
                unit: randomUnit,
                amount: 1.0,
                canBeDeleted: false,
                custom: false
        ])
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.ingredientService = Stub(IngredientService) { save(_) >> ingredient}
        User.findByUsername(user.username) >> user

        when:
        request.method = 'PUT'
        controller.update(ingredient)

        then:
        response.status == 200
    }

    @Test
    void "test updating an ingredient with non admin and is custom"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role userRole = new Role(authority: enums.Role.USER.name)
        UserRole.create(user, userRole)
        Ingredient ingredient = new Ingredient([
                name: 'testDefaultIngredient',
                unit: randomUnit,
                amount: 1.0,
                canBeDeleted: true,
                custom: true
        ])
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.ingredientService = Stub(IngredientService) { save(_) >> ingredient}
        User.findByUsername(user.username) >> user

        when:
        request.method = 'PUT'
        controller.update(ingredient)

        then:
        response.status == 200
    }

    @Test
    void "test fails to delete ingredient because ID is not present"() {
        when:
            request.method = 'DELETE'
            controller.delete(null)

        then:
            response.status == 400
    }

    @Test
    void "test delete ingredient fails because non admin user and default"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role userRole = new Role(authority: enums.Role.USER.name)
        UserRole.create(user, userRole)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }

        when:
            request.method = 'DELETE'
            controller.delete(defaultIngredient.id as Long)

        then:
            response.status == 401
    }

    @Test
    void "test delete ingredient is successful"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role userRole = new Role(authority: enums.Role.ADMIN.name)
        UserRole.create(user, userRole)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.ingredientService = ingredientService

        when:
        request.method = 'DELETE'
        controller.delete(defaultIngredient.id as Long)

        then:
        response.status == 204
    }

    // Test UI Call
    @Test
    void "test validateIngredients returns bad request"() {
        controller.params.apiCallCount = 1
        controller.metaClass.createIngredientsFromParams { params, role ->
            return [ingredientA]
        }
        when:
        controller.validate(params)

        then:
        response.status == 400
    }

    @Test
    void "test validateIngredients returns ok"() {
        controller.params.apiCallCount = 1
        controller.params.ingredientName = 'New Ingredient'
        controller.params.ingredientUnit = 'WEDGE'
        controller.params.ingredientAmount = 2.5
        when:
        controller.validate(params)

        then:
        response.status == 200
    }

    // notFound()
    @Test
    void "test not found returns 404"() {
        when:
        controller.notFound('','')

        then:
        response.status == 404
    }
}
