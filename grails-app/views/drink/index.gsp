<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Display All Drinks</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <g:set var="drink" value="${message(code: 'drink.label', default: 'Drink')}" />
        <g:set var="ingredient" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    </head>
    <body>
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <g:render template="drinkNav"/>
                </section>
                <section class="row">
                    <div id="list-drink" class="col-12 content scaffold-list">
                        <h1><g:message code="default.list.label" args="[drink]" /></h1>
                        <g:if test="${flash.message}">
                            <div class="message" role="status">${flash.message}</div>
                        </g:if>
                        <g:if test="${drinkCount <= 0}">
                            <p>No custom drinks found!</p>
                        </g:if><g:else>
                            <table>
                                <thead>
                            <tr>
                                <th>Count</th>
                                <th>ID</th>
                                <th>Drink Name</th>
                                <th>Drink Symbol</th>
                                <th>Drink Number</th>
                                <th>Alcohol Type</th>
                                <th>Ingredients</th>
                                <th>Suggested Glass</th>
                            </tr>
                            </thead>
                                <tbody>
                                <g:if test="${drinkList.size() > 0}">
                                    <% int index = 1; %>
                                    <g:each in="${drinkList}" var="drink">
                                        <g:if test="${params.offset && (params.offset as int) != 0}">
                                            <g:set var="idx" value="${index + (params.offset as int)}"/>
                                        </g:if><g:else>
                                        <g:set var="idx" value="${index}"/>
                                    </g:else>
                                        <tr>
                                            <td>${idx}</td>
                                            <td>${drink.id}</td>
                                            <td><g:link controller="drink" action="show" params='[id:"${drink.id}"]'>${drink.name}</g:link> </td>
                                            <td>${drink.symbol}</td>
                                            <td>${drink.number}</td>
                                            <td>${drink.alcoholType}</td>
                                            <td>${(drink.ingredients as List).sort(false, {d1, d2 -> d1.id <=> d2.id })}</td>
                                            <td>${drink.suggestedGlass}</td>
                                        </tr>
                                        <% index++; %>
                                    </g:each>
                                </g:if>
                                </tbody>
                            </table>
                        </g:else>
                        <g:if test="${drinkCount > max}">
                        <div class="pagination">
                            <g:paginate total="${drinkCount}"
                                        controller="drink"
                                        action="${params.action}"
                                        max="${params.max ?: 10}" />
                        </div>
                        </g:if>
                    </div>
                </section>
            </div>
        </div>
    </body>
</html>