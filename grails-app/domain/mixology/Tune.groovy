package mixology

class Tune {

    String title
    String artist
    int duration

    static constraints = {
        artist(nullable: true)
    }

    static namedQueries = {
        longSongs { seconds ->
            gte 'duration', seconds
        }
    }
}
