package mixology

import enums.*
import exceptions.UnsupportedGlassException
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
            case (GlassType.ABSINTHE) : {
                glassImage = "absintheGlass.png"
                break
            }
            case (GlassType.BALLOON_WINE) : {
                glassImage = "balloonWineGlass.png"
                break
            }
            case (GlassType.BEER_MUG) : {
                glassImage = "beerMug.png"
                break
            }
            case (GlassType.BRANDY) : {
                glassImage = "brandyGlass.png"
                break
            }
            case (GlassType.CHAMPAGNE_FLUTE) : {
                glassImage = "champagneFluteGlass.png"
                break
            }
            case (GlassType.GOBLET) : {
                glassImage = "gobletGlass.png"
                break
            }
            case (GlassType.GRAPPA) : {
                glassImage = "grappaGlass.png"
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
            case (GlassType.IRISH_COFFEE) : {
                glassImage = "irishCoffeeGlass.png"
                break
            }
            case (GlassType.MARGARITA) : {
                glassImage = "margaritaGlass.png"
                break
            }
            case (GlassType.MARTINI) : {
                glassImage = "martiniGlass.png"
                break
            }
            case (GlassType.OLD_FASHIONED) : {
                glassImage = "oldFashionedGlass.png"
                break
            }
            case (GlassType.ON_THE_ROCKS) : {
                glassImage = "onTheRocksGlass.png"
                break
            }
            case (GlassType.PILSNER) : {
                glassImage = "pilsnerGlass.png"
                break
            }
            case (GlassType.PINT) : {
                glassImage = "pintGlass.png"
                break
            }
            case (GlassType.RED_WINE) : {
                glassImage = "redWineGlass.png"
                break
            }
            case (GlassType.SHERRY) : {
                glassImage = "sherryGlass.png"
                break
            }
            case (GlassType.SHOOTER) : {
                glassImage = "shooterGlass.png"
                break
            }
            case (GlassType.SHOT) : {
                glassImage = "shotGlass.png"
                break
            }
            case (GlassType.SLING) : {
                glassImage = "slingGlass.png"
                break
            }
            case (GlassType.TOM_COLLINS) : {
                glassImage = "tomCollinsGlass.png"
                break
            }
            case (GlassType.VODKA) : {
                glassImage = "vodkaGlass.png"
                break
            }
            case (GlassType.WHITE_WINE) : {
                glassImage = "whiteWineGlass.png"
                break
            }
            case (GlassType.WINE_TASTING) : {
                glassImage = "wineTastingGlass.png"
                break
            }
            case (GlassType.WOBBLE) : {
                glassImage = "wobbleGlass.png"
                break
            }
            default : {
                throw new UnsupportedGlassException("The glass you have chosen is not available")
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