package mixology

import groovy.transform.ToString

import org.apache.commons.*
import org.apache.commons.io.*

@ToString
class User {

    String firstName
    String lastName
    String email
    String password
    String passwordConfirm
    String mobileNumber
    String photo

    static hasMany = [drinks:Drink]

    static constraints = {
        firstName(nullable: false)
        lastName(nullable: false)
        email(nullable: false)
        password(nullable: false)
        passwordConfirm(nullable: false)
        mobileNumber(size:10..10, nullable: false)
        photo(sqlType: 'LONGBLOB', nullable: true)
    }

    @Override
    String toString() { firstName + ' ' + lastName }

    static transients = ['']

//    String getImage() {
//        String logoPath = this.getPhoto()
//        logoPath = logoPath.replace('/','').replace('..','')
//        File logo = new File(logoPath)
////        String base64 = Base64.getEncoder().encode(IOUtils.toByteArray(logoPath))
////        byte[] bytes = IOUtils.toByteArray(base64)
////        return bytes
//        byte[] fileContent = FileUtils.readFileToByteArray(logo)
//        String encodedString = Base64.getEncoder().encodeToString(fileContent)
//        return encodedString
//    }

}

