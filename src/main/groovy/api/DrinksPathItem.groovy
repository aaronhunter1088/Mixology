package api

import enums.Alcohol
import enums.GlassType
import enums.Unit
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.tags.Tag

class DrinksPathItem {

    static final String TAG_NAME = 'Drink'

    static Tag getTag() {
        new Tag()
            .name(TAG_NAME)
            .description('The endpoints configured to work with the Drink object')
    }

    static PathItem pathItem(String path) {
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
                        .tags([TAG_NAME])
                        .description("View all the drinks for a single user. If you do not pass in any AUTH token, then ALL drinks are returned.")
                        .operationId('viewAllDrinks')
                        .summary('View All Drinks'))
                RequestBody postBody = new RequestBody()
                        .description('Create a drink body')
                postBody.setContent(new Content().addMediaType('application/json', createDrinkPostMediaType()))
                pathItem.setPost(new Operation()
                        .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .requestBody(postBody)
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description('Create a new drink for a user.')
                        .operationId('createADrink')
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
                            new Parameter().in('path').name('drinkId').schema(new Schema().type('integer')).required(true).description('The ID of the drink to retrieve')
                        ])
                        .responses([
                            '200':new ApiResponse().description('Ok'),
                            '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("View a single drink for a user. If you pass in authorization, and that user does not have any relation to the drink you are trying to view, then a 400 will be returned. If the user does have a relationship to the drink, a 200 will be returned. If you do not pass in authorization, the drink will be returned, if one exists.")
                        .operationId('viewADrink')
                        .summary('View A Drink'))
                RequestBody putBody = new RequestBody()
                        .description('Update a drink body')
                putBody.setContent(new Content().addMediaType('application/json', createDrinkPutMediaType()))
                pathItem.setPut(new Operation()
                        .parameters([
                            new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                            new Parameter().in('path').name('drinkId').schema(new Schema().type('integer')).required(true).description('The ID of the drink to update')
                        ])
                        .requestBody(putBody)
                        .responses([
                            '200':new ApiResponse().description('Ok'),
                            '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("Update an existing drink for a user. If you pass in authorization, and that user does not have any relation to the drink you are trying to view, then a 400 will be returned. If the user does have a relationship to the drink, a 200 will be returned. If you do not pass in authorization, a 400 will be returned.")
                        .operationId('updateADrink')
                        .summary('Update A Drink'))
                RequestBody deleteBody = new RequestBody()
                        .description('Create a drink body')
                deleteBody.setContent(new Content().addMediaType('application/json', createDrinkDeleteMediaType()))
                pathItem.setDelete(new Operation()
                        .parameters([
                            new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                            new Parameter().in('path').name('drinkId').schema(new Schema().type('integer')).required(true).description('The ID of the drink to delete'),
                        ])
                        .requestBody(deleteBody)
                        .responses([
                            '200':new ApiResponse().description('Ok'),
                            '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description('Delete a single drink from a user and the application itself.')
                        .operationId('deleteADrink')
                        .summary('Delete A Drink'))
                break
            }
            default: throw new RuntimeException("Path, $path, not defined in DrinksPathItem!")
        }
        pathItem
    }

    private static MediaType createDrinkPostMediaType() {
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
                                .items(new Schema()
                                    .type('number')
                                    .example(Integer.valueOf(15))
                                ).items(new Schema()
                                .type('object')
                                .properties([
                                    amount:new Schema()
                                            .type('number')
                                            .example(2.5),
                                    name:new Schema()
                                            .type('string')
                                            .example('The name of your ingredient'),
                                    unit:new Schema()
                                            .type('string')
                                            .example(Unit.randomUnit)
                                ])
                                .example([15,30,
                                          "{'name':'ofNewDrink1','unit':'ofNewDrink1','amount':'ofNewDrink1'}",
                                          "{'name':'ofNewDrink2','unit':'ofNewDrink2','amount':'ofNewDrink2'}"]),
                    ),
                            mixingInstruction:new Schema()
                            .type('string')
                            .example('Add existing ingredients by passing in the ID, or add a new ingredient following the object example'),
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
                ])
        )
        mediaType
    }

    private static MediaType createDrinkPutMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
            new Schema()
                .type('object')
                .properties([
                    name:new Schema()
                        .type('string')
                        .example('Updated Name from API')
                ]))
        mediaType
    }

    private static MediaType createDrinkDeleteMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
                new Schema()
                        .type('object')
                        .properties([
                                Confirm:new Schema()
                                        .type('string').example('Yes')
                        ]))
        mediaType
    }
}
