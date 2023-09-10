<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/31/23
  Time: 4:24 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="mixology.User;" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Show User</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <g:set var="user" scope="request" value="${message(code: 'user.label', default: 'User')}" />
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
                width:550px;
            }
            fieldset::before {
                content: "";
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

    <body>
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <div class="nav" role="navigation">
                        <ul>
                            <li><a class="fa fa-home" href="${createLink(uri: '/')}">&nbsp;<g:message code="default.home.label"/></a></li>
                            <li><g:link class="fa fa-home" controller="drink" action="showCustomDrinks">&nbsp;Custom Home</g:link></li>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <li><g:link class="fa-solid fa-list" controller="drink" action="index">&nbsp;Drinks</g:link></li>
                                <li><g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;Your Drinks</g:link></li>
                                <li><g:link class="fa-solid fa-list" controller="ingredient" action="index">&nbsp;Ingredients</g:link></li>
                                <li><g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;Your Ingredients</g:link> </li>
                            </sec:ifAnyGranted>
                            <sec:ifAnyGranted roles="ROLE_USER">
                                <li><g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;Your Drinks</g:link></li>
                                <li><g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;Your Ingredients</g:link> </li>
                            </sec:ifAnyGranted>
                            <li><g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="create">&nbsp;New Drink</g:link></li>
                            <li><g:link class="fa-solid fa-jar-wheat" controller="ingredient" action="create">&nbsp;New Ingredient</g:link></li>
                        </ul>
                    </div>
                </section>
                <div id="show-user" class="col-12 scaffold-show" role="main">
                    <h1>Show User</h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <div style="display:flex;justify-content:center;">
                        <div style="display:block;justify-content:center;">
                            <fieldset style="border:thick solid #007bff;">
                                <legend style="margin-left:25px;padding-left:10px;width:auto;">
                                    ${user.toString()} &emsp14;
                                    <hr style="height:1px;background-color:#007bff;">
                                </legend>
                                <div style="display:flex;justify-content:center;">
                                    <div id="user" style="width:50%;float:left;">
                                        <div class="formfield" id="firstName">
                                            <label for='firstName'>First Name</label>
                                            <div class="input-wrapper">
                                                <input type="text" disabled name="firstName" value="${user.firstName}"/>
                                            </div>
                                        </div>
                                        <div class="formfield" id="lastName">
                                            <label for='lastName'>LastName</label>
                                            <div class="input-wrapper">
                                                <input type="text" disabled name="lastName" value="${user.lastName}"/>
                                            </div>
                                        </div>
                                        <div class="formfield" id="email">
                                            <label for='email'>Email</label>
                                            <div class="input-wrapper">
                                                <input type="text" disabled name="email" value="${user.email}"/>
                                            </div>
                                        </div>
                                        <div class="formfield" id="cellphone">
                                            <label for='cellphone'>Cellphone</label>
                                            <div class="input-wrapper">
                                                <input type="text" disabled name="cellphone" value="${user.mobileNumber}"/>
                                            </div>
                                        </div>
                                        <div class="formfield">
                                            <label for='password'>Password</label>
                                            <div class="input-wrapper">
                                                <div class="input-wrapper">
                                                    <input type="password" disabled name="password" value="${user.password}" id="password" />
                                                    <span id="togglepassword" onclick="showPassword('password');" class="fa fa-fw fa-eye" style="position:relative;margin-top:7px;margin-left:-40px;float:right;"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <g:if test="${user.drinks.size() > 0}">
                                            <div class="formfield" id="drinks" style="display:block;">
                                                <label>Users Drinks</label><br>
                                                <div style="margin-top:-25px;height:100px;overflow-y:auto;">
                                                    <g:each in="${user.drinks.sort{ it.id } }" var="drink" status="i">
                                                        <div style="display:block;">
                                                            <input hidden type="checkbox" disabled name="drink" id="${drink.id}" checked value="${drink}"/>
                                                            <g:link action="show" controller="drink" params='[id:"${drink.id}"]'>${drink}</g:link> : ${drink.id}
                                                        </div>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </g:if>
                                        <g:else>
                                            <div class="formfield" id="nodrinks">
                                                <div class="input-wrapper">
                                                    <p>After you create or copy a drink, they will appear here.</p>
                                                </div>
                                            </div>
                                        </g:else>
                                    </div>
                                    <div id="photo" style="width:50%;float:right;text-align:center;">
                                        <g:if test="${!user.photo || user.photo == ''}">
                                            <p>No image uploaded!</p>
                                        </g:if>
                                        <g:else>
                                            <img src="data:image/png;base64, ${user.photo}" style="margin-left:40px;width:200px;height:200px;" alt="photo"/>
                                        </g:else>
                                    </div>
                                </div>
                            </fieldset>
                            <g:form resource="${this.user}" method="DELETE">
                                <fieldset class="buttons">
                                    <g:link class="fa-solid fa-pen-to-square" action="edit" resource="${this.user}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                                    <i class="fa-solid fa-trash-can">
                                        <input type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                    </i>
                                    </fieldset>
                            </g:form>


                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            $(document).ready(function() {
                console.log("show-user page loaded");
            });
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