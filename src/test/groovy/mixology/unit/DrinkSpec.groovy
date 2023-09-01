package mixology.unit

import enums.Alcohol
import enums.GlassType
import exceptions.UnsupportedGlassException
import grails.testing.gorm.DomainUnitTest
import mixology.Drink
import mixology.Ingredient
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static groovy.test.GroovyAssert.assertNotNull
import static groovy.test.GroovyAssert.shouldFail

class DrinkSpec extends Specification implements DomainUnitTest<Drink> {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    void "test creating a valid drink"() {
        Drink drink = new Drink([
                name: 'Long Island Iced Tea',
                number: 34,
                mixingInstructions: 'Pour ingredients over crushed ice in highball glass. Add cola for color and garnish with lemon',
                suggestedGlass: GlassType.HIGHBALL,
                alcoholType: Alcohol.VODKA,
                symbol: 'Li',
                ingredients: Ingredient.createFillerIngredients(3)
        ]).save(failOnError:true)

        expect: 'drink values are valid'
            assertNotNull drink.id
            assert drink.toString() == 'Long Island Iced Tea (Li) (34)'
    }

    // Not valid anymore.
//    @Test
//    void "test drink fails validation because no ingredients"() {
//        when:
//        domain.ingredients = null
//
//        then: 'drink validation fails'
//        !domain.validate(['ingredients'])
//    }

    @Unroll('Drink.validate() with name: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drink fails validation because name falls out of range"() {
        when:
        domain.name = value

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
    void "test drink fails validation because symbol falls out of range"() {
        when:
        domain.symbol = value

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

    @Unroll('Drink.validate() with drinkNumber: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drink fails validation because drinkNumber is too low"() {
        when:
        domain.number = value

        then:
        expected == domain.validate(['drinkNumber'])
        domain.errors['drinkNumber']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        0       | false    | 'min.notmet'
        1       | true     | null
    }

    @Unroll('Drink.validate() with alcoholType: #alcohol should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drink validation with alcoholType"() {
        when:
        domain.alcoholType = alcohol as Alcohol

        then:
        expected == domain.validate(['alcoholType'])
        domain.errors['alcoholType']?.code == expectedErrorCode

        where:
        alcohol   | expected | expectedErrorCode
        null        | false    | 'nullable'
        Alcohol.VODKA | true | null
    }

    @Unroll('Drink.validate() with mixing instructions: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drink validation with mixing instructions"() {
        when:
        domain.mixingInstructions = value

        then:
        expected == domain.validate(['mixingInstructions'])
        domain.errors['mixingInstructions']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        ''      | false    | 'blank'
        null    | false    | 'nullable'
        'Mix me'| true     | null
    }

    @Unroll('Drink.validate() with suggestedGlass: #glass should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test drink validation with suggestedGlass"() {
        when:
        domain.suggestedGlass = glass as GlassType

        then:
        expected == domain.validate(['suggestedGlass'])
        domain.errors['suggestedGlass']?.code == expectedErrorCode

        where:
        glass   | expected | expectedErrorCode
        null        | false    | 'nullable'
        GlassType.HIGHBALL | true | null
    }

    @Test
    void "test getting glass image"() {
        when:
        domain.suggestedGlass = GlassType.GOBLET

        then: 'proper glassType image'
            assert domain.getGlassImage() == 'gobletGlass.png'
    }

    @Test
    void "test getting glass image throws exception"() {
        when:
        domain.suggestedGlass

        then:
        def err = shouldFail(UnsupportedGlassException) {
            domain.getGlassImage()
        }
        and:
        err.getMessage() == 'The glass you have chosen is not available'
    }

}
