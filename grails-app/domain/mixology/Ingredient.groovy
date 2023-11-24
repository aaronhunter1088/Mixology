package mixology

import enums.Unit

import static enums.Unit.getPluralUnit
import groovy.transform.ToString

import javax.persistence.Transient

@ToString
class Ingredient implements Comparable<Ingredient>, Serializable {

    String name
    Unit unit
    Double amount
    boolean canBeDeleted = true
    boolean custom = true

    static constraints = {
        name(nullable:false, blank:false, size:3..30)
        unit(nullable:false, blank:false, validator: { if (!(it in Unit.values())) return ['invalid.unit'] })
        amount(nullable:false, blank:false)
        canBeDeleted(nullable:false, default:true)
        custom(nullable:false, default:true)
        drinks(nullable:true)
        user(nullable:true,default:0)
    }
    /*
    Ingredient belongs to a User
    There will be a foreign key in the User table referencing the Ingredient primary key
     */
    static hasOne = [user:User]
    static hasMany = [
            drinks:Drink // tbl: ingredient_drinks
    ]
    static belongsTo = Drink
    static mapping = {
        table 'ingredients'
    }
    static transients = ['fillerIngredient','givenId','prettyName']

    @Override
    String toString() {
        name + " : " + amount + " : " + unit.getValue().toUpperCase()
    }

    String prettyName(boolean displayWithName = true) {
        def unitToUse = this.amount == 1 ? this.unit.getValue().toLowerCase() : getPluralUnit(unit).getValue().toLowerCase()
        if (amount % 1 == 0) {
            if (displayWithName) (int)amount + ' ' + unitToUse + ' of ' + name
            else (int)amount + ' ' + unitToUse + ' of '
        } else {
            if (displayWithName) amount + ' ' + unitToUse + ' of ' + name
            else amount + ' ' + unitToUse + ' of '
        }
    }

    /**
     * Compare. If 0, same, 1, different
     * @param i the object to be compared.
     * @return
     */
    @Override
    int compareTo(Ingredient i) {
        if (
             // if the name, unit, and amount are the same
             (this.name == i.name && this.unit.value == i.unit.value && this.amount == i.amount)
             // or, if both present, the ids are the same
             || (this.id && i.id && this.id == i.id)
            )
            return 0
        else return 1
    }

    boolean idIsNull() {
        !this.id
    }

    static List<Ingredient> createFillerIngredients(count) {
        List<Ingredient> fillerIngredients = new ArrayList<>()
        for (i in 1..count) {
            List<Unit> allUnits = Collections.unmodifiableList(Arrays.asList(Unit.values()))
            Random random = new Random()
            def randomUnit = allUnits.get(random.nextInt(allUnits.size()))
            Ingredient filler = new Ingredient([
                    name: "Ingredient${i}",
                    unit: randomUnit,
                    amount: 1
                    ,givenId: "${i}" as Long
            ])
            fillerIngredients.add(filler)
        }
        return fillerIngredients.sort{a,b -> a.givenId <=> b.givenId}
    }

    //static Set<Ingredient> copyAll(Set<Ingredient> ingredients) {
    static def copyAll ( def ingredients ) {
        //Set<Ingredient> copySet = []
        def copySet = []
        //for (ingredient in ingredients) {
        ingredients.each { ingredient ->
            Ingredient newIngredient = new Ingredient([
                name:ingredient.name,
                unit:ingredient.unit,
                amount:ingredient.amount
                //,canBeDeleted:true
                //,custom:true
            ])
            copySet << newIngredient
        }
        //return copySet
        copySet
    }

    transient Long givenId
    Long getGivenId() { return givenId }
    void setGivenId(Long givenId) { this.givenId = givenId }
}
