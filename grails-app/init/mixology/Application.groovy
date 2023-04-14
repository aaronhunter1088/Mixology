package mixology

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

import groovy.transform.CompileStatic
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@CompileStatic
class Application extends GrailsAutoConfiguration {
    static Logger logger = LogManager.getLogger(Application.class)

    static void main(String[] args) {
        logger.info("Starting app Mixology")
        GrailsApp.run(Application, args)
    }
}