package api.v1

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import junit.framework.TestCase
import mixology.AuthTokenService
import mixology.Drink
import mixology.DrinkService
import mixology.Ingredient
import mixology.IngredientService
import mixology.RoleService
import mixology.User
import mixology.Role
import mixology.UserRole
import mixology.UserRoleService
import mixology.UserService
import org.apache.logging.log4j.LogManager
import org.grails.testing.GrailsUnitTest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import spock.lang.Specification

class DrinkResourceTest extends Specification implements DataTest {

    private static def logger = LogManager.getLogger(DrinkResourceTest.class)
    private DrinkResource resource
    private User adminUser

    def drinkService = new DrinkService()
    def ingredientService = new IngredientService()
    def userService
    def roleService
    def userRoleService
    def authTokenService = new AuthTokenService()
    Class<?>[] getDomainClassesToMock(){ return [Drink, Role, User, UserRole] as Class[] }

    @BeforeClass
    void beforeClass() {
        logger.info("DrinkResourceTests starting")
    }

    @BeforeEach
    void setupEach() {
        getDomainClassesToMock()
        resource = new DrinkResource()
        userService = new UserService()
        roleService = new RoleService()
        userRoleService = new UserRoleService()

        adminUser = createUser('admin')
        userService.save(adminUser, false)
        def roleAdmin = roleService.save('ROLE_ADMIN')
        userRoleService.save(adminUser, roleAdmin)

        //controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
        drinkService.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> adminUser}

        resource.drinkService = this.drinkService
    }

    @AfterEach
    void tearDown() {
        resource = null
    }

    @AfterClass
    void tearDownAll() {
        logger.info("DrinkResourceTests concluded")
    }

    @Test
    void "test"() {
        assert true
    }

    // User methods
    User createUser(def type) {
        User user = null
        switch (type) {
            case 'regular' : {
                user = new User([
                        username: "testRegularUser@gmail.com",
                        password: "testMe123\$",
                        passwordConfirm: "testMe123\$",
                        email: "testRegularUser@gmail.com",
                        firstName: "regular",
                        lastName: "user"
                ])
                user = userService.save(user, false)
                Role regularRole = new Role(authority: enums.Role.USER.name)
                UserRole.create(user, regularRole)
                user
                break
            }
            case 'admin' : {
                user = new User([
                        username: "testAdminUser@gmail.com",
                        email: "testAdminUser@gmail.com",
                        password: "testMe123\$",
                        passwordConfirm: "testMe123\$",
                        firstName: "admin",
                        lastName: "user"
                ])
                user
                break
            }
            case 'Tobias' : {
                user = new User([
                        firstName: 'Tobias',
                        lastName: 'Husky',
                        username: 'thusky@gmail.com',
                        email: 'thusky@gmail.com',
                        password: 'p@ssword1',
                        passwordConfirm: 'p@ssword1'
                ])
                break
            }
            case 'unsaved' : {
                user = new User([
                        username: "testUnsavedUser@gmail.com",
                        email: "testUnsavedUser@gmail.com",
                        password: "testMe123\$",
                        passwordConfirm: "testMe123\$",
                        firstName: "unsavedRegular",
                        lastName: "user"
                ])
                Role regularRole = new Role(authority: enums.Role.USER.name)
                UserRole.create(user, regularRole)
                user
                break
            }
            default : throw new Exception("Unknown user option: $type")
        }
        user
    }
}
