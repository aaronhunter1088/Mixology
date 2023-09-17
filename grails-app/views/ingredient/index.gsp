<%@ page import="enums.*; mixology.*;" %>
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
        <g:include view="base/includeAll.gsp"/>
    </head>
    <g:set var="drink" value="${message(code: 'drink.label', default: 'Drink')}" />
    <g:set var="ingredient" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
    <body>
    <div id="content">
        <div class="container">
            <section class="row" id="navigation">
                <g:render template="ingredientNav"/>
            </section>
            <section class="row">
                <div id="list-ingredient" class="col-12 content scaffold-list" role="main">
                    <h1><g:message code="default.list.label" args="[ingredient]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:if test="${ingredientCount <= 0}">
                        <p>No custom ingredients found!</p>
                    </g:if><g:else>
                        <table>
                            <thead>
                                <tr>
                                    <th>Count</th>
                                    <th>ID</th>
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
                                    <td>${ingredient.id}</td>
                                    <td><g:link controller="ingredient" action="show" params='[id:"${ingredient.id}"]'>${ingredient.name}</g:link> </td>
                                    <td>${ingredient.unit}</td>
                                    <td>${ingredient.amount}</td>
                                    <td>${ingredient.drinks}</td>
                                </tr>
                                <% index++; %>
                            </g:each>
                            </tbody>
                        </table>
                    </g:else>
                    <g:if test="${ingredientCount > max}">
                    <div class="pagination">
                        <g:paginate total="${ingredientCount}"
                                    controller="ingredient"
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