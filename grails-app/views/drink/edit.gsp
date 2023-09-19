<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Edit Drink</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
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
                content: "Use this form to update a drink";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
        </style>
    </head>
    <g:set var="drinkObj" value="${message(code: 'drink.label', default: 'Drink')}" />
    <body>
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <g:render template="../navigation"/>
                </section>
                <div id="edit-drink" class="col-12 scaffold-show">
%{--                    <h1>Edit Drink</h1>--}%
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
                    <fieldset style="border:thick solid #000080;">
                        <legend style="margin-left:25px;width:auto;">
                            &emsp14;<g:message code="default.edit.label" args="[drinkObj]" />&emsp14;
                            <hr style="height:1px;background-color:#000080">
                        </legend>
                        <g:set var="drink" scope="session"/>
                        <g:form resource="${this.drink}" method="put" name="updateDrink">
                            <input type="text" hidden name="version" value="${drink.version}"/>
                            <div id="update-drink1" style="width:50%;float:left;">
                                <div class="formfield">
                                    <label for='name'><span class='required-indicator'>*</span> Drink Name</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="name" value="${drink.name}" required="" id="name" />
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for='number'><span class='required-indicator'>*</span> Drink Number</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="number" value="${drink.number}" required="" id="number" />
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for='alcoholType'><span class='required-indicator'>*</span> Drink Type</label>
                                    <div class="input-wrapper">
                                        <select name="alcoholType" class="form-control" style="width:42%;">
                                            <option label="Select One" selected disabled>Select One</option>
                                            <g:each in="${Alcohol.values()}" var="alcohol" name="alcoholType">
                                                <g:if test="${drink.alcoholType == alcohol}">
                                                    <option value="${alcohol}" selected>${alcohol}</option>
                                                </g:if>
                                                <g:else>
                                                    <option value="${alcohol}">${alcohol}</option>
                                                </g:else>
                                            </g:each>
                                        </select>
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for='symbol'><span class='required-indicator'>*</span> Drink Symbol</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="symbol" value="${drink.symbol}" required="" id="symbol" />
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for="glass"><span class='required-indicator'>*</span> Suggested Glass</label>
                                    <div class="input-wrapper">
                                        <select name="glass" class="form-control" style="width:42%;">
                                            <option label="Select One" selected disabled>Select One</option>
                                            <g:each in="${GlassType.values()}" var="glass" name="glass">
                                                <g:if test="${drink.suggestedGlass == glass}">
                                                    <option value="${glass}" selected>${glass}</option>
                                                </g:if>
                                                <g:else>
                                                    <option value="${glass}">${glass}</option>
                                                </g:else>
                                            </g:each>
                                        </select>
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for='mixingInstructions'><span class='required-indicator'>*</span> Mixing Instructions</label>
                                    <div class="input-wrapper">
                                        <g:textArea form="updateDrink" name="mixingInstructions" value="${drink.mixingInstructions}" rows="5" cols="40"/>
                                    </div>
                                </div>
                            </div>
                            <div id="update-drink2" style="width:50%;display:block;float:right;">
                                <div class="formfield">
                                    <label style="text-align:right;padding-right:30px;"><span class='required-indicator'>*</span> Ingredients</label><br>
                                    <div style="margin-top:-25px;height:419px;overflow-y:auto;">
                                        <g:each in="${drink.ingredients.sort{it.id}}" var="ingredient" status="i">
                                            <g:if test="${drink.ingredients.contains(ingredient)}">
                                                <div style="display:block;">
                                                    <button id="addIngredientBtn${ingredient.id}" type="button" class="btn btn-success btn-xs" onclick="addIngredient('${ingredient.id}');">Added</button>
                                                    <button id="removeIngredientBtn${ingredient.id}" type="button" class="btn btn-outline-danger btn-xs" onclick="removeIngredient('${ingredient.id}');">Remove</button>
                                                    <input hidden type="checkbox" name="ingredients" checked id="ingredient${ingredient.id}" value="${ingredient.id}"/> ${ingredient.prettyNameWithoutName()} <g:link action="show" controller="ingredient" params='[id:"${ingredient.id}"]'>${ingredient.name}</g:link> : ${ingredient.id}
                                                </div>
                                            </g:if>
                                        </g:each>
                                        <g:each in="${ingredients}" var="ingredient" status="i">
                                            <div style="display:block;">
                                                <button id="addIngredientBtn${ingredient.id}" type="button" class="btn btn-outline-info btn-xs" onclick="addIngredient('${ingredient.id}');">Add</button>
                                                <button hidden id="removeIngredientBtn${ingredient.id}" type="button" class="btn btn-outline-danger btn-xs" onclick="removeIngredient('${ingredient.id}');">Remove</button>
                                                <input hidden type="checkbox" name="ingredients" id="ingredient${ingredient.id}" value="${ingredient.id}"/> ${ingredient.prettyNameWithoutName()} <g:link action="show" controller="ingredient" params='[id:"${ingredient.id}"]'>${ingredient.name}</g:link> : ${ingredient.id}
                                            </div>
                                        </g:each>
                                    </div>
                                </div>
                            </div>
                            <div class="formfield" style="margin-top:25px;text-align:center;">
                                <a style="margin-right:10px;" class="btn btn-outline-danger" id="cancel" href="${createLink(uri: "/drink/show/${drink.id}")}"><g:message code="default.cancel.label" default="Cancel"/></a>
                                <button style="margin-left:10px;" id="updateDrink" class="btn btn-outline-primary" type="submit" form="updateDrink">Update</button>
                            </div>
                        </g:form>
                    </fieldset>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("edit loaded");
            });
            function isValid() {
                console.log("isValid()");
                return true;
            }
            function addIngredient(ingredientId) {
                document.getElementById('ingredient'+ingredientId).checked = true;
                console.log('checkbox for ingredient'+ingredientId+ ' checked');
                document.getElementById('addIngredientBtn'+ingredientId).classList.remove('btn-outline-info');
                document.getElementById('addIngredientBtn'+ingredientId).classList.add('btn-success');
                document.getElementById('addIngredientBtn'+ingredientId).innerHTML = "Added";
                document.getElementById('removeIngredientBtn'+ingredientId).hidden = false;
            }
            function removeIngredient(ingredientId) {
                document.getElementById('ingredient'+ingredientId).checked = false;
                console.log('checkbox for ingredient'+ingredientId+ ' unchecked');
                document.getElementById('addIngredientBtn'+ingredientId).classList.remove('btn-success');
                document.getElementById('addIngredientBtn'+ingredientId).classList.add('btn-outline-info');
                document.getElementById('addIngredientBtn'+ingredientId).innerHTML = "Add";
                document.getElementById('removeIngredientBtn'+ingredientId).hidden = true;
            }
        </script>
    </body>
</html>