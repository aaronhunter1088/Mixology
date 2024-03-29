<%@ page contentType="text/html"%>
<!DOCTYPE html>
<html>
<head>
    <title>Reset Password Email</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width" />
</head>

<body>
Hi ${user.firstName},
<br/><br/>
You are receiving this email because you requested to reset your password.
<br/><br/>
Click on the following link and enter your new password. This link will expire after 10 minutes.
<br/>
<% if ( testing ) { %>
    <p><a href="http://localhost:5009/login/resetPassword?token=${token}">Reset Password</a></p>
<% } else { %>
    <p><a href="https://mixology.com/login/resetPassword?token=${token}">Reset Password</a></p>
<% } %>
</body>
</html>