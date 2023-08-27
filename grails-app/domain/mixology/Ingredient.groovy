package mixology

import enums.Unit
import groovy.transform.ToString

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
        canBeDeleted(nullable:true, default:true)
        custom(nullable:true, default:true)
    }

    // This creates a join table: drink_ingredients
    static belongsTo = [Drink, User]
    static hasMany = [drinks:Drink]

    @Override
    String toString() {
        name + " : " + amount + " : " + unit.getValue().toUpperCase()
    }

    String prettyName() {
        if (amount % 1 == 0) {
            (int)amount + ' ' + unit.getValue().toUpperCase() + ' of ' + name
        } else {
            amount + ' ' + unit.getValue().toUpperCase() + ' of ' + name
        }
    }

    String prettyNameWithoutName() {
        if (amount % 1 == 0) {
            (int)amount + ' ' + unit.getValue().toUpperCase() + ' of '
        } else {
            amount + ' ' + unit.getValue().toUpperCase() + ' of '
        }
    }

    @Override
    int compareTo(Ingredient i) {
        if (
             // if the name, unit, and amount are the same
             (this.name == i.name && this.unit.value == i.unit.value && this.amount == i.amount)
             // or if the id is the same
             || (this.id == i.id)
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
            int size = allUnits.size()
            Random random = new Random()
            def randomUnit = allUnits.get(random.nextInt(size))
            fillerIngredients.add(new Ingredient([
                    name: "Ingredient${i}",
                    unit: randomUnit,
                    amount: 1
            ]))
        }
        return fillerIngredients
    }

    static Set<Ingredient> copyAll(Set<Ingredient> ingredients) {
        Set<Ingredient> copySet = []
        for (ingredient in ingredients) {
            Ingredient newIngredient = new Ingredient([
                name:ingredient.name,
                unit:ingredient.unit,
                amount:ingredient.amount,
                canBeDeleted:true,
                custom:true
            ])
            copySet << newIngredient
        }
        return copySet
    }
}
