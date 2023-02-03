package mixology

import grails.gorm.services.Service

@Service(Drink)
interface DrinkService {

    Drink get(Serializable id)

    List<Drink> list(Map args)

    Long count()

    void delete(Serializable id)

    Drink save(Drink drink)
}
