package mixology

import enums.*
import exceptions.UnsupportedGlassException
import groovy.transform.ToString

@ToString
class Drink implements Serializable{

    String name
    int number
    String mixingInstructions
    GlassType suggestedGlass
    Alcohol alcohol
    String symbol
    boolean canBeDeleted = true
    boolean custom = true

    static constraints = {
        name(size:3..30, blank:false, nullable:false)
        symbol(size:2..2, blank:false, nullable:false)
        number(min:1, max:999, nullable:false)
        alcohol(blank:false, nullable:false, validator: { if (!(it in Alcohol.values())) return ['invalid.alcoholType'] })
        mixingInstructions(blank:false, nullable:false)
        suggestedGlass(blank:false, nullable:false, validator: { if (!(it in GlassType.values())) return ['invalid.suggestedGlass'] })
        canBeDeleted(default:true)
        custom(default:true)
        ingredients(minSize:0, nullable:true)
        user(nullable:true)
    }
    /*
    Drink belongs to a User
    There will be a foreign key in the User table referencing the Drink primary key
     */
    static hasOne = [user:User]
    static hasMany = [
            ingredients:Ingredient // tbl: drink_ingredients
    ]
    static mapping = {
        table 'drinks'
    }
    static transients = ['glassImage','fillerDrink']

    @Override
    String toString() {
        name + " ($symbol)" + " ($number)"
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

    String getFullPathToImage() {
        if (this.glassImage) {
            def imagePath = this.glassImage //"grails-app/assets/images/${this.glassImage}"
            if (!imagePath) throw new Exception("Could not find image")
            else imagePath
        } else {
            throw new Exception("No image found")
        }
    }

    static Drink createFillerDrink(alcoholType) {
        return new Drink([
                name: 'FillerDrink',
                number: 0,
                mixingInstructions: 'Filler instructions',
                suggestedGlass: GlassType.getRandomGlass(),
                alcohol: alcoholType,
                symbol: 'Fd',
                ingredients: Ingredient.createFillerIngredients(3),
                canBeDeleted: true,
                custom: true
                ,fillerDrink: true /* transient */
        ])
    }

    static Drink copyDrink(Drink drink) {
        Drink copiedDrink = new Drink([
                name : drink.name,
                symbol : drink.symbol,
                number : drink.number,
                alcohol : drink.alcohol,
                mixingInstructions : drink.mixingInstructions,
                suggestedGlass : drink.suggestedGlass,
                canBeDeleted : drink.canBeDeleted,
                custom : true
        ])
        copiedDrink
    }

    transient boolean fillerDrink
    boolean getFillerDrink() { return fillerDrink }
    void setFillerDrink(boolean fillerDrink) { this.fillerDrink = fillerDrink }

}

/*
To view json representation of class
/drink.json: shows all
/drink/show/1.json shows drink with id:1
 */