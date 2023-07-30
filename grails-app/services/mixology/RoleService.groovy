package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Transactional
@Service(Role)
class RoleService {

    Role save(String authority) {}
    Role findByAuthority(String authority) {}

}
