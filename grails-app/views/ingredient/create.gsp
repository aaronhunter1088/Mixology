<%@ page import="enums.Unit;" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Create Ingredients</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <g:set var="entityName" value="${message(code: 'ingredient.label', default: 'Ingredient')}" />
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
                content: "Use this form to create a new ingredient";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
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
                <fieldset style="border:thick solid #008011;" class="no-before">
                    <legend style="margin-left:25px;width:auto;">
                        &emsp14;Create An Ingredient&emsp14;
                        <hr style="height:1px;background-color:#008011">
                    </legend>
                    <g:form url="[controller:'ingredient', action:'save']" id="newIngredient" name="newIngredient">
                        <form id="ingredientForm" name="ingredientForm">
                            <table id="ingredientTable" class="table" style="width:100%;">
                            <thead>
                                <th>Name<span class='required-indicator'>*</span></th>
                                <th>Unit<span class='required-indicator'>*</span></th>
                                <th>Amount<span class='required-indicator'>*</span></th>
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
                                    select.setAttribute('class', 'form-control')
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
                            <tbody id="stringOptsBody">
                                <tr id="row">
                                    <td>
                                        <input type="text" id="ingredientName" name="ingredientName" class="form-control" required>
                                    </td>
                                <td>
                                    <select name="ingredientUnit" class="form-control" required="required">
                                        <option value="" label="Select One" selected disabled>Select One</option>
                                        <g:each in="${Unit.values()}" var="unit" name="ingredientUnit">
                                            <option value="${unit}">${unit}</option>
                                        </g:each>
                                    </select>
                                </td>
                                    <td>
                                        <input type="text" id="ingredientAmount" name="ingredientAmount" class="form-control" required>
                                    </td>
                                <td>
                                    <a style="color:black;text-decoration:none;" class="btn btn-outline-danger" href="javascript:removeRow('optRow')"><b>X</b></a>
                                </td>
                            </tr>
                            </tbody>
                            <script>
                                function removeRow(trId) {
                                    console.log("clicked X... removing row")
                                    $('#'+trId).remove();
                                }
                            </script>
                        </table>
                        </form>
                        <div class="formfield" style="margin-top:25px;padding-left:10px;">
                            <button class="btn btn-outline-primary" type="submit" form="newIngredient" formaction="/mixology/ingredient/save">Create Ingredient(s)</button>
                        </div>
                    </g:form>
                </fieldset>
            </div>
        </div>
        <script type="text/javascript">
        function isValid() {
            let tableRows = $("#ingredientTable > tbody > tr");
            let rowCount = tableRows.length;
            let row;
            let result = false;
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
                    url: "${createLink(controller:'ingredient', action:'validate')}",
                    data: {
                        ingredientName: cellValue1,
                        ingredientUnit: cellValue2,
                        ingredientAmount: cellValue3,
                        apiCallCount: ajaxCalls
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
                            $("#ingredientErrorMessages").addClass("errors");
                            $("#ingredientErrorMessages > h3").html(message);
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
            if (successCount === rowCount) {
                return true;
            } else {
                return false;
            }
        }
    </script>
    </body>
</html>
