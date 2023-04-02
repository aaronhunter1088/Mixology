<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/31/23
  Time: 3:17 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Create User</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css">
    <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
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
            content: "Use this form to create a new user";
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

<body>
    <div id="content" role="main">
        <div class="container">
            <section class="row" id="navigation">
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
                <g:hasErrors bean="${this.user}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.user}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>
            </div>
            <section class="row">
                <div id="edit-user" class="col-12 scaffold-show" role="main">
                    <h1>Create User</h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <div style="display:flex;justify-content:center;">
                        <fieldset style="border:thick solid #007bff;">
                            <legend style="margin-left:25px;width:auto;">
                                &emsp14;<g:message code="default.create.label" args="[entityName]" />&emsp14;
                                <hr style="height:1px;background-color:#007bff">
                            </legend>
                            <g:form url="[controller:'user', action:'save']" id="newUser" name="newUser" enctype="multipart/form-data">
                                <div id="create-user" style="text-align:left;float:left;">
                                    <div class="formfield" style="">
                                        <label for='firstName'><span class='required-indicator'>*</span> First Name</label>
                                        <div class="input-wrapper">
                                            <input type="text" name="firstName" value="" required="" id="firstName" />
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='lastName'><span class='required-indicator'>*</span> Last Name</label>
                                        <div class="input-wrapper">
                                            <input type="text" name="lastName" value="" required="" id="lastName" />
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='email'><span class='required-indicator'>*</span> Email</label>
                                        <div class="input-wrapper">
                                            <input type="text" name="email" value="" required="" id="email" />
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='cellphone'><span class='required-indicator'>*</span> Cellphone</label>
                                        <div class="input-wrapper">
                                            <input type="text" name="cellphone" value="" required="" id="cellphone" />
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='password'><span class='required-indicator'>*</span> Password</label>
                                        <div class="input-wrapper">
                                            <input type="password" name="password" value="" required="" id="password" />
                                            <span id="togglepassword" onclick="showPassword('password');" class="fa fa-fw fa-eye" style="position:relative;margin-top:7px;margin-left:-40px;float:right;"></span>
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='passwordConfirm'><span class='required-indicator'>*</span> Confirm Password</label>
                                        <div class="input-wrapper">
                                            <input type="password" name="passwordConfirm" value="" required="" id="passwordConfirm" />
                                            <span id="togglepasswordConfirm" onclick="showPassword('passwordConfirm');" class="fa fa-fw fa-eye" style="position:relative;margin-top:7px;margin-left:-40px;float:right;"></span>
                                        </div>
                                    </div>
                                    <div class="formfield">
                                        <label for='photo'><span>&nbsp;&nbsp;</span> Photo</label>
                                        <div id="uploadPhoto" onclick="upload();" class="btn btn-outline-primary btn-xs" style="width:100%;height:32px;">
                                            <span id="uploadSpan" style="margin-top:5px;">Upload</span>
                                            <input class="input-wrapper" type="file" name="photo" id="photo" style="vertical-align:middle;text-align:center;"/>
                                        </div>
                                    </div>
                                    <div class="formfield" style="margin-top:25px;padding-left:50%;">
                                        <button id="createUser" class="btn btn-outline-primary" type="submit" form="newUser">Create</button>
                                    </div>
                                </div>
                            </g:form>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <script type="text/javascript">
        $(document).ready(function() {
            console.log("create-user page loaded");
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
    </script>
</body>
</html>