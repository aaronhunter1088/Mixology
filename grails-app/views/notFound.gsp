<%@ page import="mixology.Drink" %>
<!doctype html>
<html>
    <head>
        <title>Page Not Found</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <g:set var="object" value="${object}"/>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <ul class="col-12 errors">
                    <g:if test="${'Drink' == object}">
                        <li>Error: Drink Not Found (404)</li>
                    </g:if>
                    <g:elseif test="${'Ingredient' == object}">
                        <li>Error: Ingredient Not Found (404)</li>
                    </g:elseif>
                    <g:elseif test="${'User' == object}">
                        <li>Error: User Not Found (404)</li>
                    </g:elseif>
                    <g:else>
                        <li>Error: Page Not Found (404)</li>
                    </g:else>
                    <li>Path: ${request.forwardURI}</li>
                </ul>
            </section>
        </div>
    </div>
    </body>
</html>
