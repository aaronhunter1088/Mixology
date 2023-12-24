package api.v1

import grails.converters.JSON
import mixology.Drink
import mixology.DrinkController
import mixology.DrinkService
import mixology.User
import org.springframework.beans.factory.annotation.Autowired

import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path('/v1/drinks')
class DrinkResource extends BaseResource {

    @Autowired
    DrinkService drinkService

    @Produces('application/json')
    @GET
    public Response getAllDrinks() {
        try {
            def list = drinkService.findAll()
            Response.ok( list ).build()
        } catch (Exception e) {
            Response.serverError().build()
        }
    }

    @Produces('application/json')
    @Path('/{drinkId}')
    @GET
    public Response getADrink(@PathParam('drinkId') Long drinkId) {
        //Response.ok( drinkId.toString() ).build() returns just the id
        try {
            def drink = Drink.findById(drinkId)
            if (!drink) throw NotFoundException("Drink not found using $drinkId")
            else Response.ok(drink).build()
        } catch (Exception e) {
            logger.error("There was an error finding the drink. Reason: ${e.getMessage()}")
            Response.serverError().build()
        }

    }

    @Produces('application/json')
    @POST
    public Response createADrink(Map params) {
        User user = getAuthenticatedUser()
        Drink newDrink
        try {
            newDrink = new Drink(params)
            if (newDrink.validate()) {
                drinkService.save(newDrink, user, false)
                def message = """
                    User ${user.firstName} ${user.lastName} added a new drink.
                    Drink ${newDrink as JSON}
                """
                Response.ok(message).build()
            } else throw new Exception("Invalid drink")
        } catch (Exception e) {
            logger.error("There was an exception while creating a new drink")
            Response.serverError().build()
        }
    }
}
