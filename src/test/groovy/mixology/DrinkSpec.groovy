package mixology

import enums.Alcohol
import enums.GlassType
import grails.testing.gorm.DomainUnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static groovy.test.GroovyAssert.assertNotNull
import static groovy.test.GroovyAssert.assertNull

class DrinkSpec extends Specification implements DomainUnitTest<Drink> {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    void "test creating a valid drink"() {
        Drink drink = new Drink([
                drinkName: 'Long Island Iced Tea',
                drinkNumber: 34,
                mixingInstructions: 'Pour ingredients over crushed ice in highball glass. Add cola for color and garnish with lemon',
                suggestedGlass: GlassType.HIGHBALL,
                alcoholType: Alcohol.VODKA,
                drinkSymbol: 'Li',
                ingredients: Ingredient.createFillerIngredients(3)
        ]).save(failOnError:true)

        expect: 'drink.id is set'
            assertNotNull drink.id
    }

    @Test
    void "test drink fails validation because no ingredients"() {
        when:
        domain.ingredients = null

        then: 'drink validation fails'
        !domain.validate(['ingredients'])
    }

    @Unroll('Drink.validate() with name: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drink fails validation because name falls out of range"() {
        when:
        domain.drinkName = value

        then:
        expected == domain.validate(['drinkName'])
        domain.errors['drinkName']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        ''      | false    | 'blank'
        'To'    | false    | 'size.toosmall'
        'T'*31  | false    | 'size.toobig'
        'Good'  | true     | null
    }

    @Unroll('Drink.validate() with symbol: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drinks fails validation because symbol falls out of range"() {
        when:
        domain.drinkSymbol = value

        then:
        expected == domain.validate(['drinkSymbol'])
        domain.errors['drinkSymbol']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        ''      | false    | 'blank'
        'T'     | false    | 'size.toosmall'
        'Too'   | false    | 'size.toobig'
        'Li'    | true     | null
    }
}
