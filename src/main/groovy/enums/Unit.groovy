package enums

import lombok.ToString

@ToString
enum Unit {

    OZ("oz"),
    SPLASH("splash"),
    DASH("dash"),
    SODA_CAN("soda can, 16 fl.oz"),
    TABLESPOON("tablespoon"),
    TEASPOON("teaspoon"),
    LEMON_WEDGE("lemon wedge"),
    LEMON("lemon"),
    LIME_WEDGE("lime wedge"),
    LIME("lime"),
    SCOOP("scoop"),
    FRUIT("fruit"),
    CUP("cup")

    String value
    Unit(value) {
        this.value = value
    }

}