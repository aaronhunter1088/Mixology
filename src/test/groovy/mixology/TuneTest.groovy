package mixology

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class TuneTest extends Specification implements DomainUnitTest<Tune> {

    def song1 = Tune.create()
    def song2 = Tune.create()

    def setup() {
        song1.duration = 900
        song1.title = "Hershe and Molly"
        song1.artist = "Michael"
        song1.save()

        song2.duration = 800
        song2.title = "Tobias and Einstein"
        song2.artist = "Michael"
        song2.save()
    }

    def cleanup() {
    }

    void "find long songs"() {
        assert Tune.longSongs(90).count() == 2
    }
}
