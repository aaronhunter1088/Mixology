package mixology

import enums.GlassType
import enums.Alcohol
import groovy.transform.ToString

@ToString
class Drink {

    String drinkName
    int drinkNumber
    String mixingInstructions
    GlassType suggestedGlass
    Alcohol drinkType
    String drinkSymbol
    List<Ingredient> ingredients

    static constraints = {
        drinkName()
        drinkSymbol()
        drinkNumber()
        drinkType()
        ingredients()
        mixingInstructions()
        suggestedGlass()
    }

    static belongsTo = Ingredient
    static hasMany = [ingredients:Ingredient]

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