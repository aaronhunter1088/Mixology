package mixology

import grails.gorm.services.Service

@Service(User)
interface UserService {

    User get(Serializable id)
    User findByUsername(String email) // findByUsername

    List<User> list(Map args)

    Long count()

    void delete(Serializable id)

    User save(User ingredient)

}