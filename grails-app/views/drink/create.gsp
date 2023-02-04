<%@ page import="enums.Alcohol; enums.GlassType; mixology.Ingredient" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'drink.label', default: 'Drink')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
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
                width: 200px;
            }
            .formfield .input-wrapper .table-form {
                width: 100%;
            }
            hr {
                margin-bottom: 10px;
            }
            fieldset {
                position: relative;
            }
            fieldset::before {
                content: "Use this form to create a new drink";
                position: absolute;
                margin-top: -35px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
        </style>
    </head>
    <body>
        <div id="content" role="main">
            <div class="container">
                <section class="row" name="navigation">
                    <a href="#create-drink" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                    <div class="nav" role="navigation">
                        <ul>
                            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                            <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                        </ul>
                    </div>
                </section>
                <div id="create-drink" class="col-12 content scaffold-create" role="main">
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
                </div>
                <fieldset style="border:thick solid #000080;">
                    <legend style="width:auto;">
                        &emsp14;<g:message code="default.create.label" args="[entityName]" />&emsp14;
                        <hr style="height:1px;background-color:#000080">
                    </legend>
                    <form action="/mixology/drink/save" method="POST" id="newDrink" class="form">
                        <div class="formfield">
                            <label for='drinkName'>Drink Name<span class='required-indicator'>*</span></label>
                            <div class="input-wrapper">
                                <input type="text" name="drinkName" value="" required="" id="drinkName" />
                            </div>
                        </div>
                        <div class="formfield">
                            <label for='drinkNumber'>Drink Number<span class='required-indicator'>*</span></label>
                            <div class="input-wrapper">
                                <input type="text" name="drinkNumber" value="" required="" id="drinkNumber" />
                            </div>
                        </div>
                        <div class="formfield">
                            <label for='alcoholType'>Drink Type<span class='required-indicator'>*</span></label>
                            <div class="input-wrapper">
                                <select name="drinkType">
                                    <option label="Select One" selected disabled>Select One</option>
                                    <g:each in="${Alcohol.values()}" var="alcohol" name="alcoholType">
                                        <option value="${alcohol}">${alcohol}</option>
                                    </g:each>
                                </select>
                            </div>
                        </div>
                        <div class="formfield">
                            <label for='drinkSymbol'>Drink Symbol<span class='required-indicator'>*</span></label>
                            <div class="input-wrapper">
                                <input type="text" name="drinkSymbol" value="" required="" id="drinkSymbol" />
                            </div>
                        </div>
                        <div class="formfield">
                            <label for="glassType">Suggested Glass<span class='required-indicator'>*</span></label>
                            <div class="input-wrapper">
                                <select name="glass">
                                    <option label="Select One" selected disabled>Select One</option>
                                    <g:each in="${GlassType.values()}" var="glass" name="glassType">
                                        <option value="${glass}">${glass}</option>
                                    </g:each>
                                </select>
                            </div>
                        </div>
                        <div class="formfield">
                            <label for='instructions'>Mixing Instructions<span class='required-indicator'>*</span></label>
                            <div class="input-wrapper">
                                <g:textArea form="newDrink" name="instructions" value="" rows="5" cols="40"/>
                            </div>
                        </div>
                        <div class="formfield">
                            <label>Ingredients<span class='required-indicator'>*</span></label> <br>
                            <div class="input-wrapper">
                                <g:each in="${Ingredient.list(sort: 'id', order: 'asc')}" var="ingredient" status="i">
                                    <input type="checkbox" name="option" id="option" value="${ingredient}"/> ${ingredient} &emsp14;
                                    <g:if test="${i%2==0 && i!=0}"><br></g:if>
                                </g:each>
                            </div>
                        </div>
                    </form>
                    <div class="formfield" style="margin-top:25px;">
                        <button class="btn btn-outline-primary" type="submit" form="newDrink" formaction="/mixology/drink/save">Create</button>
                    </div>
                </fieldset>
            </div>
        </div>
    </body>
</html>
