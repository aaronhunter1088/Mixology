<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Mixology Reset Password</title>
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
        width: 150px;
    }

    #login .inner .chk {
        height: 12px;
    }
    </style>
</head>

<body>
<div id="login">
    <div class="inner">
        <div class="fheader">Enter Your New Password</div>

        <g:if test='${flash.message}'>
            <div class="login_message">${flash.message}</div>
        </g:if>
        <div id="passwordCriteria"><g:message code='default.invalid.user.password.criteria'/></div>
        <hr/>

        <g:form id="forgotPasswordForm" controller="user" action="updatePassword" onsubmit="return validateInputs();" method="POST" class="cssform" autocomplete="off">
            <input type="hidden" value="${params.token}" name="token"/>
            <p>
                <label for="password"><g:message code='springSecurity.login.password.label'/>:</label>
                <input type="password" class="text_" name="password" id="password"/>
            </p>
            <p>
                <label for="passwordConfirm">Password Confirm:</label>
                <input type="password" class="text_" name="passwordConfirm" id="passwordConfirm"/>
            </p>
            <p>
                <input class="btn btn-primary" type="submit" id="submit" value="Save Password"/>
            </p>
        </g:form>
    </div>
</div>
<script>
    (function() {
        console.log("Forgot Password Form");
        $("#username").focus();
    })();

    function validateInputs() {
        let p1 = $("#password").val();
        let p2 = $("#passwordConfirm").val();
        if (p1 !== p2) {
            alert("Passwords do not match. Please check your values and try again");
            return false;
        }
        else return true;
    }
</script>
</body>
</html>