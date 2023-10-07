<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Edit User</title>
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
            .btn-xs
            {
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
                width:550px;
            }
            fieldset::before {
                content: "Use this form to edit a user";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
            fieldset .no-before::before {
                content: "";
            }
            ::-webkit-scrollbar {
                -webkit-appearance: none;
                width: 7px;
            }
            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgba(0, 0, 0, .5);
                box-shadow: 0 0 1px rgba(255, 255, 255, .5);
            }
            #uploadPhoto1,
            #uploadPhoto2 {
                width: 100px;
                justify-content: center;
                display: inline-flex;
                text-align: center;
                overflow: hidden;
                cursor: pointer;
                position: relative;
            }
            #photo {
                opacity:0;
                filter: alpha(opacity=0);
                width: 100px;
                display: none;
                cursor: pointer;
                vertical-align: center;
                left:0;
                top:20px;
            }
        </style>
    </head>
    <g:set var="user" scope="request" value="${message(code: 'user.label', default: 'User')}" />
    <g:set var="darkMode" value="${darkMode}"/>
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
                        <g:render template="../navigation" model="[user:user]"/>
                    </div>
                </div>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.user}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.user}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>
                <div id="edit-user" class="container">
                    <div style="display:inline-flex;text-align:left;">
                        <fieldset style="border:thick solid #007bff;">
                            <legend style="margin-left:25px;padding-left:10px;width:auto;color:${darkMode?'white':'black'};">
                                <g:message code="default.edit.label" args="[user]"/>&emsp14;
                                <hr style="height:1px;background-color:#007bff;">
                            </legend>
                            <g:form resource="${this.user}" method="put" name="updateUser" enctype="multipart/form-data">
                                <div style="display:block;justify-content:center;color:${darkMode?'white':'black'};">
                                <g:if test="${!user.photo}"><div id="user" style="width:100%;"></g:if>
                                <g:else><div id="user" style="width:50%;float:left;"></g:else>
                                <div class="formfield" id="firstName">
                                    <label for='firstName'>First Name</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="firstName" value="${user.firstName}" required=""/>
                                    </div>
                                </div>
                                <div class="formfield" id="lastName">
                                    <label for='lastName'>LastName</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="lastName" value="${user.lastName}" required=""/>
                                    </div>
                                </div>
                                <div class="formfield" id="email">
                                    <label for='email'>Email</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="email" value="${user.email}" required=""/>
                                    </div>
                                </div>
                                <div class="formfield" id="cellphone">
                                    <label for='cellphone'>Cellphone</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="cellphone" value="${user.mobileNumber}" />
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for='password'>Password</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="password" value="" id="password" />
                                    </div>
                                </div>
                                <div class="formfield">
                                    <label for='passwordConfirm'>Confirm Password</label>
                                    <div class="input-wrapper">
                                        <input type="text" name="passwordConfirm" value="" id="passwordConfirm" />
                                    </div>
                                </div>
                                <g:if test="${!user.photo}">
                                    <div class="formfield">
                                        <label for='photo'><span>&nbsp;&nbsp;</span> Photo</label>
                                        <div id="uploadPhoto1" onclick="upload();" class="btn btn-outline-primary btn-xs" style="width:43%;height:32px;">
                                            <span id="uploadSpan" style="margin-top:5px;">Upload</span>
                                            <input class="input-wrapper" type="file" name="photo" id="photo" style="vertical-align:middle;text-align:center;"/>
                                        </div>
                                    </div>
                                    <g:if test="${user.drinks.size() > 0}">
                                        <div class="formfield" id="drinks" style="display:block;">
                                            <label>Users Drinks</label><br>
                                            <div style="margin-top:-15px;height:100px;overflow-y:auto;">
                                                <g:each in="${user.drinks}" var="drink" status="i">
                                                    <div style="display:block;">
                                                        <div style="display:inline-flex;width:100%;">
                                                            <button id="removeDrinkBtn${drink.id}" type="button" class="btn btn-outline-danger btn-xs" onclick="removeDrink('${drink.id}');">Remove</button>
                                                            <input hidden type="checkbox" name="drinks" id="drink${drink.id}" checked value="${drink.id}"/>
                                                            <input style="width:100%;" type="text" disabled name="drink" id="${drink.id}" checked value="${drink}"/>
                                                        </div>
                                                    </div>
                                                </g:each>
                                            </div>
                                        </div>
                                    </g:if>
                                </g:if>
                                </div>
                                <g:if test="${user.photo}">
                                    <div id="uploadPhoto2" style="width:50%;float:right;">
                                        <img id="img" src="data:image/png;base64, ${user.photo}" style="margin-left:40px;width:200px;height:200px;" alt="photo"/>
                                        <a id="xBtn" href="javascript:clearImage();"><b>X</b></a>
                                    </div>
                                </g:if>
                                </div>
                                <div class="formfield" style="margin-top:25px;text-align:center;">
                                    <a style="margin-right:10px;" class="btn btn-outline-danger" id="cancel" href="${createLink(uri: "/user/show/${user.id}")}"><g:message code="default.cancel.label" default="Cancel"/></a>
                                    <button id="updateUser" class="btn btn-outline-primary" type="submit" form="updateUser">Update</button>
                                </div>
                                <input type="hidden" name="clearedImage" id="clearedImage" value="false"/>
                                <input type="hidden" name="id" id="id" value="${user.id}"/>
                            </g:form>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("edit-user loaded");
            });
            function removeDrink(drinkId) {
                if ( document.getElementById('drink'+drinkId).checked ) {
                    document.getElementById('drink'+drinkId).checked = false;
                    console.log('checkbox for drink'+drinkId+ ' unchecked');
                    document.getElementById('removeDrinkBtn'+drinkId).classList.remove('btn-outline-danger');
                    document.getElementById('removeDrinkBtn'+drinkId).classList.add('btn-outline-info');
                    document.getElementById('removeDrinkBtn'+drinkId).innerHTML = "Will be removed"
                } else {
                    document.getElementById('drink'+drinkId).checked = true;
                    console.log('checkbox for drink'+drinkId+ ' checked');
                    document.getElementById('removeDrinkBtn'+drinkId).classList.remove('btn-outline-info');
                    document.getElementById('removeDrinkBtn'+drinkId).classList.add('btn-outline-danger');
                    document.getElementById('removeDrinkBtn'+drinkId).innerHTML = "Remove"
                }
            }
            function upload() {
                document.getElementById('photo').click();
                document.getElementById('photo').oninput = function() {
                    document.getElementById('uploadSpan').innerText = 'Uploaded';
                };
            }
            function clearImage() {
                console.log("before clearedImage value: " + $('#clearedImage').val());
                $('#clearedImage').val(true);
                console.log("after clearedImage value: " + $('#clearedImage').val());
                let image = $('#uploadPhoto2');
                $('#img').remove();
                $('#xBtn').remove();

                let div = document.createElement('div');
                div.setAttribute('id', 'uploadPhoto1');
                div.setAttribute('onclick', 'upload();');
                div.setAttribute('class', 'btn btn-outline-primary btn-xs');
                div.setAttribute('style', 'margin-left:40px;width:75%;height:32px;');
                let span = document.createElement('span');
                span.setAttribute('style', 'margin-top:5px;');
                span.setAttribute('id', 'uploadSpan');
                span.innerText = 'Upload';
                let input = document.createElement('input');
                input.setAttribute('id', 'photo');
                input.setAttribute('class', 'input-wrapper');
                input.setAttribute('type', 'file');
                input.setAttribute('name', 'photo');
                input.setAttribute('style', 'vertical-align:middle;text-align:center;');
                input.innerText = 'Test';

                div.appendChild(span);
                div.appendChild(input);
                image.append(div);

            }
        </script>
    </body>
</html>