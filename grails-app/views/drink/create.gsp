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
            fieldset .no-before::before {
                content: "";
            }
            th {
                text-align: center;
            }
            /*Keeps scroll bar present*/
            ::-webkit-scrollbar {
                -webkit-appearance: none;
                width: 7px;
            }
            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgba(0, 0, 0, .5);
                box-shadow: 0 0 1px rgba(255, 255, 255, .5);
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
                <div id="errorMessages" class="col-12 content scaffold-create" role="main">
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
                    <g:form url="[controller:'drink', action:'save']" name="newDrink">
                        <div id="create-drink" style="width:55%;float:left;">
%{--                        <form action="/mixology/drink/save" method="POST" id="newDrink" class="form">--}%
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
                                <div style="margin-top:-25px; height:200px; overflow-y:auto;">
                                    <g:each in="${Ingredient.list(sort: 'id', order: 'asc')}" var="ingredient" status="i">
                                        <input type="checkbox" name="ingredients" id="ingredients" value="${ingredient}"/> ${ingredient} &emsp14;
%{--                                        <g:if test="${i%2!=0 && i!=0 || i==1}"><br></g:if> Print <br> every two checkboxes --}%
                                        <br>
                                    </g:each>
                                </div>
                            </div>
%{--                        </form>--}%
                            <div class="formfield" style="margin-top:25px;">
                                <button id="createDrink" class="btn btn-outline-primary" type="submit" form="newDrink">Create</button> %{-- formaction="/mixology/drink/save"--}%
                            </div>
                        </div>
                        <div id="create-ingredient" style="width:45%;float:right;">
                        <fieldset style="border:thick solid #008011;" class="no-before">
                            <legend style="width:auto;">
                                &emsp14;Create A New Ingredient&emsp14;
                                <hr style="height:1px;background-color:#008011">
                            </legend>
%{--                            <form action="/mixology/ingredient/save" method="POST" id="newIngredient">--}%
%{--                            <mix:formRemote id="newIngredient" name="newIngredient" method="POST" url="[controller:'ingredient', action:'save']"--}%
%{--                                    before="before()" after="after()">--}%
                                <table class="table" style="width:100%;">
                                    <thead>
                                        <th>Name</th>
                                        <th>Unit</th>
                                        <th>Amount</th>
                                        <th><a style="color:black;" class="btn btn-outline-success" href="javascript:addRow('stringOptsBody', 'ingredient')"><b>+</b></a></th>
                                    </thead>
                                    <script>
                                        let i = 0;
                                        function addRow(tbody, prefix) {
                                            console.log("clicked +... adding row")
                                            i++;
                                            // create Name
                                            let tr = document.createElement('tr');
                                            tr.setAttribute('id', prefix + 'Row' + i);
                                            let td = document.createElement('td');
                                            let input = document.createElement('input');
                                            input.setAttribute('type', 'text');
                                            input.setAttribute('id', prefix + 'Name');
                                            input.setAttribute('name', prefix + 'Name');
                                            input.setAttribute('class', 'form-control');
                                            input.setAttribute('required', '');
                                            td.appendChild(input);
                                            tr.appendChild(td);
                                            // create Unit
                                            td = document.createElement('td');
                                            input = document.createElement('input');
                                            input.setAttribute('type', 'text');
                                            input.setAttribute('id', prefix + 'Unit');
                                            input.setAttribute('name', prefix + 'Unit');
                                            input.setAttribute('class', 'form-control');
                                            td.appendChild(input);
                                            tr.appendChild(td);
                                            // create Amount
                                            td = document.createElement('td');
                                            input = document.createElement('input');
                                            input.setAttribute('type', 'text');
                                            input.setAttribute('id', prefix + 'Amount');
                                            input.setAttribute('name', prefix + 'Amount');
                                            input.setAttribute('class', 'form-control');
                                            td.appendChild(input);
                                            tr.appendChild(td);
                                            // create X button
                                            td = document.createElement('td');
                                            let a = document.createElement('a');
                                            a.setAttribute('class', 'btn btn-outline-danger');
                                            a.setAttribute('href', 'javascript:removeRow("'+prefix + 'Row' + i + '")');
                                            a.setAttribute('style', 'color:black;text-decoration:none;');
                                            let bold = document.createElement('B');
                                            let X = document.createTextNode('X');
                                            bold.appendChild(X);
                                            a.appendChild(bold);
                                            td.appendChild(a);
                                            tr.appendChild(td);
                                            $('#'+tbody).append(tr);
                                        }
                                    </script>
                                    <tbody id="stringOptsBody"></tbody>
                                    <script>
                                        function removeRow(trId) {
                                            console.log("clicked X... removing row")
                                            $('#'+trId).remove();
                                        }
                                    </script>
                                </table>
%{--                            </form>--}%
%{--                            </mix:formRemote>--}%
%{--                            <button id="createIngredients" class="btn btn-outline-primary" type="submit" form="newIngredient" formaction="/mixology/ingredient/save">Create Ingredient(s)</button>--}%
                        </fieldset>
                    </div>
                    </g:form>
                </fieldset>
            </div>
        </div>
    </body>
    <script>
        $(function() {
            console.log("page loaded");
        });
        function createDrink() {

        }
    </script>
</html>
