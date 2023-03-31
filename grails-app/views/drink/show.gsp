<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="enums.*; mixology.Drink; mixology.Ingredient;" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Show A Drink</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <g:set var="drink" scope="request" value="${message(code: 'drink.label', default: 'Drink')}" />
        <g:set var="ingredient" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <style>
            .formfield {
                display: table;
                width: 100%;
                padding-bottom: 25px;
            }
            .formfield label,
            .formfield .input-wrapper {
                display: table-cell;
                vertical-align: top;
            }
            .formfield label {
                width: 150px;
                padding-left: 10px;
            }
            .formfield .input-wrapper .table-form {
                width: 100%;
            }
            .btn-xs
            {
                padding: 1px 5px !important;
                font-size: 12px !important;
                line-height: 1.5 !important;
                border-radius: 3px !important;
            }
            hr {
                margin-bottom: 10px;
            }
            fieldset {
                position: relative;
            }
            fieldset::before {
                content: "";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
        </style>
    </head>
    <body>
        <div id="content" role="main">
            <div class="container">
                <section class="row">
                    <a href="#show-drink" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                    <div class="nav" role="navigation">
                        <ul>
                            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                            <li><g:link class="list" action="index">Drink List</g:link></li>
                            <li><g:link class="create" action="create">New Drink</g:link></li>
                            <li><g:link class="create" controller="ingredient" action="create">New Ingredient</g:link></li>
                        </ul>
                    </div>
                </section>
                <section class="row">
                    <div id="show-drink" class="col-12 scaffold-show" role="main">
    %{--                    <h1><g:message code="default.show.label" args="[drink]" /></h1> Show <drinkName> ... wrong --}%
                        <h1>Show Drink</h1>
                        <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                        </g:if>
    %{--                    <f:display bean="drink" />--}%
                        <fieldset style="border:thick solid #000080;">
                            <legend style="margin-left:25px;padding-left:10px;width:auto;">
    %{--                            &emsp14;<g:message code="default.show.label" args="[drink]" />&emsp14;--}%
                                ${drink.drinkName} &emsp14;
                                <hr style="height:1px;background-color:#000080">
                            </legend>
                            <div id="drink" style="width:50%;float:left;">
                                <div class="formfield" id="name">
                                    <label for='drinkName'><span class='required-indicator'>*</span> Drink Name</label>
                                    <div class="input-wrapper">
                                        <input type="text" disabled name="drinkName" value="${drink.drinkName}" required="" id="drinkName" />
                                    </div>
                                </div>
                                <div class="formfield" id="number">
                                    <label for='drinkNumber'><span class='required-indicator'>*</span> Drink Number</label>
                                    <div class="input-wrapper">
                                        <input type="text" disabled name="drinkNumber" value="${drink.drinkNumber}" required="" id="drinkNumber" />
                                    </div>
                                </div>
                                <div class="formfield" id="alcohol">
                                    <label for='alcoholType'><span class='required-indicator'>*</span> Drink Type</label>
                                    <div class="input-wrapper">
                                        <input type="text" disabled name="alcoholType" value="${drink.alcoholType}" required="" id="alcoholType" />
                                    </div>
                                </div>
                                <div class="formfield" id="symbol">
                                    <label for='drinkSymbol'><span class='required-indicator'>*</span> Drink Symbol</label>
                                    <div class="input-wrapper">
                                        <input type="text" disabled name="drinkSymbol" value="${drink.drinkSymbol}" required="" id="drinkSymbol" />
                                    </div>
                                </div>
                                <div class="formfield" id="glass">
                                    <label for="glassType"><span class='required-indicator'>*</span> Suggested Glass</label>
                                    <div class="input-wrapper">
                                        <input type="text" disabled name="drinkSymbol" value="${drink.suggestedGlass}" required="" id="glassType" />
                                    </div>
                                </div>
                                <div class="formfield" id="instructions">
                                    <label for='instructions'><span class='required-indicator'>*</span> Mixing Instructions</label>
                                    <div class="input-wrapper">
                                        <textarea disabled readonly name="instructions" rows="5" cols="40">${drink.mixingInstructions}</textarea>
                                    </div>
                                </div>
                                <div class="formfield" id="ingredients">
                                    <label><span class='required-indicator'>*</span> Ingredients</label><br>
                                    <div style="margin-top:-25px;height:100px;overflow-y:auto;">
                                        <g:each in="${Ingredient.list(sort: ['amount':'asc','name':'asc'])}" var="ingredient" status="i">
                                            <g:if test="${drink.ingredients.contains(ingredient)}">
                                                <div style="display:block;">
                                                    <input hidden type="checkbox" disabled name="ingredients" id="ingredient${ingredient.id}" checked value="${ingredient}"/> ${ingredient} &emsp14;
                                                </div>
                                            </g:if>
                                        </g:each>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                        <g:form resource="${this.drink}" method="DELETE">
                            <fieldset class="buttons">
                                <g:link class="edit" action="edit" resource="${this.drink}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                                <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                            </fieldset>
                        </g:form>
                    </div>
                </section>
            </div>
        </div>
    </body>
</html>
