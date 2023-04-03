<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 4/1/23
  Time: 8:34 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
%{--        <meta name="layout" content="${gspLayout ?: 'main'}"/>--}%
%{--        <title><g:message code='springSecurity.login.title'/></title>--}%
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Mixology</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <style>
            #login {
                margin: 15px 0px;
                padding: 0px;
                text-align: center;
            }

            #login .inner {
                width: 340px;
                padding-bottom: 6px;
                margin: 60px auto;
                text-align: left;
                border: 1px solid #aab;
                background-color: #f0f0fa;
                -moz-box-shadow: 2px 2px 2px #eee;
                -webkit-box-shadow: 2px 2px 2px #eee;
                -khtml-box-shadow: 2px 2px 2px #eee;
                box-shadow: 2px 2px 2px #eee;
            }

            #login .inner .fheader {
                padding: 18px 26px 14px 26px;
                background-color: #f7f7ff;
                margin: 0px 0 14px 0;
                color: #2e3741;
                font-size: 18px;
                font-weight: bold;
            }

            #login .inner .cssform p {
                clear: left;
                margin: 0;
                padding: 4px 0 3px 0;
                padding-left: 105px;
                margin-bottom: 20px;
                height: 1%;
            }

            #login .inner .cssform input[type="text"] {
                width: 120px;
            }

            #login .inner .cssform label {
                font-weight: bold;
                float: left;
                text-align: right;
                margin-left: -105px;
                width: 110px;
                padding-top: 3px;
                padding-right: 10px;
            }

            #login #remember_me_holder {
                padding-left: 120px;
            }

            #login #submit {
                margin-left: 15px;
            }

            #login #remember_me_holder label {
                float: none;
                margin-left: 0;
                text-align: left;
                width: 200px
            }

            #login .inner .login_message {
                padding: 6px 25px 20px 25px;
                color: #c33;
            }

            #login .inner .text_ {
                width: 120px;
            }

            #login .inner .chk {
                height: 12px;
            }
        </style>
    </head>

    <body>
        <div id="login">
            <div class="inner">
                <div class="fheader"><g:message code='springSecurity.login.header'/></div>

                <g:if test='${flash.message}'>
                    <div class="login_message">${flash.message}</div>
                </g:if>

                <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" class="cssform" autocomplete="off">
                    <p>
                        <label for="username"><g:message code='springSecurity.login.username.label'/>:</label>
                        <input type="text" class="text_" name="${usernameParameter ?: 'username'}" id="username"/>
                    </p>

                    <p>
                        <label for="password"><g:message code='springSecurity.login.password.label'/>:</label>
                        <input type="password" class="text_" name="${passwordParameter ?: 'password'}" id="password"/>
                    </p>

%{--                    <p>--}%
%{--                        <label for="coordinateValue">${position}</label>--}%
%{--                        <input type="hidden" name="coordinatePosition" id="coordinatePosition" value="${position}"/>--}%
%{--                        <input type="text" class="text_" name="coordinateValue" id="coordinateValue"/>--}%
%{--                    </p>--}%

                    <p id="remember_me_holder">
                        <input type="checkbox" class="chk" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>/>
                        <label for="remember_me"><g:message code='springSecurity.login.remember.me.label'/></label>
                    </p>

                    <p>
                        <a class="btn btn-secondary btn-xs" id="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                        <input class="btn btn-primary btn-xs" type="submit" id="submit" value="${message(code: 'springSecurity.login.button')}"/>
                    </p>
                </form>
            </div>
        </div>
        <script>
            (function() {
                document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
            })();
        </script>
    </body>
</html>