package api.v1

import grails.plugin.springsecurity.SpringSecurityService
import mixology.AuthToken
import mixology.User
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException

import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path('/v1/tokens')
class TokenResource extends BaseResource {
    private static Logger logger = LogManager.getLogger( TokenResource.class )

    @Autowired
    AuthenticationManager authManager
    @Autowired
    SpringSecurityService springSecurityService

    @Produces('application/json')
    @GET
    public Response getAllTokens() {
        User user = getAuthenticatedUser()
        def usersTokens = AuthToken.findAllByUsername(user.username)

        if (usersTokens.isEmpty()) {
            logger.info('No tokens found for user. Returning empty list')
            Response.ok([]).build()
        } else {
            Response.ok( usersTokens ).build()
        }
    }

    @Transactional
    @Produces('application/json')
    @Path('/create')
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
                throw new Exception("There was an error creating the AuthToken")
            }
        } catch (AuthenticationException ae) {
            logger.error("Auth Manager did not authenticate token")
            Response.serverError().build()
        } catch (Exception e) {
            logger.error("There was an exception creating the auth token")
            Response.serverError().build()
        }

    }
}
