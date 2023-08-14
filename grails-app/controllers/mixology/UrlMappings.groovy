package mixology

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

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

        "/mixology/search" {
            controller = "search"
            action = "index"
        }
        "/mixology/search/search" {
            controller = "search"
            action = "search"
        }

        "/mixology/user/create" {
            controller = "user"
            action = "create"
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
