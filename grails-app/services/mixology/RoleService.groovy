package mixology

import grails.gorm.services.Service
import javax.transaction.Transactional

@Service(Role)
class RoleService {

    @Transactional
    Role save(String authority, boolean validate = false) {
        if (validate) {
            new Role(authority:authority).save(validate:validate, flush:true)
        }
        else new Role(authority:authority).save(validate:false, flush:true)
    }

    Role findByAuthority(String authority) {
        Role.findByAuthority(authority)
    }

}
