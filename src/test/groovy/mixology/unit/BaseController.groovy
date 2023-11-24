package mixology.unit

import enums.Unit
import grails.testing.gorm.DataTest
import mixology.DrinkService
import mixology.Ingredient
import mixology.IngredientService
import mixology.Role
import mixology.RoleService
import mixology.User
import mixology.UserRole
import mixology.UserRoleService
import mixology.UserService
import org.apache.logging.log4j.Logger
import spock.lang.Specification

abstract class BaseController extends Specification implements DataTest {

    // DataTest
    def drinkService = getDatastore().getService(DrinkService)
    def ingredientService = getDatastore().getService(IngredientService)
    def userService = getDatastore().getService(UserService)
    def roleService = getDatastore().getService(RoleService)
    def userRoleService = getDatastore().getService(UserRoleService)

    static Logger logger = null

    // User methods
    User createUser(def type) {
        User user = null
        switch (type) {
            case 'regular' : {
                user = new User([
                        username: "testRegularUser@gmail.com",
                        password: "testMe123\$",
                        passwordConfirm: "testMe123\$",
                        email: "testRegularUser@gmail.com",
                        firstName: "regular",
                        lastName: "user"
                ])
                user = userService.save(user, false)
                Role regularRole = new Role(authority: enums.Role.USER.name)
                UserRole.create(user, regularRole)
                user
                break
            }
            case 'admin' : {
                user = new User([
                        username: "testAdminUser@gmail.com",
                        email: "testAdminUser@gmail.com",
                        password: "testMe123\$",
                        passwordConfirm: "testMe123\$",
                        firstName: "admin",
                        lastName: "user"
                ])
                user = userService.save(user, false)
                Role adminRole = new Role(authority: enums.Role.ADMIN.name)
                UserRole.create(user, adminRole)
                user
                break
            }
            case 'Tobias' : {
                user = new User([
                        firstName: 'Tobias',
                        lastName: 'Husky',
                        username: 'thusky@gmail.com',
                        email: 'thusky@gmail.com',
                        password: 'p@ssword1',
                        passwordConfirm: 'p@ssword1'
                ])
                break
            }
            case 'unsaved' : {
                user = new User([
                        username: "testUnsavedUser@gmail.com",
                        email: "testUnsavedUser@gmail.com",
                        password: "testMe123\$",
                        passwordConfirm: "testMe123\$",
                        firstName: "unsavedRegular",
                        lastName: "user"
                ])
                Role regularRole = new Role(authority: enums.Role.USER.name)
                UserRole.create(user, regularRole)
                user
                break
            }
            default : throw new Exception("Unknown user option: $type")
        }
        user
    }

    // Ingredient methods
    Ingredient createIngredient(def option) {
        Ingredient ingredient = null
        switch (option) {
            case 'Vodka' : {
                ingredient = new Ingredient([
                        name: 'Vodka',
                        unit: Unit.OZ,
                        amount: 1.5
                ])
                break
            }
            case 'A' : {
                ingredient = new Ingredient([
                        name: 'IngredientA',
                        unit: Unit.OZ,
                        amount: 1.5
                ])
                break
            }
            case 'B' : {
                ingredient = new Ingredient([
                        name: 'IngredientB',
                        unit: Unit.SPLASHES,
                        amount: 3
                ])
                break;
            }
            case 'C' : {
                ingredient = new Ingredient([
                        name: 'IngredientC',
                        unit: Unit.TEASPOON,
                        amount: 1.5
                ])
                break;
            }
            case 'D' : {
                ingredient = new Ingredient([
                        name: 'IngredientD',
                        unit: Unit.TABLESPOON,
                        amount: 1.5
                ])
                break
            }
            case 'E' : {
                ingredient = new Ingredient([
                        name: 'IngredientE',
                        unit: Unit.DASH,
                        amount: 2
                ])
                break
            }
            case 'default' : {
                ingredient = new Ingredient([
                        name: 'DefaultIngredient',
                        unit: Unit.OZ,
                        amount: 1.5,
                        canBeDeleted: false,
                        custom: false
                ])
                break
            }
            default : throw new Exception("Unknown option: $option")
        }
        ingredient
    }

}
