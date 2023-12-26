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
    String passwordConfirm
    String mobileNumber
    String photo
    boolean darkMode
    String language

    def springSecurityService

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
        passwordConfirm(nullable:false, blank:false )
        mobileNumber(size:10..10, nullable: true)
        photo(sqlType: 'LONGBLOB', nullable: true, blank: true)
        enabled(default:1)
        accountLocked(default:0)
        passwordExpired(default:0, nullable:true)
        darkMode(default:0)
        language(default:'en',nullable:false,blank:true)
    }

    static mapping = {
        table 'users'
        password column: '`password`'
        autowire true
    }
    static transients = ['springSecurityService', 'authorities']

    @Override
    String toString() { "$firstName $lastName" }

    /**
     * Get roles of user
     * @return
     */
    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

    boolean isAdmin() {
        boolean result = false
        this.authorities?.each {role ->
            if (role.authority == enums.Role.ADMIN.name) result = true; return
        }
        result
    }
}

