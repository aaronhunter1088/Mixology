<%@ page import="enums.Alcohol; enums.GlassType; mixology.Ingredient" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'drink.label', default: 'Drink')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <a href="#create-drink" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="create-drink" class="col-12 content scaffold-create" role="main">
%{--                    <h1><g:message code="default.create.label" args="[entityName]" /></h1>--}%
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
%{--                    <g:form resource="${this.drink}" method="POST">--}%
%{--                        <fieldset class="form">--}%
%{--                            <f:all bean="drink"/>--}%
%{--                        </fieldset>--}%
%{--                    <fieldset class="buttons">--}%
%{--                        <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />--}%
%{--                    </fieldset>--}%
%{--                    </g:form>--}%
                </div>
                <form action="/mixology/drink/save" method="POST" id="newDrink" class="form">
                    <fieldset>
                        <legend><h1><g:message code="default.create.label" args="[entityName]" /></h1></legend>
                        <label for='drinkName'>Drink Name<span class='required-indicator'>*</span></label>
                        <input type="text" name="drinkName" value="" required="" id="drinkName" />
                    </fieldset>
                    <fieldset>
                        <label for='drinkNumber'>Drink Number<span class='required-indicator'>*</span></label>
                        <input type="text" name="drinkNumber" value="" required="" id="drinkNumber" />
                    </fieldset>
                    <fieldset>
                        <label for='alcoholType'>Drink Type<span class='required-indicator'>*</span></label>
                        <select name="drinkType">
                            <option label="Select One" selected disabled>Select One</option>
                            <g:each in="${Alcohol.values()}" var="alcohol" name="alcoholType">
                                <option value="${alcohol}">${alcohol}</option>
                            </g:each>
                        </select>
                    </fieldset>
                    <fieldset>
                        <label for='drinkSymbol'>Drink Symbol<span class='required-indicator'>*</span></label>
                        <input type="text" name="drinkSymbol" value="" required="" id="drinkSymbol" />
                    </fieldset>
                    <fieldset>
                        <label for="glassType">Suggested Glass<span class='required-indicator'>*</span></label>
                        <select name="glass">
                            <option label="Select One" selected disabled>Select One</option>
                            <g:each in="${GlassType.values()}" var="glass" name="glassType">
                                <option value="${glass}">${glass}</option>
                            </g:each>
                        </select>
                    </fieldset>
                    <fieldset>
                        <label for='instructions'>Mixing Instructions<span class='required-indicator'>*</span></label>
                        <g:textArea form="newDrink" name="instructions" value="" rows="5" cols="40"/>
                    </fieldset>
                    <fieldset>
                        <label>Ingredients<span class='required-indicator'>*</span></label> <br>
                        <g:each in="${Ingredient.list(sort: 'id', order: 'asc')}" var="ingredient" status="i">
                            <g:checkBox name="option" value="${ingredient}" checked="false"/>
                            <label for="option">${ingredient}</label>
                            <g:if test="${i%4==0 && i>0}">
                                <br>
                            </g:if>
                        </g:each>
                    </fieldset>
                    <fieldset>
                        <button type="submit" formaction="/mixology/drink/save">Create</button>
                    </fieldset>
                </form>
            </section>
        </div>
    </div>
    </body>
</html>
