package api.request

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.Resource
import mixology.DrinkService

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path('/v1/drinks')
class DrinkResource {

    DrinkService drinkService

    @GET
    public Response getAllDrinks() {
        try {
            return Response.ok( drinkService.findAll()).build()
        } catch (Exception e) {
            return Response.serverError().build()
        }
    }

    @GET
    @Path('/{drinkId}')
    public Response getADrink(@PathParam('drinkId') Long drinkId) {
        return Response.ok( drinkId ).build()
    }
}
