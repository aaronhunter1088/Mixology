package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.transaction.Transactional

@Service(Role)
class RoleService {

    private static Logger logger = LogManager.getLogger(RoleService.class)

    Role save(String authority, boolean validate = false) {
        logger.info("authority:: $authority\tvalidate:: $validate")
        Role.withTransaction {
            new Role(authority:authority).save(validate:validate, flush:validate)
        }
    }

    Role findByAuthority(String authority) {
        Role.findByAuthority(authority)
    }

}
