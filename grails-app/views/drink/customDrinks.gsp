<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Custom Drinks</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <style>
            .arrow-right:before {
                width: 75px;
                height: 2px;
                background: red;
                content: "";
                display: inline-block;
                vertical-align: middle;
            }
            .arrow-right:after {
                content: "";
                display: inline-block !important;
                width: 0;
                height: 0;
                border-left: 8px solid red;
                border-top: 8px solid transparent;
                border-bottom: 8px solid transparent;
                vertical-align: middle;
            }
            .arrow-left:before {
                content: "";
                display: inline-block !important;
                width: 0;
                height: 0;
                border-left: 8px solid red;
                border-top: 8px solid transparent;
                border-bottom: 8px solid transparent;
                vertical-align: middle;
                transform: rotate(-180deg);
            }
            .arrow-left:after {
                width: 75px;
                height: 2px;
                background: red;
                content: "";
                display: inline-block;
                vertical-align: middle;
            }
            /*These two webkit scrollbars control size and color*/
            ::-webkit-scrollbar {
                -webkit-appearance:none;
                width: 5px;
            }
            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgb(128, 128, 128);
                box-shadow: 0 0 1px rgba(255, 255, 255, .5);
            }
        </style>
    </head>
    <%
        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        def userService = grailsApplication.mainContext.getBean('userService')
        def user = userService.getByUsername(springSecurityService.authentication.getPrincipal().username as String)
    %>
    <g:set var="darkMode" value="${user.darkMode}"/>
    <body style="overflow-x:scroll;padding:50px;margin:0;background-color:${darkMode?'black':'white'};">
        <div id="container" style="">
            <div style="display:block;position:fixed;top:20px;z-index:100;" class="row" id="navigation">
                <g:render template="/topBar"/>
            </div>
            <div style="padding-top:75px;text-align:center;">
                <div id="periodicTable" style="justify-content:center;display:inline-flex;padding:2em;margin:0;">
                    <div id="column1" style="margin:0;padding:0;width:600px;">
                        <div id="tequilaDrinks" style="margin:0;padding:0;">
                            <div class="card" style="overflow-y:auto;height:1823px;">
                                <p style="text-align:center;margin-bottom:0;">Custom Tequila Drinks</p>
                                <%
                                    List tequilaDrinks = drinkList.findAll { it.alcoholType == Alcohol.TEQUILA}
                                            .sort((d1, d2) -> d1.number <=> d2.number).collect()
                                    int minimumSize = Math.max(12, (tequilaDrinks?.size() ?: 0))
                                    for (int i=0; i<minimumSize; i+=2) {
                                        Drink drink1, drink2
                                        try { drink1 = (Drink)tequilaDrinks.get(i) }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.TEQUILA) }
                                        try { drink2 = (Drink)tequilaDrinks.get(i+1) }
                                        catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.TEQUILA) }
                                %>
                                <div style="display:inline-flex;">
                                    <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ed969e',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ed969e',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                </div>
                                <% } %>
                            </div>
                        </div>
                        <div id="spacer" style="height:100px;"></div> <!-- for white space only -->
                        <div id="key" style="text-align:center;padding:0;">
                            <g:render template="../periodicTableKey" model="[darkMode:darkMode]"/>
                        </div>
                    </div>
                    <div id="column2" style="margin:0;padding:0;width:2100px;">
                        <div id="title" style="width:1200px;height:600px;">
                            <h1 id="chartTitle" style="width:2100px;font-size:180px;padding-left:35px;color:${darkMode?'white':'black'};">Periodic Table of Mixology</h1>
                            <div id="reference" style="display:inline-flex;">
                                <div id="chart" style="margin-left:70px;margin-right:50px;width:1000px;">
                                    <g:render template="customReferenceChart" model="[drinks:drinkList.sort{it.number},darkMode:darkMode]"/>
                                </div>
                                <div id="measurements" style="margin-left:50px;margin-right:50px;">
                                    <g:render template="../measurementsCard"/>
                                </div>
                                <div id="glasses" style="text-align:center;">
                                    <img title="Click me to make me bigger!" onclick="makeSuggestedGlassesBigger(${darkMode});"
                                         width="450px" height="300px" style="mix-blend-mode:initial;" alt="All Cocktails"
                                         <g:if test="${darkMode}">
                                             src="${resource(dir:'../assets/images',file:'allGlasses-darkMode.png')}"
                                         </g:if><g:else>
                                            src="${resource(dir:'../assets/images',file:'allGlasses-white.png')}"
                                         </g:else>
                                    />
                                    <p style="font-size:2em;margin:0;color:#a60000;"><b>Suggested Glass Options</b></p>
                                </div>
                            </div>
                        </div>
                        <div id="vodkaAndGin" style="display:inline-flex;">
                            <div id="vodkaDrinks" style="margin:0;padding:0;width:1200px;">
                                <div class="card" style="overflow-y:auto;height:923px;">
                                    <p style="text-align:center;margin-bottom:0;">Custom Vodka Drinks</p>
                                    <%
                                        List vodkaDrinks = drinkList.findAll { it.alcoholType == Alcohol.VODKA}
                                                .sort((d1, d2) -> d1.number <=> d2.number).collect()
                                        minimumSize = Math.max(12, (vodkaDrinks?.size() ?: 0))
                                        for (int i=0; i<minimumSize; i+=4) {
                                            Drink drink1, drink2, drink3, drink4
                                            try {
                                                drink1 = vodkaDrinks.get(i)
                                            }
                                            catch (Exception e) {
                                                drink1 = Drink.createFillerDrink(Alcohol.VODKA)
                                            }
                                            try {
                                                 drink2 = (Drink) vodkaDrinks.get(i+1)
                                            }
                                            catch (Exception e) {
                                                drink2 = Drink.createFillerDrink(Alcohol.VODKA)
                                            }
                                            try {
                                                drink3 = (Drink) vodkaDrinks.get(i+2)
                                            }
                                            catch (Exception e) {
                                                drink3 = Drink.createFillerDrink(Alcohol.VODKA)
                                            }
                                            try {
                                                drink4 = (Drink) vodkaDrinks.get(i+3)
                                            }
                                            catch (Exception e) {
                                                drink4 = Drink.createFillerDrink(Alcohol.VODKA)
                                            }
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ffdf7e',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ffdf7e',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#ffdf7e',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                        <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'#ffdf7e',opacity:drink4.number == 0 ? 0.5 : 1]"/>
                                    </div>
                                    <% } %>
                                </div>
                            </div>
                            <div id="gin" style="padding-right:5px;width:905px;">
                                <div class="card" style="overflow-y:auto;height:923px;">
                                    <p style="text-align:center;margin-bottom:0;">Custom Gin Drinks</p>
                                    <%
                                        def ginDrinks = drinkList.findAll { it.alcoholType == Alcohol.GIN }
                                                .sort((d1, d2) -> d1.number <=> d2.number).collect()
                                        minimumSize = Math.max(9, (ginDrinks?.size() ?: 0))
                                        for (int i=0; i<minimumSize; i+=3) {
                                            Drink drink1, drink2, drink3
                                            try { drink1 = ginDrinks.get(i) }
                                            catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.GIN) }
                                            try { drink2 = ginDrinks.get(i+1) }
                                            catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.GIN) }
                                            try { drink3 = ginDrinks.get(i+2) }
                                            catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.GIN) }

                                    %>
                                    <div style="display:inline-flex;">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#46a5c8',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#46a5c8',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#46a5c8',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                    </div>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <div id="shooterDrinks" style="width:2500px;padding:5em;">
                            <div class="card" style="overflow-y:auto;height:623px;">
                                <p style="text-align:center;margin-bottom:0;">Custom Shooter Drinks</p>
                                <%
                                    List shooterDrinks = drinkList.findAll { it.alcoholType == Alcohol.SHOOTER }
                                            .sort((d1, d2) -> d1.number.compareTo(d2.number)).collect()
                                    minimumSize = Math.max(16, (shooterDrinks?.size() ?: 0))
                                    for (int i=0; i<minimumSize; i+=8) {
                                        Drink drink1, drink2, drink3, drink4, drink5, drink6, drink7, drink8
                                        try { drink1 = shooterDrinks.get(i) }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink2 = shooterDrinks.get(i+1) }
                                        catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink3 = shooterDrinks.get(i+2) }
                                        catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink4 = shooterDrinks.get(i+3) }
                                        catch (Exception e) { drink4 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink5 = shooterDrinks.get(i+4) }
                                        catch (Exception e) { drink5 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink6 = shooterDrinks.get(i+5) }
                                        catch (Exception e) { drink6 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink7 = shooterDrinks.get(i+6) }
                                        catch (Exception e) { drink7 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                        try { drink8 = shooterDrinks.get(i+7) }
                                        catch (Exception e) { drink8 = Drink.createFillerDrink(Alcohol.SHOOTER) }
                                %>
                                <div style="display:inline-flex;">
                                    <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumpurple',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumpurple',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'mediumpurple',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'mediumpurple',opacity:drink4.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink5,backgroundColor:'mediumpurple',opacity:drink5.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink6,backgroundColor:'mediumpurple',opacity:drink6.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink7,backgroundColor:'mediumpurple',opacity:drink7.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink8,backgroundColor:'mediumpurple',opacity:drink8.number == 0 ? 0.5 : 1]"/>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <div id="column3" style="margin:0;padding:0;width:600px;">
                        <div id="frozenDrinks" style="">
                            <div class="card" style="overflow-y:auto;height:1523px;">
                                <p style="text-align:center;margin-bottom:0;">Custom  Frozen Drinks</p>
                                <div style="display:inline-flex;">
                                    <div style="width:300px;height:300px;background-color:${darkMode?'black':'white'};">
                                        <img title="Don't Drink And Drive!" width="290px" height="290px" alt="Don't Drink and Drive"
                                             <g:if test="${darkMode}">
                                                 src="${resource(dir:'../assets/images',file:'dontDrinkAndDriveDarkMode.png')}"
                                             </g:if><g:else>
                                                 src="${resource(dir:'../assets/images',file:'dontDrinkAndDrive.png')}"
                                            </g:else>
                                        />
                                    </div>
                                    <%
                                        List frozenDrinks = drinkList.findAll { it.alcoholType == Alcohol.FROZEN }
                                                .sort((d1, d2) -> d1.number <=> d2.number).take(9).collect()
                                        Drink drink1, drink2
                                        try { drink1 = frozenDrinks.get(0) }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.FROZEN) }
                                    %>
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                </div>
                                <%
                                    minimumSize = Math.max(9, (frozenDrinks?.size() ?: 0))
                                    for (int i=1; i<minimumSize; i+=2) {
                                        try { drink1 = (Drink)frozenDrinks.get(i) }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.FROZEN) }
                                        try { drink2 = (Drink)frozenDrinks.get(i+1) }
                                        catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.FROZEN) }
                                %>
                                <div style="display:inline-flex;">
                                    <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumseagreen',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    <script>
    $(document).ready(function() {
        console.log("custom drinks loaded");
    });
    function makeSuggestedGlassesBigger(darkMode) {
        let newWindow = window.open("", "glasses", "width=1000,height=800");
        newWindow.document.write("<head><title>Suggested Glass Options</title></head>");
        if (darkMode) {
            newWindow.document.write("<body style='background-color:black;'>");
            newWindow.document.write("<p style=\"text-align:center;font-size:2em;margin:0;color:white;\"><b>Suggested Glass Options</b></p>");
            newWindow.document.write("<img style=\"mix-blend-mode:initial;\" src=\"${resource(dir:'../assets/images',file:'allGlasses-darkMode.png')}\" alt=\"All Cocktails\"/>");
        } else {
            newWindow.document.write("<body style='background-color:white;'>");
            newWindow.document.write("<p style=\"text-align:center;font-size:2em;margin:0;color:black;\"><b>Suggested Glass Options</b></p>");
            newWindow.document.write("<img style=\"mix-blend-mode:initial;\" src=\"${resource(dir:'../assets/images',file:'allGlasses-white.png')}\" alt=\"All Cocktails\"/>");
        }
        newWindow.document.write("</body>")
    }
</script>
    </body>
</html>