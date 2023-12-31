package mixology

import api.DrinksPathItem
import api.IngredientsPathItem
import api.TokensPathItem
import api.UsersPathItem
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
        //Drinks
        mapOfPaths.addPathItem('/v1/drinks', DrinksPathItem.pathItem('drinks'))
        mapOfPaths.addPathItem('/v1/drinks/{drinkId}', DrinksPathItem.pathItem('drinks/drinksId'))
        //Ingredients
        mapOfPaths.addPathItem('/v1/ingredients', IngredientsPathItem.pathItem('ingredients'))
        mapOfPaths.addPathItem('/v1/ingredients/{ingredientId}', IngredientsPathItem.pathItem('ingredients/ingredientId'))
        //Tokens
        mapOfPaths.addPathItem('/v1/tokens', TokensPathItem.pathItem('tokens'))
        mapOfPaths.addPathItem('/v1/tokens/{tokenId}', TokensPathItem.pathItem('tokens/tokensId'))
        mapOfPaths.addPathItem('v1/tokens/removeExpiredTokens', TokensPathItem.pathItem('tokens/removeExpiredTokens'))
        //Users
        mapOfPaths.addPathItem('/v1/users', UsersPathItem.pathItem('users'))
        mapOfPaths.addPathItem('/v1/users/{userId}', UsersPathItem.pathItem('users/userId'))
        mapOfPaths
    }

    private static List<Tag> apiTags() {
        List<Tag> tags = []
        tags.addAll(// add more tags here
            DrinksPathItem.tag,
            IngredientsPathItem.tag,
            TokensPathItem.tag,
            UsersPathItem.tag
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