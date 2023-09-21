package mixology

import grails.gorm.services.Service
import io.micronaut.context.ApplicationContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.grails.orm.hibernate.support.HibernateRuntimeUtils
import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.security.core.parameters.P

import javax.persistence.Tuple
import javax.persistence.criteria.CompoundSelection
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaDelete
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.CriteriaUpdate
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Order
import javax.persistence.criteria.Root
import javax.persistence.criteria.Selection
import javax.transaction.Transactional
import java.beans.Transient

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service(User)
class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class)

    User get(Long id) {
        User user = User.findById(id as Long)
        logger.info("get($id) ==> ${user?'found':'not found'}")
        user
    }

    User getByUsername(String username) {
        User user = User.findWhere(username: username)
        logger.info("getByUsername($username) ==> ${user?'found':'not found'}")
        user
    }

    List<User> list(Map args) {
        User.list(args)
    }

    Long count() {
        User.all.size()
    }

    User save(User user, boolean validate = false) {
        if (!user) return null
        try {
            User.withNewTransaction {
                user.save(validate:validate, flush:true, failOnError:true)
                logger.info("user saved")
            }
        } catch (Exception e) {
            logger.error("Failed to save user:: $user", e)
        }
        user
    }

    @Transactional
    void delete(Long id) {
        User user = User.findById(id)
        if (!user) {
            logger.error("Could not delete user:: $user")
        } else {
            User.withTransaction {
                user.delete(flush:true)
            }
            logger.info("User '${user.name}' deleted!")
        }
    }

}