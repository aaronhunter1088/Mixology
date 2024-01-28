package mixology.unit

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import mixology.AuthToken
import mixology.User
import mixology.AuthTokenController
import mixology.AuthTokenService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
class AuthTokenControllerSpec extends BaseController implements ControllerUnitTest<AuthTokenController> {

    private static final Logger logger = LogManager.getLogger(AuthTokenControllerSpec.class)

    Class<?>[] getDomainClassesToMock(){ return [AuthToken, User] as Class[] }

    def populateValidParams(params) {
        //assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        //assert false // "TODO: Provide a populateValidParams() implementation for this generated test suite"
    }

    def authTestUser

    def setup() {
        authTestUser = createUser('regular')
        userService.save(authTestUser, false)

        controller.userService = userService
        controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> authTestUser}
    }

    void "Test the index action returns the correct model"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            list(_) >> []
            count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.authTokenList
        model.authTokenCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.authToken!= null
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/authToken/index'
        flash.message != null
    }

    void "Test the save action correctly persists"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {}

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def authToken = new AuthToken(params)
        authToken.id = 1

        controller.save(authToken)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/authToken/show/1'
        controller.flash.message != null
    }

    void "Test the save action with an invalid instance"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            save(_ as AuthToken) >> { AuthToken authToken ->
                throw new ValidationException("Invalid instance", authToken.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def authToken = new AuthToken()
        controller.save(authToken)

        then:"The create view is rendered again with the correct model"
        model.authToken != null
        view == 'create'
    }

    void "Test the show action with a null id"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            get(2) >> new AuthToken()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.authToken instanceof AuthToken
    }

    void "Test the edit action with a null id"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            get(2) >> new AuthToken()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.authToken instanceof AuthToken
    }


    void "Test the update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/authToken/index'
        flash.message != null
    }

    void "Test the update action correctly persists"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            save(_) >> new AuthToken()
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def authToken = new AuthToken(params)
        authToken.id = 1

        controller.update(authToken)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/authToken/show/1'
        controller.flash.message != null
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {
            save(_ as AuthToken) >> { AuthToken authToken ->
                throw new ValidationException("Invalid instance", authToken.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new AuthToken())

        then:"The edit view is rendered again with the correct model"
        model.authToken != null
        view == 'edit'
    }

    void "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A 404 is returned"
        response.redirectedUrl == '/authToken/index'
        flash.message != null
    }

    void "Test the delete action with an instance"() {
        given:
        controller.authTokenService = Stub(AuthTokenService) {}

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/authToken/index'
        flash.message != null
    }
}






