package mixology

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

import groovy.transform.CompileStatic
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.SpecVersion
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean

@EnableAutoConfiguration
@CompileStatic
class Application extends GrailsAutoConfiguration {
    static Logger logger = LogManager.getLogger(Application.class)

    static void main(String[] args) {
        logger.info("Starting app Mixology")
        GrailsApp.run(Application, args)
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = apiInfo()
        OpenAPI api = new OpenAPI()
                api.setComponents(getApiComponents())
                api.info(info)
                .specVersion(SpecVersion.V30)
                .servers(getApiServers())
                .paths(apiPaths())
                .tags(apiTags())
        api
    }

    private static Components getApiComponents() {
        Components components = new Components()
        RequestBody body = new RequestBody()
                .description('Delete A Drink Body')
        body.setContent(new Content().addMediaType('application/json', myMediaType()))
        components.requestBodies(
            [deleteADrinkBody:body]
        )
    }

    private static MediaType myMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
                new Schema()
                    .type('object')
                    .properties(
                        [Confirm:new Schema()
                            .type('string')
                            .example(new Example().value('Yes'))
                        ]))
        mediaType
    }

    private static Info apiInfo() {
        return new Info()
                .title ("My REST API")
                .description("Some custom description of API.")
                .summary("API TOS")
                .termsOfService("Terms of service")
                .contact(getInfoContact())
                .license(new License())
    }

    private static Contact getInfoContact() {
        Contact contact = new Contact()
        contact.name("John Doe")
        contact.email("myeaddress@company.com")
        contact.url("www.example.com")
    }

    private static Paths apiPaths() {
        Paths mapOfPaths = new Paths()
        mapOfPaths.addPathItem('/v1/drinks', pathItem('drinks'))
        mapOfPaths.addPathItem('/v1/drinks/{drinkId}', pathItem('drinks/{drinkId}'))
        mapOfPaths
    }

    private static PathItem pathItem(String path) {
        PathItem pathItem = new PathItem()
        switch (path) {
            case 'drinks': {
                pathItem.summary('Drinks API Summary')
                pathItem.description('Drinks API Description')
                pathItem.setGet(new Operation()
                    .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                    .responses(['200':new ApiResponse().description('')] as ApiResponses)
                    .tags(['Drink'])
                    .description("View all the drinks for a single user. If you do not pass in any AUTH token, then ALL drinks are returned.")
                    .operationId('viewAllDrinksUsingToken')
                    .summary('View All Drinks')
                )
                break
            }
            case 'drinks/{drinkId}' : {
                pathItem.summary('Drinks API Summary')
                pathItem.description('Drinks API Description')
                pathItem.setGet(new Operation()
                        .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .responses(['200':new ApiResponse().description('')] as ApiResponses)
                        .tags(['Drink'])
                        .description("""View a single drink for a user. 
If you pass in an AUTH token, and that user does not have any relation to the drink you are 
trying to view, then a 400 will be returned. If the user does have a relationship to the drink, 
a 200 will be returned. If you do not pass in any AUTH token, the drink will be returned, if one exists.""")
                        .operationId('viewAllDrinksUsingToken')
                        .summary('View A Drink')
                )
                break
            }
            default: throw new RuntimeException("Path, $path, not defined in openAPI!")
        }
        pathItem
    }

    private static List<Tag> apiTags() {
        List<Tag> tags = []
        tags.add(
            new Tag().name('Drink')
                     .description("""
The drink resource files allows for the following: 
a GET /drinks, to return all drinks for a user, 
a GET /drinks/id, to return a single drink for a user, 
a POST /drinks/id, to create a single drink for a user, 
a PUT /drinks/id, to update a single drink for a user, and finally, 
a DELETE /drinks/id, to delete a single drink, from the user and the application itself.""")
            // TODO: add more tags here
        )
        tags
    }

    private static List<Server> getApiServers() {
        [
        new Server().url('http://localhost')
        ]
    }
}