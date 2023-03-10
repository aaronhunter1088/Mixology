<%@ page import="enums.*; mixology.Ingredient" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        %{--    <meta name="layout" content="main"/>--}%
        <title>Create Drinks</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        %{--        <asset:link rel="icon" href="cocktailRed.png" type="image/png" id="icon"/>--}%
        %{--        <link rel="icon" type="image/x-ico" href="../../assets/images/cocktail.ico">--}%
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'cocktail.ico')}" />
%{--        <meta name="layout" content="main" />--}%
        <g:set var="entityName" value="${message(code: 'drink.label', default: 'Drink')}" />
%{--        <title><g:message code="default.create.label" args="[entityName]" /></title>--}%

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
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("page loaded");
            });
            let rowId = 0;
        </script>
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
                    <g:form url="[controller:'drink', action:'save']" id="newDrink" name="newDrink" onsubmit="return isValid()">
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
                                    <select name="drinkType" class="form-control" style="width:43%;">
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
                                    <select name="glass" class="form-control" style="width:43%;">
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
                            <form id="ingredientForm" name="ingredientForm">
                                <fieldset style="border:thick solid #008011;" class="no-before">
                                    <legend style="width:auto;">
                                        &emsp14;Create A New Ingredient&emsp14;
                                        <hr style="height:1px;background-color:#008011">
                                    </legend>
                                    <div id="ingredientErrorMessagesDiv" class="col-12 content scaffold-create" role="main">
                                        <h3 id="ingredientErrorMessage" role="alert">${errorMessage}</h3>
                                    </div>
                                    <table id="ingredientTable" class="table" style="width:100%;">
                                        <thead>
                                            <th>Name</th>
                                            <th>Unit</th>
                                            <th>Amount</th>
                                            <th><a style="color:black;" class="btn btn-outline-success" href="javascript:addRow('stringOptsBody', 'ingredient')"><b>+</b></a></th>
                                        </thead>
                                        <script>
                                            function addRow(tbody, prefix) {
                                                console.log("clicked +... adding row")
                                                rowId++;
                                                // create Name
                                                let tr = document.createElement('tr');
                                                tr.setAttribute('id', prefix + 'Row' + rowId);
                                                tr.setAttribute('name', prefix + 'Row' + rowId);
                                                let td = document.createElement('td');
                                                let input = document.createElement('input');
                                                input.setAttribute('type', 'text');
                                                input.setAttribute('id', prefix + 'Name');
                                                input.setAttribute('name', prefix + 'Name');
                                                input.setAttribute('class', 'form-control');
                                                input.setAttribute('required', 'true');
                                                td.appendChild(input);
                                                tr.appendChild(td);
                                                // create Unit
                                                td = document.createElement('td');
                                                let select = document.createElement('select');
                                                select.setAttribute('name', 'ingredientUnit');
                                                select.setAttribute('required', 'true');
                                                select.setAttribute('class', 'form-control');
                                                let first = document.createElement('option');
                                                first.setAttribute('label', 'Select One');
                                                first.selected = true;
                                                first.disabled = true;
                                                first.setAttribute('text', 'Select One');
                                                select.appendChild(first);
                                                let option = document.createElement('option');
                                                <g:each in="${Unit.values()}" status="i" var="unit">
                                                    option.value = '${unit}';
                                                    option.text = '${unit}';
                                                    select.appendChild(option);
                                                    option = document.createElement('option');
                                                </g:each>
                                                td.appendChild(select);
                                                tr.appendChild(td);
                                                // create Amount
                                                td = document.createElement('td');
                                                input = document.createElement('input');
                                                input.setAttribute('type', 'text');
                                                input.setAttribute('id', prefix + 'Amount');
                                                input.setAttribute('name', prefix + 'Amount');
                                                input.setAttribute('class', 'form-control');
                                                input.setAttribute('required', 'true');
                                                td.appendChild(input);
                                                tr.appendChild(td);
                                                // create X button
                                                td = document.createElement('td');
                                                let a = document.createElement('a');
                                                a.setAttribute('class', 'btn btn-outline-danger');
                                                a.setAttribute('href', 'javascript:removeRow("'+prefix + 'Row' + rowId + '")');
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
                                                console.log("clicked X... removing rowId " + rowId)
                                                $('#'+trId).remove();
                                                rowId--;
                                                console.log("rowId count is at " + rowId)
                                            }
                                        </script>
                                    </table>
                                </fieldset>
                            </form>
                        </div>
                    </g:form>
                </fieldset>
            </div>
        </div>
        <script type="text/javascript">
            function isValid() {
                let tableRows = $("#ingredientTable > tbody > tr");
                let row;
                let numberOfRows = tableRows.length
                let successCount = 0;
                let failCount = 0;
                let ajaxCalls = 0;
                tableRows.each(function () {
                    row = $(this);
                    let cellValue1 = row.find('td:nth-child(1) > input').val();
                    let cellValue2 = row.find('td:nth-child(2) > select > option:selected').val();
                    let cellValue3 = row.find('td:nth-child(3) > input').val();
                    $.ajax({
                        headers: {
                            accept: "application/json",
                            contentType: "application/json"
                        },
                        async: false,
                        type: "GET",
                        url: "${createLink(controller:'drink', action:'validateIngredients')}",
                        data: {
                            ingredientName: cellValue1,
                            ingredientUnit: cellValue2,
                            ingredientAmount: cellValue3,
                            apiCallCount: ++ajaxCalls
                        },
                        statusCode: {
                            200: function(data) {
                                console.log(JSON.stringify(data))
                                successCount += 1;
                            },
                            400: function(data) {
                                failCount += 1;
                                let response = JSON.parse(JSON.stringify(data['responseJSON']))
                                let message;
                                if (failCount === 1) {
                                    message = response.message;
                                } else {
                                    let messageArr = response.message.split(" ");
                                    let firstPart = "Some ingredients have ";
                                    message = firstPart + messageArr.slice(2).toString().replaceAll(",", " ");
                                }
                                console.log("FAILED: " + message);
                                row.addClass("errors");
                                let errorMessage = $("#ingredientErrorMessage")
                                errorMessage.addClass("errors");
                                errorMessage.html(message);
                            },
                            404: function(data) {
                                console.log(JSON.stringify(data));
                            },
                            500: function(data) {
                                console.log(JSON.stringify(data));
                            }
                        }
                    });
                });
                if (successCount === numberOfRows) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
        </script>
    </body>
</html>
