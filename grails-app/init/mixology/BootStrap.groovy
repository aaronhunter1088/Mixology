package mixology

import enums.Alcohol
import enums.GlassType
import enums.Unit
import grails.util.Environment

class BootStrap {

    def drinkService
    def ingredientService
    def userRoleService
    def roleService
    def userService

    def init = { servletContext ->
        // This will start the app using the H2 embedded database with these default drinks, ingredients, and users*
        if (Environment.DEVELOPMENT == Environment.current) {
            // Test admin user and regular user
            def adminRole = roleService.save(enums.Role.ADMIN.name, true)
            User adminUser = new User([version:1, firstName:'Admin', passwordExpired: false, photo:null, accountExpired:false,
                                       language:'en', darkMode:false, mobileNumber:'1234560789', username:'admin@email.com',
                                       accountLocked:false, password:'testAdmin$', lastName:'User', passwordConfirm:'testAdmin$',
                                       enabled:true, email:'admin@email.com'])
            adminUser = userService.save(adminUser, true)
            userRoleService.save(adminUser, adminRole, true)

            def userRole = roleService.save(enums.Role.USER.name, true)
            User regularUser = new User([version:1, firstName:'Regular', passwordExpired: false, photo:null, accountExpired:false,
                                         language:'en', darkMode:false, mobileNumber:'9870654321', username:'regular@email.com',
                                         accountLocked:false, password:'testRegular$', lastName:'User', passwordConfirm:'testRegular$',
                                         enabled:true, email:'regular@email.com'])
            regularUser = userService.save(regularUser, true)
            userRoleService.save(regularUser, userRole, true)

            // Ingredients ingredientService.save(new Ingredient([name:'', unit:Unit.option, amount:#, custom:false, canBeDeleted:false]))
            // Drinks new Drink([name:'', number:#, mixingInstructions:'', suggestedGlass:GlassType.option, alcohol:Alcohol.option, symbol:'', custom:false, canBeDeleted:false])

            def drink1Ingredient1 = ingredientService.save(new Ingredient([name:'Tequila', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]), true)
            def drink1Ingredient2 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.OZ, amount: 1, custom:false, canBeDeleted:false]), true)
            def drink1Ingredient3 = ingredientService.save(new Ingredient([name:'Pineapple Juice', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]), true)
            def drink1Ingredient4 = ingredientService.save(new Ingredient([name:'Lemon-Lime Soda', unit:Unit.SPLASH, amount:1, custom:false, canBeDeleted:false]), true)

            def drink1 = new Drink([name:'Alamo Splash', number:'1', mixingInstructions:'Mix with crushed ice and strain into collins glass.', suggestedGlass:GlassType.TOM_COLLINS, alcohol: Alcohol.TEQUILA, symbol:'As', custom:false, canBeDeleted:false])
            drink1.ingredients = [drink1Ingredient1, drink1Ingredient2, drink1Ingredient3, drink1Ingredient4]
            drinkService.save(drink1, true)

            def drink2Ingredient1 = ingredientService.save(new Ingredient([name:'Tequila', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]), true)
            def drink2Ingredient2 = ingredientService.save(new Ingredient([name:'Amaretto', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]), true)
            def drink2Ingredient3 = ingredientService.save(new Ingredient([name:'Pineapple Juice', unit:Unit.FLUID_OZ, amount:16, custom:false, canBeDeleted:false]), true)
            def drink2Ingredient4 = ingredientService.save(new Ingredient([name:'Grenadine', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]), true)

            def drink2 = new Drink([name:'Red Hooter', number:'2', mixingInstructions:'Pour Tequila and Amaretto over ice in collins glass. Fill with Pineapple Juice, topped with Grenadine, and garnished with cherry.', suggestedGlass:GlassType.TOM_COLLINS, alcohol: Alcohol.TEQUILA, symbol:'Rh', custom:false, canBeDeleted:false])
            drink2.ingredients = [drink2Ingredient1, drink2Ingredient2, drink2Ingredient3, drink2Ingredient4]
            drinkService.save(drink2, true)

            def drink3Ingredient1 = ingredientService.save(new Ingredient([name:'Coffee Liqueur', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]), true)
            def drink3Ingredient2 = ingredientService.save(new Ingredient([name:'Vodka', unit:Unit.OZ, amount:0.25, custom:false, canBeDeleted:false]), true)
            def drink3Ingredient3 = ingredientService.save(new Ingredient([name:'Vanilla Ice Cream', unit:Unit.SCOOP, amount:1, custom:false, canBeDeleted:false]), true)
            def drink3Ingredient4 = ingredientService.save(new Ingredient([name:'Strawberries', unit:Unit.FRUIT, amount:4, custom:false, canBeDeleted:false]), true)

            def drink3 = new Drink([name:'Blushin\' Russian', number:3, mixingInstructions:'Combine ingredients in blender and blend smoothly. Pour into hurricane glass then garnish with a chocolate-covered strawberry.', suggestedGlass:GlassType.HURRICANE, alcohol:Alcohol.FROZEN, symbol:'Br', custom:false, canBeDeleted:false])
            drink3.ingredients = [drink3Ingredient1, drink3Ingredient2, drink3Ingredient3, drink3Ingredient4]
            drinkService.save(drink3, true)

            def drink4Ingredient2 = ingredientService.save(new Ingredient([name:'Blue Caracao', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]), true)
            def drink4Ingredient3 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]), true)

            def drink4 = new Drink([name:'Blue Margarita', number:4, mixingInstructions:'Encircle rim of margarita glass with lime juice, then dip rim in coarse salt. Pour ingredients over crushed ice, shake, then strain into glass.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.TEQUILA, symbol:'Bm', custom:false, canBeDeleted:false])
            drink4.ingredients = [drink1Ingredient1, drink4Ingredient2, drink4Ingredient3]
            drinkService.save(drink4, true)

            def drink5Ingredient2 = ingredientService.save(new Ingredient([name:'Tequila', unit:Unit.OZ, amount:1.25, custom:false, canBeDeleted:false]))
            def drink5Ingredient3 = ingredientService.save(new Ingredient([name:'Red Wine', unit:Unit.OZ, amount:1.25, custom:false, canBeDeleted:false]))
            def drink5Ingredient4 = ingredientService.save(new Ingredient([name:'Triple Sec', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink5Ingredient5 = ingredientService.save(new Ingredient([name:'Sour Mix', unit:Unit.OZ, amount:6.5, custom:false, canBeDeleted:false]))
            def drink5Ingredient6 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.DASH, amount:1, custom:false, canBeDeleted:false]))

            def drink5 = new Drink([name:'Cactus Berry', number:5, mixingInstructions:'Shake with crushed ice, pour into salt rimmed margarita glass.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.TEQUILA, symbol:'Cb', custom:false, canBeDeleted:false])
            drink5.ingredients = [drink1Ingredient4, drink5Ingredient2, drink5Ingredient3, drink5Ingredient4, drink5Ingredient5, drink5Ingredient6]
            drinkService.save(drink5, true)

            def drink6Ingredient2 = ingredientService.save(new Ingredient([name:'Amaretto', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink6Ingredient3 = ingredientService.save(new Ingredient([name:'Vanilla Ice Cream', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))

            def drink6 = new Drink([name:'Blue Cloud Cocktail', number:6, mixingInstructions:'Combine ingredients in blender and blend smoothly. Pour into hurricane glass, then top with whipped cream and strawberries.', suggestedGlass:GlassType.HURRICANE, alcohol:Alcohol.FROZEN, symbol:'Bl', custom:false, canBeDeleted:false])
            drink6.ingredients = [drink4Ingredient2, drink6Ingredient2, drink6Ingredient3]
            drinkService.save(drink6, true)

            def drink7Ingredient1 = ingredientService.save(new Ingredient([name:'Black Raspberry Liqueur', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink7Ingredient2 = ingredientService.save(new Ingredient([name:'Melon Liqueur', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink7Ingredient3 = ingredientService.save(new Ingredient([name:'Vanilla Ice Cream', unit:Unit.OZ, amount:4, custom:false, canBeDeleted:false]))
            def drink7Ingredient4 = ingredientService.save(new Ingredient([name:'Blue Caracao', unit:Unit.FLUID_OZ, amount:16, custom:false, canBeDeleted:false]))

            def drink7 = new Drink([name:'Blue Velvet', number:7, mixingInstructions:'Mix ingredients in blender with crushed ice and blend smoothly. Pour into hurricane glass and top with whipped cream and drizzle with caracao. Top with cherry.', suggestedGlass:GlassType.HURRICANE, alcohol:Alcohol.FROZEN, symbol:'Bv', custom:false, canBeDeleted:false])
            drink7.ingredients = [drink7Ingredient1, drink7Ingredient2, drink7Ingredient3, drink7Ingredient4]
            drinkService.save(drink7, true)

            def drink8Ingredient2 = ingredientService.save(new Ingredient([name:'Peach Schnapps', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink8Ingredient3 = ingredientService.save(new Ingredient([name:'Blue Caracao', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink8Ingredient4 = ingredientService.save(new Ingredient([name:'Sour Mix', unit:Unit.OZ, amount:4, custom:false, canBeDeleted:false]))

            def drink8 = new Drink([name:'Catalina Margarita', number:8, mixingInstructions:'Shake with crushed ice, pour into salt rimmed margarita glass.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.TEQUILA, symbol:'Cm', custom:false, canBeDeleted:false])
            drink8.ingredients = [drink1Ingredient1, drink8Ingredient2, drink8Ingredient3, drink8Ingredient4]
            drinkService.save(drink8, true)

            def drink9Ingredient1 = ingredientService.save(new Ingredient([name:'Tequila', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))
            def drink9Ingredient2 = ingredientService.save(new Ingredient([name:'Vodka', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))
            def drink9Ingredient3 = ingredientService.save(new Ingredient([name:'Triple Sec', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink9Ingredient4 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.OZ, amount:3, custom:false, canBeDeleted:false]))
            def drink9Ingredient5 = ingredientService.save(new Ingredient([name:'Grenadine', unit:Unit.DASH, amount:2.5, custom:false, canBeDeleted:false]))

            def drink9 = new Drink([name:'Hairy Sunrise', number:9, mixingInstructions:'Mix all ingredients (no grenadine) in blender. Pour into collins glass. Top off with grenadine and slice of lime.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.TEQUILA, symbol:'Hs', custom:false, canBeDeleted:false])
            drink9.ingredients = [drink9Ingredient1, drink9Ingredient2, drink9Ingredient3, drink9Ingredient4, drink9Ingredient5]
            drinkService.save(drink9, true)

            def drink10Ingredient1 = ingredientService.save(new Ingredient([name:'100 Proof Vodka', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink10Ingredient2 = ingredientService.save(new Ingredient([name:'Chilled Beer', unit:Unit.FLUID_OZ, amount:16, custom:false, canBeDeleted:false]))
            def drink10Ingredient3 = ingredientService.save(new Ingredient([name:'Chilled Ale', unit:Unit.FLUID_OZ, amount:16, custom:false, canBeDeleted:false]))
            def drink10Ingredient4 = ingredientService.save(new Ingredient([name:'Tabasco Sauce', unit:Unit.DASH, amount:2, custom:false, canBeDeleted:false]))

            def drink10 = new Drink([name:'Beer Buster', number:10, mixingInstructions:'Pour vodka into highball glass, then fill with beer or ale. Add dashes of Tabasco sauce then stir lightly.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.VODKA, symbol:'Bb', custom:false, canBeDeleted:false])
            drink10.ingredients = [drink10Ingredient1, drink10Ingredient2, drink10Ingredient3, drink10Ingredient4]
            drinkService.save(drink10, true)

            def drink11Ingredient1 = ingredientService.save(new Ingredient([name:'Vodka', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink11Ingredient2 = ingredientService.save(new Ingredient([name:'Coffee Liqueur', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))

            def drink11 = new Drink([name:'Black Russian', number:11, mixingInstructions:'Pour over crushed ice into old-fashioned glass.', suggestedGlass:GlassType.OLD_FASHIONED, alcohol:Alcohol.VODKA, symbol:'Br', custom:false, canBeDeleted:false])
            drink11.ingredients = [drink11Ingredient1, drink11Ingredient2]
            drinkService.save(drink11, true)

            def drink12Ingredient2 = ingredientService.save(new Ingredient([name:'Lemonade', unit:Unit.OZ, amount:5, custom:false, canBeDeleted:false]))
            def drink12Ingredient3 = ingredientService.save(new Ingredient([name:'Lime', unit:Unit.WEDGE, amount:1, custom:false, canBeDeleted:false]))

            def drink12 = new Drink([name:'Bull Frog', number:12, mixingInstructions:'Pour over crushed ice in collins glass, then garnish with a slice of lime.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.VODKA, symbol:'Bf', custom:false, canBeDeleted:false])
            drink12.ingredients = [drink11Ingredient1, drink12Ingredient2, drink12Ingredient3]
            drinkService.save(drink12, true)

            def drink13Ingredient3 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.OZ, amount:5, custom:false, canBeDeleted:false]))

            def drink13 = new Drink([name:'Cape Codder', number:13, mixingInstructions:'Pour ingredients into highball glass containing crushed ice. Stir vigorously, then garnish with a lime wedge.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.VODKA, symbol:'Cc', custom:false, canBeDeleted:false])
            drink13.ingredients = [drink11Ingredient1, drink12Ingredient3, drink13Ingredient3]
            drinkService.save(drink13, true)

            def drink14Ingredient1 = ingredientService.save(new Ingredient([name:'Lemon Juice', unit:Unit.FRUIT, amount:0.5, custom:false, canBeDeleted:false]))
            def drink14Ingredient2 = ingredientService.save(new Ingredient([name:'Powdered Sugar', unit:Unit.TEASPOON, amount:1, custom:false, canBeDeleted:false]))
            def drink14Ingredient3 = ingredientService.save(new Ingredient([name:'Gin', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))
            def drink14Ingredient4 = ingredientService.save(new Ingredient([name:'Club Soda', unit:Unit.FLUID_OZ, amount:16, custom:false, canBeDeleted:false]))

            def drink14 = new Drink([name:'Alabama Fizz', number:14, mixingInstructions:'Shake ingredients (no soda) with crushed ice then strain into highball glass. Fill with club soda and garnish with 2 sprigs of fresh mint.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.GIN, symbol:'Af', custom:false, canBeDeleted:false])
            drink14.ingredients = [drink14Ingredient1, drink14Ingredient2, drink14Ingredient3, drink14Ingredient4]
            drinkService.save(drink14, true)

            def drink15Ingredient1 = ingredientService.save(new Ingredient([name:'Lemon Juice', unit:Unit.FRUIT, amount:0.25, custom:false, canBeDeleted:false]))
            def drink15Ingredient2 = ingredientService.save(new Ingredient([name:'Triple Sec', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))
            def drink15Ingredient3 = ingredientService.save(new Ingredient([name:'Gin', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))

            def drink15 = new Drink([name:'Chelsea Sidecar', number:15, mixingInstructions:'Shake ingredients with crushed ice then strain into cocktail glass.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.GIN, symbol:'Cs', custom:false, canBeDeleted:false])
            drink15.ingredients = [drink15Ingredient1, drink15Ingredient2, drink15Ingredient3]
            drinkService.save(drink15, true)

            def drink16Ingredient4 = ingredientService.save(new Ingredient([name:'Chilled Champagne', unit:Unit.SODA_CAN, amount:1, custom:false, canBeDeleted:false]))

            def drink16 = new Drink([name:'Diamond Fizz', number:16, mixingInstructions:'Shake with crushed ice and strain into highball glass containing 2 ice cubes. Fill with champagne and stir vigorously.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.GIN, symbol:'Df', custom:false, canBeDeleted:false])
            drink16.ingredients = [drink14Ingredient1, drink14Ingredient2, drink14Ingredient3, drink16Ingredient4]
            drinkService.save(drink16, true)

            def drink17Ingredient2 = ingredientService.save(new Ingredient([name:'Irish Cream Liqueur', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))
            def drink17Ingredient3 = ingredientService.save(new Ingredient([name:'Brandy', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))
            def drink17Ingredient4 = ingredientService.save(new Ingredient([name:'Light Cream', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))

            def drink17 = new Drink([name:'Canyon Quake', number:17, mixingInstructions:'Mix ingredients in blender with crushed ice and blend smoothly. Pour into large wine glass.', suggestedGlass:GlassType.WHITE_WINE, alcohol:Alcohol.FROZEN, symbol:'Cq', custom:false, canBeDeleted:false])
            drink17.ingredients = [drink6Ingredient2, drink17Ingredient2, drink17Ingredient3, drink17Ingredient4]
            drinkService.save(drink17, true)

            def drink18Ingredient1 = ingredientService.save(new Ingredient([name:'Bourbon', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink18Ingredient2 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink18Ingredient3 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink18Ingredient4 = ingredientService.save(new Ingredient([name:'Sugar', unit:Unit.TEASPOON, amount:1, custom:false, canBeDeleted:false]))

            def drink18 = new Drink([name:'Cranberry Cooler', number:18, mixingInstructions:'Mix ingredients in blender with 1 cup of crushed ice and blend smoothly. Pour into hurricane glass.', suggestedGlass:GlassType.HURRICANE, alcohol:Alcohol.FROZEN, symbol:'Ca', custom:false, canBeDeleted:false])
            drink18.ingredients = [drink18Ingredient1, drink18Ingredient2, drink18Ingredient3, drink18Ingredient4]
            drinkService.save(drink18, true)

            def drink19Ingredient3 = ingredientService.save(new Ingredient([name:'Peppermint Schnapps', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink19Ingredient4 = ingredientService.save(new Ingredient([name:'Grapefruit Juice', unit:Unit.TABLESPOON, amount:1, custom:false, canBeDeleted:false]))

            def drink19 = new Drink([name:'Hot Pants', number:19, mixingInstructions:'Shake with crushed ice then pour into old-fashioned glass rimmed with salt.', suggestedGlass:GlassType.OLD_FASHIONED, alcohol:Alcohol.TEQUILA, symbol:'Hp', custom:false, canBeDeleted:false])
            drink19.ingredients = [drink1Ingredient1, drink14Ingredient2, drink19Ingredient3, drink19Ingredient4]
            drinkService.save(drink19, true)

            def drink20Ingredient4 = ingredientService.save(new Ingredient([name:'Lemon Juice', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink20 = new Drink([name:'Margarita', number:20, mixingInstructions:'Encircle rim of margarita glass with lemon or lime wedge, then dip rim into salt. Shake ingredients with ice then strain into glass.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.TEQUILA, symbol:'Mg', custom:false, canBeDeleted:false])
            drink20.ingredients = [drink1Ingredient1, drink4Ingredient3, drink9Ingredient3, drink20Ingredient4]
            drinkService.save(drink20, true)

            def drink21Ingredient4 = ingredientService.save(new Ingredient([name:'Lemonade', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))
            def drink21Ingredient5 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink21 = new Drink([name:'Citronella Cooler', number:21, mixingInstructions:'Pour ingredients into highball glass containing crushed ice. Stir vigorously, then garnish with a lime wedge.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.VODKA, symbol:'Cl', custom:false, canBeDeleted:false])
            drink21.ingredients = [drink1Ingredient1, drink4Ingredient3, drink12Ingredient3, drink21Ingredient4, drink21Ingredient5]
            drinkService.save(drink21, true)

            def drink22Ingredient2 = ingredientService.save(new Ingredient([name:'Citrus Vodka', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))

            def drink22 = new Drink([name:'Crocodile Cooler', number:22, mixingInstructions:'Pour ingredients (no soda) into hurricane glass filled with crushed ice. Add lemon-lime soda and stir, and garnish with cherry or lemon wedge.', suggestedGlass:GlassType.HURRICANE, alcohol:Alcohol.VODKA, symbol:'Cr', custom:false, canBeDeleted:false])
            drink22.ingredients = [drink15Ingredient2, drink22Ingredient2]
            drinkService.save(drink22, true)

            def drink23Ingredient1 = ingredientService.save(new Ingredient([name:'Vodka', unit:Unit.OZ, amount:1.25, custom:false, canBeDeleted:false]))
            def drink23Ingredient2 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink23Ingredient3 = ingredientService.save(new Ingredient([name:'Pineapple Juice', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))

            def drink23 = new Drink([name:'Desert Sunrise', number:23, mixingInstructions:'Pour ingredients over crushed ice in collins glass. Top off with grenadine.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.VODKA, symbol:'Ds', custom:false, canBeDeleted:false])
            drink23.ingredients = [drink23Ingredient1, drink23Ingredient2, drink23Ingredient3, drink2Ingredient4]
            drinkService.save(drink23, true)

            def drink24Ingredient3 = ingredientService.save(new Ingredient([name:'Sour Mix', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))
            def drink24Ingredient4 = ingredientService.save(new Ingredient([name:'Lemon-Lime Soda', unit:Unit.FLUID_OZ, amount:16, custom:false, canBeDeleted:false]))

            def drink24 = new Drink([name:'Electric Jam', number:24, mixingInstructions:'Pour ingredients over crushed ice in collins glass. Top off with lemon-lime soda.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.VODKA, symbol:'Ej', custom:false, canBeDeleted:false])
            drink24.ingredients = [drink4Ingredient2, drink23Ingredient1, drink24Ingredient3, drink24Ingredient4]
            drinkService.save(drink24, true)

            def drink25Ingredient1 = ingredientService.save(new Ingredient([name:'Gin', unit:Unit.OZ, amount:1.75, custom:false, canBeDeleted:false]))
            def drink25Ingredient2 = ingredientService.save(new Ingredient([name:'Dry Vermouth', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink25 = new Drink([name:'Dry Martini', number:25, mixingInstructions:'Mix vermouth and gin in mixing glass, then strain into cocktail glass. Serve with a twist of lemon peel or olive.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.GIN, symbol:'Dm', custom:false, canBeDeleted:false])
            drink25.ingredients = [drink25Ingredient1, drink25Ingredient2]
            drinkService.save(drink25, true)

            def drink26Ingredient2 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.FRUIT, amount:1, custom:false, canBeDeleted:false]))
            def drink26Ingredient3 = ingredientService.save(new Ingredient([name:'Gin', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink26Ingredient4 = ingredientService.save(new Ingredient([name:'Bitters', unit:Unit.DASH, amount:1, custom:false, canBeDeleted:false]))
            def drink26Ingredient5 = ingredientService.save(new Ingredient([name:'Creme de Menthe (White)', unit:Unit.TEASPOON, amount:0.5, custom:false, canBeDeleted:false]))
            def drink26Ingredient6 = ingredientService.save(new Ingredient([name:'Cherries', unit:Unit.FRUIT, amount:2, custom:false, canBeDeleted:false]))

            def drink26 = new Drink([name:'Fallen Angel', number:26, mixingInstructions:'Shake with crushed ice and strain into margarita glass. Serve with cherries.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.GIN, symbol:'Fa', custom:false, canBeDeleted:false])
            drink26.ingredients = [drink14Ingredient1, drink26Ingredient2, drink26Ingredient3, drink26Ingredient4, drink26Ingredient5, drink26Ingredient6]
            drinkService.save(drink26, true)

            def drink27Ingredient3 = ingredientService.save(new Ingredient([name:'Powdered Sugar', unit:Unit.TEASPOON, amount:0.5, custom:false, canBeDeleted:false]))
            def drink27Ingredient4 = ingredientService.save(new Ingredient([name:'Grenadine', unit:Unit.TABLESPOON, amount:1, custom:false, canBeDeleted:false]))
            def drink27Ingredient5 = ingredientService.save(new Ingredient([name:'Club Soda', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))
            def drink27Ingredient6 = ingredientService.save(new Ingredient([name:'Ginger-ale', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))

            def drink27 = new Drink([name:'Floradora Cooler', number:27, mixingInstructions:'Pour ingredients over crushed ice in collins glass. Stir vigorously.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.GIN, symbol:'Fc', custom:false, canBeDeleted:false])
            drink27.ingredients = [drink14Ingredient3, drink26Ingredient2, drink27Ingredient3, drink27Ingredient4, drink27Ingredient5, drink27Ingredient6]
            drinkService.save(drink27, true)

            def drink28Ingredient1 = ingredientService.save(new Ingredient([name:'Gin', unit:Unit.CUP, amount:0.5, custom:false, canBeDeleted:false]))
            def drink28Ingredient2 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.CUP, amount:0.5, custom:false, canBeDeleted:false]))
            def drink28Ingredient3 = ingredientService.save(new Ingredient([name:'Lemon Juice', unit:Unit.CUP, amount:0.5, custom:false, canBeDeleted:false]))
            def drink28Ingredient4 = ingredientService.save(new Ingredient([name:'Heavy Cream', unit:Unit.CUP, amount:0.5, custom:false, canBeDeleted:false]))
            def drink28Ingredient5 = ingredientService.save(new Ingredient([name:'Sugar', unit:Unit.TABLESPOON, amount:1, custom:false, canBeDeleted:false]))
            def drink28Ingredient7 = ingredientService.save(new Ingredient([name:'Club Soda', unit:Unit.OZ, amount:10, custom:false, canBeDeleted:false]))

            def drink28 = new Drink([name:'Creamy Gin Sour', number:28, mixingInstructions:'Mix ingredients in blender with 0.75 cups of crushed ice and blend to froth. Pour into large wine glass. Makes 4-6 servings.', suggestedGlass:GlassType.WHITE_WINE, alcohol:Alcohol.FROZEN, symbol:'Cg', custom:false, canBeDeleted:false])
            drink28.ingredients = [drink28Ingredient1, drink28Ingredient2, drink28Ingredient3, drink28Ingredient4, drink28Ingredient5, drink18Ingredient4, drink28Ingredient7]
            drinkService.save(drink28, true)

            def drink29Ingredient5 = ingredientService.save(new Ingredient([name:'Sour Mix', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink29 = new Drink([name:'Citron Neon', number:29, mixingInstructions:'Mix ingredients in blender with crushed ice and blend smoothly. Pour into hurricane glass, then garnish with lemon wedge and cherries.', suggestedGlass:GlassType.HURRICANE, alcohol:Alcohol.FROZEN, symbol:'Cn', custom:false, canBeDeleted:false])
            drink29.ingredients = [drink4Ingredient2, drink7Ingredient2, drink18Ingredient3, drink22Ingredient2, drink29Ingredient5]
            drinkService.save(drink29, true)

            def drink30Ingredient2 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.OZ, amount:3, custom:false, canBeDeleted:false]))
            def drink30Ingredient3 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink30Ingredient4 = ingredientService.save(new Ingredient([name:'Gold Tequila', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink30Ingredient5 = ingredientService.save(new Ingredient([name:'Orange', unit:Unit.WEDGE, amount:1, custom:false, canBeDeleted:false]))

            def drink30 = new Drink([name:'Mexican Madras', number:30, mixingInstructions:'Pour ingredients into shaker half-filled with ice. Shake well and strain into old-fashioned glass. Garnish with orange slice.', suggestedGlass:GlassType.OLD_FASHIONED, alcohol:Alcohol.TEQUILA, symbol:'Mm', custom:false, canBeDeleted:false])
            drink30.ingredients = [drink5Ingredient6, drink30Ingredient2, drink30Ingredient3, drink30Ingredient4, drink30Ingredient5]
            drinkService.save(drink30, true)

            def drink31Ingredient4 = ingredientService.save(new Ingredient([name:'Sloe Gin', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink31Ingredient5 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))

            def drink31 = new Drink([name:'Purple Pancho', number:31, mixingInstructions:'Shake with crushed ice and pour into salt-rimmed margarita glass. Garnish with lime wedge.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.TEQUILA, symbol:'Pp', custom:false, canBeDeleted:false])
            drink31.ingredients = [drink2Ingredient1, drink4Ingredient2, drink24Ingredient3, drink31Ingredient4, drink31Ingredient5]
            drinkService.save(drink31, true)

            def drink32Ingredient4 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.SPLASH, amount:1, custom:false, canBeDeleted:false]))

            def drink32 = new Drink([name:'Handball Cooler', number:32, mixingInstructions:'Pour vodka over crushed ice in highball glass. Fill almost to top with club soda, fill to top with orange juice stirring vigorously. Garnish with lime wedge.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.VODKA, symbol:'Hc', custom:false, canBeDeleted:false])
            drink32.ingredients = [drink11Ingredient1, drink12Ingredient3, drink14Ingredient4, drink32Ingredient4]
            drinkService.save(drink32, true)

            def drink33Ingredient1 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.OZ, amount:4, custom:false, canBeDeleted:false]))
            def drink33Ingredient2 = ingredientService.save(new Ingredient([name:'Galliano', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink33Ingredient3 = ingredientService.save(new Ingredient([name:'Vodka', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink33 = new Drink([name:'Harvey Wallbanger', number:33, mixingInstructions:'Pour vodka and orange juice over crushed ice in collins glass. Stir vigorously, then float Galliano at top.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.VODKA, symbol:'Hw', custom:false, canBeDeleted:false])
            drink33.ingredients = [drink33Ingredient1, drink33Ingredient2, drink33Ingredient3]
            drinkService.save(drink33, true)

            def drink34Ingredient2 = ingredientService.save(new Ingredient([name:'Vodka', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink34Ingredient4 = ingredientService.save(new Ingredient([name:'Light Rum', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink34Ingredient5 = ingredientService.save(new Ingredient([name:'Tequila', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink34Ingredient6 = ingredientService.save(new Ingredient([name:'Cola', unit:Unit.DASH, amount:1, custom:false, canBeDeleted:false]))

            def drink34 = new Drink([name:'Long Island Ice Tea', number:34, mixingInstructions:'Pour ingredients over crushed ice in highball glass. Add cola for color and garnish with lemon.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.VODKA, symbol:'Li', custom:false, canBeDeleted:false])
            drink34.ingredients = [drink14Ingredient1, drink34Ingredient2, drink28Ingredient1, drink34Ingredient4, drink34Ingredient5, drink34Ingredient6]
            drinkService.save(drink34, true)

            def drink35Ingredient3 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]))
            def drink35Ingredient4 = ingredientService.save(new Ingredient([name:'Triple Sec', unit:Unit.SPLASH, amount:1, custom:false, canBeDeleted:false]))
            def drink35Ingredient5 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.SPLASH, amount:1, custom:false, canBeDeleted:false]))

            def drink35 = new Drink([name:'Pink Lemonade', number:35, mixingInstructions:'Pour ingredients over crushed ice in collins glass. Shake then garnish with lemon wedge.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.VODKA, symbol:'Pl', custom:false, canBeDeleted:false])
            drink35.ingredients = [drink22Ingredient2, drink24Ingredient3, drink35Ingredient3, drink35Ingredient4, drink35Ingredient5]
            drinkService.save(drink35, true)

            def drink36Ingredient3 = ingredientService.save(new Ingredient([name:'Ginger-ale', unit:Unit.SODA_CAN, amount:1, custom:false, canBeDeleted:false]))

            def drink36 = new Drink([name:'Fog Horn', number:36, mixingInstructions:'Pour ingredients over crushed ice in highball glass. Top off with ginger ale and stir. Garnish with slice of lime.', suggestedGlass:GlassType.HIGHBALL, alcohol:Alcohol.GIN, symbol:'Fh', custom:false, canBeDeleted:false])
            drink36.ingredients = [drink26Ingredient2, drink26Ingredient3, drink36Ingredient3]
            drinkService.save(drink36, true)

            def drink37Ingredient2 = ingredientService.save(new Ingredient([name:'Grenadine', unit:Unit.DASH, amount:1, custom:false, canBeDeleted:false]))
            def drink37Ingredient3 = ingredientService.save(new Ingredient([name:'Gin', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))
            def drink37Ingredient4 = ingredientService.save(new Ingredient([name:'Orange Juice', unit:Unit.TABLESPOON, amount:1, custom:false, canBeDeleted:false]))

            def drink37 = new Drink([name:'Gin and Sin', number:37, mixingInstructions:'Shake with crushed ice and strain into margarita glass.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.GIN, symbol:'Gs', custom:false, canBeDeleted:false])
            drink37.ingredients = [drink20Ingredient4, drink37Ingredient2, drink37Ingredient3, drink37Ingredient4]
            drinkService.save(drink37, true)

            def drink38Ingredient4 = ingredientService.save(new Ingredient([name:'Pineapple Juice', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink38 = new Drink([name:'Rum Runner', number:38, mixingInstructions:'Shake with crushed ice and strain over ice cubes into old-fashioned glass rimmed with salt.', suggestedGlass:GlassType.OLD_FASHIONED, alcohol:Alcohol.GIN, symbol:'Rr', custom:false, canBeDeleted:false])
            drink38.ingredients = [drink18Ingredient4, drink26Ingredient2, drink26Ingredient3, drink26Ingredient4, drink38Ingredient4]
            drinkService.save(drink38, true)

            def drink39Ingredient2 = ingredientService.save(new Ingredient([name:'Light Rum', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink39Ingredient3 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink39Ingredient4 = ingredientService.save(new Ingredient([name:'Triple Sec', unit:Unit.TABLESPOON, amount:1, custom:false, canBeDeleted:false]))

            def drink39 = new Drink([name:'Frozen Daiquiri', number:39, mixingInstructions:'Mix ingredients in blender with 1 cup of crushed ice and blend at low speed for 5 seconds, then at high speed until smooth. Pour into wine glass and top with a cherry.', suggestedGlass:GlassType.WHITE_WINE, alcohol:Alcohol.FROZEN, symbol:'Rr', custom:false, canBeDeleted:false])
            drink39.ingredients = [drink18Ingredient4, drink39Ingredient2, drink39Ingredient3, drink39Ingredient4]
            drinkService.save(drink39, true)

            def drink40Ingredient6 = ingredientService.save(new Ingredient([name:'Grenadine', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink40Ingredient7 = ingredientService.save(new Ingredient([name:'Lemon', unit:Unit.WEDGE, amount:1, custom:false, canBeDeleted:false]))

            def drink40 = new Drink([name:'Frozen Fuzzy', number:40, mixingInstructions:'Mix ingredients in blender, fill with crushed ice to reach level of liquid and blend smoothly. Pour into wine glass and garnish with lemon or lime wedge.', suggestedGlass:GlassType.WHITE_WINE, alcohol:Alcohol.FROZEN, symbol:'Ff', custom:false, canBeDeleted:false])
            drink40.ingredients = [drink1Ingredient4, drink8Ingredient2, drink9Ingredient3, drink12Ingredient3, drink18Ingredient3, drink40Ingredient6, drink40Ingredient7]
            drinkService.save(drink40, true)

            def drink41Ingredient3 = ingredientService.save(new Ingredient([name:'Cream', unit:Unit.OZ, amount:1.5, custom:false, canBeDeleted:false]))
            def drink41Ingredient4 = ingredientService.save(new Ingredient([name:'Creme de Cacao', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink41 = new Drink([name:'Silk Stockings', number:41, mixingInstructions:'Shake ingredients with crushed ice. Strain into margarita glass then sprinkle top with cinnamon.', suggestedGlass:GlassType.MARGARITA, alcohol:Alcohol.TEQUILA, symbol:'Ss', custom:false, canBeDeleted:false])
            drink41.ingredients = [drink1Ingredient1, drink37Ingredient2, drink41Ingredient3, drink41Ingredient4]
            drinkService.save(drink41, true)

            def drink42Ingredient3 = ingredientService.save(new Ingredient([name:'Tequila', unit:Unit.OZ, amount:2, custom:false, canBeDeleted:false]), true)
            def drink42Ingredient4 = ingredientService.save(new Ingredient([name:'Tropical Fruit Schnapps', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]), true)

            def drink42 = new Drink([name:'Tijuana Taxi', number:42, mixingInstructions:'Pour tequila, curacao, and schnapps over crushed ice in collins glass. Fill with lemon-lime soda then garnish with an orange slice and cherry.', suggestedGlass:GlassType.TOM_COLLINS, alcohol:Alcohol.TEQUILA, symbol:'Tt', custom:false, canBeDeleted:false])
            drink42.ingredients = [drink8Ingredient3, drink24Ingredient4, drink42Ingredient3, drink42Ingredient4]
            drinkService.save(drink42, true)

            def drink43Ingredient3 = ingredientService.save(new Ingredient([name:'Strawberry Schnapps', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink43 = new Drink([name:'Affair', number:43, mixingInstructions:'Stir with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Ar', custom:false, canBeDeleted:false])
            drink43.ingredients = [drink1Ingredient2, drink21Ingredient5, drink43Ingredient3]
            drinkService.save(drink43, true)

            def drink44Ingredient1 = ingredientService.save(new Ingredient([name:'Grand Marnier', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink44Ingredient2 = ingredientService.save(new Ingredient([name:'Irish Cream Liqueur', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink44Ingredient3 = ingredientService.save(new Ingredient([name:'Coffee Liqueur', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink44 = new Drink([name:'B-52', number:44, mixingInstructions:'Pour carefully into shot glass in order given to ensure that each ingredient floats on top of preceding ingredient.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Bt', custom:false, canBeDeleted:false])
            drink44.ingredients = [drink44Ingredient1, drink44Ingredient2, drink44Ingredient3]
            drinkService.save(drink44, true)

            def drink45Ingredient4 = ingredientService.save(new Ingredient([name:'Brandy', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink45 = new Drink([name:'Between-the-sheets', number:45, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Bs', custom:false, canBeDeleted:false])
            drink45.ingredients = [drink9Ingredient3, drink15Ingredient1, drink34Ingredient4, drink45Ingredient4]
            drinkService.save(drink45, true)

            def drink46Ingredient3 = ingredientService.save(new Ingredient([name:'Amaretto', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]), true)

            def drink46 = new Drink([name:'Buzzard\'s Breath', number:46, mixingInstructions:'Stir with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Bz', custom:false, canBeDeleted:false])
            drink46.ingredients = [drink19Ingredient3, drink44Ingredient3, drink46Ingredient3]
            drinkService.save(drink46, true)

            def drink47Ingredient2 = ingredientService.save(new Ingredient([name:'Lime Juice', unit:Unit.TEASPOON, amount:1, custom:false, canBeDeleted:false]))

            def drink47 = new Drink([name:'C. C. Kazi', number:47, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Cz', custom:false, canBeDeleted:false])
            drink47.ingredients = [drink5Ingredient2, drink47Ingredient2, drink35Ingredient3]
            drinkService.save(drink47, true)

            def drink48Ingredient3 = ingredientService.save(new Ingredient([name:'Black Raspberry Liqueur', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink48 = new Drink([name:'Galactic Ale', number:48, mixingInstructions:'Shake with crushed ice and strain into shot glass. Makes 2 servings.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Ga', custom:false, canBeDeleted:false])
            drink48.ingredients = [drink4Ingredient3, drink23Ingredient1, drink48Ingredient3, drink8Ingredient3]
            drinkService.save(drink48, true)

            def drink49Ingredient2 = ingredientService.save(new Ingredient([name:'Lemonade', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink49Ingredient3 = ingredientService.save(new Ingredient([name:'Rum', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink49Ingredient4 = ingredientService.save(new Ingredient([name:'Melon Liqueur', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink49 = new Drink([name:'Green Demon', number:49, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Gd', custom:false, canBeDeleted:false])
            drink49.ingredients = [drink34Ingredient2, drink49Ingredient2, drink49Ingredient3, drink49Ingredient4]
            drinkService.save(drink49, true)

            def drink50Ingredient6 = ingredientService.save(new Ingredient([name:'Grapefruit Juice', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))
            def drink50Ingredient7 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink50 = new Drink([name:'Johnny Beach', number:50, mixingInstructions:'Shake with crushed ice and strain into shot glass. Makes 2 servings.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Jb', custom:false, canBeDeleted:false])
            drink50.ingredients = [drink1Ingredient3, drink7Ingredient1, drink7Ingredient2, drink11Ingredient1, drink30Ingredient3, drink50Ingredient6, drink50Ingredient7]
            drinkService.save(drink50, true)

            def drink51 = new Drink([name:'Kamikaze', number:51, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Kz', custom:false, canBeDeleted:false])
            drink51.ingredients = [drink9Ingredient3, drink18Ingredient3, drink34Ingredient2]
            drinkService.save(drink51, true)

            def drink52 = new Drink([name:'Lemon Drop', number:52, mixingInstructions:'Shake vodka with crushed ice and strain into shot glass. Dip each side of lemon wedge in sugar. Shoot vodka and immediately follow with bite into lemon wedge.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Ld', custom:false, canBeDeleted:false])
            drink52.ingredients = [drink11Ingredient1, drink28Ingredient5, drink40Ingredient7]
            drinkService.save(drink52, true)

            def drink53Ingredient2 = ingredientService.save(new Ingredient([name:'Jamaican Rum', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))
            def drink53Ingredient3 = ingredientService.save(new Ingredient([name:'Light Rum', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]))

            def drink53 = new Drink([name:'Parisian Blonde', number:53, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Pb', custom:false, canBeDeleted:false])
            drink53.ingredients = [drink15Ingredient2, drink53Ingredient2, drink53Ingredient3]
            drinkService.save(drink53, true)

            def drink54Ingredient3 = ingredientService.save(new Ingredient([name:'Black Raspberry Liqueur', unit:Unit.OZ, amount:0.25, custom:false, canBeDeleted:false]))

            def drink54 = new Drink([name:'Purple Hooter', number:54, mixingInstructions:'Shake with crushed ice and strain into chilled shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Ph', custom:false, canBeDeleted:false])
            drink54.ingredients = [drink9Ingredient3, drink22Ingredient2, drink54Ingredient3]
            drinkService.save(drink54, true)

            def drink55Ingredient3 = ingredientService.save(new Ingredient([name:'Southern Comfort', unit:Unit.OZ, amount:1, custom:false, canBeDeleted:false]))

            def drink55 = new Drink([name:'Purple Hooter', number:55, mixingInstructions:'Shake with crushed ice and strain into chilled shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Rm', custom:false, canBeDeleted:false])
            drink55.ingredients = [drink6Ingredient2, drink18Ingredient3, drink55Ingredient3]
            drinkService.save(drink55, true)

            def drink56Ingredient5 = ingredientService.save(new Ingredient([name:'Cranberry Juice', unit:Unit.SODA_CAN, amount:1, custom:false, canBeDeleted:false]))

            def drink56 = new Drink([name:'Purple Hooter', number:56, mixingInstructions:'Shake with crushed ice and strain into shot glass. Top with cranberry juice.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Sb', custom:false, canBeDeleted:false])
            drink56.ingredients = [drink34Ingredient2, drink38Ingredient4, drink48Ingredient3, drink49Ingredient4, drink56Ingredient5]
            drinkService.save(drink56, true)

            def drink57Ingredient3 = ingredientService.save(new Ingredient([name:'Apple Liqueur', unit:Unit.OZ, amount:0.75, custom:false, canBeDeleted:false]), true)
            def drink57Ingredient4 = ingredientService.save(new Ingredient([name:'Lemon-Lime Soda', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]), true)

            def drink57 = new Drink([name:'Sour Apple', number:57, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Sa', custom:false, canBeDeleted:false])
            drink57.ingredients = [drink9Ingredient2, drink49Ingredient4, drink57Ingredient3, drink57Ingredient4]
            drinkService.save(drink57, true)

            def drink58Ingredient3 = ingredientService.save(new Ingredient([name:'Peach Schnapps', unit:Unit.OZ, amount:0.5, custom:false, canBeDeleted:false]))

            def drink58 = new Drink([name:'Woo Woo', number:58, mixingInstructions:'Shake with crushed ice and strain into shot glass.', suggestedGlass:GlassType.SHOT, alcohol:Alcohol.SHOOTER, symbol:'Ww', custom:false, canBeDeleted:false])
            drink58.ingredients = [drink21Ingredient5, drink34Ingredient2, drink58Ingredient3]
            drinkService.save(drink58, true)
            // End of default drinks and ingredients
        }
    }

    def destroy = {
    }

}