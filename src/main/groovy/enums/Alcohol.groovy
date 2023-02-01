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

}