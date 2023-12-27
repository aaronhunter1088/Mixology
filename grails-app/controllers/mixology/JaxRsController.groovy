package mixology

import org.grails.plugins.jaxrs.core.JaxrsRequestWrapper

import javax.servlet.ServletResponse
import javax.servlet.ServletResponseWrapper

class JaxRsController {

    static defaultAction = "handle"

    def jaxrsContext

    def handle() {
        def unwrapped = unwrap(response)
        jaxrsContext.jaxrsService.process(new JaxrsRequestWrapper(request), unwrapped)
        jaxrsContext.outputStream.flush()
        return null
    }

    private static ServletResponse unwrap(ServletResponse response) {
        if (response instanceof ServletResponseWrapper)
            return unwrap(response.response)
        response
    }
}
