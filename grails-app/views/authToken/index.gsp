<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><g:message code="drink.index.display.all.authTokens" default="Display all authTokens"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <g:set var="entityName" value="${message(code: 'authToken.label', default: 'AuthToken')}" />
        <style>
        .btn-xs {
            padding: 1px 5px !important;
            font-size: 12px !important;
            line-height: 1.5 !important;
            border-radius: 3px !important;
        }
        #filterDrinksFormDiv > a,
        #filterDrinksFormDiv > input::placeholder,
        #filterDrinksFormDiv > select {
            color:black;
        }
        #filterDrinksFormDiv > input,
        #filterDrinksFormDiv > select,option {
            background-color:white;
            border-color:black;
            margin: auto 10px;
        }
        a {
            color: black;
        }
        a:visited {
            color: rgb(128, 128, 128);
        }
        a:hover {
            color: rgb(128, 128, 128);
        }
        a:active {
            color: coral;
        }
        </style>
    </head>
    <g:set var="language" value="${user.language}"/>
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:if test="${darkMode}">
        <style>
            a, p {
                color: black;
            }
            a:visited {
                color: rgb(128, 128, 128);
            }
            a:hover {
                color: rgb(128, 128, 128);
            }
            a:active {
                color: coral;
            }
        </style>
    </g:if>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <a href="#list-authToken" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="list-authToken" class="col-12 content scaffold-list" role="main">
                    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <f:table collection="${authTokenList}" />

                    <g:if test="${authTokenCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${authTokenCount ?: 0}" />
                    </div>
                    </g:if>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>