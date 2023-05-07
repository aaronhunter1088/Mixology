package mixology.unit

import enums.Alcohol
import grails.testing.services.ServiceUnitTest
import mixology.Drink
import mixology.DrinkService
import spock.lang.Specification

class DrinkServiceSpec extends Specification implements ServiceUnitTest<DrinkService>{

    def drinkService

    def setup() {
        drinkService = grailsApplication.mainContext.getBean('drinkService')
    }

    def cleanup() {
    }

    void "test getting all tequila drinks"() {
        List<Drink> tequilaDrinks = drinkService.list().each { Drink d -> d.alcoholType == Alcohol.TEQUILA}
        tequilaDrinks.each {
            assert it.alcoholType == Alcohol.TEQUILA
        }
    }
}
