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
    void delete(Long id, User user, boolean flush) {
        AuthToken token = null
        Map<Long,AuthToken> tokens = [:]
        if (id instanceof Long) token = get(id as Long)
        else if (id instanceof Collection<Long>) {
            id.each {
                tokens.put(it, get(it as Long))
            }
        }
        try {
            if (!token && tokens.size() == 0) {
                logger.error("Token not found with id:: $id")
                throw new Exception("Token not found with id:: $id")
            }
            // TODO determine how to cycle through tokens to do this if check
            if (token && user.username != token?.username) {
                logger.warn("A user, id:: ${user.id} is deleting a token they did not create.")
                logger.warn("Token belongs to user, id:: ${User.findByUsername(token.username).id}")
            }
            if (token) {
                AuthToken.withNewTransaction {
                    token.delete(flush:flush)
                }
                logger.info("Token: ${token.toPrettyString()} deleted!")
            } else if (tokens) {
                AuthToken.withNewTransaction {
                    tokens.keySet().each { key ->
                        def keyToken = tokens.get(key)
                        keyToken.delete(flush:flush)
                        logger.info("Token: ${keyToken.toPrettyString()} deleted!")
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Could not delete token:: ${token.toPrettyString()}, ${e.message}")
            e
        }
    }
}


