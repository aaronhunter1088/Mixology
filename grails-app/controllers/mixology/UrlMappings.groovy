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
        "/mixology/ingredient/save" {
            controller = "ingredient"
            action = "save"
        }
        "/mixology/validate" {
            controller = "ingredient"
            action = "validate"
        }
        "/mixology/search" {
            controller = "search"
            action = "index"
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
