<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title><g:message code="forgot.password" default="Forgot Password"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <g:include view="includeAll.gsp"/>
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
        text-align: center;
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
        #clear: left;
        margin: 0;
        #padding: 4px 0 3px 0;
        #padding-left: 105px;
        margin-bottom: 20px;
        height: 1%;
        text-align: center;
    }

    #login .inner .cssform input[type="text"] {
        width: 200px;
    }

    #login .inner .cssform label {
        font-weight: bold;
        #float: left;
        text-align: right;
        margin-left: -105px;
        width: 110px;
        padding-top: 3px;
        padding-right: 10px;
    }

    #login #submit {
        #margin-left:15px;
    }

    #login .inner .login_message {
        padding: 6px 25px 20px 25px;
        color: #c33;
        text-align:left;
    }

    #login .inner .text_ {
        width: 200px;
    }

    #login .inner .chk {
        height: 12px;
    }
    </style>
</head>

<body>
<div id="login">
    <div class="inner">
        <div class="fheader"><g:message code="enter.email" default="Enter Your Email Address"/></div>

        <g:if test='${flash.message}'>
            <div class="login_message">${flash.message}</div>
        </g:if>

        <g:form id="forgotPasswordForm" controller="emails" action="forgotPasswordEmail" method="POST" class="cssform" autocomplete="off">
            <p style="padding: 4px 0 3px 0;padding-left: 70px;">
                <label for="username"><g:message code='springSecurity.login.username.label'/>:</label>
                <input type="text" class="text_" name="${usernameParameter ?: 'username'}" id="username" required/>
            </p>
            <p>
                <a class="btn btn-secondary" id="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                &emsp;
                <input class="btn btn-primary" type="submit" id="submit" value="${g.message(code:"send.email", default:"Send Email")}"/>
            </p>
        </g:form>
    </div>
</div>
<script>
    (function() {
        console.log("Forgot Password Form");
        $("#username").focus();
    })();
</script>
</body>
</html>