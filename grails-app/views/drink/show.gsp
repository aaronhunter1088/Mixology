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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css">
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
            a.fa fa-clone::before {
                background-color: #efefef;
                overflow: hidden;
                padding: 0.3em;
                -moz-box-shadow: 0 0 3px 1px #aaaaaa;
                -webkit-box-shadow: 0 0 3px 1px #aaaaaa;
                box-shadow: 0 0 3px 1px #aaaaaa;
                margin: 0.1em 0 0 0;
                border: none;
                content: "\f24d  Copy"
            }
        </style>
    </head>
    <body>
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <a href="#show-drink" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                    <g:render template="drinkNav"/>
                </section>
                <div id="show-drink" class="col-12 scaffold-show">
                    <h1>Show Drink</h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.drink}">
                        <ul class="errors" role="alert">
                            <g:eachError bean="${this.drink}" var="error">
                                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                            </g:eachError>
                        </ul>
                    </g:hasErrors>
                    <div style="display:flex;justify-content:center;">
                        <div style="display:block;">
                            <fieldset style="border:thick solid #000080;" class="no-before">
                                <legend style="margin-left:25px;padding-left:10px;width:auto;">
                                    ${drink.drinkName} &emsp14;
                                    <hr style="height:1px;background-color:#000080">
                                </legend>
                                <div id="drink" style="width:100%;float:left;">
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
                                        <label><span class='required-indicator'>*</span> Ingredients</label><br/>
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
                                    <sec:ifLoggedIn>
                                        <g:link class="fa fa-clone" action="copy" resource="${this.drink}">&nbsp;&nbsp;<g:message code="default.button.copy.label" default="Copy"/></g:link>
                                        <g:if test="${drink.custom}">
                                            <g:link class="edit" action="edit" resource="${this.drink}"><g:message code="default.button.edit.label" default="Edit"/></g:link>
                                            <g:link class="fa fa-solid fa-share" action="sendADrinkEmail" resource="${this.drink}"><g:message code="default.email.share" default="Share"/></g:link>
                                            <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                        </g:if>
                                    </sec:ifLoggedIn>
                                    </fieldset>
                            </g:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("show drink loaded");
            });
        </script>
    </body>
</html>
