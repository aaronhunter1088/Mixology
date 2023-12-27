package mixology

import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

import javax.servlet.http.HttpServletRequest
import java.time.LocalDateTime

class AuthenticationService  {

    @Autowired
    SpringSecurityService springSecurityService
    @Autowired
    UserPasswordEncoderListener userPasswordEncoderListener

    public void checkToken(TrustedToken trustedToken ) throws Exception {
        if (trustedToken.basic) {
            handleBasic(trustedToken)
        } else if (trustedToken.api) {
            handleToken(trustedToken)
        } else {
            throw new Exception("Could not validate type of TrustedToken:${trustedToken}")
        }
    }

    protected void handleBasic(TrustedToken trustedToken) {
        User user = User.withTransaction {User.findByUsername(trustedToken?.username)}
        if (!user) throw new Exception("Could not find a user on this token")
        else {
            boolean passwordsMatch = userPasswordEncoderListener.passwordEncoder.matches(trustedToken.password, user.password)
            if (!passwordsMatch) throw new RuntimeException("BASIC Authentication failed. Password does not match expected.")
            else {
                login(user, trustedToken.httpServletRequest)
            }
        }
    }

    protected void handleToken(TrustedToken trustedToken) throws Exception {
        AuthToken authToken = AuthToken.withTransaction {AuthToken.findByTokenValue(trustedToken.authTokenValue)}
        if (!authToken) throw new Exception("No auth token was found using the value ${trustedToken.authTokenValue}")
        else {
            User.withTransaction {
                User user = User.withTransaction {User.findByUsername( trustedToken.username,[cache:true] )}
                login(user, trustedToken.httpServletRequest)
                authToken.lastUsedDate = LocalDateTime.now()
                authToken.save(flush:true)
            }
        }
    }

    private void login(User user, HttpServletRequest request) {
        User.withNewSession {
            List<GrantedAuthority> springAuthorities = []
            user.getAuthorities().each{ role -> springAuthorities << new SimpleGrantedAuthority(role.authority) }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.password, springAuthorities)
            SecurityContextHolder.context.authentication = token
            request.session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.context)
        }
    }
}
