package mixology

class LogoutController {

    def index() {
        println "${principal.fullname} is now logged out!"
        session.invalidate()
        redirect uri: '/'
    }
}
