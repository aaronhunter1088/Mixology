package mixology

import grails.config.Config
import grails.converters.JSON
import grails.core.support.GrailsConfigurationAware
import groovy.sql.Sql
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.multiverse.api.exceptions.LockedException
import grails.plugin.springsecurity.SpringSecurityUtils
import javax.security.auth.login.AccountExpiredException
import javax.security.auth.login.CredentialExpiredException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import java.sql.Timestamp
import java.time.LocalDateTime

class LoginController extends grails.plugin.springsecurity.LoginController implements GrailsConfigurationAware {

    private static Logger logger = LogManager.getLogger(LoginController.class)

    def springSecurityService

    def index = {
        ConfigObject conf = getConf()
        if (springSecurityService.isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl//SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
        }
        else {
            redirect action: 'auth', params: params
        }
    }

    def auth = {

        ConfigObject conf = getConf()

        if (springSecurityService.isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
            return
        }

        //Collections.shuffle(coordinatePositions)
        //String position = coordinatePositions.first()

        String postUrl = request.contextPath + conf.apf.filterProcessesUrl
        logger.info("postUrl: ${postUrl}")
        render view: 'auth', model: [postUrl: postUrl,
                                     rememberMeParameter: conf.rememberMe.parameter,
                                     usernameParameter: conf.apf.usernameParameter,
                                     passwordParameter: conf.apf.passwordParameter]
                                     //,gspLayout: conf.gsp.layoutAuth]
                                     //,position: position]
    }

    /**
     * Show denied page.
     */
    def denied = {
        if (springSecurityService.isLoggedIn()) {//&&
            //authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
            // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
            redirect action: 'full', params: params
        }
    }

    /**
     * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
     */
    def full = {
        def config = SpringSecurityUtils.securityConfig
        def postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
        logger.info("postUrl: ${postUrl}")
        render view: 'auth', params: params,
                model: [//hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
                        postUrl: postUrl]
    }

    /**
     * The redirect action for Ajax requests.
     */
    def authAjax = {
        response.setHeader 'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
        response.sendError HttpServletResponse.SC_UNAUTHORIZED
    }

    /**
     * Callback after a failed login. Redirects to the auth page with a warning message.
     */
    def authfail = {
        def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY]// .SPRING_SECURITY_LAST_USERNAME_KEY]
        String msg = ''
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.expired")
            }
            else if (exception instanceof CredentialExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.passwordExpired")
            }
            else if (exception instanceof LockedException) {
                msg = g.message(code: "springSecurity.errors.login.locked")
            }
            else {
                msg = g.message(code: "springSecurity.errors.login.fail")
            }
        }

        if (springSecurityService.isAjax(request)) {
            render([error: msg] as JSON)
        }
        else {
            flash.message = msg
            redirect action: 'auth', params: params
        }
    }

    /**
     * The Ajax success redirect url.
     */
    def ajaxSuccess = {
        render([success: true, username: springSecurityService.authentication.name] as JSON)
    }

    /**
     * The Ajax denied redirect url.
     */
    def ajaxDenied = {
        render([error: 'access denied'] as JSON)
    }

    def forgotPassword = {
        render view:'forgotPassword'
    }

    def resetPassword = {
        def dataSource = grailsApplication.mainContext.dataSource
        def db = new Sql(dataSource)
        db.eachRow("select * from userPasswordReset where token='"+params.token+"';" as String) {
            if (LocalDateTime.now() > (it['tokenExpires'] as Timestamp)?.toLocalDateTime()) {
                logger.info("The token has expired. User must request a new Reset Link")
                flash.message = "${g.message(code:'token.has.expired.message', default:'The token has expired. Please request a new Reset Link.')}"
                redirect controller:'login', action:'forgotPassword'
            } else {
                render view:'resetPassword'
            }
        }
    }

    @Override
    void setConfiguration(Config co) {
        //coordinatePositions = co.getProperty('security.coordinate.positions', List, []) as List<String>
    }
}
