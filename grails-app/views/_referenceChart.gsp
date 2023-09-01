<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/11/23
  Time: 7:22 PM
--%>
<%@ page import="enums.*; mixology.Drink; mixology.DrinkService;" %>
<p style="font-size:35px;margin:0;color:#a60000;"><b>NUMBER, SYMBOL, AND NAME QUICK REFERENCE CHART</b></p>
<div style="display:inline-flex;">
    <div style="display:block;">
        <% for (int i=1; i<13; i++) { Drink drink = Drink.findById(i); %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.id}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
        <% } %>
    </div>
    <div style="display:block;">
        <% for (int i=13; i<25; i++) { Drink drink = Drink.findById(i); %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink.id}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
    <div style="display:block;">
        <% for (int i=25; i<37; i++) { Drink drink = Drink.findById(i); %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink.id}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
    <div style="display:block;">
        <% for (int i=37; i<49; i++) { Drink drink = Drink.findById(i); %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink.id}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
    <div style="display:block;">
            <% for (int i=49; i<59; i++) { Drink drink = Drink.findById(i); %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink.id}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
            <% } %>
        </div>
</div>