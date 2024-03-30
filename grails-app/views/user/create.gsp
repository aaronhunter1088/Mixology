<%@ page import="org.springframework.context.i18n.LocaleContextHolder; enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><g:message code="user.create.user" default="Create a user"/></title>
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
                width:500px;
            }
            fieldset::before {
                content: "${g.message(code:'user.create.fieldset.message',default:'Use this form to create a new user')}";
                width:250px;
                text-align:right;
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
            #uploadPhoto {
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
    <g:set var="user" value="${g.message(code:'user.label', args:" ", default:'User')}" />
    <g:set var="darkMode" value="${params.darkMode}" />
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
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};">
            <section style="background-color:${darkMode?'black':'white'};">
                <div class="container">
                    <div id="navigation" style="display:flex;justify-content:center;">
                        <g:render template="../navigation" model="[user:currentUser]"/>
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
                <div id="create-user">
                    <div style="display:flex;justify-content:center;">
                        <fieldset style="border:thick solid #007bff;">
                            <legend style="margin-left:25px;width:auto;color:${darkMode?'white':'black'};">
                                &emsp14;<g:message code="default.create.label" args="[user]" />&emsp14;
                                <hr style="height:1px;background-color:#007bff">
                            </legend>
                            <g:form url="[controller:'user', action:'save']" id="newUser" name="newUser" enctype="multipart/form-data" onsubmit="return isValid();">
                                <g:hiddenField id="passwordsMatch" name="passwordsMatch" value="false" />
                                <div id="create-user" style="text-align:left;float:left;">
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='firstName'><span class='required-indicator'>*</span>&nbsp;<g:message code="user.create.first.name" default="First Name"/></label>
                                        <div class="input-wrapper">
                                            <input type="text" name="firstName" value="" required="" id="firstName" />
                                        </div>
                                    </div>
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='lastName'><span class='required-indicator'>*</span> <g:message code="user.create.last.name" default="Last Name"/></label>
                                        <div class="input-wrapper">
                                            <input type="text" name="lastName" value="" required="" id="lastName" />
                                        </div>
                                    </div>
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='email'><span class='required-indicator'>*</span> <g:message code="user.create.email" default="Email"/></label>
                                        <div class="input-wrapper">
                                            <input type="text" name="email" value="" required="" id="email" />
                                        </div>
                                    </div>
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='cellphone'>&emsp;<g:message code="user.create.cellphone" default="Cellphone"/></label>
                                        <div class="input-wrapper">
                                            <input type="text" name="cellphone" value="" id="cellphone" />
                                        </div>
                                    </div>
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='password'><span class='required-indicator'>*</span> <g:message code="springSecurity.login.password.label" default="Password"/></label>
                                        <div class="input-wrapper">
                                            <input type="password" name="password" value="" required="" id="password" />
                                            <span id="togglepassword" onclick="showPassword('password');" class="fa fa-fw fa-eye" style="position:relative;margin-top:7px;margin-left:-40px;float:right;"></span>
                                        </div>
                                    </div>
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='passwordConfirm'><span class='required-indicator'>*</span> <g:message code="springSecurity.login.confirm.password.label" default="Confirm Password"/></label>
                                        <div class="input-wrapper">
                                            <input type="password" name="passwordConfirm" value="" required="" id="passwordConfirm" />
                                            <span id="togglePasswordConfirm" onclick="showPassword('passwordConfirm');" class="fa fa-fw fa-eye" style="position:relative;margin-top:7px;margin-left:-40px;float:right;"></span>
                                        </div>
                                    </div>
                                    <div class="formfield" style="color:${darkMode?'white':'black'};">
                                        <label for='photo'><span>&nbsp;&nbsp;</span> <g:message code="user.create.photo" default="Photo"/></label>
                                        <div id="uploadPhoto" onclick="upload();" class="btn btn-outline-primary btn-xs" style="width:100%;height:32px;">
                                            <span id="uploadSpan" style="margin-top:5px;"><g:message code="default.button.upload.label" default="Upload"/></span>
                                            <input class="input-wrapper" type="file" name="photo" id="photo" style="vertical-align:middle;text-align:center;"/>
                                        </div>
                                    </div>
                                    <div class="formfield" style="text-align:center;margin-top:25px;padding-left:50%;color:${darkMode?'white':'black'};">
                                        <button id="createUser" class="btn btn-outline-primary" type="submit" form="newUser"><g:message code="default.button.create.label" default="Create"/></button>
                                    </div>
                                </div>
                            </g:form>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("create-user page loaded");
                $('#togglepassword').hide();
                $('#togglePasswordConfirm').hide();

                $('#password').on('keyup',function(){
                    if ($('#password').val() === '') $('#togglepassword').hide();
                    else $('#togglepassword').show();
                });
                $('#passwordConfirm').on('keyup',function(){
                    if ($('#passwordConfirm').val() === '') $('#togglePasswordConfirm').hide();
                    else $('#togglePasswordConfirm').show();
                });

            });
            function upload() {
                document.getElementById('photo').click();
                document.getElementById('photo').oninput = function() {
                    document.getElementById('uploadSpan').innerText = 'Uploaded';
                };
            }
            function showPassword(inputField) {
                let passwordField = document.getElementById(inputField);
                let icon = document.getElementById('toggle'+inputField)
                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    icon.setAttribute('class', '');
                    icon.setAttribute('class', 'fa fa-fw fa-eye-slash');
                } else {
                    passwordField.type = "password";
                    icon.setAttribute('class', '');
                    icon.setAttribute('class', 'fa fa-fw fa-eye');
                }
            }
            function isValid() {
                let matching = $('#password').val() === $('#passwordConfirm').val();
                $('#passwordsMatch').val(matching)
                console.log("Passwords match?: " + matching);
                return matching = true; // hidden field set
            }
        </script>
    </body>
</html>