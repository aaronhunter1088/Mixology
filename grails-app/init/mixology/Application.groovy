package mixology

import enums.Alcohol
import enums.GlassType
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
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@EnableAutoConfiguration
@CompileStatic
class Application extends GrailsAutoConfiguration {

    @Autowired
    static MessageSource messageSource
    static Logger logger = LogManager.getLogger(Application.class)

    static void main(String[] args) {
        logger.info("Starting app Mixology")
        GrailsApp.run(Application, args)
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
            }
        };
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
                //.security(apiSecurity())
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
                            .example('Yes')
                        ]))
        mediaType
    }

    private static MediaType createDrinkMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
            new Schema()
                .type('object')
                .properties([
                    alcoholType:new Schema()
                        .type('string')
                        .example(Alcohol.randomAlcohol),
                    ingredients:new Schema()
                       .type('array')
                       .items(new Schema().type('number').example(Integer.valueOf(15)))
                       .example([15,30,65]),
                    mixingInstruction:new Schema()
                       .type('string')
                       .example('Write in your instructions here.'),
                    name:new Schema()
                        .type('string')
                        .example('OpenAPI Drink'),
                    number:new Schema()
                        .type('integer')
                        .example(07),
                    suggestedGlass:new Schema()
                        .type('string')
                        .example(GlassType.randomGlass),
                    symbol:new Schema()
                        .type('string')
                        .example('OD')
                    ]
                )
            )
        mediaType
    }

    private static Info apiInfo() {
        return new Info()
                .title ('Mixology Application Programming Interface')
                .description("Endpoints configured to work with the Mixology application")
                .summary("*SUMMARY*")
                .termsOfService("*Terms of service*")
                .contact(getInfoContact())
                .license(new License())
    }

    private static Contact getInfoContact() {
        Contact contact = new Contact()
        contact.name("Michael (Aaron Hunter) Ball")
        contact.email("hidden@gmail.com")
        contact.url("hiddenwebsite.com")
    }

    private static Paths apiPaths() {
        Paths mapOfPaths = new Paths()
        mapOfPaths.addPathItem('/v1/drinks', pathItem('drinks'))
        mapOfPaths.addPathItem('/v1/drinks/{drinkId}', pathItem('drinks/drinksId'))
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
                    .responses([
                            '200':new ApiResponse().description('Ok'),
                            '400':new ApiResponse().description('AuthToken has expired')
                    ] as ApiResponses)
                    .tags(['Drink'])
                    .description("View all the drinks for a single user. If you do not pass in any AUTH token, then ALL drinks are returned.")
                    .operationId('viewAllDrinksUsingToken')
                    .summary('View All Drinks')
                )
                RequestBody body = new RequestBody()
                        .description('Create a drink body')
                body.setContent(new Content().addMediaType('application/json', createDrinkMediaType()))
                pathItem.setPost(new Operation()
                        .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .requestBody(body)
                        .tags(['Drink'])
                        .description('Create a new drink for a user.')
                        .operationId('createADrinkUsingToken')
                        .summary('Create A Drink')
                )
                break
            }
            case 'drinks/drinksId': {
                pathItem.summary('Drinks API Summary')
                pathItem.description('Drinks API Description')
                pathItem.setGet(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('drinkId').schema(new Schema().type('integer')).required(true).description('The ID of the drink')
                        ])
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags(['Drink'])
                        .description("View a single drink for a user. If you pass in authorization, and that user does not have any relation to the drink you are trying to view, then a 400 will be returned. If the user does have a relationship to the drink, a 200 will be returned. If you do not pass in authorization, the drink will be returned, if one exists.")
                        .operationId('viewADrink')
                        .summary('View A Drinks'))
                pathItem.setPut(new Operation())
                pathItem.setDelete(new Operation())
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
The drink resource files allows for the following:\n\n
a GET /drinks, to return all drinks for a user,\n 
a GET /drinks/id, to return a single drink for a user,\n
a POST /drinks/id, to create a single drink for a user,\n
a PUT /drinks/id, to update a single drink for a user, and finally,\n
a DELETE /drinks/id, to delete a single drink, from the user and the application itself.""")
            // TODO: add more tags here
        )
        tags
    }

    private static List<Server> getApiServers() {
        [
        //new Server().url('/').description('Default Server URL') /* Needed for resolving cors issue on swagger ui */
        new Server().url('http://localhost:5009').description('Default Server URL') /* Needed for resolving cors issue on swagger ui */
        ]
    }

    private static List<SecurityRequirement> apiSecurity() {
        List<SecurityRequirement> securities = []
        //securities << new SecurityRequirement().addList('basicAuth', '[]')
        securities << new SecurityRequirement().addList('auth', '')
    }

}