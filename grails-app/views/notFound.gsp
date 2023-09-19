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
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <ul class="col-12 errors">
                    <li>Error: Page Not Found (404)</li>
                    <li>Path: ${request.forwardURI}</li>
                </ul>
            </section>
        </div>
    </div>
    </body>
</html>
