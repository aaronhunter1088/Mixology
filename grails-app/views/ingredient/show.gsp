<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="enums.*; mixology.Drink; mixology.Ingredient;" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Show An Ingredient</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <g:set var="drink" value="${message(code: 'drink.label', default: 'Drink')}" />
        <g:set var="ingredient" scope="request" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
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
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <g:render template="ingredientNav"/>
                </section>
                <div id="show-ingredient" class="col-12 scaffold-show">
                    <h1>Show Ingredient</h1>
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
                    <div style="display:flex;justify-content:center;">
                        <div style="display:block;">
                            <fieldset style="border:thick solid #008011; width:100%;" class="no-before">
                                <legend style="margin-left:25px;width:auto;">
                                    ${ingredient.name} &emsp14;
                                    <hr style="height:1px;background-color:#008011">
                                </legend>
                                <div id="ingredient" style="width:100%;float:left;">
                                    <div class="formfield" id="name">
                                        <label for='ingredientName'>Ingredient Name</label>
                                        <div class="input-wrapper">
                                            <input type="text" disabled name="ingredientName" value="${ingredient.name}" required="" id="ingredientName" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="unit">
                                        <label for='ingredientUnit'>Ingredient Unit</label>
                                        <div class="input-wrapper">
                                            <input type="text" disabled name="ingredientUnit" value="${ingredient.unit}" required="" id="ingredientUnit" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="amount">
                                        <label for='ingredientAmount'>Ingredient Amount</label>
                                        <div class="input-wrapper">
                                            <input type="text" disabled name="ingredientAmount" value="${ingredient.amount}" required="" id="ingredientAmount" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="drinks">
                                        <label>Ingredient Drinks</label><br>
                                        <div style="margin-top:-25px;height:100px;overflow-y:auto;">
                                            <g:each in="${ingredient.drinks}" var="drink" status="i">
                                                <div style="display:block;">
                                                    <input hidden type="checkbox" disabled name="ingredients" id="" checked value="${drink}"/> ${drink}
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
                                        <g:if test="${ingredient.custom}">
                                            <g:link class="fa-solid fa-pen-to-square" action="edit" resource="${this.ingredient}">&nbsp;<g:message code="default.button.edit.label" default="Edit" /></g:link>
                                        %{--<g:link class="fa fa-solid fa-share" action="sendADrinkEmail" resource="${this.drink}">&nbsp;<g:message code="default.email.share" default="Share"/></g:link>--}%
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
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("show ingredient loaded");
            });
        </script>
    </body>
</html>
