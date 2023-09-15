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
                        <g:set var="action" value="${adminIsLoggedIn ? 'index' : 'showCustomIndex'}"/>
                        <g:form action="${action}" controller="drink" name="filterDrinks" method="get">
                            <input type="text" name="id" id="id" placeholder="id" value="" />
                            <input type="text" name="name" id="name" placeholder="name" value="" />
                            <input type="text" name="number" id="number" placeholder="number" value="" />
                            <input type="text" name="alcohol" id="alcohol" placeholder="alcohol" value="" />
                            <input type="text" name="glass" id="glass" placeholder="glass" value="" />
                            <label for="defaultDrink">Default Drink? </label>
                            <g:if test="${params.defaultDrink}">
                                <input type="checkbox" name="defaultDrink" id="defaultDrink" checked onclick="triggerCustomCheckbox();" />
                            </g:if><g:else>
                                <input type="checkbox" name="defaultDrink" id="defaultDrink" onclick="triggerCustomCheckbox();" />
                            </g:else>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <button style="margin-left:10px;" id="filterDrink" class="btn btn-primary" type="submit" form="filterDrinks">Filter</button>
                            </sec:ifAnyGranted>
                            <sec:ifAnyGranted roles="ROLE_USER">
                                <a style="margin-right:10px;" class="btn btn-primary" id="filter" href="${createLink(action:action, controller:drink)}"><g:message code="default.filter.label" default="Filter"/></a>
                            </sec:ifAnyGranted>
                        </g:form>
                        <g:if test="${drinkList.totalCount <= 0}">
                        <p>No default drinks found!</p>
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
                        <div class="pagination">
                            <g:paginate controller="drink"
                                        action="${params.action}"
                                        total="${drinkList.totalCount}"
                                        max="5"
                                        params="${params}"/>
                        </div>
                    </div>
                </section>
            </div>
        </div>
        <script type="text/javascript">
            function triggerCustomCheckbox() {
                let checkbox = $('#custom');
                let checked = checkbox.attr('checked');
                checked ? checkbox.attr('checked','checked') : checkbox.attr('checked','');
                console.log("custom checkbox ==> " + checked);
            }
        </script>
    </body>
</html>