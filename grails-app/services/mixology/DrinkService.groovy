package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Transactional
@Service(Drink)
class DrinkService {

    Drink get(Long id) {}

    List<Drink> list(Map args) {}

    Long count() {}

    void delete(Long id) {}

    Drink save(Drink drink) {}
}
