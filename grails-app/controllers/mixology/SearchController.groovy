package mixology

class SearchController {

    def index(params) {
        render(view: 'index', model: params)
    }
}
