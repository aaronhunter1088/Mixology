package mixology

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class AuthTokenServiceSpec extends Specification {

    AuthTokenService authTokenService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new AuthToken(...).save(flush: true, failOnError: true)
        //new AuthToken(...).save(flush: true, failOnError: true)
        //AuthToken authToken = new AuthToken(...).save(flush: true, failOnError: true)
        //new AuthToken(...).save(flush: true, failOnError: true)
        //new AuthToken(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //authToken.id
    }

    void "test get"() {
        setupData()

        expect:
        authTokenService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<AuthToken> authTokenList = authTokenService.list(max: 2, offset: 2)

        then:
        authTokenList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        authTokenService.count() == 5
    }

    void "test delete"() {
        Long authTokenId = setupData()

        expect:
        authTokenService.count() == 5

        when:
        authTokenService.delete(authTokenId)
        sessionFactory.currentSession.flush()

        then:
        authTokenService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        AuthToken authToken = new AuthToken()
        authTokenService.save(authToken)

        then:
        authToken.id != null
    }
}
