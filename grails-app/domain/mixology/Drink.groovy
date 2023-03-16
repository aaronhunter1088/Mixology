package mixology

import enums.*
import groovy.transform.ToString

import java.beans.Transient

@ToString
class Drink implements Serializable{

    String drinkName
    int drinkNumber
    String mixingInstructions
    GlassType suggestedGlass
    Alcohol drinkType
    String drinkSymbol

    static constraints = {
        drinkName()
        drinkSymbol(size:2..2)
        drinkNumber()
        drinkType()
        ingredients()
        mixingInstructions()
        suggestedGlass()
    }

    static belongsTo = Ingredient
    static hasMany = [ingredients:Ingredient]
    static transients = ['glassImage']

    @Override
    String toString() {
        drinkName + " ($drinkSymbol)" + " ($drinkNumber)"
    }

    @Transient
    String getGlassImage() {
        String glassImage = ""
        switch (this.getSuggestedGlass()) {
            case (GlassType.COCKTAIL) : {
                glassImage = "martiniGlass.png"
                break
            }
            case (GlassType.HIGHBALL) : {
                glassImage = "highballGlass.png"
                break
            }
            case (GlassType.HURRICANE) : {
                glassImage = "hurricaneGlass.png"
                break
            }
            case (GlassType.OLDFASHIONED) : {
                glassImage = "oldFashionedGlass.png"
                break
            }
            case (GlassType.SHOT) : {
                glassImage = "shotGlass.png"
                break
            }
            case (GlassType.TOMCOLLINS) : {
                glassImage = "tomCollinsGlass.png"
                break
            }
        }
        return glassImage
    }
}

/*
To view json representation of class
/drink.json: shows all
/drink/show/1.json shows drink with id:1
 */