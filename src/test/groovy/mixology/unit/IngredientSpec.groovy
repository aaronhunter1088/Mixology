package mixology.unit

import enums.Unit
import grails.testing.gorm.DomainUnitTest
import mixology.Ingredient
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class IngredientSpec extends BaseController implements DomainUnitTest<Ingredient> {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    void "test creating a valid ingredient"() {
        Ingredient i = createIngredient('Vodka')
        i = ingredientService.save(i,true)

        expect: 'ingredient values are valid'
            assertNotNull i.id
            assert i.prettyName() == '1.5 ounces of Vodka'

        Ingredient j = new Ingredient([
                name: 'Strawberry',
                unit: Unit.FRUIT,
                amount: 2.5
        ]).save(failOnError:true)

        and: 'ingredient values are valid'
            assertNotNull j.id
            assert j.prettyName() == '2.5 fruits of Strawberry'
    }

    @Unroll('Ingredient.validate() with name: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test ingredient validation with name"() {
        when:
        domain.name = value

        then:
        expected == domain.validate(['name'])
        domain.errors['name']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        null    | false    | 'nullable'
        ''      | false    | 'blank'
        'I'     | false    | 'size.toosmall'
        'I'*31  | false    | 'size.toobig'
        'Good'  | true     | null
    }

    @Unroll('Ingredient.validate() with unit: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test ingredient validation with unit"() {
        when:
        domain.unit = value as Unit

        then:
        expected == domain.validate(['unit'])
        domain.errors['unit']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        null    | false    | 'nullable'
        //'' as Unit | false    | 'blank'
        //'SPLASH'| false    | 'invalid.unit'
        Unit.DASH | true   | null
    }

    @Unroll('Ingredient.validate() with amount: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test ingredient validation with amount"() {
        when:
        domain.amount = value as Double

        then:
        expected == domain.validate(['amount'])
        domain.errors['amount']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        null    | false    | 'nullable'
        1       | true     | null
        1.0     | true     | null
    }

    @Test
    void "test copyAll copies ingredient completely"() {
        Ingredient a = new Ingredient([
                name: 'IngredientA',
                unit: Unit.OZ,
                amount: 1
        ])
        Ingredient b = new Ingredient([
                name: 'IngredientB',
                unit: Unit.DASH,
                amount: 2.5
        ])
        Ingredient c = new Ingredient([
                name: 'IngredientC',
                unit: Unit.CUP,
                amount: 2
        ])
        Set<Ingredient> original = new HashSet<>()
        original << a << b << c

        when:
        Set<Ingredient> copy = Ingredient.copyAll(original)

        then:
        assert copy.size() == 3
    }

    @Test
    void "test compareTo(another ingredient)"() {
        when:
        Ingredient one = new Ingredient([
                name: 'One',
                unit: Unit.SPLASH,
                amount: 1
        ])
        Ingredient two = new Ingredient([
                name: 'Two',
                unit: Unit.SPLASH,
                amount: 1
        ])
        Ingredient anotherOne = new Ingredient([
                name: 'One',
                unit: Unit.SPLASH,
                amount: 1
        ])

        then:
        assert one.compareTo(two) == 1
        assert one.compareTo(anotherOne) == 0
    }

    @Test
    void "test idIsNull"() {
        when:
        Ingredient one = new Ingredient([
                name: 'One',
                unit: Unit.SPLASH,
                amount: 1
        ]).save(failOnError:true)
        Ingredient two = new Ingredient([
                name: 'Two',
                unit: Unit.SPLASH,
                amount: 1
        ])

        then:
            assertFalse one.idIsNull()
            assertTrue two.idIsNull()
    }
}
