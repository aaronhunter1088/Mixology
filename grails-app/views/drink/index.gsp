<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        %{--    <meta name="layout" content="main"/>--}%
        <title>Drinks</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
%{--        <asset:stylesheet src="application.css"/>--}%
%{--        <asset:javascript src="application.js"/>--}%
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        %{--        <asset:link rel="icon" href="cocktailRed.png" type="image/png" id="icon"/>--}%
        %{--        <link rel="icon" type="image/x-ico" href="../../assets/images/cocktail.ico">--}%
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'cocktail.ico')}" />

%{--        <meta name="layout" content="main" />--}%
        <g:set var="entityName" value="${message(code: 'drink.label', default: 'Drink')}" />
%{--        <title><g:message code="default.list.label" args="[entityName]" /></title>--}%
    </head>
    <body>
    <div id="content">
        <div class="container">
            <section class="row">
                <a href="#list-drink" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="list-drink" class="col-12 content scaffold-list">
                    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <f:table collection="${drinkList}" />

                    <g:if test="${drinkCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${drinkCount ?: 0}" />
                    </div>
                    </g:if>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>