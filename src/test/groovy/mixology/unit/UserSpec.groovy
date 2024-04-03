package mixology.unit

import grails.testing.gorm.DomainUnitTest
import mixology.Role
import mixology.User
import mixology.UserRole
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static groovy.test.GroovyAssert.assertNotNull
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertNull

class UserSpec extends BaseController implements DomainUnitTest<User> {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    void "test creating a valid user"() {
        when:
        User testMe = createUser('Tobias')
        testMe.save(failOnError:true)

        then:
        assertNotNull(testMe.id)
        assertTrue testMe.toString() == 'Tobias Husky'

        and: 'user role is created'
        def role = new Role(authority: enums.Role.USER.name).save()

        and: 'userRole is created using user and role'
        def userRole = UserRole.create(testMe, role).save(flush:true)

        and: 'role is retrievable on user'
        def retrievedRole = testMe.getAuthorities()[0]

        and: 'role matches one created for user'
        userRole.id == retrievedRole.id
    }

    @Unroll('User.validate() with firstName: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test user validation with firstName"() {
        when:
        domain.firstName = value

        then:
        expected == domain.validate(['firstName'])
        domain.errors['firstName']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        null    | false    | 'nullable'
        ''      | false    | 'blank'
        'T'     | false    | 'size.toosmall'
        'T'*41  | false    | 'size.toobig'
        'Good'  | true     | null
    }

    @Unroll('User.validate() with lastName: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test user validation with lastName"() {
        when:
        domain.lastName = value

        then:
        expected == domain.validate(['lastName'])
        domain.errors['lastName']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        null    | false    | 'nullable'
        ''      | false    | 'blank'
        'T'     | false    | 'size.toosmall'
        'T'*41  | false    | 'size.toobig'
        'Good'  | true     | null
    }

    @Unroll('User.validate() with firstName: #value should have returned #expected with errorCode: #expectedErrorCode')
    @Test
    void "test user validation with username1"() {
        when:
        domain.username = value

        then:
        expected == domain.validate(['username'])
        domain.errors['username']?.code == expectedErrorCode

        where:
        value   | expected | expectedErrorCode
        null    | false    | 'nullable'
        ''      | false    | 'blank'
    }

    @Test
    void "test user validation with username2"() {
        when:
            def user1 = new User([
                    firstName: 'Tobias',
                    lastName: 'Husky',
                    username: 'thusky@gmail.com',
                    email: 'thusky@gmail.com',
                    password: 'p@ssword1',
                    passwordConfirm: 'p@ssword1'
            ])

        then:
            user1.validate()

        and: 'there is another user to save'
            user1.save(flush:true)
        and:
            assertNotNull user1.id
        and: 'attempt to save another user with same username'
        when:
            def user2 = new User([
                    firstName: 'Tobias',
                    lastName: 'Husky',
                    username: 'thusky@gmail.com',
                    email: 'thusky@gmail.com',
                    password: 'p@ssword1'
            ])

        then: 'Failed to validate user2'
            assertFalse user2.validate(['username'])
        and: 'user2 has an error code'
            user2.errors['username']?.code == 'unique'
        and: 'user2 did not save'
            assertTrue !user2.save()
        and: 'user2 does not have an id'
            assertNull user2.id
    }

    @Test
    void "test user validation with email"() {
        when:
        def user1 = new User([
                firstName: 'Tobias',
                lastName: 'Husky',
                username: 'thusky@gmail.com',
                email: 'thusky@gmail.com',
                password: 'p@ssword1',
                passwordConfirm: 'p@ssword1'
        ])

        then:
        user1.validate()

        and: 'there is another user to save'
        user1.save(flush:true)
        and:
        assertNotNull user1.id
        and: 'attempt to save another user with same email'
        when:
        def user2 = new User([
                firstName: 'Tobias',
                lastName: 'Husky',
                username: 'thusky@gmail.com',
                email: 'thusky@gmail.com',
                password: 'p@ssword1'
        ])

        then: 'Failed to validate user2'
        assertFalse user2.validate(['email'])
        and: 'user2 has an error code'
        user2.errors['email']?.code == 'unique'
        and: 'user2 did not save'
        assertTrue !user2.save()
        and: 'user2 does not have an id'
        assertNull user2.id
    }

    @Test
    void "test user validation with password"() {
        when:
        def user = new User([
                firstName: 'Tobias',
                lastName: 'Husky',
                username: 'thusky@gmail.com',
                email: 'thusky@gmail.com',
                password: ''
        ])

        then: 'user is not valid because password blank'
        assertFalse user.validate()

        and: 'user has error code'
        user.errors['password']?.code == 'nullable'

        when: 'user updates password to be too small'
        user.password = 'bad'

        then: 'user is not valid because of password validation'
        assertFalse user.validate()

        and: 'user has error code'
        user.errors['password']?.code == 'size.toosmall'

        when: 'update password to be too big'
        user.password = 'stillThisPasswordNoGood'

        then: 'user is not valid'
        assertFalse user.validate()

        and: 'user has error code'
        user.errors['password']?.code == 'default.invalid.user.password.criteria'

        when: 'update password to fail custom validator'
        user.password = 'failsValidator'

        then: 'user is not valid'
        assertFalse user.validate()

        and: 'user has error code'
        user.errors['password']?.code == 'default.invalid.user.password.criteria'

        when: 'update password to still fail custom validator'
        user.password = 'noSpecialChar'

        then: 'user is not valid'
        assertFalse user.validate()

        and: 'user has error code'
        user.errors['password']?.code == 'default.invalid.user.password.criteria'

        when: 'update password to not include proper special characters'
        user.password = '^notAcceptable'

        then: 'user is not valid'
        assertFalse user.validate()

        and: 'user has error code'
        user.errors['password']?.code == 'default.invalid.user.password.criteria'
    }

    @Test
    void "test user password is acceptable"() {
        when:
        def user = new User([
                firstName: 'Tobias',
                lastName: 'Husky',
                username: 'thusky@gmail.com',
                email: 'thusky@gmail.com',
                password: 'p@ssword$2',
                passwordConfirm: 'p@ssword$2'
        ])

        then:
        assertTrue( user.validate() )

        and:
        assertNull ( user.errors['password']?.code )
    }

    @Test
    void "test user validation with mobile number"() {
        when: 'create user with invalid mobile number'
        def user = new User([
                firstName: 'Tobias',
                lastName: 'Husky',
                username: 'thusky@gmail.com',
                email: 'thusky@gmail.com',
                password: 'p@ssword!',
                passwordConfirm: 'p@ssword!',
                mobileNumber: 123
        ])

        then:
        assertFalse user.validate()

        and:
        user.errors['mobileNumber']?.code == 'size.toosmall'

        when: 'update user mobile number to be too big'
        user.mobileNumber = '1'*11

        then:
        assertFalse user.validate()

        and:
        user.errors['mobileNumber']?.code == 'size.toobig'

        when: 'update user mobile number to be 10 digits'
        user.mobileNumber = '1'*10

        then:
        assertTrue ( user.validate() )

        and:
        assertNull ( user.errors['mobileNumber']?.code )
    }

}