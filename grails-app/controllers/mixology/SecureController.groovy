package mixology

import grails.plugin.springsecurity.annotation.Secured

class SecureController {

    //@Secured(['ROLE_ADMIN,ROLE_USER'])
    @Secured('IS_AUTHENTICATED_FULLY')
    def index() {
        render 'Secure access only'
    }
}
