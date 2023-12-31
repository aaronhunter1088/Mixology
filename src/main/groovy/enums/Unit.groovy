package enums

import lombok.ToString

import java.security.SecureRandom

@ToString
enum Unit {

    // second value used for printing to screen only
    OZ("oz"), OZS("ozs"),
    FLUID_OZ("fluid_oz"), FL_OZS("fluid_ozs"),
    SPLASH("splash"), SPLASHES("slashes"),
    DASH("dash"), DASHES("dashes"),
    SODA_CAN("soda_can"), SODA_CANS("soda_cans"),
    TABLESPOON("tablespoon"), TABLESPOONS("tablespoons"),
    TEASPOON("teaspoon"), TEASPOONS("teaspoons"),
    WEDGE("wedge"), WEDGES("wedges"),
    SCOOP("scoop"), SCOOPS("scoops"),
    FRUIT("fruit"), FRUITS("fruits"),
    CUP("cup"), CUPS("cups")

    String value
    Unit(value) {
        this.value = value
    }

    static getRandomUnit() {
        SecureRandom r = new SecureRandom()
        int randNum = r.nextInt(values().length)
        println "random unit: ${values()[randNum]}"
        return values()[randNum]
    }

    static Unit getPluralUnit(Unit unit) {
        switch (unit) {
            case OZ: return OZS; break;
            case FLUID_OZ: return FL_OZS; break;
            case SPLASH: return SPLASHES; break;
            case DASH: return DASHES; break;
            case SODA_CAN: return SODA_CANS; break;
            case TABLESPOON: return TABLESPOONS; break
            case TEASPOON: return TEASPOONS; break;
            case WEDGE: return WEDGES; break;
            case SCOOP: return SCOOPS; break;
            case FRUIT: return FRUITS; break;
            case CUP: return CUPS; break;
            default: return unit;
        }
    }
}