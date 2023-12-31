package api

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

class IngredientsPathItem {

    static final String TAG_NAME = 'Ingredient'

    static Tag getTag() {
        new Tag()
            .name('Ingredient')
            .description('The endpoints configured to work with the Ingredient object')
    }

    static PathItem pathItem(String path) {
        PathItem pathItem = new PathItem()
        switch (path) {
            case 'ingredients': {
                pathItem.summary('Ingredients API Summary')
                pathItem.description('Ingredients API Description')
                pathItem.setGet(new Operation()
                        .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .responses([
                            '200':new ApiResponse().description('Ok'),
                            '400':new ApiResponse().description('AuthToken has expired')
                        ] as ApiResponses)
                        .tags(['Ingredient'])
                        .description("View all the ingredients for a single user. If you do not pass in any AUTH token, then ALL ingredients are returned.")
                        .operationId('viewAllIngredients')
                        .summary('View All Ingredients'))
                RequestBody postBody = new RequestBody()
                        .description('Create an ingredient body')
                        postBody.setContent(new Content().addMediaType('application/json', createIngredientPostMediaType()))
                        pathItem.setPost(new Operation()
                                .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                                .requestBody(postBody)
                                .tags(['Ingredient'])
                                .description('Create a new ingredient for a user.')
                                .operationId('createAnIngredient')
                                .summary('Create An Ingredient')
                        )
                break
            }
            case 'ingredients/ingredientId': {
                pathItem.summary('Ingredient API Summary')
                pathItem.description('Ingredient API Description')
                pathItem.setGet(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('ingredientId').schema(new Schema().type('integer')).required(true).description('The ID of the ingredient to retrieve')
                        ])
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("View a single ingredient for a user. If you pass in authorization, and that user does not have any relation to the ingredient you are trying to view, then a 400 will be returned. If the user does have a relationship to the ingredient, a 200 will be returned. If you do not pass in authorization, the ingredient will be returned, if one exists.")
                        .operationId('viewAnIngredient')
                        .summary('View A Drink'))
                RequestBody putBody = new RequestBody()
                        .description('Create an ingredient body')
                putBody.setContent(new Content().addMediaType('application/json', createIngredientPutMediaType()))
                pathItem.setPut(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('ingredientId').schema(new Schema().type('integer')).required(true).description('The ID of the ingredient to update')
                        ])
                        .requestBody(putBody)
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("Update an existing ingredient for a user. If you pass in authorization, and that user does not have any relation to the ingredient you are trying to view, then a 400 will be returned. If the user does have a relationship to the ingredient, a 200 will be returned. If you do not pass in authorization, a 400 will be returned.")
                        .operationId('updateAnIngredient')
                        .summary('Update An Ingredient'))
                RequestBody deleteBody = new RequestBody()
                        .description('Create an ingredient body')
                deleteBody.setContent(new Content().addMediaType('application/json', createIngredientDeleteMediaType()))
                pathItem.setDelete(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('ingredientId').schema(new Schema().type('integer')).required(true).description('The ID of the ingredient to delete'),
                        ])
                        .requestBody(deleteBody)
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not an ID in the users list of drinks.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description('Delete a single ingredient from a user and the application itself.')
                        .operationId('deleteAnIngredient')
                        .summary('Delete An Ingredient'))
                break
            }
            default: throw new RuntimeException("Path, $path, not defined in IngredientsPathItem!")
        }
        pathItem
    }

    private static MediaType createIngredientPostMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
            new Schema()
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
        )
        mediaType
    }

    private static MediaType createIngredientPutMediaType() {
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

    private static MediaType createIngredientDeleteMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
            new Schema()
                .type('object')
                .properties([
                    Confirm:new Schema()
                        .type('string')
                        .example('Yes')
                ]))
        mediaType
    }

}
