package enums

import lombok.ToString

@ToString
enum Alcohol {

    TEQUILA("Tequila"),
    VODKA("Vodka"),
    GIN("Gin"),
    FROZEN("Frozen"),
    SHOOTER("Shooter")

    String alcoholName
    Alcohol(alcoholName) {
        this.alcoholName = alcoholName
    }

    public static Alcohol getRandomAlcohol() {
        List<Alcohol> alcohols = Collections.unmodifiableList(Arrays.asList(values()))
        Random random = new Random()
        def randomAlcohol = alcohols.get(random.nextInt(alcohols.size()))
        randomAlcohol
    }

}