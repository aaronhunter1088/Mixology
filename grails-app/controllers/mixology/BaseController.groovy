package mixology

abstract class BaseController implements IController {


}

interface IController {

    void okRequest(def method, def message)         // 200, ok
    void createdRequest(def method, def message)    // 201, created
    void noContentRequest(def method, def message)  // 204, no content

    void badRequest(def method, def message)        // 400
    void unauthorized(def method, def message)      // 401
    void notFound(def method, def message)          // 404
    void methodNotAllowed(def method, def message)  // 405

}