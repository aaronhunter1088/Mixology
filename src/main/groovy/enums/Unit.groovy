package enums

import lombok.ToString

import java.security.SecureRandom

@ToString
enum Unit {

    // second value used for printing to screen only
    // S=Singular, P=Plural
    OZ("oz"), OZS("ozs", 'P'),
    FLUID_OZ("fluid_oz"), FL_OZS("fluid_ozs", 'P'),
    SPLASH("splash"), SPLASHES("slashes", 'P'),
    DASH("dash"), DASHES("dashes", 'P'),
    SODA_CAN("soda_can"), SODA_CANS("soda_cans", 'P'),
    TABLESPOON("tablespoon"), TABLESPOONS("tablespoons", 'P'),
    TEASPOON("teaspoon"), TEASPOONS("teaspoons", 'P'),
    WEDGE("wedge"), WEDGES("wedges", 'P'),
    SCOOP("scoop"), SCOOPS("scoops", 'P'),
    FRUIT("fruit"), FRUITS("fruits", 'P'),
    CUP("cup"), CUPS("cups", 'P')

    String value
    String type
    Unit(String value, String type = 'S') {
        this.value = value
        this.type = type
    }

    static getRandomUnit() {
        SecureRandom r = new SecureRandom()
        int randNum = r.nextInt(values().length)
        //println "random unit: ${values()[randNum]}"
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