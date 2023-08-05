package mixology.unit

import enums.Alcohol
import enums.GlassType
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import mixology.Drink
import mixology.Role
import mixology.UserRole
import mixology.UserService
import mixology.RoleService
import mixology.UserRoleService
import mixology.Ingredient
import mixology.User
import mixology.UserController
import org.asynchttpclient.request.body.multipart.part.InputStreamMultipartPart
import org.grails.plugins.testing.GrailsMockMultipartFile
import org.grails.plugins.testing.MockPart
import org.junit.Test
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import java.awt.image.BufferedImage

import static org.mockito.Mockito.*
import static org.springframework.web.multipart.support.StandardMultipartHttpServletRequest.StandardMultipartFile

@ContextConfiguration
class UserControllerSpec extends Specification implements ControllerUnitTest<UserController>, DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [Drink, Ingredient, User] as Class[]
    }

    def userService = getDatastore().getService(UserService)
    def roleService = getDatastore().getService(RoleService)
    def userRoleService = getDatastore().getService(UserRoleService)

    User regularUser = createUser('regular')
    User adminUser = createUser('admin')
    User unsavedUser = createUser('unsaved')
    User createUser(def type) {
        if ('regular' == type) {
            if (!regularUser) {
                regularUser = new User([
                        username: "testRegularUser@gmail.com",
                        firstName: "regular",
                        lastName: "user"
                ])
                regularUser = userService.save(regularUser, false)
                Role regularRole = new Role(authority: enums.Role.USER.name)
                UserRole.create(regularUser, regularRole)
                regularUser
            }
            else {
                regularUser
            }
        } else if ('admin' == type) {
            if (!adminUser) {
                adminUser = new User([
                        username: "testAdminUser@gmail.com",
                        firstName: "admin",
                        lastName: "user"
                ])
                adminUser = userService.save(adminUser, false)
                Role adminRole = new Role(authority: enums.Role.ADMIN.name)
                UserRole.create(adminUser, adminRole)
                adminUser
            }
            else {
                adminUser
            }
        } else {
            if (!unsavedUser) {
                unsavedUser = new User([
                        username: "testUnsavedUser@gmail.com",
                        firstName: "unsavedRegular",
                        lastName: "user"
                ])
                Role regularRole = new Role(authority: enums.Role.USER.name)
                UserRole.create(unsavedUser, regularRole)
                unsavedUser
            }
            else {
                unsavedUser
            }
        }
    }

    def setup() {
        mockDomain Drink
        mockDomain Ingredient
        mockDomain User
        controller.userService = userService
        controller.roleService = roleService
        controller.userRoleService = userRoleService
    }

    def cleanup() {
        User.all.each { it.delete()}
        println "Total user after cleanup: ${userService.count()}"
    }

    @Test
    void "test user index"() {
        when: 'call controller.index'
            controller.index()
        then:
            model.userCount == userService.count()
    }

    @Test
    void "test show user returns user"() {
        when:
            controller.show(regularUser.id)
        then:
            response.status == 200
    }

    @Test
    void "test create action"() {
        when:
            controller.create()
        then:
            response.status == 200
    }

    // Test Saving
    @Test
    void "test save fails because method is GET"() {
        given:
            request.method = 'GET'
        when:
            controller.save(regularUser)
        then:
            response.status == 405
    }

    @Test
    void "test save fails because method is PUT"() {
        given:
            request.method = 'PUT'
        when:
            controller.save(regularUser)
        then:
            response.status == 405
    }

    @Test
    void "test save fails because method is DELETE"() {
        given:
            request.method = 'DELETE'
        when:
            controller.save(regularUser)
        then:
            response.status == 405
    }

    @Test
    void "test save fails because no user sent in to save"() {
        given:
            request.method = 'POST'
        when:
            controller.save(null)
        then:
            response.status == 400
    }

    @Test
    void "test save user fails because passwordsMatch is false"() {
        given:
            request.method = 'POST'
            controller.params.passwordsMatch = false
        when:
            controller.save(regularUser)
        then:
            response.status == 400
    }

    @Test
    void "test save user fails because password and passwordConfirm do not match"() {
        given:
            request.method = 'POST'
            controller.params.password = 'testLongEnough$'
            controller.params.passwordConfirm = 'testLongEnoughAgain$'
            controller.params.passwordsMatch = true
        when:
            controller.save(regularUser)
        then:
            response.status == 400
    }

    @Test
    void "test save user fails because password is too short"() {
        given:
            Role userRole = new Role(authority: enums.Role.USER.name)
            request.method = 'POST'
            controller.params.passwordsMatch = true
            controller.params.password = 'test'
            controller.params.passwordConfirm = 'test'
            controller.params.firstName = regularUser.firstName
            controller.params.lastName = regularUser.lastName
            controller.params.email = regularUser.username
            controller.params.cellphone = '1234560789'
            controller.roleService = Stub(RoleService) {findByAuthority(enums.Role.USER.name) >> userRole }
        when:
            controller.save(unsavedUser)
        then:
            response.status == 400
            unsavedUser.id == null
    }

    @Test
    void "test save user fails because of validation errors"() {
        given:
            request.method = 'POST'
            controller.params.passwordsMatch = true
            regularUser.errors.reject('default.invalid.user.password.instance',
                [params.password, params.passwordConfirm] as Object[],
                '[Password and PasswordConfirm do not match]')
            controller.metaClass.createUserFromParams { user, params, file ->
                return regularUser
            }
        when:
            controller.save(regularUser)
        then:
            response.status == 400
    }

    @Test
    void "test save user returns success"() {
        given:
            Role userRole = new Role(authority: enums.Role.USER.name)
            request.method = 'POST'
            controller.params.passwordsMatch = true
            controller.params.password = 'testLongEnough$'
            controller.params.passwordConfirm = 'testLongEnough$'
            controller.params.firstName = unsavedUser.firstName
            controller.params.lastName = unsavedUser.lastName
            controller.params.email = unsavedUser.username
            controller.params.cellphone = '1234560789'
            controller.roleService = Stub(RoleService) {findByAuthority(enums.Role.USER.name) >> userRole }
            controller.userService = Stub(UserService) {save(_,_) >> regularUser }
        when:
            controller.save(unsavedUser)
        then:
            response.status == 200
            !unsavedUser.id
    }

    @Test
    void "test edit user returns edit user page"() {
        when:
            controller.edit(regularUser.id as Long)
        then:
            response.status == 200
    }

    // Test Updates
    @Test
    void "test updating fails because method is GET"() {
        given:
            request.method = 'GET'
        when:
            controller.update(regularUser)
        then:
            response.status == 405
    }

    @Test
    void "test updating fails because method is POST"() {
        given:
            request.method = 'POST'
        when:
            controller.update(regularUser)
        then:
            response.status == 405
    }

    @Test
    void "test updating fails because method is DELETE"() {
        given:
            request.method = 'DELETE'
        when:
            controller.update(regularUser)
        then:
            response.status == 405
    }

    @Test
    void "test updating fails because no user"() {
        when:
            request.method = 'PUT'
            controller.update(null)
        then:
            response.status == 400
    }

    @Test
    void "test updating succeeds"() {
        given:
            request.method = 'PUT'
            Drink drink = new Drink([
                drinkName: 'Drink1',
                drinkNumber: 1,
                mixingInstructions: 'Instructions',
                suggestedGlass: GlassType.HURRICANE,
                alcoholType: Alcohol.TEQUILA,
                drinkSymbol: 'D1',
                ingredients: null,
                canBeDeleted: true,
                custom: true
            ])
            regularUser.drinks = [drink]
        when:
            controller.update(regularUser)
        then:
            response.status == 302
    }

    @Test
    void "test reduce image size"() {
        given:
            BufferedImage bufImg = new BufferedImage(5,5,1)
            Expando file = new Expando()
            file.filename = 'Test File'
            file.metaClass.getBytes = {-> new byte[1]}
            controller.metaClass.scaleImage = {one, two -> bufImg}
        when:
            controller.reduceImageSize(file)
        then:
            response.status == 200
    }

    @Test
    void "test scale image"() {
        given:
            BufferedImage mockedBufImg = new BufferedImage(500,500,1)
        when:
            controller.scaleImage(mockedBufImg, 200)
        then:
            response.status == 200
    }
}
