//package mixology.api.v1
//
//
//import api.v1.DrinkResource
//import grails.plugin.springsecurity.SpringSecurityService
//import grails.testing.gorm.DataTest
//import mixology.AuthTokenService
//import mixology.Drink
//import mixology.DrinkService
//import mixology.IngredientService
//import mixology.RoleService
//import mixology.User
//import mixology.Role
//import mixology.UserRole
//import mixology.UserRoleService
//import mixology.UserService
//import org.apache.logging.log4j.LogManager
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.test.context.ContextConfiguration
//
//@ContextConfiguration
//class DrinkResourceTest extends groovy.test.GroovyAssert implements DataTest {
//
//    Class<?>[] getDomainClassesToMock(){ return [Drink, Role, User, UserRole] as Class[] }
//
//    private static def logger = LogManager.getLogger(DrinkResourceTest.class)
//    private DrinkResource resource
//    private User adminUser
//
//    def drinkService
//    def ingredientService
//    def userService
//    def roleService
//    def userRoleService
//    def authTokenService
//
//    static void beforeAll() {
//        logger.info("DrinkResourceTests starting")
//    }
//
//    @BeforeEach
//    void setupEach() {
//        getDomainClassesToMock()
//        resource = new DrinkResource()
//        drinkService = new DrinkService()
//        ingredientService = new IngredientService()
//        userService = new UserService()
//        roleService = new RoleService()
//        userRoleService = new UserRoleService()
//        authTokenService = new AuthTokenService()
//
//        adminUser = createUser('admin')
//        adminUser.save()
//        logger.info("adminUser saved: id-${adminUser.id}")
//        def roleAdmin = roleService.save('ROLE_ADMIN')
//        UserRole.create(roleAdmin, roleOfUser, true)
//
//        //controller.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> testUser}
//        drinkService.springSecurityService = Stub(SpringSecurityService) {getPrincipal() >> adminUser}
//
//        resource.drinkService = this.drinkService
//    }
//
//    @AfterEach
//    void tearDown() {
//        resource = null
//    }
//
//    static void tearDownAll() {
//        logger.info("DrinkResourceTests concluded")
//    }
//
//    @Test
//    void "test"() {
//        assert true
//    }
//
//    // User methods
//    User createUser(def type) {
//        User user = null
//        switch (type) {
//            case 'regular' : {
//                user = new User([
//                        username: "testRegularUser@gmail.com",
//                        password: "testMe123\$",
//                        passwordConfirm: "testMe123\$",
//                        email: "testRegularUser@gmail.com",
//                        firstName: "regular",
//                        lastName: "user"
//                ])
//                user = userService.save(user, false)
//                Role regularRole = new Role(authority: enums.Role.USER.name)
//                UserRole.create(user, regularRole)
//                user
//                break
//            }
//            case 'admin' : {
//                user = new User([
//                        username: "testAdminUser@gmail.com",
//                        email: "testAdminUser@gmail.com",
//                        password: "testMe123\$",
//                        passwordConfirm: "testMe123\$",
//                        firstName: "admin",
//                        lastName: "user"
//                ])
//                user
//                break
//            }
//            case 'Tobias' : {
//                user = new User([
//                        firstName: 'Tobias',
//                        lastName: 'Husky',
//                        username: 'thusky@gmail.com',
//                        email: 'thusky@gmail.com',
//                        password: 'p@ssword1',
//                        passwordConfirm: 'p@ssword1'
//                ])
//                break
//            }
//            case 'unsaved' : {
//                user = new User([
//                        username: "testUnsavedUser@gmail.com",
//                        email: "testUnsavedUser@gmail.com",
//                        password: "testMe123\$",
//                        passwordConfirm: "testMe123\$",
//                        firstName: "unsavedRegular",
//                        lastName: "user"
//                ])
//                Role regularRole = new Role(authority: enums.Role.USER.name)
//                UserRole.create(user, regularRole)
//                user
//                break
//            }
//            default : throw new Exception("Unknown user option: $type")
//        }
//        user
//    }
//}
