package mixology.unit

import grails.testing.gorm.DomainUnitTest
import mixology.Role
import mixology.User
import mixology.UserRole
import org.junit.Test
import spock.lang.Specification

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class UserRoleSpec extends BaseController implements DomainUnitTest<UserRole> {

    def user
    def role_user
    def role_admin

    def setup() {
        user = createUser('Tobias')
        user.save(failOnError:true)

        role_user = new Role([authority:enums.Role.USER.name])
        role_user.save(failOnError:true)

        role_admin = new Role([authority:enums.Role.ADMIN.name])
        role_admin.save(failOnError:true)
    }

    def cleanup() {

    }

    @Test
    void "test getting userrole assigned to user"(){
        when:
        def userRole = UserRole.create(user as User, role_user as Role)
        userRole.save(failOnError:true)

        then:
        assertNotNull userRole.id

        and: 'verify role can be retrieved'
        def retrievedUserRole = UserRole.get(user.id, role_user.id)

        and: 'retrieved userRole is same as saved userRole'
        assertTrue retrievedUserRole.id == userRole.id

        and: 'retrieved userRole exists'
        assertTrue UserRole.exists(user.id, role_user.id)
    }

    @Test
    void "test removing userRole from assigned user"() {
        when: 'user is assigned role they should not have'
        def userRole = UserRole.create(user, role_admin).save(failOnError:true)

        then: 'verify user has admin role'
        assertTrue UserRole.get(user.id, role_admin.id).id.equals( userRole.id )

        and:
        assertTrue UserRole.exists(user.id, role_admin.id)

        and:
        user.getAuthorities().size() == 1

        when: 'test removing role removes the role'
        assertTrue UserRole.remove(user, role_admin)

        then: 'userRole does not exist'
        assertNull UserRole.findById(userRole.id)

        and: 'user does not have role anymore'
        assert user.getAuthorities().size() == 0
    }

    @Test
    void "test UserRole1 is not equals to UserRole2"(){
        when: 'userRoles are created'
        UserRole ur1 = UserRole.create(user, role_user).save()
        UserRole ur2 = UserRole.create(user, role_admin).save()

        then:
        assertFalse ur1.equals(ur2)
    }
}
