import lombok.ToString

@ToString
enum DrinkType {

    TEQUILA("Tequila"),
    VODKA("Vodka"),
    GIN("Gin"),
    FROZEN("Frozen"),
    SHOOTER("Shooter")

    String drinkName
    DrinkType(drinkName) {
        this.drinkName = drinkName
    }

}