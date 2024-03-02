package mixology

import groovy.transform.ToString
import org.springframework.beans.factory.annotation.Autowired

import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest

import static mixology.MixologyConstants.BASIC_AUTHORIZATION_HEADER_KEY
import static mixology.MixologyConstants.TOKEN_AUTHORIZATION_HEADER_KEY

@ToString
class TrustedToken {

    ServletRequest servletRequest
    HttpServletRequest httpServletRequest
    String authTokenValue
    boolean api = false
    boolean basic = false
    boolean authPresent = false
    String username
    String password


    public TrustedToken(User user, HttpServletRequest request){
        this.basic = true
        this.authPresent = true
        this.username = user.username
        this.password = UserPasswordEncoderListener.decodePasswordConfirm(user.passwordConfirm)
        this.httpServletRequest = request
    }
    public TrustedToken( ServletRequest servletRequest ) {
        this.servletRequest = servletRequest
        if( servletRequest instanceof HttpServletRequest ) {
            this.httpServletRequest = servletRequest
        }
        determineAuthorization()
    }

    protected void determineAuthorization() {
        if (httpServletRequest) {
            String basicAuth = httpServletRequest.getHeader(BASIC_AUTHORIZATION_HEADER_KEY)
            if (basicAuth) {
                String[] decoded = new String(basicAuth.replaceAll(/[bB]asic\s/, '').decodeBase64()).split(':')
                if ( decoded?.size() > 1) {
                    this.username = decoded[0]
                    this.password = decoded[1]
                    this.basic = true
                    this.authPresent = true
                }
            }
            String tokenValue = httpServletRequest.getHeader(TOKEN_AUTHORIZATION_HEADER_KEY)
            if (tokenValue) {
                this.basic = false
                this.api = true
                AuthToken authToken = AuthToken.withTransaction {AuthToken.findByTokenValue(tokenValue)}
                if (authToken) {
                    this.username = authToken.username
                    this.authPresent = true
                    this.authTokenValue = authToken.tokenValue
                }
            }
        }
    }


    @Override
    public String toString() {
        """{
        authTokenValue='$authTokenValue'
        , api=$api
        , basic=$basic
        , authPresent=$authPresent
        , username=$username
        , password=$password
        }"""
    }
}
