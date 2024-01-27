<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<p style="font-size:35px;margin:0;color:#a60000;"><b>NUMBER, SYMBOL, AND NAME QUICK REFERENCE CHART</b></p>
<div style="display:block;overflow-y:auto;overflow-x:hidden;height:260px;">
<%  int count = 0
    int total = drinks.size()
    int rows = (int) (total/60)
    if (total % 60!=0) rows += 1
    int cols = 0
    int colRow = 0
    Drink drink
    out: for (int row=0; row<rows; row++) { %>
        <div id="row${row+1}" style="display:inline-flex; text-align:left;">
            <%
                if (count % 12 == 0 || count == 0) { %>
                <div style="display:block;width:205px;">
                <% } %>
                <% for(count; count<(60*(row+1)); count++) {
                    drink = drinks[count] ?: null
                    if (!drink) {
                        int remaining = 12 - colRow
                        if (remaining != 12) {
                            for(int i=0; i<remaining; i++) { %>
                                <div style="display:inline-flex;padding:0;">&nbsp;</div><br/>
                            <% }
                        } %>
                        </div>
                        <%  cols += 1
                            while (cols != 5) { %>
                            <div style="display:block;width:205px;">
                                <% for(int i=0; i<12; i++) { %>
                                    <div style="display:inline-flex;padding:0;">&nbsp;</div><br/>
                                <% } %>
                            </div>
                        <%  cols += 1 } %>
                        </div>
                    <% continue out
                    } else if (count % 12 == 0 && count != (60*row) && count != 0 || count==(60*(row+1)) ) { %>
                        </div>
                        <div style="display:block;">
                    <% } %>
                    <div id="drink${count+1}" style="display:inline-flex;padding:0;">
                        <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'dodgerblue':'navy'};"><b>${drink.number}</b></p><br/>
                        <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'mediumseagreen':'#155724'};"><b>${drink.symbol}</b></p>
                        <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'white':'black'};">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
                    </div><br/>
                <% colRow+= 1 } %>
            </div>
        </div>
<% } %>
</div>