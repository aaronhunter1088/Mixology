package mixology

import java.time.LocalDateTime

import java.security.SecureRandom

class AuthToken {

    enum AuthTokenType {
        API;
        AuthTokenType(){}
    }

    String tokenValue
    String username
    LocalDateTime createdDate = LocalDateTime.now()
    LocalDateTime lastUsedDate
    LocalDateTime expiresDate = createdDate.plusHours(3)
    AuthTokenType type = AuthTokenType.API
    protected static SecureRandom random = new SecureRandom()

    static constraints = {
        tokenValue blank:false, unique:true
        username nullable:false
        createdDate nullable:false
        lastUsedDate nullable:true
        expiresDate nullable:true
        type blank:false
    }

    static mapping = {
        createdDate sqlType: 'datetime'
        lastUsedDate sqlType: 'datetime'
        expiresDate sqlType: 'datetime'
    }

    AuthToken(){}
    AuthToken(User user) {
        this.username = user.username
        this.tokenValue = Long.toString( Math.abs(random.nextLong()), 16 )
    }

}