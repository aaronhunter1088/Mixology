<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="enums.*; mixology.Drink; mixology.Ingredient;" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Edit An Ingredient</title>
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
                content: "Use this form to update an ingredient";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
        </style>
    </head>
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:if test="${darkMode}">
        <style>
        fieldset::before {
            background: #000000;
            color: #fff;
        }
        .input-wrapper > select,option {
            background-color:black;
            border-color:white;
            color:white;
        }
        .input-wrapper > input,textarea {
            background-color:black;
            color:white;
        }
        </style>
    </g:if>
    <body style="padding:50px;background-color:${darkMode?'black':'white'};">
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="text-align:center;background-color:${darkMode?'black':'white'};">
                <div style="display:inline-flex;text-align:center;">
                    <div id="navigation">
                        <g:render template="../navigation"/>
                    </div>
                    <div id="header" style="margin:auto;padding-top:10px;vertical-align:middle;">
                        <h1 style="color:${darkMode?'white':'black'};"><g:message code="default.edit.label" args="[ingredient]" /></h1>
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
                <div style="display:inline-flex;text-align:left;">
                    <div id="edit-ingredient" class="col-12 scaffold-show">
                        <fieldset style="border:thick solid #008011;">
                            <legend style="margin-left:25px;width:auto;color:${darkMode?'white':'black'};">
                                &emsp14;<g:message code="default.edit.label" args="[ingredient]" />&emsp14;
                                <hr style="height:1px;background-color:#008011;">
                            </legend>
                            <g:form resource="${this.ingredient}" method="put" name="updateIngredient">
                                <div id="update-ingredient1" style="width:50%;float:left;color:${darkMode?'white':'black'};">
                                    <div class="formfield">
                                        <label for='name'><span class='required-indicator'>*</span> Ingredient Name</label>
                                        <div class="input-wrapper">
                                            <input type="text" name="name" value="${ingredient.name}" required="" id="name" />
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='unit'><span class='required-indicator'>*</span> Unit</label>
                                        <div class="input-wrapper">
                                            <select name="alcoholType" class="form-control" style="width:42%;">
                                                <option label="Select One" selected disabled>Select One</option>
                                                <g:each in="${Unit.values()}" var="unit" name="unit">
                                                    <g:if test="${ingredient.unit == unit}">
                                                        <option value="${unit}" selected>${unit}</option>
                                                    </g:if>
                                                    <g:else>
                                                        <option value="${unit}">${unit}</option>
                                                    </g:else>
                                                </g:each>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='amount'><span class='required-indicator'>*</span> Amount</label>
                                        <div class="input-wrapper">
                                            <input type="text" name="amount" value="${ingredient.amount}" required="" id="amount" />
                                        </div>
                                    </div>
                                    <div style="display:inline-flex;">
                                        <div class="formfield">
                                            <label for='custom'>Default Ingredient?&nbsp;</label>
                                            <div class="input-wrapper">
                                                <input type="checkbox" disabled name="custom" value="${!ingredient.custom}" <g:if test="${!ingredient.custom}"> checked="checked" </g:if> id="custom" />
                                            </div>
                                        </div>
                                        <div class="formfield">
                                            <label for='deletable'>Deletable?&nbsp;</label>
                                            <div class="input-wrapper">
                                                <input type="checkbox" disabled name="deletable" value="${ingredient.canBeDeleted}" <g:if test="${ingredient.canBeDeleted}"> checked="checked" </g:if> id="deletable" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="update-ingredient2" style="width:50%;display:block;float:right;">
                                    <div class="formfield">
                                        <div style="margin-top:-25px;height:419px;overflow-y:auto;">
                                            <p style="text-align:left;">Drinks</p>
                                            <g:each in="${ingredient.drinks.sort{ it.id }}" var="drink" status="i">
                                                <g:if test="${(ingredient as Ingredient).drinks.contains(drink)}">
                                                    <div style="display:block;color:${darkMode?'white':'black'};">
                                                        <button id="addDrinkBtn${drink.id}" type="button" class="btn btn-success btn-xs" onclick="addDrink('${drink.id}');">Added</button>
                                                        <button id="removeDrinkBtn${drink.id}" type="button" class="btn btn-outline-danger btn-xs" onclick="removeDrink('${drink.id}');">Remove</button>
                                                        <input hidden type="checkbox" name="drinks" id="drink${drink.id}" checked value="${drink.id}"/> ${drink} &emsp14;
                                                    </div>
                                                </g:if>
                                            </g:each>
                                            <g:each in="${drinks.sort{it.id}}" var="drink" status="i">
                                                <g:if test="${!(ingredient as Ingredient).drinks.contains(drink)}">
                                                    <div style="display:block;color:${darkMode?'white':'black'};">
                                                        <button id="addDrinkBtn${drink.id}" type="button" class="btn btn-outline-info btn-xs" onclick="addDrink('${drink.id}');">Add</button>
                                                        <button hidden id="removeDrinkBtn${drink.id}" type="button" class="btn btn-outline-danger btn-xs" onclick="removeDrink('${drink.id}');">Remove</button>
                                                        <input hidden type="checkbox" name="drinks" id="drink${drink.id}" value="${drink.id}"/> ${drink} &emsp14;
                                                    </div>
                                                </g:if>
                                            </g:each>
                                        </div>
                                    </div>
                                </div>
                                <div class="formfield" style="margin-top:25px;text-align:center;">
                                    <a style="margin-right:10px;" class="btn btn-outline-danger" id="cancel" href="${createLink(uri: "/ingredient/show/${ingredient.id}")}"><g:message code="default.cancel.label" default="Cancel"/></a>
                                    <button style="margin-left:10px;" id="updateDrink" class="btn btn-outline-primary" type="submit" form="updateIngredient">Update</button>
                                </div>
                            </g:form>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("edit loaded");
            });
            function isValid() {
                console.log("isValid()");
                return true;
            }
            function addDrink(drinkId) {
                document.getElementById('drink'+drinkId).checked = true;
                console.log('checkbox for drink'+drinkId+ ' checked');
                document.getElementById('addDrinkBtn'+drinkId).classList.remove('btn-outline-info');
                document.getElementById('addDrinkBtn'+drinkId).classList.add('btn-success');
                document.getElementById('addDrinkBtn'+drinkId).innerHTML = "Added";
                document.getElementById('removeDrinkBtn'+drinkId).hidden = false;
            }
            function removeDrink(drinkId) {
                document.getElementById('drink'+drinkId).checked = false;
                console.log('checkbox for drink'+drinkId+ ' unchecked');
                document.getElementById('addDrinkBtn'+drinkId).classList.remove('btn-success');
                document.getElementById('addDrinkBtn'+drinkId).classList.add('btn-outline-info');
                document.getElementById('addDrinkBtn'+drinkId).innerHTML = "Add";
                document.getElementById('removeDrinkBtn'+drinkId).hidden = true;
            }
        </script>
    </body>
</html>


