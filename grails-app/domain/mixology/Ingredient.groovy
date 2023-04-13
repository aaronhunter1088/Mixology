package mixology

import enums.Unit
import groovy.transform.ToString

@ToString
class Ingredient implements Comparable<Ingredient>, Serializable {

    String name
    Unit unit
    Double amount
    boolean canBeDeleted
    boolean custom

    static constraints = {
        name(nullable: false)
        unit(nullable: false)
        amount(nullable: false)
        canBeDeleted(nullable:true, default:true)
        custom(nullable:true, default:true)
    }

    static belongsTo = Drink
    static hasMany = [drinks:Drink]

    @Override
    String toString() {
        name + " : " + amount + " : " + unit.getValue().toUpperCase()
    }

    @Override
    int compareTo(Ingredient i) {
        if (this.name == i.name && this.unit.value == i.unit.value && this.amount == i.amount) return 0
        else return 1
    }

    boolean idIsNull() {
        !this.id
    }

    static List<Ingredient> createFillerIngredients(count) {
        List<Ingredient> fillerIngredients = new ArrayList<>();
        for (i in 1..count) {
            fillerIngredients.add(new Ingredient([
                    name: "Ingredient${i}",
                    unit: Unit.WEDGE,
                    amount: 1
            ]))
        }
        return fillerIngredients
    }

    static Set<Ingredient> copyAll(Set<Ingredient> ingredients) {
        Set<Ingredient> copiedList = []
        for (ingredient in ingredients) {
            Ingredient newIngredient = new Ingredient([
                name:ingredient.name,
                unit:ingredient.unit,
                amount:ingredient.amount
            ])
            copiedList << newIngredient
        }
        return copiedList
    }
}
