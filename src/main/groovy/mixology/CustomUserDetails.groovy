package mixology

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class CustomUserDetails extends GrailsUser {

    final String fullname
    final String mobile
    final String photo

    CustomUserDetails(String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<GrantedAuthority> authorities,
                      long id, String fullname, String mobile, String photo) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities, id)

        this.fullname = fullname
        this.mobile = mobile
        this.photo = photo
    }
}
