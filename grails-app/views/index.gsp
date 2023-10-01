<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Mixology</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <style>
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
            .arrow-right:before {
                width: 75px;
                height: 2px;
                background: red;
                content: "";
                display: inline-block;
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
            a:link {
                text-decoration:none;
            }
        </style>
    </head>
    <%
        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        def userService = grailsApplication.mainContext.getBean('userService')
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
    %>
    <g:set var="darkMode" value="${user?.darkMode ?: false}"/>
    <body style="overflow-x:scroll;padding:50px;margin:0;background-color:${darkMode?'black':'white'};">
        <div id="container" style="text-align:center;">
            <div style="display:block;position:fixed;top:20px;z-index:100;" class="row" id="navigation">
                <g:render template="/topBar"/>
            </div>
            <div style="padding-top:75px;">
                <div id="periodicTable" style="justify-content:center;display:inline-flex;padding:2em;margin:0;">
                    <div id="column1" style="margin:0;padding:0;width:600px;">
                        <div id="tequilaDrinks" style="margin:0;padding:0;">
                            <div class="card" style="">
                                <p style="text-align:center;margin-bottom:0;">Tequila Drinks</p>
                                <%
                                    def tequilaDrinks = Drink.findAllByAlcoholType(Alcohol.TEQUILA).take(12)
                                    for (int i=0; i<tequilaDrinks.size(); i+=2) {
                                        Drink drink1 = (Drink) tequilaDrinks.get(i)
                                        Drink drink2 = (Drink) tequilaDrinks.get(i+1)
                                %>
                                <div style="display:inline-flex;">
                                    <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ed969e']"/>
                                    <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ed969e']"/>
                                </div>
                                <% } /* end for loop */ %>
                            </div>
                        </div>
                        <div id="spacer" style="height:100px;"></div> <!-- for white space only -->
                        <div id="key" style="text-align:center;padding:0;">
                            <g:render template="/periodicTableKey"/>
                        </div>
                    </div>
                    <div id="column2" style="margin:0;padding:0;width:2100px;">
                        <div id="title" style="width:1200px;height:600px;">
                            <h1 id="chartTitle" style="color:${darkMode?'white':'black'};width:2100px;font-size:180px;padding-left:35px;">Periodic Table of Mixology</h1>
                            <div id="reference" style="display:inline-flex;">
                                <div id="chart" style="margin-left:70px;margin-right:50px;width:1000px;">
                                    <g:render template="/referenceChart" model="[darkMode:darkMode]"/>
                                </div>
                                <div id="measurements" style="margin-left:50px;margin-right:50px;">
                                    <g:render template="/measurementsCard" model="[darkMode:darkMode]"/>
                                </div>
                                <div id="glasses" style="text-align:center;">
                                    <img title="Click me to make me bigger!" onclick="makeSuggestedGlassesBigger();" width="450px" height="300px" style="mix-blend-mode:initial;" src="${resource(dir:'../assets/images',file:'allGlasses-white.jpg')}" alt="All Cocktails"/>
                                    <p style="font-size:2em;margin:0;color:#a60000;"><b>Suggested Glass Options</b></p>
                                </div>
                            </div>
                        </div>
                        <div id="vodkaAndGin" style="display:inline-flex;">
                            <div id="vodkaDrinks" style="margin:0;padding:0;width:1200px;">
                                <div class="card">
                                    <p style="text-align:center;margin-bottom:0;">Vodka Drinks</p>
                                    <%
                                        List vodkaDrinks = Drink.findAllByAlcoholType(Alcohol.VODKA).stream().limit(12).collect()
                                        for (int i=0; i<vodkaDrinks.size(); i+=4) {
                                            Drink drink1 = (Drink) vodkaDrinks.get(i)
                                            Drink drink2 = (Drink) vodkaDrinks.get(i+1)
                                            Drink drink3 = (Drink) vodkaDrinks.get(i+2)
                                            Drink drink4 = (Drink) vodkaDrinks.get(i+3)
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:link controller="drink" action="show" params="[id:drink1.id]">
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ffdf7e']"/>
                                        </g:link>
                                        <g:link controller="drink" action="show" params="[id:drink2.id]">
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ffdf7e']"/>
                                        </g:link>
                                        <g:link controller="drink" action="show" params="[id:drink3.id]">
                                            <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#ffdf7e']"/>
                                        </g:link>
                                        <g:link controller="drink" action="show" params="[id:drink4.id]">
                                            <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'#ffdf7e']"/>
                                        </g:link>
                                    </div>
                                    <% } // end for loop %>
                                </div>
                            </div>
                            <div id="ginDrinks" style="margin:0;padding:0;width:905px;">
                                <div class="card">
                                    <p style="text-align:center;margin-bottom:0;">Gin Drinks</p>
                                    <%
                                        List ginDrinks = Drink.findAllByAlcoholType(Alcohol.GIN).stream().limit(9).collect()
                                        for (int i=0; i<ginDrinks.size(); i+=3) {
                                            Drink drink1 = (Drink) ginDrinks.get(i)
                                            Drink drink2 = (Drink) ginDrinks.get(i+1)
                                            Drink drink3 = (Drink) ginDrinks.get(i+2)
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:link controller="drink" action="show" params="[id:drink1.id]">
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#46a5c8']"/>
                                        </g:link>
                                        <g:link controller="drink" action="show" params="[id:drink2.id]">
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#46a5c8']"/>
                                        </g:link>
                                        <g:link controller="drink" action="show" params="[id:drink3.id]">
                                            <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#46a5c8']"/>
                                        </g:link>
                                    </div>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <div id="shooterDrinks" style="width:2550px;padding:5em;">
                            <div class="card">
                                <p style="text-align:center;margin-bottom:0;">Shooter Drinks</p>
                                <%
                                    List shooterDrinks = Drink.findAllByAlcoholType(Alcohol.SHOOTER).stream().limit(16).collect()
                                    for (int i=0; i<shooterDrinks.size(); i+=8) {
                                        Drink drink1 = (Drink) shooterDrinks.get(i)
                                        Drink drink2 = (Drink) shooterDrinks.get(i+1)
                                        Drink drink3 = (Drink) shooterDrinks.get(i+2)
                                        Drink drink4 = (Drink) shooterDrinks.get(i+3)
                                        Drink drink5 = (Drink) shooterDrinks.get(i+4)
                                        Drink drink6 = (Drink) shooterDrinks.get(i+5)
                                        Drink drink7 = (Drink) shooterDrinks.get(i+6)
                                        Drink drink8 = (Drink) shooterDrinks.get(i+7)
                                %>
                                <div style="display:inline-flex;">
                                    <g:link controller="drink" action="show" params="[id:drink1.id]">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink2.id]">
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink3.id]">
                                        <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink4.id]">
                                        <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink5.id]">
                                        <g:render template="/drinkCard" model="[drink:drink5,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink6.id]">
                                        <g:render template="/drinkCard" model="[drink:drink6,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink7.id]">
                                        <g:render template="/drinkCard" model="[drink:drink7,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink8.id]">
                                        <g:render template="/drinkCard" model="[drink:drink8,backgroundColor:'mediumpurple']"/>
                                    </g:link>
                                </div>
                                <% } // end for loop %>
                            </div>
                        </div>
                    </div>
                    <div id="column3" style="margin:0;padding:0;width:600px;">
                        <div id="frozenDrinks" style="">
                            <div class="card">
                                <p style="text-align:center;margin-bottom:0;">Frozen Drinks</p>
                                <div style="display:inline-flex;">
                                    <div style="width:300px;height:300px;padding:5px;background-color:${darkMode?'black':'white'};">
                                        <img title="Don't Drink And Drive!" width="290px" height="290px" src="${resource(dir:'../assets/images',file:'dontDrinkAndDrive.png')}" alt="Don't Drink and Drive"/>
                                    </div>
                                    <%
                                        List frozenDrinks = Drink.findAllByAlcoholType(Alcohol.FROZEN).collect()
                                        frozenDrinks.stream().limit(1).collect()
                                        Drink drink1 = (Drink) frozenDrinks.get(0)
                                    %>
                                    <g:link controller="drink" action="show" params="[id:drink1.id]">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen']"/>
                                    </g:link>
                                </div>
                                <%
                                    frozenDrinks = Drink.findAllByAlcoholType(Alcohol.FROZEN).stream().limit(9).collect()
                                    for (int i=1; i<9; i+=2) {
                                        drink1 = (Drink) frozenDrinks.get(i)
                                        Drink drink2 = (Drink) frozenDrinks.get(i+1)
                                %>
                                <div style="display:inline-flex;">
                                    <g:link controller="drink" action="show" params="[id:drink1?.id]">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen']"/>
                                    </g:link>
                                    <g:link controller="drink" action="show" params="[id:drink2?.id]">
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumseagreen']"/>
                                    </g:link>
                                </div>
                                <% } // end for loop %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("index loaded");
            });
            function makeSuggestedGlassesBigger() {
                let newWindow = window.open("", "glasses", "width=1000,height=800");
                newWindow.document.write("<head><title>Suggested Glass Options</title></head>");
                newWindow.document.write("<img style=\"mix-blend-mode:initial;\" src=\"${resource(dir:'../assets/images',file:'allGlasses-white.jpg')}\" alt=\"All Cocktails\"/>");
                newWindow.document.write("<p style=\"text-align:center;font-size:2em;margin:0;color:#a60000;\"><b>Suggested Glass Options</b></p>");
            }
        </script>
    </body>
</html>