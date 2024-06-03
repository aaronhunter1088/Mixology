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
class LogoutControllerSpec extends BaseController implements ControllerUnitTest<LogoutController> {

    Class<?>[] getDomainClassesToMock(){ return [Drink, Ingredient, User] as Class[] }

    User testUser

    def setup() {
        def roleUser = roleService.save(Role.USER.name)
        testUser = createUser('regular')
        testUser = userService.save(testUser, false)
        userRoleService.save(testUser, roleUser)

        //controller.userService = userService
        logger = LogManager.getLogger(LogoutControllerSpec.class)
    }

    def cleanup() {}

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
        controller.springSecurityService = Stub(SpringSecurityService) { getPrincipal() >> testUser }

        when:
        controller.index()

        then:
        response.status == 302
    }
}
