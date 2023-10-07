<%@ page import="enums.*; mixology.Drink; mixology.Ingredient;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Show An Ingredient</title>
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
            /*Keeps scroll bar present*/
            ::-webkit-scrollbar {
                -webkit-appearance: none;
                width: 0 !important
            }
        </style>
    </head>
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:if test="${darkMode}">
        <style>
            .input-wrapper > input,textarea {
                background-color:black;
                color:white;
            }
        </style>
    </g:if>
    <body style="padding:50px;background-color:${darkMode?'black':'white'};">
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="text-align:center;background-color:${darkMode?'black':'white'};">
                <div style="display:inline-flex;vertical-align:middle;">
                    <div id="navigation">
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
                                        <label for='ingredientName'>Ingredient Name</label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="ingredientName" value="${ingredient.name}" required="" id="ingredientName" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="unit">
                                        <label for='ingredientUnit'>Ingredient Unit</label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="ingredientUnit" value="${ingredient.unit}" required="" id="ingredientUnit" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="amount">
                                        <label for='ingredientAmount'>Ingredient Amount</label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="ingredientAmount" value="${ingredient.amount}" required="" id="ingredientAmount" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="drinks" style="">
                                        <label>Drinks</label><br>
                                        <div style="margin-top:-25px;height:100px;overflow-y:auto;text-align:right;">
                                            <g:each in="${ingredient.drinks.sort{ it.id }}" var="drink" status="i">
                                                <div style="display:block;">
                                                    <input hidden type="checkbox" disabled name="ingredients" id="" checked value="${drink}"/>
                                                    <g:link action="show" controller="drink" params='[id:"${drink.id}"]'>${drink}</g:link>
                                                </div>
                                            </g:each>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                            <g:form resource="${this.ingredient}" method="DELETE">
                                <fieldset class="buttons" style="width:100%;">
                                    <sec:ifLoggedIn>
                                        <g:link class="fa fa-clone" action="copy" resource="${this.ingredient}">&nbsp;<g:message code="default.button.copy.label" default="Copy"/></g:link>
                                        <g:if test="${ingredient.custom || role}">
                                            <g:link class="fa-solid fa-pen-to-square" action="edit" resource="${this.ingredient}">&nbsp;<g:message code="default.button.edit.label" default="Edit" /></g:link>
                                            <i class="fa-solid fa-trash-can">
                                                <input type="submit" value="&nbsp;${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                            </i>
                                        </g:if>
                                    </sec:ifLoggedIn>
                                </fieldset>
                            </g:form>
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
