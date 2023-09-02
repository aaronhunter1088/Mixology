<%@ page contentType="text/html;charset=UTF-8" %>
<head>
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
        #login .inner .cssform p {
            clear: left;
            margin: 0;
            padding-left: 105px;
            height: 1%;
        }
        #login .inner .cssform input[type="text"] {
            width: 150px;
        }
        #login .inner .cssform input[type="password"] {
            width: 150px;
        }
        #login .inner .cssform input[type="button"] {
            font-size: 8px;
            margin-top:-10px;
            padding-left: 94px;
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
            <p>
                <input class="btn btn-group-sm btn-xs" type="button" id="forgot" value="<g:message code="default.forgotPassword.label"/>" onclick="${createLink(controller:'user', action:'forgotPassword')}"/>
            </p>

            %{--                    <p>--}%
            %{--                        <label for="coordinateValue">${position}</label>--}%
            %{--                        <input type="hidden" name="coordinatePosition" id="coordinatePosition" value="${position}"/>--}%
            %{--                        <input type="text" class="text_" name="coordinateValue" id="coordinateValue"/>--}%
            %{--                    </p>--}%

            <p id="rememberMePlaceholder">
                <input type="checkbox" class="chk" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>/>
                <label for="remember_me"><g:message code='springSecurity.login.remember.me.label'/></label>
            </p>
            <input class="btn btn-primary btn-xs" style="" type="submit" id="submit" value="${message(code: 'springSecurity.login.button')}"/>
        </form>
    </div>
</div>
<script>
    (function() {
        document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
    })();
</script>



