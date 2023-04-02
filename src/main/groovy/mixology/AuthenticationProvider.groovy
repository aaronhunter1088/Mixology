package mixology

import groovy.transform.CompileStatic
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
class AuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        //super.additionalAuthenticationChecks(userDetails, authentication)
        String presentedPassword = authentication.getCredentials().toString()
        String encodedPresented = this.passwordEncoder.encode(presentedPassword)
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value")
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
        }

        Object details = authentication.details

        if ( !(details instanceof CustomUserDetails) ) {
            logger.debug("Authentication failed: authenticationToken principal is not a Principal");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

}
