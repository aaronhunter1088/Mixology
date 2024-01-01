package api.v1

import grails.converters.JSON
import mixology.Ingredient
import mixology.IngredientService
import mixology.User
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired

import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path('/v1/ingredients')
class IngredientResource extends BaseResource {

    private static Logger logger = LogManager.getLogger(IngredientResource.class)

    @Autowired
    IngredientService ingredientService

    @Produces('application/json')
    @GET
    public Response getAllIngredients() {
        User user = BaseResource.getAuthenticatedUser()
        try {
            def errorMessage = request.getAttribute("error") as String ?: ''
            if (errorMessage) badRequest(errorMessage)
            else {
                def list = (user) ? ingredientService.findAll(user) : ingredientService.findAll()
                Response.ok( list ).build()
            }
        } catch (Exception e) {
            logger.error("There was an exception getting the ingredients because ${e.message}")
            badRequest("There was an exception getting the ingredients because ${e.message}")
        }
    }

    @Produces('application/json')
    @Path('/{ingredientId}')
    @GET
    public Response getAnIngredient(@PathParam('ingredientId') Long ingredientId) {
        User user = BaseResource.getAuthenticatedUser()
        try {
            def ingredient = ingredientService.get(ingredientId)
            def errorMessage = request.getAttribute("error") as String ?: ''
            if (errorMessage) badRequest(errorMessage)
            else if (!ingredient) badRequest("Ingredient not found using $ingredientId")
            else {
                if (!user) Response.ok(ingredient).build()
                else {
                    if (!user.ingredients.contains(ingredient)) badRequest("The user you provided does not have the ingredient you are inquiring about.")
                    else {
                        Response.ok(ingredient).build()
                    }
                }
            }
        } catch (Exception e) {
            logger.error("There was an error finding the ingredient. Reason: ${e.getMessage()}")
            Response.serverError().build()
        }
    }

    @Produces('application/json')
    @POST
    public Response createAnIngredient(Map params) {
        User user = BaseResource.getAuthenticatedUser()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!user) badRequest("A user must be provided")
        else {
            Ingredient newIngredient
            try {
                newIngredient = new Ingredient(params)
                if (newIngredient.validate()) {
                    ingredientService.save(newIngredient, user, false)
                    def message = """
                    User ${user.firstName} ${user.lastName} added a new ingredient.
                    Ingredient ${newIngredient as JSON}
                """
                    Response.ok(message).build()
                } else throw new Exception("Invalid ingredient")
            } catch (Exception e) {
                logger.error("There was an exception while creating a new ingredient: ${e.message}")
                badRequest("There was an exception while creating a new ingredient: ${e.message}")
            }
        }
    }

    @Produces('application/json')
    @Path('/{ingredientId}')
    @PUT
    public Response updateAnIngredient(@PathParam('ingredientId') Long ingredientId, Map params) {
        User user = BaseResource.getAuthenticatedUser()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!user) badRequest("A user must be provided")
        else {
            Ingredient updateIngredient
            def message = ''
            try {
                updateIngredient = Ingredient.findById(ingredientId)
                params.keySet().each { String prop ->
                    // only update property if not in list
                    // if user is admin user, we can update any value we wish
                    if ( !ignoreSpecificProperties(prop) || user.isAdmin() ) {
                        def value = params."$prop"
                        println "params.$prop = $value"
                        try {
                            updateIngredient."$prop" = value
                        } catch (MissingPropertyException mpe) {
                            logger.info("${mpe.class.simpleName} for Ingredient, property:$prop")
                            message += "$prop is not a property of the Ingredient object.\n"
                        }
                    } else {
                        message += "Cannot update property: $prop\n"
                    }
                }
                if (!updateIngredient) {
                    badRequest("No ingredient was found using the id: $ingredientId")
                } else if ( !user.ingredients*.id.contains(ingredientId) ) {
                    badRequest("User does not have an ingredient using the id: $ingredientId")
                }
                if (updateIngredient.validate() && message == '') {
                    ingredientService.save(updateIngredient, user, false)
                    message = """
                    User ${user.firstName} ${user.lastName} updated their ingredient.
                    Ingredient ${updateIngredient as JSON}
                """
                    logger.info(message)
                    Response.ok(updateIngredient).build()
                }
                else if (message != '') {
                    message += "Please fix all errors to update your ingredient"
                    badRequest(message)
                }
                else throw new Exception("Invalid ingredient")
            }
            catch (Exception e) {
                logger.error("There was an exception while updating a ingredient: ${e.message}")
                badRequest("There was an exception while updating a ingredient: ${e.message}")
            }
        }
    }

    @Produces('application/json')
    @Path('/{ingredientId}')
    @DELETE
    public Response deleteAnIngredient(@PathParam('ingredientId') Long ingredientId, Map params) {
        User user = BaseResource.getAuthenticatedUser()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!user) badRequest("A user must be provided")
        else {
            Ingredient deleteIngredient
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
                    deleteIngredient = Ingredient.findById(ingredientId)
                    if (!deleteIngredient) {
                        badRequest("There was no ingredient found with id: $ingredientId")
                    } else {
                        ingredientService.delete(deleteIngredient.id, user, true)
                        Response.ok("User ${user.firstName} ${user.lastName} deleted their ingredient.").build()
                    }
                }
            } catch (Exception e2) {
                logger.error("There was an exception while trying to delete an ingredient:: ${e2.message}")
                badRequest("There was an exception while trying to delete an ingredient:: ${e2.message}")
            }
        }
    }
}
