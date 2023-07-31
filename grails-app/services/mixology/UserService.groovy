package mixology

import grails.gorm.services.Service

import javax.transaction.Transactional

@Transactional
@Service(User)
class UserService {

    User get(Long id) {
        User.findById(id)
    }

    List<User> list(Map args) {
        User.list(args)
    }

    Long count() {
        User.all.size()
    }

    boolean update(User user) {

    }

    User save(User user, boolean validate = false) {
        user.save(validate:validate, flush:true, failOnError:true)
        user
    }

    void delete(Long id) {
        Ingredient ingredient = Ingredient.findById(id)
        if (ingredient) ingredient.delete(flush:true)
    }

}