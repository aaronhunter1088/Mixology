package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Transactional
@Service(UserRole)
class UserRoleService {

    UserRole save(UserRole ur) {}

}