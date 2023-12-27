package api.v1

import mixology.User
import mixology.UserService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path('/v1/users')
class UserResource extends BaseResource {

    private static Logger logger = LogManager.getLogger(UserResource.class)

    @Autowired
    UserService userService

    @Produces('application/json')
    @GET
    public Response getUserDetails() {
        User loggedInUser = getAuthenticatedUser()
        if (!loggedInUser) badRequest("You must authenticate yourself for this request.")
        else {
            Response.ok(loggedInUser).build()
        }
    }

    @Produces('application/json')
    @Path('/{userId}')
    @GET
    public Response getUserDetails(@PathParam('userId') Long userId) {
        User loggedInUser = getAuthenticatedUser()
        if (!loggedInUser) badRequest("You must be logged in to retrieve specific user credentials")
        else {
            User userToObtain = userService.get(userId)
            if (loggedInUser.isAdmin()) {
                if (!userToObtain) badRequest("No user found using id:$userId")
                else Response.ok(userToObtain).build()
            } else {
                if (userToObtain?.id != loggedInUser.id) {
                    logger.info("The loggedInUser attempted to retrieve details other than themself. Not allowing")
                    badRequest("You cannot retrieve user details that are not yours. Try with id=${loggedInUser.id}")
                } else {
                    Response.ok(userToObtain).build()
                }
            }
        }
    }

}
