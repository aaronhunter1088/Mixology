<%@ page import="enums.*; mixology.*; static mixology.DrinkController.isOn;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Display All Drinks</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <style>
            .btn-xs {
                padding: 1px 5px !important;
                font-size: 12px !important;
                line-height: 1.5 !important;
                border-radius: 3px !important;
            }
            #filterDrinksFormDiv input,select {
                margin: auto 10px;
            }
        </style>
    </head>
    <g:set var="drink" value="${message(code: 'drink.label', default: 'Drink')}" />
    <g:set var="ingredient" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
    <body>
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <g:render template="../navigation"/>
                </section>
                <section class="row">
                    <div id="list-drink" class="col-12 content scaffold-list">
                        <h1><g:message code="default.list.label" args="[drink]" /></h1>
                        <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                        </g:if>

                        <g:if test="${drinkCount <= 0}">
                            <g:if test="${customDrinks}">
                            <p>No custom drinks found!</p>
                            </g:if><g:else>
                            <p>No default drinks found!</p>
                            </g:else>
                        </g:if><g:else>
                            <g:set var="action" value="${adminIsLoggedIn ? 'index' : 'showCustomIndex'}"/>
                            <div id="filter" style="text-align:center;width:auto;display:flex;justify-content:center;">
                                <g:form action="${action}" controller="drink" name="filterDrinks" method="get">
                                    <div id="filterDrinksFormDiv" style="display:flex;">
                                        <label for="id"></label>
                                        <input type="text" name="id" id="id" placeholder="id" value="${params.id}" style="width:50px;text-align:center;" class="form-control" />
                                        <label for="name"></label>
                                        <input type="text" name="name" id="name" placeholder="name" value="${params.name}" style="text-align:center;" class="form-control" />
                                        <label for="number"></label>
                                        <input type="text" name="number" id="number" placeholder="number" value="${params.number}" style="width:100px;text-align:center;" class="form-control" />
                                        <label for="alcoholSelect"></label>
                                        <select id="alcoholSelect" name="alcohol" style="width:100px;text-align:center;" class="form-control">
                                            <option label="Alcohols" <g:if test="${!params.alcohol}">selected</g:if> disabled>Alcohols</option>
                                            <g:each in="${Alcohol.values()}" var="alcohol">
                                                <option value="${alcohol}" <g:if test="${(params.alcohol as String) == alcohol.alcoholName.toUpperCase()}">selected</g:if>>${alcohol}</option>
                                            </g:each>
                                        </select>
                                        <label for="glassSelect"></label>
                                        <select id="glassSelect" name="glass" style="text-align:center;" class="form-control">
                                            <option label="Glasses" selected disabled>Glasses</option>
                                            <g:each in="${GlassType.values()}" var="glass">
                                                <option value="${glass}">${glass}</option>
                                            </g:each>
                                        </select>
                                        <g:if test="${!customDrinks}">
                                            <label style="margin: auto 10px;" for="defaultDrink">Default Drink? </label>
                                            <input type="checkbox" name="defaultDrink" id="defaultDrink"
                                                   <g:if test="${params.defaultDrink && isOn(params.defaultDrink as String)}">checked="checked"</g:if>
                                                   onclick="triggerCustomCheckbox();" />
                                        </g:if>
                                        <button style="margin: auto 10px;" id="filterDrinkBtn" class="btn btn-primary btn-xs" type="submit" form="filterDrinks">Filter</button>
                                        <g:link action="${params.action}" controller="drink" class="btn btn-outline-primary btn-xs" style="text-align:center;margin-top:auto;margin-bottom:auto;">Clear</g:link>
                                    </div>
                                </g:form>
                            </div>
                            <p></p>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Count</th>
%{--                                        <th>ID</th>--}%
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
                                <% int index = 1 %>
                                <g:each in="${drinkList}" var="drink">
                                    <g:if test="${params.offset && (params.offset as int) != 0}">
                                        <g:set var="idx" value="${index + (params.offset as int)}"/>
                                    </g:if><g:else>
                                    <g:set var="idx" value="${index}"/>
                                </g:else>
                                    <tr>
                                        <td>${idx}</td>
%{--                                        <td>${drink.id}</td>--}%
                                        <td><g:link controller="drink" action="show" params='[id:"${drink.id}"]'>${drink.name}</g:link> </td>
                                        <td>${drink.symbol}</td>
                                        <td>${drink.number}</td>
                                        <td>${drink.alcoholType}</td>
                                        <td>${(drink.ingredients as List).sort(false, {d1, d2 -> d1.id <=> d2.id })}</td>
                                        <td>${drink.suggestedGlass}</td>
                                    </tr>
                                    <% index += 1 %>
                                </g:each>
                                </g:if>
                                </tbody>
                            </table>
                            <div class="pagination">
                            <g:paginate controller="drink"
                                        action="${params.action}"
                                        total="${drinkCount}"
                                        max="5"
                                        params="${params}" />
                            </div>
                        </g:else>
                    </div>
                </section>
            </div>
        </div>
        <script type="text/javascript">
            function triggerCustomCheckbox() {
                let checkbox = $('#defaultDrink');
                let checked = checkbox.attr('checked');
                console.log("checked: " + checked);
                if (checked === undefined) { checkbox.attr('checked','checked'); checked = true; }
                else { checkbox.attr('checked',''); checked = false; }
                console.log("custom checkbox ==> " + checked);
            }
        </script>
    </body>
</html>