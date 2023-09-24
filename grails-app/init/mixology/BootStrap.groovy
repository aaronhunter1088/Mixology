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
        //updateAllTestUserIngredients()
        def user = User.findById(2)
//        def users = User.all
//        users.each {User user ->
//            println "Updating password"
//            if (user.id == 1) {user.password = 'p@ssword1!'; user.passwordConfirm = 'p@ssword1!';}
            if (user.id == 2) {user.password = 'password123$'; user.passwordConfirm = 'password123$';}
//            if (user.id == 4) {user.password = 'gingerNix123$'; user.passwordConfirm = 'gingerNix123$';}
//        }
//        users.each {User user ->
            User.withNewTransaction {
                user.save(flush:true, validate:false)
            }
//        }
    }

    def destroy = {
    }

    @Transactional
    void updateAllTestUserIngredients() {
        User user = User.findById(2)
        user.ingredients = []
        // Get all drinks that belong to the user
        def user2Drinks = user.drinks
        def user2Ingredients = []
        user.drinks.eachWithIndex {drink, idx ->
            println "${idx+1}: $drink"
            println "# of ingredients for drink${idx+1}: ${drink.ingredients.size()}"
            drink.ingredients.each {
                if (it.custom) {
                    println "Adding $it to user[${user.firstName} ${user.lastName}]"
                    user.addToIngredients(it as Ingredient)
                } else {
                    it.custom = true
                    it.canBeDeleted = true
                    it.save(flush:true, validate:false)
                    println "Adding $it to user[${user.firstName} ${user.lastName}]"
                    user.addToIngredients(it as Ingredient)
                }
            }
            user.save(deepValidate:false, failOnError:false, validate:false, flush:true)
            println "User saved!"
        }
        //user2Ingredients =
//        user2Ingredients.eachWithIndex { ingredient, idx ->
//            println "${idx+1}: $ingredient"
//        }
        println "Ending for now..."
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
}
