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
        </style>
    </head>

    <%
        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        def userService = grailsApplication.mainContext.getBean('userService')
        def user = userService.getByUsername(springSecurityService.authentication.getPrincipal().username as String)
    %>
    <g:set var="darkMode" value="${user.darkMode}"/>
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
                                <p style="text-align:center;margin-bottom:0;">Custom Tequila Drinks</p>
                                <%
                                    List tequilaDrinks = drinkList.findAll { it.alcoholType == Alcohol.TEQUILA}
                                            .sort((d1, d2) -> d1.number <=> d2.number).collect()
                                    for (int i=0; i<12; i+=2) {
                                        Drink drink1 = null
                                        if (i < 12) {
                                            try { drink1 = (Drink)tequilaDrinks.get(i); if (!drink1) {drink1 = Drink.createFillerDrink(Alcohol.TEQUILA); drink1.id = i+1;} }
                                            catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.TEQUILA); drink1.id = i+1;}
                                        }
                                        Drink drink2 = null
                                        if (i+1 < 12) {
                                            try { drink2 = (Drink)tequilaDrinks.get(i+1); if (!drink2) {drink2 = Drink.createFillerDrink(Alcohol.TEQUILA); drink2.id = i+2;} }
                                            catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.TEQUILA); drink2.id = i+2;}
                                        }
                                %>
                                <div style="display:inline-flex;">
                                    <g:if test="${drink1.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink1.id]">
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ed969e',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ed969e',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink2.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink2.id]">
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ed969e',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ed969e',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                </div>
                                <% } %>
                            </div>
                        </div>
                        <div id="spacer" style="height:100px;"></div> <!-- for white space only -->
                        <div id="key" style="text-align:center;padding:0;">
                            <g:render template="../periodicTableKey"/>
                        </div>
                    </div>
                    <div id="column2" style="margin:0;padding:0;width:2100px;">
                        <div id="title" style="width:1200px;height:600px;">
                            <h1 id="chartTitle" style="width:2100px;font-size:180px;padding-left:35px;color:${darkMode?'white':'black'};">Periodic Table of Mixology</h1>
                            <div id="reference" style="display:inline-flex;">
                                <div id="chart" style="margin-left:70px;margin-right:50px;width:1000px;">
                                    <g:render template="customReferenceChart" model="[drinks:drinkList,darkMode:darkMode]"/>
                                </div>
                                <div id="measurements" style="margin-left:50px;margin-right:50px;">
                                    <g:render template="../measurementsCard"/>
                                </div>
                                <div id="glasses" style="text-align:center;">
                                    <img title="Click me to make me bigger!" onclick="makeSuggestedGlassesBigger();" width="450px" height="300px" style="mix-blend-mode:initial;" src="${resource(dir:'../assets/images',file:'allGlasses-white.jpg')}" alt="All Cocktails"/>
                                    <p style="font-size:2em;margin:0;color:#a60000;"><b>Suggested Glass Options</b></p>
                                </div>
                            </div>
                        </div>
                        <div id="vodkaAndGin" style="display:inline-flex;">
                            <div id="vodkaDrinks" style="margin:0;padding:0;width:1200px;">
                                <div class="card" style="">
                                    <p style="text-align:center;margin-bottom:0;">Custom Vodka Drinks</p>
                                    <%
                                        def vodkaDrinks = drinkList.findAll { it.alcoholType == Alcohol.VODKA}
                                                .sort((d1, d2) -> d1.number <=> d2.number).collect()
                                        for (int i=0; i<12; i+=4) {
                                            Drink drink1 = null, drink2 = null, drink3 = null, drink4 = null;
                                            if (i < 12) {
                                                try { drink1 = (Drink)vodkaDrinks.get(i); if (!drink1) {drink1 = Drink.createFillerDrink(Alcohol.VODKA); drink1.id = i+1;} }
                                                catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.VODKA); drink1.id = i+1;}
                                            }
                                            if (i+1 < 12) {
                                                try { drink2 = (Drink)vodkaDrinks.get(i+1); if (!drink2) {drink2 = Drink.createFillerDrink(Alcohol.VODKA); drink2.id = i+2;} }
                                                catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.VODKA); drink2.id = i+2;}
                                            }
                                            if (i+2 < 12) {
                                                try { drink3 = (Drink)vodkaDrinks.get(i+2); if (!drink3) {drink3 = Drink.createFillerDrink(Alcohol.VODKA); drink3.id = i+3;} }
                                                catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.VODKA); drink3.id = i+3;}
                                            }
                                            if (i+3 < 12) {
                                                try { drink4 = (Drink)vodkaDrinks.get(i+3); if (!drink4) {drink4 = Drink.createFillerDrink(Alcohol.VODKA); drink4.id = i+4;} }
                                                catch (Exception e) { drink4 = Drink.createFillerDrink(Alcohol.VODKA); drink4.id = i+4;}
                                            }
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:if test="${drink1.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink1.id]">
                                                <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ffdf7e',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#ffdf7e',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                        <g:if test="${drink2.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink2.id]">
                                                <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ffdf7e',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#ffdf7e',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                        <g:if test="${drink3.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink3.id]">
                                                <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#ffdf7e',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#ffdf7e',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                        <g:if test="${drink4.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink4.id]">
                                                <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'#ffdf7e',opacity:drink4.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'#ffdf7e',opacity:drink4.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                    </div>
                                    <% } %>
                                </div>
                            </div>
                            <div id="gin" style="margin:0;padding:0;width:905px;">
                                <div class="card">
                                    <p style="text-align:center;margin-bottom:0;">Custom Gin Drinks</p>
                                    <%
                                        def ginDrinks = drinkList.findAll { it.alcoholType == Alcohol.GIN }
                                                .sort((d1, d2) -> d1.number <=> d2.number).collect()
                                        for (int i=0; i<9; i+=3) {
                                            Drink drink1 = null
                                            if (i < 9) {
                                                try { drink1 = (Drink)ginDrinks.get(i); if (!drink1) {drink1 = Drink.createFillerDrink(Alcohol.GIN); drink1.id = i+1 } }
                                                catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.GIN); drink1.id = i+1 }
                                            }
                                            Drink drink2 = null
                                            if (i+1 < 9) {
                                                try { drink2 = (Drink)ginDrinks.get(i+1); if (!drink2) {drink2 = Drink.createFillerDrink(Alcohol.GIN); drink2.id = i+2}  }
                                                catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.GIN); drink2.id = i+1 }
                                            }
                                            Drink drink3 = null
                                            if (i+2 < 9) {
                                                try { drink3 = (Drink)ginDrinks.get(i+2); if (!drink3) {drink3 = Drink.createFillerDrink(Alcohol.GIN); drink3.id = i+3}  }
                                                catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.GIN); drink3.id = i+1 }
                                            }
                                    %>
                                    <div style="display:inline-flex;">
                                        <g:if test="${drink1.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink1.id]">
                                                <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#46a5c8',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'#46a5c8',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                        <g:if test="${drink2.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink2.id]">
                                                <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#46a5c8',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'#46a5c8',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                        <g:if test="${drink3.number != 0}">
                                            <g:link controller="drink" action="show" params="[id:drink3.id]">
                                                <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#46a5c8',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                            </g:link>
                                        </g:if><g:else>
                                            <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'#46a5c8',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                        </g:else>
                                    </div>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <div id="shooterDrinks" style="width:2500px;padding:5em;">
                            <div class="card">
                                <p style="text-align:center;margin-bottom:0;">Custom Shooter Drinks</p>
                                <%
                                    List shooterDrinks = drinkList.findAll { it.alcoholType == Alcohol.SHOOTER }
                                            .sort((d1, d2) -> d1.number.compareTo(d2.number)).collect()
                                    for (int i=0; i<16; i+=8) {
                                        Drink drink1 = null
                                        if (i < 16) {
                                            try { drink1 = (Drink)shooterDrinks.get(i); if (!drink1) {drink1 = Drink.createFillerDrink(Alcohol.SHOOTER); drink1.id = i+1;} }
                                            catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.SHOOTER); drink1.id = i+1;}
                                        }
                                        Drink drink2 = null
                                        if (i+1 < 16) {
                                            try { drink2 = (Drink)shooterDrinks.get(i+1); if (!drink2) {drink2 = Drink.createFillerDrink(Alcohol.SHOOTER); drink2.id = i+2;} }
                                            catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.SHOOTER); drink2.id = i+2;}
                                        }
                                        Drink drink3 = null
                                        if (i+2 < 16) {
                                            try { drink3 = (Drink)shooterDrinks.get(i+2); if (!drink3) {drink3 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+3;} }
                                            catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+3;}
                                        }
                                        Drink drink4 = null
                                        if (i+3 < 16) {
                                            try { drink4 = (Drink)shooterDrinks.get(i+3); if (!drink4) {drink4 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+4;} }
                                            catch (Exception e) { drink4 = Drink.createFillerDrink(Alcohol.SHOOTER); drink4.id = i+4;}
                                        }
                                        Drink drink5 = null
                                        if (i+4 < 16) {
                                            try { drink5 = (Drink)shooterDrinks.get(i+4); if (!drink5) {drink5 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+5;} }
                                            catch (Exception e) { drink5 = Drink.createFillerDrink(Alcohol.SHOOTER); drink5.id = i+5;}
                                        }
                                        Drink drink6 = null
                                        if (i+5 < 16) {
                                            try { drink6 = (Drink)shooterDrinks.get(i+5); if (!drink6) {drink6 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+6;} }
                                            catch (Exception e) { drink6 = Drink.createFillerDrink(Alcohol.SHOOTER); drink6.id = i+6;}
                                        }
                                        Drink drink7 = null
                                        if (i+6 < 16) {
                                            try { drink7 = (Drink)shooterDrinks.get(i+6); if (!drink7) {drink7 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+7;} }
                                            catch (Exception e) { drink7 = Drink.createFillerDrink(Alcohol.SHOOTER); drink7.id = i+7;}
                                        }
                                        Drink drink8 = null
                                        if (i+7 < 16) {
                                            try { drink8 = (Drink)shooterDrinks.get(i+7); if (!drink8) {drink8 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+8;} }
                                            catch (Exception e) { drink8 = Drink.createFillerDrink(Alcohol.SHOOTER); drink8.id = i+8;}
                                        }
                                %>
                                <div style="display:inline-flex;">
                                    <g:if test="${drink1.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink1.id]">
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumpurple',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumpurple',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink2.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink2.id]">
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumpurple',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumpurple',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink3.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink3.id]">
                                            <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'mediumpurple',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink3,backgroundColor:'mediumpurple',opacity:drink3.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink4.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink4.id]">
                                            <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'mediumpurple',opacity:drink4.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink4,backgroundColor:'mediumpurple',opacity:drink4.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink5.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink5.id]">
                                            <g:render template="/drinkCard" model="[drink:drink5,backgroundColor:'mediumpurple',opacity:drink5.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink5,backgroundColor:'mediumpurple',opacity:drink5.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink6.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink6.id]">
                                            <g:render template="/drinkCard" model="[drink:drink6,backgroundColor:'mediumpurple',opacity:drink6.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink6,backgroundColor:'mediumpurple',opacity:drink6.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink7.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink7.id]">
                                            <g:render template="/drinkCard" model="[drink:drink7,backgroundColor:'mediumpurple',opacity:drink7.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink7,backgroundColor:'mediumpurple',opacity:drink7.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink8.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink8.id]">
                                            <g:render template="/drinkCard" model="[drink:drink8,backgroundColor:'mediumpurple',opacity:drink8.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink8,backgroundColor:'mediumpurple',opacity:drink8.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <div id="column3" style="margin:0;padding:0;width:600px;">
                        <div id="frozenDrinks" style="">
                            <div class="card">
                                <p style="text-align:center;margin-bottom:0;">Custom  Frozen Drinks</p>
                                <div style="display:inline-flex;">
                                    <div style="width:300px;height:300px;">
                                        <img title="Don't Drink And Drive!" width="290px" height="290px" src="${resource(dir:'../assets/images',file:'dontDrinkAndDrive.png')}" alt="Don't Drink and Drive"/>
                                    </div>
                                    <%
                                        def frozenDrinks = drinkList.findAll { it.alcoholType == Alcohol.FROZEN }
                                                .sort((d1, d2) -> d1.number.compareTo(d2.number)).take(9).collect() as List<Drink>
                                        Drink drink1 = null
                                        try { drink1 = (Drink)frozenDrinks.get(0); if (!drink1) {drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = 1;} }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = 1;}
                                    %>
                                    <g:if test="${drink1.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink1.id]">
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                </div>
                                <%
                                    for (int i=1; i<9; i+=2) {
                                        drink1 = null
                                        if (i < 9) {
                                            try { drink1 = (Drink)frozenDrinks.get(i); if (!drink1) {drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = i+1;} }
                                            catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = i+1;}
                                        }
                                        Drink drink2 = null
                                        if (i+1 < 9) {
                                            try { drink2 = (Drink)frozenDrinks.get(i+1); if (!drink2) {drink2 = Drink.createFillerDrink(Alcohol.FROZEN); drink2.id = i+2;} }
                                            catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.FROZEN); drink2.id = i+2;}
                                        }
                                %>
                                <div style="display:inline-flex;">
                                    <g:if test="${drink1.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink1.id]">
                                            <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink1,backgroundColor:'mediumseagreen',opacity:drink1.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                    <g:if test="${drink1.number != 0}">
                                        <g:link controller="drink" action="show" params="[id:drink2.id]">
                                            <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumseagreen',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                        </g:link>
                                    </g:if><g:else>
                                        <g:render template="/drinkCard" model="[drink:drink2,backgroundColor:'mediumseagreen',opacity:drink2.number == 0 ? 0.5 : 1]"/>
                                    </g:else>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script>
        $(document).ready(function() {
            console.log("custom drinks loaded");
        });
        function makeSuggestedGlassesBigger() {
            let newWindow = window.open("", "glasses", "width=1000,height=800");
            newWindow.document.write("<head><title>Suggested Glass Options</title></head>");
            newWindow.document.write("<img style=\"mix-blend-mode:initial;\" src=\"${resource(dir:'../assets/images',file:'allGlasses-white.jpg')}\" alt=\"All Cocktails\"/>");
            newWindow.document.write("<p style=\"text-align:center;font-size:2em;margin:0;color:#a60000;\"><b>Suggested Glass Options</b></p>");
        }
    </script>
</html>