<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<p style="font-size:35px;margin:0;color:#a60000;"><b>NUMBER, SYMBOL, AND NAME QUICK REFERENCE CHART</b></p>
<div style="display:inline-flex;">
    <div style="display:block;">
        <!-- Change from i=1; i<13 i++ when done. Remove if-->
        <% int count = 0; for (int i=0; i<=30; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
        <% count++; if (count == 12) break; } %>
    </div>
    <!-- Uncomment when done creating all drinks -->
    <div style="display:block;">
        <% for (int i=13; i<25; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
    <!-- Uncomment when done creating all drinks -->
    <div style="display:block;">
        <% for (int i=25; i<37; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
    <!-- Uncomment when done creating all drinks -->
    <div style="display:block;">
        <% for (int i=37; i<49; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
    <!-- Uncomment when done creating all drinks -->
    <div style="display:block;">
        <% for (int i=49; i<59; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
        <div style="display:inline-flex;padding:0;">
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
        </div><br/>
        <% } %>
    </div>
</div>