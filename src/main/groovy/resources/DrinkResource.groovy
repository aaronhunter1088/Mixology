package resources

import grails.converters.JSON
import mixology.DrinkService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/drink")
class DrinkResource {

    DrinkService drinkService

    @GetMapping("/")
    def getAllDrinks() {
        return drinkService.findAll() as JSON
    }
}
