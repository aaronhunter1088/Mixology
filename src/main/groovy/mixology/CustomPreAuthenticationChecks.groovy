package mixology

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.SpringSecurityMessageSource
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsChecker

/**
 * Copy of the private class in AbstractUserDetailsAuthenticationProvider
 * to make subclassing or replacement easier.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public class CustomPreAuthenticationChecks implements UserDetailsChecker {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor()
    protected final Logger log = LogManager.getLogger(CustomPreAuthenticationChecks.class)

    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            log.debug("User account is locked")

            throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                    "User account is locked"))
        }

        if (!user.isEnabled()) {
            log.debug("User account is disabled")

            throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled",
                    "User is disabled"))
        }

        if (!user.isAccountNonExpired()) {
            log.debug("User account is expired")

            throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired",
                    "User account has expired"))
        }
    }
}
