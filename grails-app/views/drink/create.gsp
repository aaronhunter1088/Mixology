<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title><g:message code="drinks.create" default="Create Drinks"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
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
                padding-right:10px;
            }
            fieldset::before {
                content: "${g.message(code:'drink.create.help', default:'Use this form to create a new drink')}";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
            fieldset .no-before::before {
                content: "";
            }
            /* th {text-align: center;} */
            /* Fix table head */
            .tableFixHead    { overflow: auto; height: 600px; }
            .tableFixHead th { position: sticky; top: 0; }
            /* Just common table stuff. */
            table  { border-collapse: collapse; width: 100%; }
            th, td { padding: 8px 16px; text-align: center; }
            th     { background:#eee; }
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
    <%
        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        def userService = grailsApplication.mainContext.getBean('userService')
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
    %>
    <g:set var="entityName" value="${message(code: 'drink.label', args:[''], default: 'Drink')}" />
    <g:set var="darkMode" value="${user.darkMode}"/>
    <g:if test="${darkMode}">
        <style>
            .input-wrapper .form-control > select,option {
                background-color:black;
                border-color:white;
                color:white;
                width:100px;
            }
            .input-wrapper > textarea {
                background-color:black;
                color:white;
            } /* Required */
            .input-wrapper > input,textarea:focus {
                background-color:black;
                color:white;
            } /* Required */
            fieldset::before {
                background: #000;
                color:white;
            }
        </style>
    </g:if>
    <body style="padding:50px;background-color:${darkMode?'black':'white'};">
        <script type="text/javascript">
            let rowId = 0;
        </script>
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="background-color:${darkMode?'black':'white'};">
                <div class="container">
                    <div id="navigation" style="display:flex;justify-content:center;">
                        <g:render template="../navigation" model="[user:user]"/>
                    </div>
                </div>
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
                <p></p>
                <div id="create-drink">
                    <div style="display:flex;justify-content:center;">
                        <fieldset style="border:thick solid #000080;height:auto;width:1000px;">
                            <legend style="margin-left:25px;width:auto;color:${darkMode?'white':'black'};">
                                &emsp14;<g:message code="default.create.label" args="[entityName]" />&emsp14;
                                <hr style="height:1px;background-color:#000080">
                            </legend>
                            <g:form url="[controller:'drink', action:'save']" id="newDrink" name="newDrink" onsubmit="return isValid();">
                                <div id="create-drink" style="width:55%;float:left;">
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='name'><span class='required-indicator'>*</span> <g:message code="drink.create.name" default="Drink Name"/></label>
                                        <div class="input-wrapper">
                                            <input type="text" name="name" required id="name" value="${params?.name}" />
                                        </div>
                                    </div> <!-- Drink Name -->
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='number'><span class='required-indicator'>*</span> <g:message code="drink.create.number" default="Drink Number"/></label>
                                        <div class="input-wrapper">
                                            <input type="text" name="number" required id="number" value="${params?.number}" />
                                        </div>
                                    </div> <!-- Drink Number -->
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='alcoholType'><span class='required-indicator'>*</span> <g:message code="drink.create.type" default="Drink Type"/></label>
                                        <div class="input-wrapper">
                                            <select name="alcohol" id="alcohol" class="form-control" style="width:55%;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};border-color:white;">
                                                <option label="${g.message(code:'select.one', default:'Select One')}" <g:if test="${params.alcohol == null}">selected</g:if> disabled><g:message code="select.one" default="Select One"/></option>
                                                <g:each in="${Alcohol.values()}" var="alcohol" name="alcoholType">
                                                    <option value="${alcohol}" <g:if test="${params.alcohol != null && params.alcohol == alcohol}">selected</g:if>>${alcohol}</option>
                                                </g:each>
                                            </select>
                                        </div>
                                    </div> <!-- Drink Alcohol -->
                                    <div class="formfield" style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                        <label for='symbol'><span class='required-indicator'>*</span> <g:message code="drink.create.symbol" default="Drink Symbol"/></label>
                                        <div class="input-wrapper" style="color:${darkMode?'black':'white'};background-color:${darkMode?'black':'white'};">
                                            <input type="text" name="symbol" value="${params?.symbol}" required id="symbol" />
                                        </div>
                                    </div> <!-- Drink Symbol -->
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for="suggestedGlass"><span class='required-indicator'>*</span> <g:message code="drink.create.glass" default="Suggested Glass"/></label>
                                        <div class="input-wrapper">
                                            <select name="glass" id="glass" class="form-control" style="width:55%;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};border-color:white;">
                                                <option label="${g.message(code:'select.one', default:'Select One')}" <g:if test="${params.glass == null}">selected</g:if> disabled><g:message code="select.one" default="Select One"/></option>
                                                <g:each in="${GlassType.values()}" var="glass" name="suggestedGlass">
                                                    <option value="${glass}" <g:if test="${params.glass == glass}">selected</g:if>>${glass}</option>
                                                </g:each>
                                            </select>
                                        </div>
                                    </div> <!-- Drink Glass -->
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='mixingInstructions'><span class='required-indicator'>*</span> <g:message code="drink.create.mixing" default="Mixing Instructions"/></label>
                                        <div class="input-wrapper" style="color:${darkMode?'black':'white'};background-color:${darkMode?'black':'white'};">
                                            <g:textArea form="newDrink" name="mixingInstructions" value="${params?.mixingInstructions}" rows="5" cols="40"/>
                                        </div>
                                    </div> <!-- Drink Mixing Instructions -->
                                    <g:if test="${user.ingredients}">
                                        <div class="formfield" style="color:${darkMode?'white':'black'};">
                                            <label><span class='required-indicator'>*</span> <g:message code="ingredient.label" args="s" default="Ingredients"/></label><br>
                                            <div style="margin-top:-25px;height:200px;overflow-y:auto;">
                                                <g:each in="${user.ingredients.sort{-it.id}}" var="ingredient" status="i"> <!-- sort{'negative'...} returns list in reverse sort -->
                                                    <div style="display:block;">
                                                        <div id="ingredientsGroup" style="display:inline-flex;justify-content:center;">
                                                            <button type="button" class="btn btn-outline-primary btn-xs" onclick="addRow('ingredientTableDiv', 'ingredient', '${ingredient}')"><g:message code="default.button.edit.me.label" default="Edit Me"/></button>
                                                            <button id="addIngredientBtn${ingredient.id}" type="button" class="btn btn-outline-info btn-xs" onclick="addIngredient('${ingredient.id}');"><g:message code="default.button.add.label" default="Add"/></button>
                                                            <button hidden id="removeIngredientBtn${ingredient.id}" type="button" class="btn btn-outline-danger btn-xs" onclick="removeIngredient('${ingredient.id}');"><g:message code="default.button.remove.label" default="Remove"/></button>
                                                            <input hidden type="checkbox" name="ingredients" id="ingredient${ingredient.id}" value="${ingredient.id}"/> ${ingredient.prettyName(true)} &emsp14;
                                                        </div>
                                                    </div>
                                                </g:each>
                                            </div>
                                        </div> <!-- Drink Ingredients -->
                                    </g:if>
                                </div>
                                <form id="ingredientForm" name="ingredientForm" style="">
                                    <fieldset id="ingredientFieldSet" style="height:85px;border:thick solid #008011;color:${darkMode?'white':'black'};" class="no-before">
                                        <legend style="margin-left:25px;width:auto;">
                                            &emsp14;<g:message code="drink.create.fly.ingredients.label" default="Create A New Ingredient"/>&emsp14;
                                            <a style="color:${darkMode?'white':'black'};" class="btn btn-outline-success btn-xs" href="javascript:addRow('ingredientTableDiv', 'ingredient', '')"><strong>+</strong></a>
                                            <i class='fa-solid fa-circle-info' title="${g.message(code:'drink.create.on.the.fly.ingredients', default:'Use this form to create new ingredients. An already existing ingredient will be used, and not created, if you attempt to create an existing ingredient. New ingredients are compared against the combination of the Name, Unit and Amount.')}"></i>
                                            <hr style="height:1px;background-color:#008011">
                                        </legend>
                                        <div id="ingredientErrorMessagesDiv">
                                            <h3 id="ingredientErrorMessage" role="alert">${errorMessage}</h3>
                                        </div> <!-- New Ingredient errors -->
                                        <div id="ingredientTableDiv" class="tableFixHead" style="height:525px;">
                                            <script type="text/javascript">
                                                function addRow(tbody, prefix, ingredient) {
                                                    rowId++;
                                                    console.log("clicked +... adding row, rowCount " + rowId);
                                                    // increase field set size as table grows until 10 rows. then it stops and table is scrollable
                                                    if (rowId <= 8) {
                                                        let tableFieldSetHeight = document.getElementById("ingredientFieldSet").style.height;
                                                        tableFieldSetHeight = tableFieldSetHeight.replaceAll("px","");
                                                        let newFieldSetHeight = Number.parseInt(tableFieldSetHeight) + 70;
                                                        document.getElementById("ingredientFieldSet").style.height = newFieldSetHeight+"px";
                                                    }
                                                    let ingredientName = '';
                                                    let ingredientAmt = '';
                                                    let ingredientUnit = '';
                                                    if (ingredient !== '') {
                                                        let values = ingredient.split(':');
                                                        ingredientName = values[0].trim();
                                                        ingredientAmt = values[1].trim();
                                                        ingredientUnit = values[2].trim().toUpperCase().replaceAll(' ', '_');
                                                        //console.log("chosen unit: " + ingredientUnit);
                                                    }

                                                    let div = document.createElement('div');
                                                    div.setAttribute('id', prefix + 'Row' + rowId)
                                                    div.setAttribute('class', 'input-wrapper');
                                                    div.setAttribute('style', 'position:relative;display:flex;justify-content:space-evenly;padding:0 5px;height:70px;');
                                                    // create Name
                                                    let nameDiv = document.createElement('div');
                                                    nameDiv.setAttribute('id', prefix + 'NameColumn' + rowId);
                                                    nameDiv.setAttribute('name', prefix + 'NameColumn' + rowId);
                                                    //nameDiv.setAttribute('class','form-control');
                                                    nameDiv.setAttribute('style', 'position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                                    let labelForName = document.createElement('label');
                                                    labelForName.setAttribute('for', prefix + 'Name' + rowId);
                                                    labelForName.innerHTML = '${g.message(code:'drink.create.name', default:'Name')}' + '&nbsp;<span class=\'required-indicator\'>*</span>';
                                                    let input = document.createElement('input');
                                                    input.setAttribute('type', 'text');
                                                    input.setAttribute('id', prefix + 'Name');
                                                    input.setAttribute('name', prefix + 'Name' + rowId);
                                                    input.setAttribute('class', 'form-control');
                                                    input.setAttribute('style', 'background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};');
                                                    input.setAttribute('required', 'true');
                                                    if (ingredientName !== '') input.setAttribute('value',ingredientName);
                                                    nameDiv.appendChild(labelForName);
                                                    nameDiv.appendChild(input);
                                                    div.appendChild(nameDiv);
                                                    // create Unit
                                                    let div2 = document.createElement('div');
                                                    div2.setAttribute('id', prefix + 'UnitRow' + rowId);
                                                    div2.setAttribute('name', prefix + 'UnitRow' + rowId);
                                                    div2.setAttribute('style', 'position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                                    //div2.setAttribute('class','form-control');
                                                    let label2 = document.createElement('label');
                                                    label2.setAttribute('for', prefix + 'Unit' + rowId);
                                                    label2.innerHTML = '${g.message(code:'drink.create.unit', default:'Unit')}' + '&nbsp;<span class=\'required-indicator\'>*</span>';
                                                    let select = document.createElement('select');
                                                    select.setAttribute('name', 'ingredientUnit' + rowId);
                                                    select.setAttribute('required', 'true');
                                                    select.setAttribute('style', 'background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};')
                                                    select.setAttribute('class', 'form-control')
                                                    let first = document.createElement('option');
                                                    first.setAttribute('label', '${g.message(code:'select.one', default:'Select One')}');
                                                    first.selected = true;
                                                    first.disabled = true;
                                                    first.setAttribute('text', '${g.message(code:'select.one', default:'Select One')}');
                                                    select.appendChild(first);
                                                    let option = document.createElement('option');
                                                    <g:each in="${Arrays.asList(Unit.values()).findAll{it.type == 'S'}}" status="i" var="unit">
                                                    if ('${unit}' === ingredientUnit) option.selected = true;
                                                    option.value = '${unit}';
                                                    option.text = '${unit}';
                                                    select.appendChild(option);
                                                    option = document.createElement('option');
                                                    </g:each>
                                                    div2.appendChild(label2);
                                                    div2.appendChild(select);
                                                    div.appendChild(div2);
                                                    // create Amount
                                                    let div3 = document.createElement('div');
                                                    div3.setAttribute('id', prefix + 'AmountRow' + rowId);
                                                    div3.setAttribute('name', prefix + 'AmountRow' + rowId);
                                                    div3.setAttribute('style', 'position:relative;display:block;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                                    //div3.setAttribute('class','form-control');
                                                    let label3 = document.createElement('label');
                                                    label3.setAttribute('for', prefix + 'Amount' + rowId);
                                                    label3.innerHTML = '${g.message(code:'drink.create.amount', default:'Amount')}' + '&nbsp;<span class=\'required-indicator\'>*</span>';
                                                    input = document.createElement('input');
                                                    input.setAttribute('type', 'number');
                                                    input.setAttribute('step', 'any');
                                                    input.setAttribute('id', prefix + 'Amount');
                                                    input.setAttribute('name', prefix + 'Amount' + rowId);
                                                    input.setAttribute('class', 'form-control');
                                                    input.setAttribute('style', 'width:75px;background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};');
                                                    input.setAttribute('required', 'true');
                                                    if (ingredientAmt !== '') input.setAttribute('value',ingredientAmt);
                                                    div3.appendChild(label3);
                                                    div3.appendChild(input);
                                                    div.appendChild(div3);
                                                    // create X button
                                                    let div4 = document.createElement('div');
                                                    div4.setAttribute('id', prefix + 'CancelRow' + rowId);
                                                    div4.setAttribute('name', prefix + 'CancelRow' + rowId);
                                                    div4.setAttribute('style', 'padding:5px;position:relative;display:block;margin-top:22px;margin-bottom:22px;color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};');
                                                    let a = document.createElement('a');
                                                    a.setAttribute('class', 'btn btn-outline-danger btn-xs');
                                                    a.setAttribute('href', 'javascript:removeRow("'+prefix + 'Row' + rowId + '")');
                                                    a.setAttribute('style', 'color:${darkMode?'white':'black'};text-decoration:none;');
                                                    let bold = document.createElement('B');
                                                    let X = document.createTextNode('X');
                                                    bold.appendChild(X);
                                                    a.appendChild(bold);
                                                    div4.appendChild(a);
                                                    div.appendChild(div4);
                                                    // used to identify as added by clicking Edit Me
                                                    if (ingredient !== '' && ${darkMode}) { nameDiv.style.backgroundColor = 'darkblue'; div2.style.backgroundColor = 'darkblue'; div3.style.backgroundColor = 'darkblue'; }
                                                    else if (ingredient !== '' && ${!darkMode}) { nameDiv.style.backgroundColor = 'lightblue'; div2.style.backgroundColor = 'lightblue'; div3.style.backgroundColor = 'lightblue'; }
                                                    $('#'+tbody).append(div);
                                                }
                                                function removeRow(trId) {
                                                    console.log("clicked X... removing rowId " + rowId)
                                                    $('#'+trId).remove();
                                                    rowId--;
                                                    // decrease field set size as table shrinks until 0 rows. then it returns to original height
                                                    if (rowId <= 7) {
                                                        let tableFieldSetHeight = document.getElementById("ingredientFieldSet").style.height;
                                                        tableFieldSetHeight = tableFieldSetHeight.replaceAll("px","");
                                                        let newFieldSetHeight = rowId === 0 ? 75 : Number.parseInt(tableFieldSetHeight) - 70;
                                                        document.getElementById("ingredientFieldSet").style.height = newFieldSetHeight+"px";
                                                        if (rowId === 0) {
                                                            $("#ingredientErrorMessagesDiv > h3").hide();
                                                        }
                                                    }
                                                    console.log("rowId count is at " + rowId)
                                                }
                                            </script>
                                            <!--
                                            New rows will be added here
                                            -->
                                        </div>
                                    </fieldset>
                                </form>
                                <div class="formfield" style="margin-top:25px;padding-left:50%;">
                                    <button id="createDrink" class="btn btn-outline-primary" type="submit" form="newDrink"><g:message code="default.button.create.label" default="Create"/></button>
                                </div>
                            </g:form>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
        <script type="text/javascript">
            function addIngredient(ingredientId) {
                document.getElementById('ingredient'+ingredientId).checked = true;
                console.log('checkbox for ingredient'+ingredientId+ ' checked');
                let addButton = document.getElementById('addIngredientBtn'+ingredientId);
                let removeButton = document.getElementById('removeIngredientBtn'+ingredientId);
                addButton.classList.remove('btn-outline-info');
                addButton.classList.add('btn-success','fa-solid','fa-check','btn-xs');
                addButton.innerHTML = "";
                removeButton.hidden = false;
            }
            function removeIngredient(ingredientId, button) {
                document.getElementById('ingredient'+ingredientId).checked = false;
                console.log('checkbox for ingredient'+ingredientId+ ' unchecked');
                let addButton = document.getElementById('addIngredientBtn'+ingredientId);
                let removeButton = document.getElementById('removeIngredientBtn'+ingredientId);
                addButton.classList.remove('btn-success', 'fa-solid', 'fa-check');
                addButton.classList.add('btn-outline-info');
                addButton.innerHTML = "${g.message(code:'default.button.add.label', default:'Add')}";
                removeButton.hidden = true;
            }
            let tableFieldSetHeight = document.getElementById("ingredientFieldSet").style.height;
            function isValid() {
                //alert('isValid');
                //let tableRows = $("#ingredientTableDiv > tbody > tr");
                let tableRows = $("#ingredientTableDiv > div");
                let numberOfRows = tableRows.length
                console.log('rows: ' + numberOfRows);
                let successCount = 0;
                let failCount = 0;
                let ajaxCalls = 0;
                tableRows.each(function () {
                    let row = $(this);
                    row.removeClass("errors");
                    //let cellValue1 = row.find('td:nth-child(1) > input').val();
                    //let cellValue2 = row.find('td:nth-child(2) > select > option:selected').val();
                    //let cellValue3 = row.find('td:nth-child(3) > input').val();
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
                        url: "${createLink(controller:'drink', action:'validateIngredients')}",
                        data: {
                            ingredientName: cellValue1,
                            ingredientUnit: cellValue2,
                            ingredientAmount: cellValue3,
                            apiCallCount: ++ajaxCalls
                        },
                        statusCode: {
                            200: function(data) {
                                console.log(JSON.stringify(data));
                                successCount += 1;
                            },
                            400: function(data) {
                                failCount += 1;
                                console.log('failCount:' + failCount);
                                console.log(data);
                                console.log(JSON.parse(JSON.stringify(data)));
                                let response = JSON.parse(data['responseText']);
                                //let response = JSON.parse(JSON.stringify(data['responseJSON']))
                                //let response = JSON.parse(JSON.stringify(data))
                                let message = "Some ingredients have already been created!";
                                let addedHeight = 250+(numberOfRows*50);
                                if (1 === failCount) {
                                    addedHeight = 250;
                                    message = response.message;
                                }
                                console.log("FAILED: " + message);
                                row.addClass("errors");
                                let errorMessageDivH3 = $("#ingredientErrorMessagesDiv > h3")
                                errorMessageDivH3.show();
                                let errorMessage = errorMessageDivH3.html(message);
                                errorMessage.addClass("errors");
                                tableFieldSetHeight = tableFieldSetHeight.replaceAll("px","");
                                let newFieldSetHeight = Number.parseInt(tableFieldSetHeight) + addedHeight;
                                document.getElementById("ingredientFieldSet").style.height = newFieldSetHeight+"px";
                            },
                            403: function(data) {
                                console.log(JSON.stringify(data));
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
                let result = successCount === numberOfRows
                console.log("SuccessCount:"+successCount + "===" + numberOfRows+":NumberOfRows ==> " + result);
                return result;
            }
            $(document).ready(function() {
                console.log("drink create loaded");
                if ("${params?.ingredients instanceof String[]}" === "true") {
                    let ingredients = Array.of("${params?.ingredients as List<String>}");
                    ingredients.toString().split(',').forEach(id => {
                        let use = id.replace('[','').trim();
                        use = use.replace(']','').trim();
                        console.log("calling addIngredient("+use+")");
                        addIngredient(use);
                    });
                } else {
                    addIngredient("${params?.ingredients}");
                }
            });
        </script>
    </body>
</html>
