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

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
