package mixology

import groovy.transform.ToString

import java.time.LocalDateTime

import java.security.SecureRandom

@ToString
class AuthToken {

    enum AuthTokenType {
        API,
        BASIC;
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

    static transients = ['toPrettyString', 'isExpired']

    AuthToken(){}
    AuthToken(User user) {
        this.username = user.username
        this.tokenValue = Long.toString( Math.abs(random.nextLong()), 16 )
    }

    @Override
    public String toString() {
        """AuthToken{
        tokenValue=$tokenValue
        , type=$type
        , id=$id
        }"""
    }

    public String toPrettyString() {
        "$tokenValue:$type:$id"
    }

    public boolean isExpired() {
        expiresDate < LocalDateTime.now()
    }

}