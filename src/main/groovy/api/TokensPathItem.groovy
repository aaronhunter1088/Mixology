package api

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

class TokensPathItem {

    static final String TAG_NAME = 'Token'

    static Tag getTag() {
        new Tag()
            .name('Token')
            .description('The endpoints configured to work with the AuthToken object')
    }

    static PathItem pathItem(String path) {
        PathItem pathItem = new PathItem()
        switch (path) {
            case 'tokens': {
                pathItem.summary('AuthTokens API Summary')
                pathItem.description('AuthTokens API Description')
                pathItem.setGet(new Operation()
                        .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("View all the tokens for a single user. If you do not pass in any AUTH token, then a 400 is returned. (Admin users will see all AuthTokens)")
                        .operationId('getAllTokens')
                        .summary('View All Tokens'))
                RequestBody postBody = new RequestBody()
                        .description('Create a token body')
                postBody.setContent(new Content().addMediaType('application/json', createTokenPostMediaType()))
                pathItem.setPost(new Operation()
                        //.parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .requestBody(postBody)
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('Invalid credentials')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description('Create a new token for a user.')
                        .operationId('createAToken')
                        .summary('Create A Token')
                )
                break
            }
            case 'tokens/tokensId': {
                pathItem.summary('AuthTokens API Summary')
                pathItem.description('AuthTokens API Description')
                pathItem.setGet(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('tokenId').schema(new Schema().type('integer')).required(true).description('The ID of the token to retrieve')
                        ])
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("View a single token for a user. If you pass in authorization, and that user does not have any relation to the token you are trying to view, then a 400 will be returned. If the user does have a relationship to the token, a 200 will be returned. If you do not pass in authorization, then a 400 is returned.")
                        .operationId('viewAToken')
                        .summary('View A Token'))
                RequestBody putBody = new RequestBody()
                        .description('Update a token body')
                putBody.setContent(new Content().addMediaType('application/json', createTokensPutMediaType()))
                pathItem.setPut(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('tokenId').schema(new Schema().type('integer')).required(true).description('The ID of the token to update')
                        ])
                        .requestBody(putBody)
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken is invalid, or TokenId is invalid.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("Update an existing token for a user. If you pass in authorization, and that user does not have any relation to the token you are trying to view, then a 400 will be returned. If the user does have a relationship to the token, a 200 will be returned. If you do not pass in authorization, a 400 will be returned. *For Admin Users only*")
                        .operationId('updateAToken')
                        .summary('Update A Token'))
                break
            }
            case 'tokens/removeExpiredTokens': {
                pathItem.summary('AuthTokens API Summary')
                pathItem.description('AuthTokens API Description')
                //RequestBody deleteBody = new RequestBody().description('Delete expired tokens')
                //deleteBody.setContent(new Content().addMediaType('application/json', createTokensDeleteExpiredTokensMediaType()))
                pathItem.setDelete(new Operation()
                    .parameters([
                        new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                    ])
                    .responses([
                            '200':new ApiResponse().description('Ok'),
                            '400':new ApiResponse().description('AuthToken has expired, or invalid user.')
                    ] as ApiResponses)
                    .tags([TAG_NAME])
                    .description('Delete all expired tokens. *For Admin Users only*')
                    .operationId('removeExpiredTokens')
                    .summary('Remove Expired Tokens'))
                break
            }
            default: throw new RuntimeException("Path, $path, not defined in DrinksPathItem!")
        }
        pathItem
    }

    private static MediaType createTokenPostMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
            new Schema()
                .type('object')
                .properties([
                    username:new Schema()
                        .type('string')
                        .example('username@email.com'),
                    password:new Schema()
                        .type('string')
                        .example('your password')
                ])
        )
        mediaType
    }

    private static MediaType createTokensPutMediaType() {
        MediaType mediaType = new MediaType()
        mediaType.setSchema(
            new Schema()
                .type('object')
                .properties([
                    expiresDate:new Schema()
                        .type('string')
                        .example('An expired date')
                ]))
        mediaType
    }

    private static MediaType createTokensDeleteExpiredTokensMediaType() {

    }
}
