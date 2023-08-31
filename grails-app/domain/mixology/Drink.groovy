package mixology

import enums.*
import exceptions.UnsupportedGlassException
import groovy.transform.ToString

@ToString
class Drink implements Serializable{

    String drinkName
    int drinkNumber
    String mixingInstructions
    GlassType suggestedGlass
    Alcohol alcoholType
    String drinkSymbol
    boolean canBeDeleted = true
    boolean custom = true

    static constraints = {
        drinkName(size:3..30, blank:false, nullable:false)
        drinkSymbol(size:2..2, blank:false, nullable:false)
        drinkNumber(min:1, nullable:false)
        alcoholType(blank:false, nullable:false, validator: { if (!(it in Alcohol.values())) return ['invalid.alcoholType'] })
        ingredients(minSize:0, nullable:true)
        mixingInstructions(blank:false, nullable:false)
        suggestedGlass(blank:false, nullable:false, validator: { if (!(it in GlassType.values())) return ['invalid.glassType'] })
        canBeDeleted(default:true)
        custom(default:true)
    }

    static mapping = {
    }

    /*
    Drink belongs to a User
    There will be a foreign key in the User table referencing the Drink primary key
     */
    static hasMany = [
            ingredients:Ingredient // tbl: drink_ingredients
    ]
    static transients = ['glassImage']

    @Override
    String toString() {
        drinkName + " ($drinkSymbol)" + " ($drinkNumber)"
    }

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

    static Drink createFillerDrink(alcoholType) {
        return new Drink([
                drinkName: 'FillerDrink',
                drinkNumber: 0,
                mixingInstructions: 'Filler instructions',
                suggestedGlass: GlassType.BRANDY,
                alcoholType: alcoholType,
                drinkSymbol: 'Fd',
                ingredients: Ingredient.createFillerIngredients(3),
                canBeDeleted: true,
                custom: true
        ])
    }

}

/*
To view json representation of class
/drink.json: shows all
/drink/show/1.json shows drink with id:1
 */