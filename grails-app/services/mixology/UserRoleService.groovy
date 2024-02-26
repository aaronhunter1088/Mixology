package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.transaction.Transactional

@Service(UserRole)
class UserRoleService {

    private static Logger logger = LogManager.getLogger(UserService.class)

    UserRole save(User user, Role roleOfUser, boolean flush = false) {
        logger.info("flush:: $flush")
        UserRole.withNewTransaction {
            UserRole.create(user, roleOfUser, flush)
        }
    }

    UserRole getUserRoleIfExists(User user, Role role) {
        if (user && role) UserRole.get(user.id, role.id)
        else null
    }
}