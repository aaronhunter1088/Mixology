package mixology

import enums.Unit
import groovy.transform.ToString

@ToString
class Ingredient implements Comparable<Ingredient>, Serializable {

    String name
    Unit unit
    Double amount

    static constraints = {
        name(nullable: false)
        unit(nullable: false)
        amount(nullable: false)
    }

    static belongsTo = Drink
    static hasMany = [drinks:Drink]

    @Override
    String toString() {
        name + " : " + amount + " : " + unit.getValue()
    }

    @Override
    int compareTo(Ingredient i) {
        if (this.name == i.name && this.unit.value == i.unit.value && this.amount == i.amount) return 0
        else return 1
    }

    boolean idIsNull() {
        !this.id
    }
}
