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
        <div id="content" role="main">
            <div class="container">
                <section class="row">
                    <a href="#show-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                    <div class="nav" role="navigation">
                        <ul>
                            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                            <li><g:link class="list" controller="drink" action="showCustomDrinks">Custom Drinks</g:link></li>
                            <li><g:link class="list" action="index">User List</g:link></li>
                            <li><g:link class="create" action="create">New User</g:link></li>
                        </ul>
                    </div>
                </section>
                <section class="row">
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
                                                    <input type="password" disabled name="password" value="${user.password}" id="password"/>
                                                </div>
                                            </div>
                                            <g:if test="${user.drinks.size() > 0}">
                                                <div class="formfield" id="drinks">
                                                    <label>Users Drinks</label><br>
                                                    <div style="margin-top:-25px;height:100px;overflow-y:auto;">
                                                        <g:each in="${user.drinks}" var="drink" status="i">
                                                            <div style="display:block;">
                                                                <input type="text" disabled name="drink" id="${drink.id}" checked value="${drink}"/>
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
                                        <div id="photo" style="width:50%;float:right;">
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
                                        <g:link class="edit" action="edit" resource="${this.user}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                                        <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                    </fieldset>
                                </g:form>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </body>
</html>