package api.v1

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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
        User loggedInUser = BaseResource.getAuthenticatedUser()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!loggedInUser) badRequest("You must authenticate yourself for this request.")
        else if (!loggedInUser.isAdmin()) { Response.ok(userService.list(loggedInUser)).build() }
        else {
            def list = userService.list()
            def jsonSlurper = new JsonSlurper()
            def jsonList = []
            list.each{ user ->
                def userObj = jsonSlurper.parseText(user.properJsonString())
                jsonList << userObj
            }
            Response.ok(jsonList).build()
        }
    }

    @Produces('application/json')
    @Path('/{userId}')
    @GET
    public Response getUserDetails(@PathParam('userId') Long userId) {
        User loggedInUser = BaseResource.getAuthenticatedUser()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!loggedInUser) badRequest("You must be logged in to retrieve specific user credentials")
        else {
            def userToObtain = userService.get(userId)
            if (loggedInUser.isAdmin()) {
                if (!userToObtain) badRequest("No user found using id:$userId")
                else {
                    def jsonSlurper = new JsonSlurper()
                    def userObj = jsonSlurper.parseText(userToObtain.properJsonString())
                    Response.ok(userObj).build()
                }
            } else {
                if (userToObtain?.id != loggedInUser.id) {
                    logger.info("The loggedInUser attempted to retrieve details other than themself. Not allowing")
                    badRequest("You cannot retrieve user details that are not yours. Try with id=${loggedInUser.id}")
                } else {
                    Response.ok(userToObtain.toJsonString()).build()
                }
            }
        }
    }

}
