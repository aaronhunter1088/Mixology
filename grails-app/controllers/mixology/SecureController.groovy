package mixology

import grails.plugin.springsecurity.annotation.Secured


class SecureController {

    @Secured('ROLE_USER')
    def index() {
        render 'Secure access only'
    }
}
