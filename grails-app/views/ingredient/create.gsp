<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
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
            content: "Use this form to create a new ingredient";
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
            <section class="row" name="navigatioin">
                <a href="#create-ingredient" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>
            <div id="create-ingredient" class="col-12 content scaffold-create" role="main">
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
            </div>
            <fieldset style="border:thick solid #000080;">
                <legend style="width:auto;">
                    &emsp14;<g:message code="default.create.label" args="[entityName]" />&emsp14;
                    <hr style="height:1px;background-color:#000080">
                </legend>
                <form action="/mixology/ingredient/save" method="POST" id="newIngredient">
                    <div class="formfield">
                        <label for='ingredientName'>Ingredient Name<span class='required-indicator'>*</span></label>
                        <div class="input-wrapper">
                            <input type="text" name="ingredientName" value="" required="" id="ingredientName" />
                        </div>
                    </div>
                    <div class="formfield">
                        <label for='ingredientUnit'>Unit<span class='required-indicator'>*</span></label>
                        <div class="input-wrapper">
                            <input type="text" name="ingredientUnit" value="" required="" id="ingredientUnit" />
                        </div>
                    </div>
                    <div class="formfield">
                        <label for='ingredientAmount'>Amount<span class='required-indicator'>*</span></label>
                        <div class="input-wrapper">
                            <input type="text" name="ingredientAmount" value="" required="" id="ingredientAmount" />
                        </div>
                    </div>
                </form>
                <div class="formfield" style="margin-top:25px;">
                    <button class="btn btn-outline-primary" type="submit" form="newIngredient" formaction="/mixology/ingredient/save">Create</button>
                </div>
            </fieldset>
        </div>
    </div>
    </body>
</html>
