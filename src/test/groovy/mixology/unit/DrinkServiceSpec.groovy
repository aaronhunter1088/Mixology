package mixology.unit

import enums.Alcohol
import grails.testing.services.ServiceUnitTest
import mixology.Drink
import mixology.DrinkService
import org.junit.Test

class DrinkServiceSpec extends BaseController implements ServiceUnitTest<DrinkService> {

    def setup() {}

    def cleanup() {
    }

    @Test
    void "test getting all tequila drinks"() {
        List<Drink> tequilaDrinks = drinkService.list(null).each { Drink d -> d.alcohol == Alcohol.TEQUILA}
        tequilaDrinks.each {
            assert it.alcohol == Alcohol.TEQUILA
        }
    }
}
