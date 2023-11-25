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
    <%
        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        def userService = grailsApplication.mainContext.getBean('userService')
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
        def darkMode = user.darkMode
    %>
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:set var="drink" value="${message(code: 'drink.label', default: 'Drink')}" />
    <g:set var="ingredient" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />

    <g:if test="${darkMode}">
        <style>
            #filterDrinksFormDiv > a,
            #filterDrinksFormDiv > input::placeholder,
            #filterDrinksFormDiv > select {
                color:#e2e3e5;
            }
            #filterDrinksFormDiv > input,
            #filterDrinksFormDiv > select,option {
                background-color:black;
                border-color:white;
            }
            #drinksHeaderRow > *{
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
                <div id="list-drink">
                    <div style="display:inline-flex;vertical-align:middle;">
                        <div>
                            <g:render template="../navigation" model="[user:user]"/>
                        </div>
                        <div style="margin:auto;padding-top:10px;vertical-align:middle;">
                            <h1 style="color:${darkMode?'white':'black'};"><g:message code="default.list.label" args="[drink]" /></h1>
                        </div>
                    </div>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:set var="action" value="${adminIsLoggedIn ? 'index' : 'showCustomIndex'}"/>
                    <div id="list" style="text-align:center;">
                        <div id="filter" style="text-align:center;padding:10px;display:flex;justify-content:center;">
                            <g:form action="${params.action}" controller="drink" name="filterDrinks" method="get">
                                <div id="filterDrinksFormDiv" style="display:flex;">
                                    <label for="id"></label>
                                    <input type="text" name="id" id="id" placeholder="id" value="${params.id}" style="width:50px;text-align:center;" class="form-control" />
                                    <label for="name"></label>
                                    <input type="text" name="name" id="name" placeholder="name" value="${params.name}" style="text-align:center;" class="form-control" />
                                    <label for="number"></label>
                                    <input type="text" name="number" id="number" placeholder="number" value="${params.number}" style="width:100px;text-align:center;" class="form-control" />
                                    <label for="alcoholSelect"></label>
                                    <select id="alcoholSelect" name="alcohol" style="width:120px;text-align:center;" class="form-control">
                                        <option label="alcohols" <g:if test="${!params.alcohol}">selected</g:if> disabled>alcohols</option>
                                        <g:each in="${Alcohol.values()}" var="alcohol">
                                            <option value="${alcohol}" <g:if test="${(params.alcohol as String) == alcohol.alcoholName.toUpperCase()}">selected</g:if>>${alcohol}</option>
                                        </g:each>
                                    </select>
                                    <label for="glassSelect"></label>
                                    <select id="glassSelect" name="glass" style="text-align:center;" class="form-control">
                                        <option label="glasses" selected disabled>glasses</option>
                                        <g:each in="${GlassType.values()}" var="glass">
                                            <option value="${glass}">${glass}</option>
                                        </g:each>
                                    </select>
                                    <g:if test="${adminIsLoggedIn}">
                                        <label style="color:${darkMode?'white':'black'};margin: auto 10px;" for="defaultDrink">Default Drink? </label>
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
                        <g:if test="${drinkCount <= 0}">
                            <g:if test="${customDrinks}">
                                <p>No custom drinks found!</p>
                            </g:if><g:elseif test="${isOn(params.defaultDrink as String)}">
                                <p>No default drinks found!</p>
                            </g:elseif><g:else>
                                <p>No drinks found!</p>
                            </g:else>
                        </g:if>
                        <g:else>
                            <table id="drinksTable">
                                <thead>
                                    <tr id="drinksHeaderRow">
                                        <th>Count (${drinkCount})</th>
                                        <th>Drink Name</th>
                                        <th>Drink Symbol</th>
                                        <th>Drink Number</th>
                                        <th>Alcohol Type</th>
                                        <th>Ingredients</th>
                                        <th>Suggested Glass</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <g:if test="${drinkCount > 0}">
                                    <% int index = 1 %>
                                    <g:each in="${drinkList}" var="drink">
                                        <g:if test="${params.offset && (params.offset as int) != 0}">
                                            <g:set var="idx" value="${index + (params.offset as int)}"/>
                                        </g:if><g:else>
                                        <g:set var="idx" value="${index}"/>
                                    </g:else>
                                        <tr style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                            <td><g:link controller="drink" action="show" params='[id:"${drink.id}"]'>${idx}</g:link></td>
                                            <td>${drink.name}</td>
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
                            <div class="pagination">
                                <g:paginate controller="drink"
                                            action="${params.action}"
                                            total="${drinkCount}"
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