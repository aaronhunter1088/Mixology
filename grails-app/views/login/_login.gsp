<%@ page import="mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <title>Login</title>
    <style>
        #login {
            padding: 0;
            text-align: center;
        }
        #login .inner {
            text-align: left;
            font-size: 12px;
            font-weight: bold;
        }
        #login .cssform .inner p {
            clear: left;
            margin: 0;
            padding-left: 105px;
            height: 1%;
        }
        #login .cssform .inner input[type="text"] {
            width: 150px;
        }
        #login .cssform .inner input[type="password"] {
            width: 150px;
        }
        #login .cssform .inner input[type="button"] {
            font-size: 8px;
            margin-top:-10px;
            padding-left: 94px;
        }
        #login .cssform .inner label {
            font-weight: bold;
            float: left;
            text-align: right;
            margin-left: -105px;
            width: 110px;
            padding-top: 3px;
            padding-right: 10px;
        }
        #login #loginButtons {
            display: inline-flex;
            text-align:center;
            font-size:12px;
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
<div id="login" class="container-fluid">
    <div class="inner">
        <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" class="cssform" autocomplete="off">
            <p>
                <label for="username"><g:message code='springSecurity.login.username.label'/>:</label>
                <input type="text" class="text_" name="${usernameParameter ?: 'username'}" id="username"/>
            </p>
            <p>
                <label for="password"><g:message code='springSecurity.login.password.label'/>:</label>
                <input type="password" class="text_" name="${passwordParameter ?: 'password'}" id="password"/><br/>
            </p>
            <g:if test="${false}">
                <p id="rememberMePlaceholder">
                    <input type="checkbox" class="chk" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>/>
                    <label for="remember_me"><g:message code='springSecurity.login.remember.me.label'/></label>
                </p>
            </g:if>
            <p>
                <input class="btn btn-primary btn-xs" type="submit" id="submit" value="${message(code:'springSecurity.login.button')}" formaction="${postUrl ?: '/login/authenticate'}" />
                <input class="btn btn-success btn-xs" type="submit" id="create" value="${message(code:'default.register.label')}" formaction="${createLink(controller:'user',action:'create')}" />
                <input class="btn btn-outline-danger btn-xs" type="submit" id="forgot" value="${message(code:'default.forgotPassword.label')}" formaction="${createLink(controller:'user',action:'forgotPassword')}" />
            </p>
        </form>
    </div>
</div>
<script>
    (function() {
        document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
    })();
</script>



