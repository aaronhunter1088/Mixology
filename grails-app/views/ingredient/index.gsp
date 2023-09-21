<%@ page import="enums.*; mixology.*; static mixology.DrinkController.isOn;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Display All Ingredients</title>
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
            #filterIngredientsFormDiv input,select {
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
                    <div id="list-ingredient" class="col-12 content scaffold-list">
                        <h1><g:message code="default.list.label" args="[ingredient]" /></h1>
                        <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                        </g:if>

                        <g:if test="${ingredientCount <= 0}">
                            <g:if test="${customIngredients}">
                                <p>No custom ingredients found!</p>
                            </g:if><g:else>
                                <p>No default ingredients found!</p>
                            </g:else>
                        </g:if><g:else>
                            <g:set var="action" value="${adminIsLoggedIn ? 'index' : 'customIndex'}"/>
                            <div id="filter" style="text-align:center;width:auto;display:flex;justify-content:center;">
                                <g:form action="${action}" controller="ingredient" name="filterIngredients" method="get">
                                    <div id="filterIngredientsFormDiv" style="display:flex;">
                                        <label for="id"></label>
                                        <input type="text" name="id" id="id" placeholder="id" value="${params.id}" style="width:50px;text-align:center;" class="form-control" />
                                        <label for="name"></label>
                                        <input type="text" name="name" id="name" placeholder="ingredient name" value="${params.name}" style="text-align:center;width:200px;" class="form-control" />
                                        <label for="unit"></label>
                                        <select id="unit" name="unit" style="width:100px;text-align:center;" class="form-control">
                                            <option label="Units" <g:if test="${!params.unit}">selected</g:if> disabled>Units</option>
                                            <g:each in="${Unit.values()}" var="unit">
                                                <option value="${unit}" <g:if test="${(params.unit as String) == unit.value.toUpperCase()}">selected</g:if>>${unit}</option>
                                            </g:each>
                                        </select>
                                        <label for="amount"></label>
                                        <input type="text" name="amount" id="amount" placeholder="amount" value="${params.amount}" style="text-align:center;width:200px;" class="form-control" />
                                        <g:if test="${!customIngredients}">
                                            <label style="margin: auto 10px;" for="defaultIngredient">Default Ingredient? </label>
                                            <input type="checkbox" name="defaultIngredient" id="defaultIngredient"
                                                   <g:if test="${params.defaultIngredient && isOn(params.defaultIngredient as String)}">checked="checked"</g:if>
                                                   onclick="triggerCustomCheckbox();" />
                                        </g:if>
                                        <button style="margin: auto 10px;" id="filterIngredientBtn" class="btn btn-primary btn-xs" type="submit" form="filterIngredients">Filter</button>
                                        <g:link class="btn btn-outline-primary btn-xs" controller="ingredient" action="${params.action}" style="text-align:center;margin-top:auto;margin-bottom:auto;">Clear</g:link>
                                    </div>
                                </g:form>
                            </div>
                            <p></p>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Count</th>
    %{--                                    <th>ID</th>--}%
                                        <th>Name</th>
                                        <th>Unit</th>
                                        <th>Amount</th>
                                        <th>Drinks</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% int index = 1; %>
                                    <g:each in="${ingredientList}" var="ingredient">
                                    <g:if test="${params.offset && (params.offset as int) != 0}">
                                        <g:set var="idx" value="${index + (params.offset as int)}"/>
                                    </g:if><g:else>
                                    <g:set var="idx" value="${index}"/>
                                </g:else>
                                    <tr>
                                        <td>${idx}</td>
    %{--                                    <td>${ingredient.id}</td>--}%
                                        <td><g:link controller="ingredient" action="show" params='[id:"${ingredient.id}"]'>${ingredient.name}</g:link> </td>
                                        <td>${ingredient.unit}</td>
                                        <td>${ingredient.amount}</td>
                                        <td>${ingredient.drinks}</td>
                                    </tr>
                                    <% index++; %>
                                </g:each>
                                </tbody>
                            </table>
                            <div class="pagination">
                            <g:paginate controller="ingredient"
                                        action="${params.action}"
                                        total="${ingredientCount}"
                                        max="5"
                                        params="${params}"/>
                            </div>
                        </g:else>
                    </div>
                </section>
            </div>
        </div>
        <script type="text/javascript">
            function triggerCustomCheckbox() {
                let checkbox = $('#defaultIngredient');
                let checked = checkbox.attr('checked');
                console.log("checked: " + checked);
                if (checked === undefined) { checkbox.attr('checked','checked'); checked = true; }
                else { checkbox.attr('checked',''); checked = false; }
                console.log("custom checkbox ==> " + checked);
            }
        </script>
    </body>
</html>