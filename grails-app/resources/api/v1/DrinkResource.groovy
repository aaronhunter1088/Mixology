package api.v1

import grails.converters.JSON
import mixology.Drink
import mixology.DrinkService
import mixology.User
import org.springframework.beans.factory.annotation.Autowired

import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

@Path('/v1/drinks')
class DrinkResource extends BaseResource {

    private static Logger logger = LogManager.getLogger(DrinkResource.class)
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
        try {
            def drink = Drink.findById(drinkId)
            if (!drink) badRequest("Drink not found using id: $drinkId")
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
            } else badRequest("Invalid drink")
        } catch (Exception e) {
            logger.error("There was an exception while creating a new drink")
            Response.serverError().build()
        }
    }

    @Produces('application/json')
    @Path('/{drinkId}')
    @PUT
    public Response updateADrink(@PathParam('drinkId') Long drinkId, Map params) {
        User user = getAuthenticatedUser()
        Drink updateDrink
        def message = """"""
        try {
            updateDrink = Drink.findById(drinkId)
            params.keySet().each { String prop ->
                // only update property if not in list
                // if user is admin user, we can update any value we wish
                if ( !ignoreSpecificProperties(prop) || user.isAdmin() ) {
                    def value = params."$prop"
                    println "params.$prop = $value"
                    updateDrink."$prop" = value
                } else {
                    message += "Cannot update property: $prop\n"
                }
            }
            if (!updateDrink) {
                Response.ok("No drink was found using the id: $drinkId").build()
            } else if ( !user.drinks*.id.contains(drinkId) ) {
                Response.ok("User does not have a drink using the id: $drinkId").build()
            }
            if (updateDrink.validate() && message == '') {
                drinkService.save(updateDrink, user, false)
                message = """
                    User ${user.firstName} ${user.lastName} updated their drink.
                    Drink ${updateDrink as JSON}
                """
                Response.ok(message).build()
            }
            else if (message != '') {
                message += "Please fix all errors to update your drink"
                badRequest(message)
            }
            else throw new Exception("Invalid drink")
        } catch (Exception e) {
            logger.error("There was an exception while updating a drink: ${e.message}")
            Response.serverError().build()
        }
    }

    public static boolean ignoreSpecificProperties(String property) {
        List<String> ignoreTheseProperties = [
                'custom', 'canBeDeleted', 'id', 'userId'
        ]
        boolean result = ignoreTheseProperties.contains(property)
        logger.info("ignore specific property:: $result")
        return result
    }

    @Produces('application/json')
    @Path('/{drinkId}')
    @DELETE
    public Response deleteADrink(@PathParam('drinkId') Long drinkId, Map params) {
        User user = getAuthenticatedUser()
        Drink deleteDrink
        try {
            def confirm
            try {
                confirm = params?.Confirm
            } catch (Exception e1) {
                logger.error("There was an error while retrieving params value for Confirm: ${e1.message}")
                confirm = 'No'
            }
            if (confirm != 'Yes') {
                badRequest("Pass in Confirm:Yes to delete")
            } else {
                deleteDrink = Drink.findById(drinkId)
                if (!deleteDrink) {
                    badRequest("There was no drink found with id: $drinkId")
                } else {
                    drinkService.delete(deleteDrink.id, user, true)
                    Response.ok("User ${user.firstName} ${user.lastName} deleted their drink.").build()
                }
            }
        } catch (Exception e2) {
            logger.error("There was an exception while trying to delete a drink:: ${e2.message}")
            Response.serverError().build()
        }
    }
}
