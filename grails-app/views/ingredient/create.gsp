<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><g:message code="create.ingredients" default="Create Ingredients"/></title>
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
                content: "${g.message(code:'create.ingredients.before', default:'Use this form to create a new ingredient')}";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
            table>thead>tr>th:hover {
                background-color: black !important;
            }
        </style>
    </head>
    <g:set var="entityName" value="${g.message(code: 'ingredient.label', default: 'Ingredient')}" />
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:if test="${darkMode}">
        <style>
            .input-wrapper > input,textarea,select {
                background-color:black;
                color:white;
                border-color:white;
            }
            fieldset::before {
                background: #000;
                color:white;
            }
        </style>
    </g:if>
    <body style="padding:50px;background-color:${darkMode?'black':'white'};">
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("page loaded");
                $("#ingredientRow1 > div:nth-child(4) > a").css('visibility', 'hidden');
            });
            let rowId = 1;
        </script>
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="background-color:${darkMode?'black':'white'};">
                <div class="container">
                    <div id="navigation" style="display:flex;justify-content:center;">
                        <g:render template="../navigation" model="[user:user]"/>
                    </div>
                </div>
                <p></p>
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
                <div id="create-ingredient">
                    <div style="display:flex;justify-content:center;">
                        <fieldset style="border:thick solid #008011;width:750px;" class="no-before">
                            <legend style="margin-left:25px;width:auto;color:${darkMode?'white':'black'};">
                                &emsp14;<g:message code="create.an.ingredient" default="Create an Ingredient"/>&emsp14;<a style="color:${darkMode?'white':'black'};" class="btn btn-outline-success" href="javascript:addRow('ingredientTable', 'ingredient')"><b>+</b></a>
                                <i class='fa-solid fa-circle-info' title="${g.message(code:'create.ingredients.help', default:'Use this form to create new ingredients. An already existing ingredient will be used, and not created, if you attempt to create an existing ingredient. New ingredients are compared against the combination of the Name, Unit and Amount.')}"></i>
                                <hr style="height:1px;background-color:#008011">
                            </legend>
                            <div id="ingredientErrorMessagesDiv" style="display:block;">
                            </div> <!-- New Ingredient errors -->
                            <g:form url="[controller:'ingredient', action:'save']" id="newIngredient" name="newIngredient" onsubmit="return isValid();">
                                <div id="ingredientTable" style="position:relative;display:block;padding:20px;">
                                    <div id="ingredientRow1" style="position:relative;display:flex;justify-content:space-evenly;">
                                        <div id="ingredientNameColumn1" style="position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                            <label for="ingredientName"><g:message code="create.ingredients.name" default="Name"/>&nbsp;<span class='required-indicator'>*</span></label>
                                            <input type="text" id="ingredientName" name="ingredientName" class="form-control" required style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                        </div>
                                        <div id="ingredientUnitColumn1" style="position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                            <label for="ingredientUnitSelect"><g:message code="create.ingredients.unit" default="Unit"/>&nbsp;<span class='required-indicator'>*</span></label>
                                            <select id="ingredientUnitSelect" style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};" name="ingredientUnit" class="form-control" required="required">
                                                <option value="" label="${g.message(code:'select.one', default:'Select One')}" selected disabled><g:message code="select.one" default="Select One"/></option>
                                                <g:each in="${Arrays.asList(Unit.values()).findAll{it.type == 'S'}}" var="unit" name="ingredientUnit">
                                                    <option value="${unit}">${unit}</option>
                                                </g:each>
                                            </select>
                                        </div>
                                        <div id="ingredientAmountColumn1" style="position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                            <label for="ingredientAmount"><g:message code="create.ingredients.amount" default="Amount"/>&nbsp;<span class='required-indicator'>*</span></label>
                                            <input type="text" id="ingredientAmount" name="ingredientAmount" class="form-control" required style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                        </div>
                                        <div style="padding:5px;position:relative;display:block;margin-top:22px;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                            <a style="height:35px;width:auto;color:${darkMode?'white':'black'};text-decoration:none;" class="btn btn-outline-danger btn-xs" href="javascript:removeRow('rowId')"><b>X</b></a>
                                        </div>
                                    </div>
                                </div>
                                <script type="text/javascript">
                                    function addRow(tbody, prefix) {
                                        rowId++;
                                        console.log("clicked +... adding row " + rowId)
                                        let div = document.createElement('div');
                                        div.setAttribute('id', prefix + 'Row' + rowId);
                                        div.setAttribute('class', 'input-wrapper');
                                        div.setAttribute('style', 'position:relative;display:flex;justify-content:space-evenly;');
                                        // create Name
                                        let nameDiv = document.createElement('div');
                                        nameDiv.setAttribute('id', prefix + 'NameColumn' + rowId);
                                        nameDiv.setAttribute('name', prefix + 'NameColumn' + rowId);
                                        //style="position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};"
                                        nameDiv.setAttribute('style', 'position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                        let labelForName = document.createElement('label');
                                        labelForName.setAttribute('for', prefix + 'NameColumn' + rowId);
                                        labelForName.innerHTML = '${g.message(code:'create.ingredients.name', default:'Name')}' + '&nbsp;<span class=\'required-indicator\'>*</span>';
                                        let input = document.createElement('input');
                                        input.setAttribute('type', 'text');
                                        input.setAttribute('id', prefix + 'Name');
                                        input.setAttribute('name', prefix + 'Name' + rowId);
                                        input.setAttribute('class', 'form-control');
                                        input.setAttribute('style', 'background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};');
                                        input.setAttribute('required', 'true');
                                        nameDiv.appendChild(labelForName);
                                        nameDiv.appendChild(input);
                                        div.appendChild(nameDiv);
                                        // create Unit
                                        let unitDiv = document.createElement('div');
                                        unitDiv.setAttribute('id', prefix + 'UnitColumn' + rowId);
                                        unitDiv.setAttribute('name', prefix + 'UnitColumn' + rowId);
                                        //style="position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};"
                                        unitDiv.setAttribute('style', 'position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                        let labelForUnit = document.createElement('label');
                                        labelForUnit.setAttribute('for', prefix + 'Unit' + rowId);
                                        labelForUnit.innerHTML = '${g.message(code:'create.ingredients.unit', default:'Unit')}' + '&nbsp;<span class=\'required-indicator\'>*</span>';
                                        let select = document.createElement('select');
                                        select.setAttribute('name', prefix + 'Unit' + rowId);
                                        select.setAttribute('required', 'true');
                                        select.setAttribute('style', 'background-color:${darkMode?'black':'white'};')
                                        select.setAttribute('class', 'form-control')
                                        //style="background-color:${darkMode?'black':'white'};" name="ingredientUnit" class="form-control" required="required">
                                        let first = document.createElement('option');
                                        first.setAttribute('label', '${g.message(code:'select.one', default:'Select One')}');
                                        first.selected = true;
                                        first.disabled = true;
                                        first.setAttribute('text', 'Select One');
                                        select.appendChild(first);
                                        let option = document.createElement('option');
                                        <g:each in="${Arrays.asList(Unit.values()).findAll{it.type == 'S'}}" status="i" var="unit">
                                        option.value = '${unit}';
                                        option.text = '${unit}';
                                        select.appendChild(option);
                                        option = document.createElement('option');
                                        </g:each>
                                        unitDiv.appendChild(labelForUnit);
                                        unitDiv.appendChild(select);
                                        div.appendChild(unitDiv);
                                        // create Amount
                                        let amountDiv = document.createElement('div');
                                        amountDiv.setAttribute('id', prefix + 'AmountColumn' + rowId);
                                        amountDiv.setAttribute('name', prefix + 'AmountColumn' + rowId);
                                        //style="position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};"
                                        amountDiv.setAttribute('style', 'position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                        let labelForAmount = document.createElement('label');
                                        labelForAmount.setAttribute('for', prefix + 'Amount' + rowId);
                                        labelForAmount.innerHTML = '${g.message(code:'create.ingredients.amount', default:'Amount')}' + '&nbsp;<span class=\'required-indicator\'>*</span>';
                                        input = document.createElement('input');
                                        input.setAttribute('type', 'text');
                                        input.setAttribute('id', prefix + 'Amount');
                                        input.setAttribute('name', prefix + 'Amount' + rowId);
                                        input.setAttribute('class', 'form-control');
                                        input.setAttribute('style', 'background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};');
                                        input.setAttribute('required', 'true');
                                        amountDiv.appendChild(labelForAmount);
                                        amountDiv.appendChild(input);
                                        div.appendChild(amountDiv);
                                        // create X button
                                        let div4 = document.createElement('div');
                                        div4.setAttribute('id', prefix + 'CancelRow' + rowId);
                                        div4.setAttribute('name', prefix + 'CancelRow' + rowId);
                                        //<div style="padding:5px;position:relative;display:block;margin-top:22px;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                        div4.setAttribute('style', 'padding:5px;position:relative;display:block;margin-top:22px;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                        let a = document.createElement('a');
                                        a.setAttribute('class', 'btn btn-outline-danger btn-xs');
                                        a.setAttribute('href', 'javascript:removeRow("'+prefix + 'Row' + rowId + '")');
                                        //<a style="height:35px;width:auto;color:${darkMode?'white':'black'};text-decoration:none;" class="btn btn-outline-danger btn-xs" href="javascript:removeRow('rowId')"><b>X</b></a>
                                        a.setAttribute('style', 'height:35px;width:auto;color:${darkMode?'white':'black'};text-decoration:none;');
                                        let bold = document.createElement('B');
                                        let X = document.createTextNode('X');
                                        bold.appendChild(X);
                                        a.appendChild(bold);
                                        div4.appendChild(a);
                                        div.appendChild(div4);
                                        $('#'+tbody).append(div);
                                    }
                                    function removeRow(trId) {
                                        --rowId;
                                        $('#'+trId).remove();
                                        console.log("clicked X... removing row, rowCount: "+rowId);
                                    }
                                </script>
                                <!--
                                New rows will be added here
                                -->
                                <div class="formfield" style="margin-top:25px;padding-left:10px;text-align:center;">
                                    <input class="btn btn-outline-primary" type="submit" form="newIngredient" formaction="/mixology/ingredient/save" value="${g.message(code:'create.ingredients', default:'Create Ingredients')}"/>
                                </div>
                            </g:form>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
        <script type="text/javascript">
            function isValid() {
                //alert("isValid");
                let tableRows = $("#ingredientTable > div");
                let rowCount = tableRows.length;
                console.log('rowCount: ' + rowCount);
                let row;
                let result = false;
                let successCount = 0;
                let failCount = 0;
                let ajaxCalls = 0;
                tableRows.each(function () {
                    row = $(this);
                    let cellValue1 = row.find('div:nth-child(1) > input').val();
                    let cellValue2 = row.find('div:nth-child(2) > select > option:selected').val();
                    let cellValue3 = row.find('div:nth-child(3) > input').val();
                    $.ajax({
                        headers: {
                            accept: "application/json",
                            contentType: "application/json"
                        },
                        async: false,
                        type: "GET",
                        url: "${createLink(controller:'ingredient', action:'validateIngredient')}",
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
                                $("#ingredientErrorMessagesDiv").addClass("errors");
                                let h3 = document.createElement('h6');
                                h3.innerText = message;
                                $("#ingredientErrorMessagesDiv").append(h3);
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
                return successCount === rowCount;
            }
        </script>
    </body>
</html>