package mixology

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/customDrinks" {
            controller = 'drink'
            action = 'customIndex'
        }

        "/emails/shareDrinkEmail"(controller:'emails', action:'shareDrinkEmail')

        "/mixology/drink/save" {
            controller = "drink"
            action = "save"
        }
        "/mixology/drink/delete/$id" {
            controller = "drink"
            action = "delete"
        }
        "/mixology/drink/update" {
            controller = "drink"
            action = "update"
        }
        "/mixology/ingredient/save" {
            controller = "ingredient"
            action = "save"
        }
        "/mixology/ingredient/delete/$id" {
            controller = "ingredient"
            action = "delete"
        }
        "/mixology/validate" {
            controller = "ingredient"
            action = "validate"
        }
        "/mixology/drink/validateIngredients" {
            controller = "drink"
            action = "validateIngredients"
        }
        "/mixology/user/create" {
            controller = "user"
            action = "create"
        }

        "/"(view:"/index")
        "/home"(view:'/index')
        "/v1/**" {controller = 'jaxRs'}
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
