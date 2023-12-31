package api.v1

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import mixology.AuthTokenService
import mixology.AuthToken
import mixology.User
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException

import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response
import java.time.LocalDateTime

@Path('/v1/tokens')
class TokenResource extends BaseResource {
    private static Logger logger = LogManager.getLogger( TokenResource.class )

    @Autowired
    AuthenticationManager authManager
    @Autowired
    SpringSecurityService springSecurityService
    @Autowired
    AuthTokenService authTokenService

    @Produces('application/json')
    @GET
    public Response getAllTokens() {
        User user = BaseResource.getAuthenticatedUser()
        def usersTokens = (user) ? AuthToken.findAllByUsername(user.username) : AuthToken.findAll()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!user.isAdmin()) badRequest('You must have admin privileges to view all AuthTokens')
        else if (usersTokens.isEmpty()) {
            logger.info('No tokens found for user. Returning empty list')
            Response.ok([]).build()
        } else {
            Response.ok( usersTokens ).build()
        }
    }

    @Produces('application/json')
    @Path('/{authTokenId}')
    @GET
    public Response getAToken(@PathParam('authTokenId') Long authTokenId) {
        User user = BaseResource.getAuthenticatedUser()
        AuthToken token = AuthToken.findById(authTokenId)
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!token) badRequest("No token was found using the id: $authTokenId")
        else if (!user) badRequest("A user is required to view their AuthToken")
        else if (user.username != token.username) badRequest("You cannot view a token not belonging to the user, $user")
        else {
            Response.ok(token).build()
        }
    }

    @Produces('application/json')
    @POST
    public Response createAToken(Map params) {
        def username = params.username
        def password = params.password
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password)

        User user = null
        AuthToken authToken = null
        try {
            if ( authManager.authenticate(token) ) {
                user = User.findByUsername(username as String)
                authToken = new AuthToken(user)
                if (authToken.validate()) AuthToken.withTransaction { authToken.save(flush:true) }
                else { throw new Exception("Auth Token was not created") }
                Response.ok( authToken ).build()
            } else {
                badRequest("There was an error authenticating the AuthToken")
            }
        } catch (AuthenticationException ae) {
            logger.error("Auth Manager did not authenticate token: ${ae.message}")
            badRequest("Auth Manager did not authenticate token: ${ae.message}")
        } catch (Exception e) {
            logger.error("There was an exception creating the auth token: ${e.message}")
            badRequest("There was an exception creating the auth token: ${e.message}")
        }
    }

    @Produces('application/json')
    @Path('/{authTokenId}')
    @PUT
    public Response updateAToken(@PathParam('authTokenId') Long authTokenId, Map params) {
        User user = BaseResource.getAuthenticatedUser()
        def message = ''
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!user || !user.isAdmin()) badRequest("An admin user is required to update any AuthToken")
        else {
            AuthToken token = authTokenService.get(authTokenId)
            params.keySet().each { prop ->
                // only update property if not in list
                // if user is admin user, we can update any value we wish
                if ( !ignoreSpecificProperties(prop as String) || user.isAdmin() ) {
                    def value = params."$prop"
                    println "params.$prop = $value"
                    try {
                        token."$prop" = value
                    } catch (MissingPropertyException mpe) {
                        logger.info("${mpe.class.simpleName} for AuthToken, property:$prop")
                        message += "$prop is not a property of the AuthToken object.\n"
                    } catch (GroovyCastException gce) {
                        logger.error("${gce.message}")
                        logger.error("Could not set value on $prop. Trying first Cast to Date")
                        try {
                            def date = LocalDateTime.parse((value as String).replace('Z',''))
                            token."$prop" = date
                        } catch (GroovyCastException gce2) {
                            logger.error("Could not set value on $prop because ${gce2.message}")
                            message += "Could not set value on $prop because ${gce2.message}\n"
                        }
                    }
                } else {
                    message += "Cannot update property: $prop\n"
                }
            }
            if (token.validate() && message == '') {
                authTokenService.save(token, false)
                message = """
                        Admin ${user.firstName} ${user.lastName} updated a token.
                        Token ${token as JSON}
                    """
                logger.info(message)
                Response.ok(token).build()
            } else {
                badRequest(message)
            }
        }
    }

    @Produces('application/json')
    @Path('/removeExpiredTokens')
    @DELETE
    public Response removeExpiredTokens() {
        List<Long> expiredIds = []
        User user = BaseResource.getAuthenticatedUser()
        def errorMessage = request.getAttribute("error") as String ?: ''
        if (errorMessage) badRequest(errorMessage)
        else if (!user.isAdmin()) notAuthorized("You are not authorized to call this endpoint")
        else {
            def expiredTokens = AuthToken.findAll("from AuthToken where expiresDate < :now",[now:LocalDateTime.now()]) as List<AuthToken>
            if (expiredTokens.isEmpty()) {
                Response.ok("No AuthTokens have expired.").build()
            } else {
                try {
                    authTokenService.delete(expiredTokens*.id, user, true)
                    expiredTokens*.id.each { expiredIds << it }
                    Response.ok(expiredIds).build()
                } catch (Exception e) {
                    logger.error("There was an exception while using authTokenService, method:delete because ${e.message}")
                    badRequest("There was an exception while using authTokenService, method:delete because ${e.message}")
                }
            }
        }
    }
}
