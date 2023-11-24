package mixology.unit

import grails.testing.gorm.DomainUnitTest
import mixology.Role
import org.junit.Test
import spock.lang.Specification

import static org.junit.Assert.assertNotNull

class RoleSpec extends BaseController implements DomainUnitTest<Role> {

    @Test
    void "test role is created"() {
        when: 'roles are created'
        def role = new Role(authority: enums.Role.USER.name).save()
        def role2 = new Role(authority: enums.Role.ADMIN.name).save()

        then: 'verify they exist'
        assertNotNull role.id
        assertNotNull role2.id
    }
}
