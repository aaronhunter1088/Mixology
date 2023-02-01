package mixology

import enums.GlassType
import enums.Alcohol
import groovy.transform.ToString

@ToString
class Drink {

    String drinkName
    int drinkNumber
    List<Ingredient> ingredients
    String mixingInstructions
    GlassType suggestedGlass
    Alcohol drinkType
    String drinkSymbol

    static constraints = {
        drinkName()
        drinkSymbol()
        drinkNumber()
        drinkType()
        ingredients()
        mixingInstructions()
        suggestedGlass()
    }

    static hasMany = [ingredients : Ingredient]

    @Override
    String toString() {
        drinkName + " ($drinkSymbol)" + " ($drinkNumber)"
    }
}

/*
To view json representation of class
/drink.json: shows all
/drink/show/1.json shows drink with id:1
 */