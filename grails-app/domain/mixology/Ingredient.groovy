package mixology

import groovy.transform.ToString

@ToString
class Ingredient implements Comparable<Ingredient> {

    String name
    String unit
    double amount

    static constraints = {
        name()
        unit()
        amount()
    }

    static belongsTo = Drink
    static hasMany = [drinks:Drink]

    @Override
    String toString() {
        name + " : " + amount + " " + unit
    }

    @Override
    int compareTo(Ingredient i) {
        if (this.name == i.name && this.unit == i.unit && this.amount == i.amount) return 0
        else return 1
    }
}
