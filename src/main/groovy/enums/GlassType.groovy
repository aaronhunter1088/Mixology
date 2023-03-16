package enums

import lombok.ToString

@ToString
enum GlassType {

    SHOT("Shot glass"),
    OLDFASHIONED("Old Fashioned glass"),
    HIGHBALL("Highball glass"),
    MARTINI("Martini glass"),
    TOMCOLLINS("Tom Collins glass"),
    WINE("Wine glass"),
    HURRICANE("Hurricane glass")

    String glassName
    GlassType(glassName) {
        this.glassName = glassName
    }
}