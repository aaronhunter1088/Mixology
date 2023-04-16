package mixology

import validators.*
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

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
        drinks:Drink,
        roles:UserRole
    ]

    static constraints = {
        firstName(nullable: false)
        lastName(nullable: false)
        username(blank: false, unique: true,)
        email(nullable: false, blank: false, unique: true, email: true, validator: EmailValidator.emailValidator) // used as username
        password(nullable: false, blank: false, password: true, size:6..15, validator: PasswordValidator.passwordValidator)
        mobileNumber(size:10..10, nullable: true)
        photo(sqlType: 'LONGBLOB', nullable: true, blank: true)
    }

    static mapping = {
        password column: '`password`'
        autowire true
        drinks joinTable:
            [
            name: 'user_drinks', // table name
            column: ['user_id', 'drink_id'] // column names
        ]
    }

    @Override
    String toString() { firstName + ' ' + lastName }

    static transients = ['springSecurityService']

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

}

