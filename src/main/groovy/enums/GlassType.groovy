package enums

import lombok.ToString

@ToString
enum GlassType {

    ABSINTHE("Absinthe glass"),
    BALLOON_WINE("Balloon wine glass"),
    BEER_MUG("Beer mug"),
    BRANDY("Brandy glass"),
    CHAMPAGNE_FLUTE("Champagne flute glass"),
    GOBLET("Goblet glass"),
    GRAPPA("Grappa glass"),
    HIGHBALL("Highball glass"),
    HURRICANE("Hurricane glass"),
    IRISH_COFFEE("Irish coffee glass"),
    MARGARITA("Margarita glass"),
    MARTINI("Martini glass"),
    OLD_FASHIONED("Old Fashioned glass"),
    ON_THE_ROCKS("On the rocks glass"),
    PILSNER("Pilsner glass"),
    PINT("Pint glass"),
    RED_WINE("Red wine glass"),
    SHERRY("Sherry glass"),
    SHOOTER("Shooter glass"),
    SHOT("Shot glass"),
    SLING("Sling glass"),
    TOM_COLLINS("Tom Collins glass"),
    VODKA("Vodka glass"),
    WHITE_WINE("White wine glass"),
    WINE_TASTING("Wine tasting glass"),
    WOBBLE("Wobble glass")

    String glassName
    GlassType(glassName) {
        this.glassName = glassName
    }
}