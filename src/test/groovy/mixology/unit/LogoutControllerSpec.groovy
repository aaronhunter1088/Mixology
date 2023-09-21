package mixology.unit

import enums.Role
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import mixology.Drink
import mixology.Ingredient
import mixology.LogoutController
import mixology.RoleService
import mixology.User
import mixology.UserRoleService
import mixology.UserService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.Test
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

@ContextConfiguration
class LogoutControllerSpec extends Specification implements ControllerUnitTest<LogoutController>, DataTest {

    private static final Logger logger = LogManager.getLogger(LogoutControllerSpec.class)

    Class<?>[] getDomainClassesToMock(){ return [Drink, Ingredient, User] as Class[] }

    User testUser
    def userService = getDatastore().getService(UserService)
    def roleService = getDatastore().getService(RoleService)
    def userRoleService = getDatastore().getService(UserRoleService)

    def setup() {
        def roleUser = roleService.save(Role.USER.name)
        testUser = new User([
                username: "username@gmail.com",
                firstName: "test",
                lastName: "user"
        ])
        testUser = userService.save(testUser, false)
        userRoleService.save(testUser, roleUser)

        controller.userService = userService
    }

    def cleanup() {
    }

    @Test
    void "test user is set up"() {
        when:
        def user = userService.get(testUser.id)

        then:
        assert user.id != null
    }

    @Test
    void "test logging out works"() {
        given:
        Expando e = new Expando()
        e.fullname = testUser.firstName + ' ' + testUser.lastName
        controller.springSecurityService = Stub(SpringSecurityService) { getPrincipal() >> e }

        when:
        controller.index()

        then:
        response.status == FOUND.value()
    }
}
