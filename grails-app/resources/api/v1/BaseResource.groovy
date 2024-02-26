package api.v1

import groovy.json.JsonBuilder
import mixology.BaseController
import mixology.Role
import mixology.User
import mixology.UserRole
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.MessageSource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.i18n.SessionLocaleResolver

import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

abstract class BaseResource extends BaseController {

    static Logger logger = LogManager.getLogger(BaseResource.class)

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
                .entity(buildErrorJson(message, Response.Status.BAD_REQUEST.statusCode))
                .type(MediaType.APPLICATION_JSON)
                .build()
    }

    public static Response okResponse(String msg) {
        Response.status(Response.Status.OK)
                .entity(buildSuccessJson(msg))
                .type(MediaType.APPLICATION_JSON)
                .build()
    }

    public def static buildErrorJson(String message, int statusCode) {
        def obj = new JsonBuilder()
        obj = {
            success:false
            error: {
                code:"$statusCode"
                message:"$message"
            }
        }
        obj
    }

    public def static buildSuccessJson(String message) {
        def obj = new JsonBuilder()
        obj = {
            success:true
            message:"$message"
        }
        obj
    }

    public def static buildSuccessJson(def message) {
        def obj = new JsonBuilder()
        obj = {
            success:true
            message:"$message"
        }
        obj
    }

    public static Response notAuthorized(String message) {
        Response.status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()
    }

}
