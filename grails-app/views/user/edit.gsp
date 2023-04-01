<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/31/23
  Time: 11:57 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="mixology.User;" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Edit User</title>
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
            <section class="row">
                <a href="#show-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="list" action="index">User List</g:link></li>
                        <li><g:link class="create" action="create">New User</g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="edit-user" class="col-12 scaffold-show" role="main">
                    <h1>Edit User</h1>
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
                                <g:form resource="${this.user}" method="put" name="updateUser" enctype="multipart/form-data">
                                    <div style="display:block;justify-content:center;">
                                        <div id="user" style="width:50%;float:left;">
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
                                                    <input type="text" name="cellphone" value="${user.mobileNumber}" required="" />
                                                </div>
                                            </div>
                                            <div class="formfield">
                                                <label for='password'><span class='required-indicator'>*</span> Password</label>
                                                <div class="input-wrapper">
                                                    <input type="text" name="password" value="${user.password}" required="" id="password" />
                                                </div>
                                            </div>
                                            <div class="formfield">
                                                <label for='passwordConfirm'><span class='required-indicator'>*</span> Confirm Password</label>
                                                <div class="input-wrapper">
                                                    <input type="text" name="passwordConfirm" value="${user.passwordConfirm}" required="" id="passwordConfirm" />
                                                </div>
                                            </div>
                                            <g:if test="${!user.photo}">
                                                 <div id="uploadPhoto" onclick="document.getElementById('photo').click();" class="btn btn-outline-primary btn-xs" style="width:75%;height:32px;">
                                                    <span style="margin-top:5px;">Upload</span>
                                                    <input class="input-wrapper" type="file" name="photo" id="photo" style="vertical-align:middle;text-align:center;"/>
                                                </div>
                                            </g:if>
                                        </div>
                                        <div>
                                            <g:if test="${user.photo}">
                                                <div id="uploadPhoto" style="width:50%;float:right;">
                                                    <img id="img" src="data:image/png;base64, ${user.photo}" style="width:200px;height:200px;" alt="photo"/>
                                                    <a id="xBtn" href="javascript:clearImage();"><b>X</b></a>
                                                </div>
                                            </g:if>
                                        </div>
                                    </div>
                                    <div class="formfield" style="margin-top:25px;padding-left:50%;">
                                        <button id="updateUser" class="btn btn-outline-primary" type="submit" form="updateUser">Update</button>
                                    </div>
                                </div>
                                </g:form>
                            </fieldset>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
            console.log("edit loaded");
        });
        function clearImage() {
            let image = $('#uploadPhoto');
            $('#img').remove();
            $('#xBtn').remove();
            let div = document.createElement('div');
            div.setAttribute('id', 'uploadPhoto');
            div.setAttribute('onclick', "document.getElementById('photo').click();");
            div.setAttribute('class', 'btn btn-outline-primary btn-xs');
            div.setAttribute('style', 'width:75%;height:32px;');
            let span = document.createElement('span');
            span.setAttribute('style', 'margin-top:5px;');
            span.innerText = 'Upload';
            let input = document.createElement('input');
            input.setAttribute('id', 'photo');
            input.setAttribute('class', 'input-wrapper');
            input.setAttribute('type', 'file');
            input.setAttribute('name', 'photo');
            input.setAttribute('style', 'vertical-align:middle;text-align:center;');

            div.appendChild(span);
            div.appendChild(input);
            image.append(div);
        }
    </script>
    </body>
</html>