package enums

import lombok.ToString

@ToString
enum Unit {

    OZ("oz"),
    FLUID_OZ("fluid oz"),
    SPLASH("splash"),
    DASH("dash"),
    SODA_CAN("soda can"),
    TABLESPOON("tablespoon"),
    TEASPOON("teaspoon"),
    //LEMON_WEDGE("lemon wedge"),
    //LEMON("lemon"),
    //LIME_WEDGE("lime wedge"),
    //LIME("lime"),
    WEDGE("wedge"),
    SCOOP("scoop"),
    FRUIT("fruit"),
    CUP("cup")

    String value
    Unit(value) {
        this.value = value
    }

}