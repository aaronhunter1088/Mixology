package api.v1

import io.swagger.annotations.Api
import mixology.BaseController
import mixology.User
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.MessageSource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.i18n.SessionLocaleResolver

import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api
abstract class BaseResource extends BaseController {

    private static Logger logger = LogManager.getLogger(BaseResource.class)

    SessionLocaleResolver localeResolver
    MessageSource messageSource

    public static User getAuthenticatedUser() {
        if ("anonymousUser" == SecurityContextHolder?.context?.authentication?.principal ) {
            throw new NotAuthorizedException("Not Authorized")
        }
        User.findByUsername( SecurityContextHolder?.context?.authentication?.principal?.username as String )
    }

    protected static boolean isAuthenticated() {
        try { SecurityContextHolder?.context?.authentication?.isAuthenticated() }
        catch (Exception e) { return false }
    }

    public static boolean ignoreSpecificProperties(String property) {
        List<String> ignoreTheseProperties = [
                'custom', 'canBeDeleted', 'id', 'userId'
        ]
        boolean result = ignoreTheseProperties.contains(property)
        logger.info("ignore specific property:: $result")
        return result
    }

    public static Response badRequest(String message) {
        Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()
    }

    public static Response notAuthorized(String message) {
        Response.status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()
    }

}
