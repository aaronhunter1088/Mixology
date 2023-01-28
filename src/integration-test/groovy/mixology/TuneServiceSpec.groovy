package mixology

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TuneServiceSpec extends Specification {

    TuneService songService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Song(...).save(flush: true, failOnError: true)
        //new Song(...).save(flush: true, failOnError: true)
        //Song song = new Song(...).save(flush: true, failOnError: true)
        //new Song(...).save(flush: true, failOnError: true)
        //new Song(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //song.id
    }

    void "test get"() {
        setupData()

        expect:
        songService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Tune> songList = songService.list(max: 2, offset: 2)

        then:
        songList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        songService.count() == 5
    }

    void "test delete"() {
        Long songId = setupData()

        expect:
        songService.count() == 5

        when:
        songService.delete(songId)
        sessionFactory.currentSession.flush()

        then:
        songService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Tune song = new Tune()
        songService.save(song)

        then:
        song.id != null
    }
}
