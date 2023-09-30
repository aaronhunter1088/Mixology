<%@ page import="enums.*; mixology.*; mixology.UserPasswordEncoderListener;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Show User</title>
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
                width: 0 !important
            }
            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgba(0, 0, 0, .5);
                box-shadow: 0 0 1px rgba(255, 255, 255, .5);
            }
            a {
                color: black;
            }
            a:visited {
                color: gray;
            }
            a:hover {
                color: gray;
            }
            a:active {
                color: coral;
            }
        </style>
    </head>
    <g:set var="darkMode" value="${currentUser.darkMode}"/>
    <g:if test="${darkMode}">
        <style>
            .input-wrapper > input {
                background-color:black;
                color:white;
            }
            a {
                color: white;
            }
        </style>
    </g:if>
    <body style="padding:50px;margin:0;background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};">
        <div id="content" class="" style="background-color:${darkMode?'black':'white'};color:${darkMode?'white':'black'};">
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
                <div id="show-user" class="container">
                    <div style="display:flex;justify-content:center;">
                        <div style="display:block;">
                            <fieldset style="border:thick solid #007bff;" class="no-before">
                                <legend style="margin-left:25px;padding-left:10px;width:auto;color:${darkMode?'white':'black'};">
                                    ${user.firstName} ${user.lastName} &emsp14;
                                    <hr style="height:1px;background-color:#007bff;">
                                </legend>
                                <div style="display:block;justify-content:center;">
                                    <div style="display:inline-flex;justify-content:center;">
                                        <div id="user" style="width:auto;color:${darkMode?'white':'black'};">
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
                                                        <input type="password" disabled name="password"
                                                            <g:if test="${showPassword}">
                                                                value="${UserPasswordEncoderListener.decrypt(user.passwordConfirm)}"
                                                            </g:if><g:else>
                                                            value="${user.passwordConfirm}"
                                                        </g:else>
                                                               id="password" />
                                                        <span id="togglepassword" onclick="showPassword('password');" class="fa fa-fw fa-eye" style="position:relative;margin-top:7px;margin-left:-40px;float:right;"></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="photo" style="width:50%;float:right;text-align:center;">
                                            <g:if test="${!user.photo || user.photo == ''}">
                                                <p>No image uploaded!</p>
                                            </g:if><g:else>
                                            <img src="data:image/png;base64, ${user.photo}" style="margin-left:40px;width:200px;height:200px;" alt="photo"/>
                                        </g:else>
                                        </div>
                                    </div>
                                    <div style="display:inline-flex;justify-content:center;">
                                        <div id="userDrinks" style="float:left;padding:10px 10px 0 10px;width:300px;color:${darkMode?'white':'black'};">
                                            <g:if test="${user.drinks.size() > 0}">
                                                <label>Users Drinks</label><br>
                                                <div style="height:100px;overflow-y:auto;">
                                                    <g:each in="${user.drinks.sort{ it.id } }" var="drink" status="i">
                                                        <div style="display:block;">
                                                            <g:link action="show" controller="drink" params='[id:"${drink.id}"]'>${drink}</g:link>
                                                        </div>
                                                    </g:each>
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
                                        <div id="userIngredients" style="float:right;padding:10px;text-align:right;color:${darkMode?'white':'black'};">
                                            <g:if test="${user.ingredients.size() > 0}">
                                                <label>Users Ingredients</label><br>
                                                <div style="height:100px;overflow-y:auto;text-align:right;">
                                                    <g:each in="${user.ingredients.sort{ it.id } }" var="ingredient" status="i">
                                                        <div style="display:block;padding-right:5px;">
                                                            <g:link action="show" controller="ingredient" params='[id:"${ingredient.id}"]'>${ingredient}</g:link>
                                                        </div>
                                                    </g:each>
                                                </div>
                                            </g:if>
                                            <g:else>
                                                <div class="formfield" id="noingredients">
                                                    <div class="input-wrapper">
                                                        <p>After you copy a drink, or create an ingredient, ingredients will appear here.</p>
                                                    </div>
                                                </div>
                                            </g:else>
                                        </div>
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
            </section>
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