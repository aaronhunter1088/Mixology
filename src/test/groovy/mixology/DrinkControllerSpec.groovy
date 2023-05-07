package mixology

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.gorm.transactions.Rollback
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.junit.Test
import spock.lang.Specification

class DrinkControllerSpec extends Specification implements ControllerUnitTest<DrinkController>, DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [Drink,Ingredient,User] as Class[]
    }

    Drink drink1, drink2
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)

    void setup() {
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

    @Test
    void "setup created two drinks with three ingredients"() {
        expect:"Drink1.id is null"
            drink1.id != null
        println "DrinkID: ${drink1.id}"
        and:"Drink1 doesn't have three ingredients"
            drink1.ingredients.size() == 3
        println "Drink1 Ingredients Size: ${drink1.ingredients.size()}"
        and:"Drink2.id is null"
            drink2.id != null
        println "DrinkID: ${drink2.id}"
        and:"Drink2 doesn't have three ingredients"
        drink2.ingredients.size() == 3
        println "Drink2 Ingredients Size: ${drink2.ingredients.size()}"
    }

    @Test
    void "confirm ingredient service saved all ingredients"() {
        expect:"Ingredients didn't add 6 ingredients"
            ingredientService.count() == 6
    }

    @Test
    void "delete one drink doesn't delete other drink"() {
        expect:"Two drinks exist"
            drinkService.count() == 2
        println "# of drinks: ${drinkService.count()}"
        List<Ingredient> ingredients = drink1.ingredients.toArray()
        ingredients.each { ingredient ->
            ingredient.removeFromDrinks(drink1)
        }
        drinkService.delete(drink1.id as Long)
        and:"A single drink should still exist"
            drinkService.count() == 1
        println "# of drinks: ${drinkService.count()}"
        and:"Drink2 exists"
            Drink.findById(2) instanceof Drink
        println "${drink2}"
        and:"6 ingredients still exists"
            ingredientService.count() == 6
        println "# of ingredients: ${ingredientService.count()}"
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
    void "test save action with one ingredient"() {
        given:
        def user = new User([
                username: "testusername@gmail.com",
                firstName: "test",
                lastName: "user"
        ]).save(validate:false)
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
        controller.params.ingredients = "100 Proof Vodka : 1.5 : OZ"
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
        controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
        controller.save()

        then:
        User.findByUsername(user.username) >> user
        response.status == 201
    }

    @Test
    void "test edit action"() {
        given:
        def drink = Stub(Drink) {
            id >> 1
        }
        controller.drinkService = Stub(DrinkService) {
            get(_) >> drink
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
        controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
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
        controller.params.ingredients = "[100 Proof Vodka : 1.5 : OZ, Orange Juice : 1.0 : SPLASH]"
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
        User.findByUsername(user.username) >> user
        controller.springSecurityService = Stub(SpringSecurityService) {
            getPrincipal() >> user
        }
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
        copied.ingredients.size() == drink1.ingredients.size()
        copied.ingredients.each {Ingredient copiedIng ->
            drink1.ingredients.each {Ingredient originalIng ->
                copiedIng.name == originalIng.name
                copiedIng.unit == originalIng.unit
                copiedIng.amount == originalIng.amount
            }
        }
    }
}
