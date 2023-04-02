package mixology

import groovy.transform.ToString

import org.apache.commons.*
import org.apache.commons.io.*

@ToString
class User implements Serializable {

    String firstName
    String lastName
    String email
    String password
    String passwordConfirm
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
        email(nullable: false, blank: false, unique: true) // used as username
        password(nullable: false, blank: false, password: true)
        passwordConfirm(nullable: false)
        mobileNumber(size:10..10, nullable: false)
        photo(sqlType: 'LONGBLOB', nullable: true)
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

    def beforeInsert() { encodePassword() }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

}

