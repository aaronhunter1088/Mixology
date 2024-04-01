<%@ page import="enums.*; mixology.Drink; mixology.Ingredient;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><g:message code="ingredient.show.an.ingredient" default="Show An Ingredient"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <style>
            .formfield {
                display: table;
                width: 400px;
                padding-bottom: 25px;
                padding-right: 10px;
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
            .btn-xs {
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
            /*These two webkit scrollbars control size and color*/
            ::-webkit-scrollbar {
                -webkit-appearance:none;
                width: 5px;
            }
            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgb(128, 128, 128);
                box-shadow: 0 0 1px rgba(255, 255, 255, .5);
            }
        </style>
    </head>
    <g:set var="language" value="${user?.language ? user.language : (params.lang) ? params.lang : 'en'}"/>
    <g:set var="darkMode" value="${user?.darkMode ?: (params.darkMode=='true') ?: false}"/>
    <g:if test="${darkMode}">
        <style>
            .input-wrapper > input,textarea {
                background-color:black;
                color:white;
            }
            .buttons {
                background-color: black;
                overflow: hidden;
                padding: 0.3em;
                -moz-box-shadow: 0 0 3px 1px black;
                -webkit-box-shadow: 0 0 3px 1px black;
                box-shadow: 0 0 3px 1px black;
                margin: 0.1em 0 0 0;
                border: none;
            }
            .buttons > a {
                color:ghostwhite;
            }
        </style>
    </g:if>
    <body style="padding:50px;background-color:${darkMode?'black':'white'};">
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="background-color:${darkMode?'black':'white'};">
                <div class="container">
                    <div id="navigation" style="display:flex;justify-content:center;">
                        <g:render template="../navigation" model="[user:user]"/>
                    </div>
                </div>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.ingredient}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.ingredient}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>
                <div id="show-ingredient" class="container">
                    <div style="display:flex;justify-content:center;">
                        <div style="display:block;text-align:left;">
                            <fieldset style="border:thick solid #008011; width:100%;" class="no-before">
                                <legend style="margin-left:25px;width:auto;color:${darkMode?'white':'black'};">
                                    ${ingredient.name} &emsp14;
                                    <hr style="height:1px;background-color:#008011">
                                </legend>
                                <div id="ingredient" style="width:100%;float:left;color:${darkMode?'white':'black'};">
                                    <div class="formfield" id="name">
                                        <label for='ingredientName'><g:message code="ingredient.show.name" default="Ingredient Name"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="ingredientName" value="${ingredient.name}" required="" id="ingredientName" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="unit">
                                        <label for='ingredientUnit'><g:message code="ingredient.show.unit" default="Ingredient Unit"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="ingredientUnit" value="${ingredient.unit}" required="" id="ingredientUnit" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="amount">
                                        <label for='ingredientAmount'><g:message code="ingredient.show.amount" default="Ingredient Amount"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="ingredientAmount" value="${ingredient.amount}" required="" id="ingredientAmount" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="drinks" style="">
                                        <g:if test="${!ingredient.drinks || ingredient.drinks.size() == 0}">
                                            <label><g:message code="ingredient.show.no.drinks" default="No Drinks"/></label>
                                            <p style="text-align:right;"><g:message code="ingredient.show.no.drinks.help" default="Once you add this ingredient to a drink, they'll show here"/></p>
                                        </g:if><g:else>
                                            <label><g:message code="ingredient.show.drinks" default="Drinks"/></label><br>
                                            <div style="margin-top:-25px;height:100px;overflow-y:auto;text-align:right;">
                                                <g:each in="${ingredient.drinks.sort{ it.id }}" var="drink" status="i">
                                                    <div style="display:block;">
                                                        <input hidden type="checkbox" disabled name="ingredients" id="" checked value="${drink}"/>
                                                        <g:link action="show" controller="drink" params='[id:"${drink.id}",lang:language,darkMode:darkMode]'>${drink}</g:link>
                                                    </div>
                                                </g:each>
                                            </div>
                                        </g:else>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset class="buttons" style="text-align:left;width:100%;display:inline-flex;">
                                <sec:ifLoggedIn>
                                    <g:link class="fa fa-clone" action="copy" resource="${this.ingredient}">&nbsp;<g:message code="default.button.copy.label" default="Copy"/></g:link>
                                    <g:if test="${ingredient.custom || role}">
                                        <g:link class="fa-solid fa-pen-to-square" action="edit" resource="${this.ingredient}">&nbsp;<g:message code="default.button.edit.label" default="Edit" /></g:link>
                                    </g:if>
                                    <g:if test="${ingredient.canBeDeleted}">
                                        <div style="display:block;">
                                            <g:form controller="ingredient" action="delete" method="DELETE">
                                                <input type="hidden" name="id" value="${ingredient.id}">
                                                <button class="fa-solid fa-trash-can btn btn-danger" type="submit"
                                                        onclick="return confirm('${message(code:'default.button.delete.confirm.message', default:'Are you sure?')}');">&nbsp;<g:message code="default.button.delete.label" default="Delete"/></button>
                                            %{--<g:link class="fa-solid fa-trash-can" action="delete" resource="${this.ingredient}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">&nbsp;<g:message code="default.button.delete.label" default="Delete"/></g:link>--}%
                                            </g:form>
                                        </div>
                                    </g:if>
                                </sec:ifLoggedIn>
                            </fieldset>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("show ingredient loaded");
            });
        </script>
    </body>
</html>
