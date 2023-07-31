package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Transactional
@Service(Drink)
class DrinkService {

    Drink get(Long id) {
        Drink.findById(id)
    }

    List<Drink> list(Map args) {
        Drink.list(args)
    }

    Long count() {
        Drink.all.size()
    }

    Drink save(Drink drink, boolean validate = false) {
        if (validate) {
            drink.save(validate:validate, flush:true, failOnError:true)
        }
        else drink.save(validate:false, flush:true, failOnError:false)
        drink
    }

    void delete(Long id) {
        Drink drink = Drink.findById(id)
        if (drink) drink.delete(flush:true)
    }
}
