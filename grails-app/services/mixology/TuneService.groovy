package mixology

import grails.gorm.services.Service

@Service(Tune)
interface TuneService {

    Tune get(Serializable id)

    List<Tune> list(Map args)

    Long count()

    void delete(Serializable id)

    Tune save(Tune tune)

}