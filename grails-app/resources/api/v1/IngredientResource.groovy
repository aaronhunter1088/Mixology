package api.v1

import grails.converters.JSON
import mixology.Ingredient
import mixology.IngredientService
import mixology.User
import org.springframework.beans.factory.annotation.Autowired

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path('/v1/ingredients')
class IngredientResource extends BaseResource {

    @Autowired
    IngredientService ingredientService

    @Produces('application/json')
    @GET
    public Response getAllIngredients() {
        try {
            def list = ingredientService.findAll()
            Response.ok( list ).build()
        } catch (Exception e) {
            Response.serverError().build()
        }
    }

    @Produces('application/json')
    @Path('/{ingredientId}')
    @GET
    public Response getAnIngredient(@PathParam('ingredientId') Long ingredientId) {
        try {
            def ingredient = Ingredient.findById(ingredientId)
            if (!ingredient) throw NotFoundException("Ingredient not found using $ingredientId")
            else Response.ok(ingredient).build()
        } catch (Exception e) {
            logger.error("There was an error finding the ingredient. Reason: ${e.getMessage()}")
            Response.serverError().build()
        }
    }

    @Produces('application/json')
    @POST
    public Response createAnIngredient(Map params) {
        User user = getAuthenticatedUser()
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
            logger.error("There was an exception while creating a new ingredient")
            Response.serverError().build()
        }
    }
}
