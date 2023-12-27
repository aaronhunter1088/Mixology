package mixology

import grails.gorm.services.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Service(AuthToken)
class AuthTokenService {

    private static Logger logger = LogManager.getLogger(AuthTokenService.class)

    /**
     * This method takes in an ID of type Long
     * and finds a Drink object. If one is found,
     * it returns it. Otherwise it will return null
     * @param id
     * @return
     */
    AuthToken get(Long id) {
        def authToken = (AuthToken.withCriteria {
            if (id) eq('id', id)
            else null
        })[0] as AuthToken
        authToken
    }

    List<AuthToken> findAll(User user = null) {
        //TODO: Implement
    }

    /**
     * Returns all the Drinks as a List
     * @param args
     * @return
     */
    List<AuthToken> list(Map args = null) {
        def returnList = AuthToken.list(args)
        returnList
    }

    /**
     * Returns the total count of all drinks
     * @return
     */
    Long count() {
        AuthToken.all.size()
    }

    /**
     * This save method is used for testing method purposes only!
     * It will save the Drink and then attach each ingredient
     * to the drink. It returns the Drink regardless of exceptions
     * @param drink
     * @param validate
     * @return
     */
    AuthToken save(AuthToken authToken, boolean validate = false) {
        try {
            AuthToken.withNewTransaction {
                authToken.save(validate:validate, flush:true, failOnError:validate)
            }
            logger.info("AuthToken saved. id: (${authToken.id})")
        } catch (Exception e) {
            logger.error("Could not save authToken:: $authToken", e)
        }
        authToken
    }

    /**
     * This delete method takes in an ID of type Long.
     * It uses it to find a Drink object. If a Drink is found,
     * then the drink is detached from each ingredient.
     * Likewise, each ingredient is detached from the Drink.
     * This method does not return anything.
     * @param id
     */
    void delete(def id, def user, def flush) {
        //TODO: Implement
    }
}


