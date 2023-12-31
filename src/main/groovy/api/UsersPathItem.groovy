package api

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.tags.Tag

class UsersPathItem {

    static final String TAG_NAME = 'User'

    static Tag getTag() {
        new Tag()
            .name(TAG_NAME)
            .description('The endpoints configured to work with the User object')
    }

    static PathItem pathItem(String path) {
        PathItem pathItem = new PathItem()
        switch (path) {
            case 'users': {
                pathItem.summary('Users API Summary')
                pathItem.description('Users API Description')
                pathItem.setGet(new Operation()
                        .parameters([new Parameter().in('header').name('AUTH').schema(new Schema().type('string'))])
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("View all the users. *For Admin Users only*")
                        .operationId('viewAllUsers')
                        .summary('View All Users'))
                break
            }
            case 'users/userId': {
                pathItem.summary('Users API Summary')
                pathItem.description('Users API Description')
                pathItem.setGet(new Operation()
                        .parameters([
                                new Parameter().in('header').name('AUTH').schema(new Schema().type('string')),
                                new Parameter().in('path').name('userId').schema(new Schema().type('integer')).required(true).description('The ID of the user to retrieve')
                        ])
                        .responses([
                                '200':new ApiResponse().description('Ok'),
                                '400':new ApiResponse().description('AuthToken has expired, or the ID provided is not valid.')
                        ] as ApiResponses)
                        .tags([TAG_NAME])
                        .description("View a users' details. ")
                        .operationId('viewAUser')
                        .summary('View A User'))
                break
            }
            default: throw new RuntimeException("Path, $path, not defined in DrinksPathItem!")
        }
        pathItem
    }
}
