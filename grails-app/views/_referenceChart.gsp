<%@ page import="enums.*; mixology.Drink;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<p style="font-size:35px;margin:0;color:#a60000;"><b>NUMBER, SYMBOL, AND NAME QUICK REFERENCE CHART</b></p>
<div style="display:inline-flex; text-align:left;">
    <div style="display:block;">
        <%  int idEntifier = 1
            Drink drink
            while (idEntifier <= 58) {
                drink = Drink.read(idEntifier)
                if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA)
                if (idEntifier == 13 || ((idEntifier-1) % 12 == 0)) { %>
                    </div>
                    <div style="display:block;">
                <% } %>
                <div style="display:inline-flex;padding:0;">
                    <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'dodgerblue':'navy'};"><b>${drink.number}</b></p><br/>
                    <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'mediumseagreen':'#155724'};"><b>${drink.symbol}</b></p>
                    <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'white':'black'};">${drink.name} (${drink.alcohol.alcoholName.charAt(0)})</p>
                </div><br/>
        <% idEntifier++ } %>
    </div>
</div>