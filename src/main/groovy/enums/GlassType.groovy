package enums

import lombok.ToString

@ToString
enum GlassType {

    SHOT("Shot glass"),
    OLDFASHIONED("Old Fashioned glass"),
    HIGHBALL("Highball glass"),
    MARTINI("Martini glass"),
    TOMCOLLINS("Tom Collins glass"),
    RED_WINE("Red wine glass"),
    WHITE_WINE("White wine glass"),
    HURRICANE("Hurricane glass")

    String glassName
    GlassType(glassName) {
        this.glassName = glassName
    }
}