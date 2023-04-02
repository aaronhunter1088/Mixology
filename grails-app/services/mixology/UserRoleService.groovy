package mixology

import grails.gorm.services.Service

@Service(UserRole)
interface UserRoleService {

    UserRole save(UserRole ur)

}