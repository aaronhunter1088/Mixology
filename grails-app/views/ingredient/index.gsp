<%@ page import="enums.*; mixology.*; static mixology.DrinkController.isOn;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><g:message code="ingredient.index.display.all.ingredients" default="Display all ingredients"/></title>
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
            #filterIngredientsFormDiv > a,
            #filterIngredientsFormDiv > input::placeholder,
            #filterIngredientsFormDiv > select {
                color:black;
            }
            #filterIngredientsFormDiv > input,
            #filterIngredientsFormDiv > select,option {
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
    <%
        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        def userService = grailsApplication.mainContext.getBean('userService')
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def darkMode = user.darkMode
    %>
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:set var="ingredient" value="${message(code: 'ingredient.label', args:'', default: 'Ingredient')}" />
    <g:set var="ingredients" value="${message(code: 'ingredient.label', args:'s', default: 'Ingredients')}" />
    <g:if test="${darkMode}">
        <style>
            #filterIngredientsFormDiv > a,
            #filterIngredientsFormDiv > input::placeholder,
            #filterIngredientsFormDiv > select {
                color:#e2e3e5;
            }
            #filterIngredientsFormDiv > input,
            #filterIngredientsFormDiv > select,option {
                background-color:black;
                border-color:white;
            }
            #ingredientsHeaderRow > *{
                color:black;
                background-color: rgb(128, 128, 128) !important;
            }
            a, p {
                color: white;
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
    <body style="padding:50px;margin:0;background-color:${darkMode?'black':'white'};">
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="text-align:center;background-color:${darkMode?'black':'white'};">
                <div id="list-ingredient">
                    <div style="display:inline-flex;vertical-align:middle;">
                        <div id="navigation">
                            <g:render template="../navigation" model="[user:user]"/>
                        </div>
                    </div>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <div id="list" style="text-align:center;">
                        <div id="filter" style="text-align:center;padding:10px;display:flex;justify-content:center;">
                            <h1 style="color:${darkMode?'white':'black'};"><g:message code="default.list.label" args="[ingredients]" /></h1>
                            <g:form action="${params.action}" controller="ingredient" name="filterIngredients" method="get">
                                <div id="filterIngredientsFormDiv" style="display:flex;">
                                    <label for="id"></label>
                                    <input type="text" name="id" id="id" placeholder="${g.message(code:'ingredient.search.id', default:'id')}" value="${params.id}" style="width:50px;text-align:center;" class="form-control" />
                                    <label for="name"></label>
                                    <input type="text" name="name" id="name" placeholder="${g.message(code:'ingredient.search.name', default:'ingredient name')}" value="${params.name}" style="text-align:center;" class="form-control" />
                                    <label for="unit"></label>
                                    <select id="unit" name="unit" style="width:100px;text-align:center;" class="form-control">
                                        <option label="${g.message(code:"ingredient.search.unit", default:"units")}" <g:if test="${!params.unit}">selected</g:if> disabled><g:message code="ingredient.search.unit" default="units"/></option>
                                        <g:each in="${Unit.values()}" var="unit">
                                            <option value="${unit}" <g:if test="${(params.unit as String) == unit.value.toUpperCase()}">selected</g:if>>${unit}</option>
                                        </g:each>
                                    </select>
                                    <label for="amount"></label>
                                    <input type="text" name="amount" id="amount" placeholder="${g.message(code:'ingredient.search.amount', default:'amount')}" value="${params.amount}" style="text-align:center;width:100px;" class="form-control" />
                                    <g:if test="${!customIngredients}">
                                        <label style="color:${darkMode?'white':'black'};margin: auto 10px;" for="defaultIngredient"><g:message code="ingredient.search.default" default="Default Ingredient?"/></label>
                                        <input type="checkbox" name="defaultIngredient" id="defaultIngredient"
                                               <g:if test="${params.defaultIngredient && isOn(params.defaultIngredient as String)}">checked="checked"</g:if>
                                               onclick="triggerCustomCheckbox();" />
                                    </g:if>
                                    <button style="margin: auto 10px;" id="filterIngredientBtn" class="btn btn-primary btn-xs" type="submit" form="filterIngredients"><g:message code="default.button.filter.label" default="Filter"/></button>
                                    <g:link class="btn btn-outline-primary btn-xs" controller="ingredient" action="${params.action}" style="text-align:center;margin-top:auto;margin-bottom:auto;"><g:message code="default.button.clear.label" default="Clear"/></g:link>
                                </div>
                            </g:form>
                        </div>
                        <p></p>
                        <g:if test="${ingredientCount <= 0}">
                            <g:if test="${customIngredients}">
                                <p>No custom ingredients found!</p>
                            </g:if><g:elseif test="${isOn(params.defaultIngredient as String)}">
                            <p>No default ingredients found!</p>
                            </g:elseif><g:else>
                                <p>No ingredients found!</p>
                            </g:else>
                        </g:if>
                        <g:else>
                            <table id="ingredientsTable">
                                <thead>
                                <tr id="ingredientsHeaderRow">
                                    <th><g:message code="ingredient.index.count" default="Count"/> (${ingredientCount})</th>
                                    <th><g:message code="ingredient.index.name" default="Name"/></th>
                                    <th><g:message code="ingredient.index.unit" default="Unit"/></th>
                                    <th><g:message code="ingredient.index.amount" default="Amount"/></th>
                                    <th><g:message code="ingredient.index.drinks" default="Drinks"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:if test="${ingredientCount > 0}">
                                    <% int index = 1 %>
                                    <g:each in="${ingredientList}" var="ingredient">
                                        <g:if test="${params.offset && (params.offset as int) != 0}">
                                            <g:set var="idx" value="${index + (params.offset as int)}"/>
                                        </g:if><g:else>
                                        <g:set var="idx" value="${index}"/>
                                    </g:else>
                                        <tr style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                            <td><g:link controller="ingredient" action="show" params='[id:"${((Ingredient)ingredient).id}"]'>${idx}</g:link></td>
                                            <td>${((Ingredient)ingredient).name}</td>
                                            <td>${((Ingredient)ingredient).unit}</td>
                                            <td>${((Ingredient)ingredient).amount}</td>
                                            <td>${((Ingredient)ingredient).drinks}</td>
                                        </tr>
                                        <% index++; %>
                                    </g:each>
                                </g:if>
                                </tbody>
                            </table>
                            <div class="pagination">
                                <g:paginate controller="ingredient"
                                            action="${params.action}"
                                            total="${ingredientCount}"
                                            max="5"
                                            params="${params}" />
                                <script type="text/javascript">
                                    let paginationLinks = $(".pagination").find('a')
                                    <g:if test="${darkMode}">
                                    paginationLinks.each( function () {
                                        $(this).css('color', 'black');
                                    })
                                    </g:if><g:else>
                                    paginationLinks.each( function () {
                                        $(this).css('color', 'black');
                                    })
                                    </g:else>
                                </script>
                            </div>
                        </g:else>
                    </div>
                </div>
            </section>
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