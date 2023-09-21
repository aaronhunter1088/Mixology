package mixology.unit

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.plugin.springsecurity.SpringSecurityService
import mixology.Role
import mixology.RoleService
import mixology.UserRole
import mixology.UserRoleService
import mixology.UserService
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
import spock.lang.Unroll

import static enums.Unit.*
import static org.springframework.http.HttpStatus.*

@ContextConfiguration
class IngredientControllerSpec extends Specification implements ControllerUnitTest<IngredientController>, DataTest {

    Class<?>[] getDomainClassesToMock(){return [Drink, Ingredient, User] as Class[]}

    Drink drink1, drink2
    User adminUser, testUser
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)
    def userService = getDatastore().getService(UserService)
    def roleService = getDatastore().getService(RoleService)
    def userRoleService = getDatastore().getService(UserRoleService)

    Set<Ingredient> createCustomIngredients(int numberToCreate) {
        Set<Ingredient> customIngredients = []
        (1..numberToCreate).each {
            def customIngredient = new Ingredient([
                    name: 'CustomIngredient'+"{$it}",
                    unit: getRandomUnit(),
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
        def defaultIngredient = createDefaultIngredient()
        ingredientService.save(defaultIngredient, false)
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

        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.roleService = roleService
        controller.userRoleService = userRoleService
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

    // Test Index
    @Test
    void "test index action"() {
        given:
        def ings1 = createIngredientsWithAAndBAndC()
        def ings2 = createIngredientsWithVodkaAndDAndE()
        def ingredients = []
        ingredients.addAll(ings1)
        ingredients.addAll(ings2)
        controller.springSecurityService = Stub(SpringSecurityService) { getPrincipal() >> testUser}

        when: 'call controller.index'
        controller.index()

        then:
        assert model.ingredientCount == 7
        assert (model.ingredientList as List).size() == 5
        assert model.ingredientList.each {
            ingredients.contains(it as Ingredient)
        }
    }

    // Test Custom Index
    @Test
    void "test custom index"() {
        given:
        drink1.ingredients.each { testUser.addToIngredients(it) }
        drink2.ingredients.each { testUser.addToIngredients(it) }

        def myCriteria = [
                list : { Object x, Object y ->
                    [
                            resultList: testUser.ingredients.take(5),
                            totalCount: testUser.ingredients.size()
                    ]
                }
        ]
        Ingredient.metaClass.'static'.createCriteria = { myCriteria }
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}

        when: 'call controller.index'
        controller.customIndex()

        then:
        assert model.ingredientCount == testUser.ingredients.size()
        assert (model.ingredientList).resultList as ArrayList == ((testUser.ingredients as List) - ingredientE)
    }

    // Test Show Ingredient
    @Test
    void "test show ingredient"() {
        given:
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}

        when:
        controller.show(1)

        then:
        response.status == 200
    }

    // Test Show all Ingredients
    @Test
    void "test show all ingredients"() {
        when:
        controller.showIngredients()

        then:
        response.status == FOUND.value()
    }

    // Test Show Custom Ingredients
    @Test
    void "test show custom ingredients"() {
        given:
        testUser.addToIngredients(ingredientA)
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}

        when:
        controller.showCustomIngredients()

        then:
        response.status == 200
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
    void "test save fails because wrong request method or params not supplied"() {
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
                    unit: WEDGE,
                    amount: 0
            ])]
        }
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
            controller.save()

        then:
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
        controller.ingredientService = Stub(IngredientService) {save(_,_,_) >> null}
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        controller.save()

        then:
        response.status == 400
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
        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        Role.findByAuthority('ROLE_ADMIN') >> role
        Role.findByAuthority('ROLE_USER') >> null
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
        controller.springSecurityService = Stub(SpringSecurityService) {
             getPrincipal() >> user
        }

        when:
        controller.save()

        then:
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
        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        Role.findByAuthority('ROLE_ADMIN') >> null
        Role.findByAuthority('ROLE_USER') >> role
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 201
    }

    /* TODO: Rethink if this is needed.
        Technically, an admin can also
        have custom drinks and ingredients. */
//    @Ignore
//    @Test
//    void "test saving an ingredient fails because ingredient is not custom"() {
//        given:
//        request.method = 'POST'
//        controller.springSecurityService = Stub(SpringSecurityService) {
//            getPrincipal() >> testUser
//        }
//
//        when:
//        controller.params.ingredientName = 'testIngredientX'
//        controller.params.ingredientUnit = Unit.WEDGE
//        controller.params.ingredientAmount = '2.2'
//
//        controller.save()
//
//        then:
//        response.status == 400
//    }

    // Test Editing
    @Test
    void "edit ingredient returns ingredient"() {
        given:
        Ingredient test = new Ingredient([
                name: 'testIngredientX',
                unit: Unit.WEDGE,
                amount: 2.2
        ])
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
        Role role = new Role(authority: enums.Role.USER.name).save()
        UserRole.create(user, role)
        controller.ingredientService = Stub(IngredientService) { get(5L) >> test}
        controller.springSecurityService = Stub(SpringSecurityService) { getPrincipal() >> user}
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        controller.edit(5L)

        then:
        response.status == 200
    }

    // Test Updating
    @Test
    void "update ingredient fails when no ingredient"() {
        when:
            request.method = 'PUT'
            controller.update()

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
        ]).save(flush:true)
        controller.ingredientService = ingredientService
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
            request.method = 'PUT'
            controller.params.id = ingredient.id
            controller.update()

        then:
            response.status == 400
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
        controller.ingredientService = ingredientService
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        request.method = 'DELETE'
        controller.delete(defaultIngredient.id as Long)

        then:
        response.status == 401
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
        ]).save(flush:true)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.ingredientService = Stub(IngredientService) { save(_) >> ingredient}
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        request.method = 'PUT'
        controller.params.id = ingredient.id
        controller.update()

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
        ]).save(flush:true)
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
        controller.ingredientService = Stub(IngredientService) { save(_) >> ingredient}
        controller.userService = userService
        controller.userRoleService = userRoleService

        when:
        request.method = 'PUT'
        controller.params.id = ingredient.id
        controller.update()

        then:
        response.status == 200
    }

    @Test
    void "test delete ingredient is successful"() {
        given:
        controller.ingredientService.springSecurityService = Stub(SpringSecurityService) { getPrincipal() >> adminUser}
        controller.springSecurityService =
                Stub(SpringSecurityService) {getPrincipal() >> adminUser}
        testUser.addToIngredients(defaultIngredient).save(flush:true)

        when:
        request.method = 'DELETE'
        controller.delete(defaultIngredient.id as Long)

        then:
        response.status == 204
    }

    // Test Copying
    @Test
    void "test copy ingredient duplicates exactly"() {
        given:
        testUser.ingredients = new HashSet<Ingredient>()
        controller.springSecurityService = Stub(SpringSecurityService) { getPrincipal() >> testUser}
        assert testUser.ingredients.size() == 0

        when:
        controller.copy(ingredientA as Ingredient)

        then:
        assert testUser.ingredients.size() == 1
        def copied = testUser.ingredients[0]
        assert copied.name == ingredientA.name
        assert copied.unit == ingredientA.unit
        assert copied.amount == ingredientA.amount
        assert copied.id != ingredientA.id
    }

    // Test Deleting

    // Test UI Call
    @Test
    void "test validateIngredients returns bad request"() {
        controller.params.apiCallCount = 1
        controller.metaClass.createIngredientsFromParams { params, role ->
            return [ingredientA]
        }
        when:
        controller.validateIngredient(params)

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
        controller.validateIngredient(params)

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
