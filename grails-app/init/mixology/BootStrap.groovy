package mixology

import grails.gorm.transactions.Transactional
import enums.Role
import mixology.Role

class BootStrap {

    //def init = { servletContext ->}

    def init = { servletContext ->
        //def role = new Role(authority: enums.Role.USER.name).save()
        //def adminRole = new Role(authority: enums.Role.ADMIN.name).save()
        //addTestUser()
        //addMBallUser()
    }

//    @Transactional
//    void addMBallUser() {
//
//        //roleService.save(role)
//        //def mballUser = User.findById(1)
//        //def userRole = UserRole.create mballUser, role
//        //userRoleService.save(userRole)
////        UserRole.withSession {
////            it.flush()
////            it.clear()
////        }
//    }

//    @Transactional
//    void addTestUser() {
//        //def adminRole = new Role(authority: enums.Role.ADMIN.name).save()//new Role(authority: 'ROLE_ADMIN').save()
//        //roleService.save(adminRole)
//        //def testUser = new User(firstName: 'test', lastName: 'user', email: 'testuser@gmail.com', username: 'testuser', password: 'password', passwordConfirm: 'password')//.save()
//        //userService.save(testUser)
//        //def testUser = User.findById(2);
//        //UserRole.create testUser, adminRole
//
//        //UserRole.withTransaction { urole ->
//            //UserRole.withSession {
//                //it.flush()
//                //it.clear()
//            //}
//        //}
//
//        //assert User.count() == 1
//        //assert Role.count() == 1
//        //assert UserRole.count() == 1
//    }

    def destroy = {
    }
}
