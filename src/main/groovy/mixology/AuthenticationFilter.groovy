package mixology

import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class AuthenticationFilter extends GenericFilterBean {

    def authenticationService

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        TrustedToken trustedToken = new TrustedToken(request)
        if (trustedToken?.authPresent) {
            try {
                authenticationService.checkToken( trustedToken )
                chain.doFilter(request, response)
            }
            catch (Exception e) {
                logger.error("There was an exception while checking trusted token: ${e.message}")
                chain.doFilter(request, response)
            }
        } else {
            chain.doFilter(request, response)
        }
    }

}
