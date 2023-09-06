package mixology

import validators.*
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    String firstName
    String lastName
    String username
    String email
    String password
    transient passwordConfirm
    String mobileNumber
    String photo

    transient springSecurityService

    static hasMany = [
        drinks:Drink, // tbl: user_drinks
        ingredients:Ingredient, // tbl: user_ingredients
        roles:UserRole // tbl: user_role
    ]

    static constraints = {
        firstName(nullable:false, blank:false, size:3..30)
        lastName(nullable:false, blank:false, size:3..40)
        username(nullable:false, blank:false, unique:true)
        email(nullable:false, blank:false, unique:true, email:true, validator: EmailValidator.emailValidator) // used as username
        password(nullable:false, blank:false, password:true, size:6..15, validator: PasswordValidator.passwordValidator)
        mobileNumber(size:10..10, nullable: true)
        photo(sqlType: 'LONGBLOB', nullable: true, blank: true)
    }

    static mapping = {
        password column: '`password`'
        autowire true
    }

    @Override
    String toString() { firstName + ' ' + lastName }

    static transients = ['springSecurityService']

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

}

