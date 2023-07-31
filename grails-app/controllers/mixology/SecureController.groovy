package mixology

import grails.plugin.springsecurity.annotation.Secured

//TODO: Delete this controller. POC
class SecureController {
    @Secured('IS_AUTHENTICATED_FULLY')
    def index() {
        render 'Secure access only'
    }
}
