<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<p style="font-size:35px;margin:0;color:#a60000;"><b>NUMBER, SYMBOL, AND NAME QUICK REFERENCE CHART</b></p>
<div style="display:block;overflow-y:auto;overflow-x:hidden;height:255px;"> <%
    int count = 0
    int total = drinks.size()
    int rows = (int) (total/60) + ( (total % 60!=0) ? 1 : 0)
    int cols = 0
    int colRow = 0
    out: for (int row=0; row<rows; row++) { %>
        <div id="row${row+1}" style="display:inline-flex; text-align:left;"> <% // start of row
            if (count % 12 == 0 || count == 0) { %>
                <div style="display:block;width:205px;"> <% // start of column
            }
            for(count; count<(60*(row+1)); count++) {
                def drink = drinks[count] ?: null
                if (!drink) {
                    int remainingColRows = 12 - colRow
                    if (remainingColRows != 12) {
                        for(int i=0; i<remainingColRows; i++) { %>
                            <div style="display:inline-flex;padding:0;"><p>&nbsp;</p></div><br/> <%
                        }
                    } %>
                    </div> <% // if we end without a full column, else use div on line 39
                    cols += 1
                    while (cols != 5) { %>
                        <div style="display:block;width:205px;">
                            <% for (int i=0; i<12; i++) { %>
                                <div style="display:inline-flex;padding:0;"><p>&nbsp;</p></div><br/> <%
                            } %>
                        </div> <%
                        cols += 1
                    } %>
                    </div> <% // if we end without a full row, else use div on line 50
                    continue out
                } else if (count % 12 == 0 && count != (60*row) && count != 0 && colRow == 12 || count==(60*(row+1)) ) { %>
                    </div> <% // end full column %>
                    <div style="display:block;"> <% // start a new column with zero colRows
                    colRow = 0
                } %>
                <div id="drink${colRow+1}" style="display:inline-flex;padding:0;">
                    <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'dodgerblue':'navy'};"><b>${drink.number}</b></p><br/>
                    <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'mediumseagreen':'#155724'};"><b>${drink.symbol}</b></p>
                    <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:${darkMode?'white':'black'};">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
                </div><br/> <%
                colRow += 1
            } %>
            </div>
        </div> <%
    } %>
</div>