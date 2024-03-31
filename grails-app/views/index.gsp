<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title><g:message code="default.mixology.title" default="Mixology"/></title>
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
        def user = userService.getByUsername(springSecurityService.getPrincipal().username as String)
    %>
    <g:set var="language" value="${user?.language ?: (params.lang) ?: 'en'}"/>
    <g:set var="darkMode" value="${user?.darkMode ?: (params.darkMode=='true') ?: false}"/>
    <body style="overflow-x:scroll;padding:50px;margin:0;background-color:${darkMode?'black':'white'};">
        <div id="container" style="">
            <div style="display:block;position:fixed;top:20px;z-index:100;text-align:left;" class="row" id="topBar">
                <g:render template="/topBar" model="[darkMode:darkMode]"/>
            </div>
            <div style="padding-top:75px;text-align:center;">
                <div id="periodicTable" style="justify-content:center;display:inline-flex;padding:2em;margin:0;">
                    <div id="column1" style="margin:0;padding:0;width:600px;">
                        <div id="tequilaDrinks" style="margin:0;padding:0;">
                            <div class="card" style="">
                                <p style="text-align:center;margin-bottom:0;"><g:message code="tequila.drinks" default="Tequila Drinks"/></p>
                                <%
                                    def tequilaDrinks = Drink.findAllByAlcohol(Alcohol.TEQUILA).take(12)
                                    for (int i=0; i<tequilaDrinks.size(); i+=2) {
                                        Drink drink1 = tequilaDrinks.get(i)
                                        Drink drink2 = tequilaDrinks.get(i+1)
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
                            <g:render template="/periodicTableKey" model="[darkMode:darkMode]"/>
                        </div>
                    </div>
                    <div id="column2" style="margin:0;padding:0;width:2100px;">
                        <div id="title" style="width:1200px;height:600px;">
                            <h1 id="chartTitle" style="color:${darkMode?'white':'black'};width:2100px;<g:if test="${language=='en'}">font-size:180px;</g:if><g:else>font-size:140px;</g:else>padding-left:35px;"><g:message code="periodic.table.title" default="Periodic Table of Mixology"/></h1>
                            <div id="reference" style="display:inline-flex;">
                                <div id="chart" style="margin-left:70px;margin-right:50px;width:1000px;">
                                    <g:render template="/referenceChart" model="[darkMode:darkMode]"/>
                                </div>
                                <div id="measurements" style="margin-left:50px;margin-right:50px;">
                                    <g:render template="/measurementsCard" model="[darkMode:darkMode]"/>
                                </div>
                                <div id="glasses" style="text-align:center;">
                                    <img title="${g.message(code:'click.to.make.bigger', default:'Click me to make me bigger!')}" onclick="makeSuggestedGlassesBigger(${darkMode});"
                                         width="450px" height="300px" style="mix-blend-mode:initial;" alt="All Cocktails"
                                         <g:if test="${darkMode}">
                                             src="${resource(dir:'../assets/images',file:'allGlasses-darkMode.png')}"
                                         </g:if><g:else>
                                             src="${resource(dir:'../assets/images',file:'allGlasses-white.png')}"
                                         </g:else>
                                    />
                                    <p style="font-size:2em;margin:0;color:#a60000;"><b><g:message code="suggested.glass.options" default="Suggested Glass Options"/></b></p>
                                </div>
                            </div>
                        </div>
                        <div id="vodkaAndGin" style="display:inline-flex;">
                            <div id="vodkaDrinks" style="margin:0;padding:0;width:1200px;">
                                <div class="card">
                                    <p style="text-align:center;margin-bottom:0;"><g:message code="vodka.drinks" default="Vodka Drinks"/></p>
                                    <%
                                        def vodkaDrinks = Drink.findAllByAlcohol(Alcohol.VODKA).take(12)
                                        for (int i=0; i<vodkaDrinks.size(); i+=4) {
                                            Drink drink1 = vodkaDrinks.get(i)
                                            Drink drink2 = vodkaDrinks.get(i+1)
                                            Drink drink3 = vodkaDrinks.get(i+2)
                                            Drink drink4 = vodkaDrinks.get(i+3)
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ffdf7e']"/>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ffdf7e']"/>
                                        <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#ffdf7e']"/>
                                        <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'#ffdf7e']"/>
                                    </div>
                                    <% } // end for loop %>
                                </div>
                            </div>
                            <div id="ginDrinks" style="margin:0;padding:0;width:905px;">
                                <div class="card">
                                    <p style="text-align:center;margin-bottom:0;"><g:message code="gin.drinks" default="Gin Drinks"/></p>
                                    <%
                                        def ginDrinks = Drink.findAllByAlcohol(Alcohol.GIN).take(9)
                                        for (int i=0; i<ginDrinks.size(); i+=3) {
                                            Drink drink1 = ginDrinks.get(i)
                                            Drink drink2 = ginDrinks.get(i+1)
                                            Drink drink3 = ginDrinks.get(i+2)
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#46a5c8']"/>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#46a5c8']"/>
                                        <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#46a5c8']"/>
                                    </div>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <div id="shooterDrinks" style="width:2540px;padding:5em;">
                            <div class="card">
                                <p style="text-align:center;margin-bottom:0;"><g:message code="shooter.drinks" default="Shooter Drinks"/></p>
                                <%
                                    def shooterDrinks = Drink.findAllByAlcohol(Alcohol.SHOOTER).take(16)
                                    for (int i=0; i<shooterDrinks.size(); i+=8) {
                                        Drink drink1 = shooterDrinks.get(i)
                                        Drink drink2 = shooterDrinks.get(i+1)
                                        Drink drink3 = shooterDrinks.get(i+2)
                                        Drink drink4 = shooterDrinks.get(i+3)
                                        Drink drink5 = shooterDrinks.get(i+4)
                                        Drink drink6 = shooterDrinks.get(i+5)
                                        Drink drink7 = shooterDrinks.get(i+6)
                                        Drink drink8 = shooterDrinks.get(i+7)
                                %>
                                <div style="display:inline-flex;">
                                    <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink5,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink6,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink7,backgroundColor:'mediumpurple']"/>
                                    <g:render template="/drinkCard" model="[drink:drink8,backgroundColor:'mediumpurple']"/>
                                </div>
                                <% } // end for loop %>
                            </div>
                        </div>
                    </div>
                    <div id="column3" style="margin:0;padding:0;width:600px;">
                        <div id="frozenDrinks" style="">
                            <div class="card">
                                <p style="text-align:center;margin-bottom:0;"><g:message code="frozen.drinks" default="Frozen Drinks"/></p>
                                <div style="display:inline-flex;">
                                    <div style="width:300px;height:300px;padding:5px;background-color:${darkMode?'black':'white'};">
                                        <img title="${g.message(code:'dont.drink.and.drive', default:"Don't Drink and Drive!")}" width="290px" height="290px" alt="${g.message(code:'dont.drink.and.drive', default:"Don't Drink and Drive!")}"
                                            <g:if test="${darkMode}">
                                                src="${resource(dir:'../assets/images',file:'dontDrinkAndDriveDarkMode.png')}"
                                            </g:if><g:else>
                                                src="${resource(dir:'../assets/images',file:'dontDrinkAndDrive.png')}"
                                            </g:else>
                                        />
                                    </div>
                                    <%
                                        List frozenDrinks = Drink.findAllByAlcohol(Alcohol.FROZEN).take(9)
                                        Drink drink1 = frozenDrinks.get(0)
                                    %>
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen']"/>
                                </div>
                                <%
                                    for (int i=1; i<frozenDrinks.size(); i+=2) {
                                        drink1 = frozenDrinks.get(i)
                                        Drink drink2 = frozenDrinks.get(i+1)
                                %>
                                <div style="display:inline-flex;">
                                    <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen']"/>
                                    <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumseagreen']"/>
                                </div>
                                <% } // end for loop %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            function makeSuggestedGlassesBigger(darkMode) {
                let newWindow = window.open("", "glasses", "width=1000,height=800");
                newWindow.document.write("<head><title><g:message code="suggested.glass.options" default="Suggested Glass Options"/></title></head>");
                if (darkMode) {
                    newWindow.document.write("<body style=\"background-color:black;\">");
                } else {
                    newWindow.document.write("<body style=\"background-color:white;\">");
                }
                newWindow.document.write("<p style=\"text-align:center;font-size:2em;margin:0;color:white;\"><b><g:message code="suggested.glass.options" default="Suggested Glass Options"/></b></p>");
                newWindow.document.write("<img  style=\"display:block;margin-left:auto;margin-right:auto;mix-blend-mode:initial;\" src=\"${resource(dir:'../assets/images',file:'allGlasses-darkMode.png')}\" alt=\"All Cocktails\"/>");
                newWindow.document.write("</body>");
            }
        </script>
    </body>
</html>
<script type="text/javascript">
    $(document).ready(function() {
        console.log("index loaded");
        console.log("language: '${language}'");
        console.log("darkMode: '${darkMode}'");
        console.log("href: " + location.href.split('lang=').length);
        if ("${language}" !== '' && location.href.split('lang=').length === 1) {
            console.log("resetting language to ${language}");
            window.location.href = location.href + '?lang=' + "${language ?: 'en'}" + '&darkMode=' + "${darkMode}";
        } else {
            console.log("not setting language");
        }
    });
</script>