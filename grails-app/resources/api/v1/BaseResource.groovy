package api.v1

import mixology.User
import org.springframework.context.MessageSource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.i18n.SessionLocaleResolver

import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

class BaseResource {

    SessionLocaleResolver localeResolver
    MessageSource messageSource

    protected static User getAuthenticatedUser() {
        if ("anonymousUser" == SecurityContextHolder?.context?.authentication?.principal ) {
            throw new NotAuthorizedException("Not Authorized")
        }
        User.findByUsername( SecurityContextHolder.context.authentication.principal.username as String )
    }

    protected static boolean isAuthenticated() {
        try { SecurityContextHolder?.context?.authentication?.isAuthenticated() }
        catch (Exception e) { return false }
    }

    public static Response badRequest(String message) {
        Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()
    }

}
