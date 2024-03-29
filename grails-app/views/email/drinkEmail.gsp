%{-- THIS HAS BEEN MOVED TO EMAILS! --}%
%{--<!DOCTYPE html>--}%
%{--<html>--}%
%{--<head>--}%
%{--    <title>Drink Email</title>--}%
%{--    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />--}%
%{--    <meta name="viewport" content="width=device-width" />--}%
%{--</head>--}%

%{--<body>--}%
%{--Hi ${rName},--}%
%{--<br/><br/>--}%
%{--${user.firstName} ${user.lastName} used Mixology to create this drink and thought you may like it.--}%
%{--<hr/>--}%
%{--<h3>${drink.name}&emsp;${drink.symbol}</h3>--}%
%{--<i>${drink.mixingInstructions}</i>--}%
%{--<p>--}%
%{--    <img src="data:image/png;base64, ${image}" alt="glassImage" style="float:left;width:42px;height:42px;">--}%
%{--    Suggested Glass: ${drink.suggestedGlass}--}%
%{--</p>--}%
%{--<br/>--}%
%{--<h4>Ingredients (${drink.ingredients.size()})</h4>--}%
%{--<ol>--}%
%{--    <%  drink.ingredients.each { %>--}%
%{--    <li>${it.prettyName()}</li>--}%
%{--    <% } %>--}%
%{--</ol>--}%
%{--<hr/>--}%
%{--<p>Make sure you let your friend know how it turns out.</p>--}%
%{--<% if (userExists) {--}%
%{--    if ( testing ) { %>--}%
%{--        <p>If you want, you can save this drink to your collection by clicking <a href="http://localhost:5009/drink/saveSharedDrink?drinkId=${drink.id}&userEmail=${rEmail}">save</a>.</p>--}%
%{--    <% } else { %>--}%
%{--        <p>If you want, you can save this drink to your collection by clicking <a href="https://mixology.com/drink/saveSharedDrink?drinkId=${drink.id}&userEmail=${rEmail}">save</a>.</p>--}%
%{--    <% } %>--}%
%{--<% } else { %>--}%
%{--    <p>If you want to create your own drinks, you can create your own account with Mixology by clicking <g:createLinkTo controller="user" action="create">here</g:createLinkTo>.</p>--}%
%{--<% } %>--}%
%{--</body>--}%
%{--</html>--}%