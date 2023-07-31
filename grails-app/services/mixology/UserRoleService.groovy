package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Transactional
@Service(UserRole)
class UserRoleService {

    UserRole save(User user, Role roleOfUser, boolean flush = false) {
        if (flush) {
            UserRole.create(user, roleOfUser, flush)
        }
        else UserRole.create(user, roleOfUser)
    }

}