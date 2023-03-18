<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/18/23
  Time: 4:42 PM
--%>
<%@ page import="enums.*; mixology.Drink; mixology.DrinkService;" %>
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
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    </head>
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
            width: 0px;
            height: 0px;
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

    <body style="overflow-x:scroll;padding:0;margin:0;">
        <nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
            <div class="container-fluid">
                <img style="width:200px;height:200px;" src="${resource(dir:'../assets/images',file:'martiniGlass.png')}" alt="Cocktail Logo"/>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
                    <ul class="nav navbar-nav ml-auto">
                        <div tag="nav">
                            <li class="dropdown" >
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#appStatus" role="button" aria-haspopup="true" aria-expanded="false">Application Status <span class="caret"></span></a>
                                <ul class="dropdown-menu" id="appStatus" style="background-color:#000000;">
                                    <li class="dropdown-item"><a href="#">Environment: ${grails.util.Environment.current.name}</a></li>
                                    <li class="dropdown-item"><a href="#">App profile: ${grailsApplication.config.getProperty('grails.profile')}</a></li>
                                    <li class="dropdown-item"><a href="#">App version:
                                        <g:meta name="info.app.version"/></a>
                                    </li>
                                    <li role="separator" class="dropdown-divider"></li>
                                    <li class="dropdown-item"><a href="#">Grails version:
                                        <g:meta name="info.app.grailsVersion"/></a>
                                    </li>
                                    <li class="dropdown-item"><a href="#">Groovy version: ${GroovySystem.getVersion()}</a></li>
                                    <li class="dropdown-item"><a href="#">JVM version: ${System.getProperty('java.version')}</a></li>
                                    <li role="separator" class="dropdown-divider"></li>
                                    <li class="dropdown-item"><a href="#">Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#artefacts" role="button" aria-haspopup="true" aria-expanded="false">Artefacts <span class="caret"></span></a>
                                <ul class="dropdown-menu" id="artefacts" style="background-color:#000000;">
                                    <li class="dropdown-header">Controllers</li>
                                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                                        <g:if test="${c.name != 'Search'}"> <!-- Skip Search-->
                                            <li class="dropdown-item"><g:link controller="${c.logicalPropertyName}">${c.name}</g:link></li>
                                        </g:if>
                                    </g:each>
                                    <li role="separator" class="dropdown-divider"></li>
                                    <li class="dropdown-header">Search</li>
                                    <li class="dropdown-item"><g:link controller="search" action="index">Search Page</g:link></li>
                                    <li role="separator" class="dropdown-divider"></li>
                                    <li class="dropdown-header">Custom Drinks</li>
                                    <li class="dropdown-item"><g:link controller="drink" action="showCustomDrinks">Custom Drinks</g:link></li>
                                    <li role="separator" class="dropdown-divider"></li>
                                    <li class="dropdown-item"><a href="#">Domains: ${grailsApplication.domainClasses.size()}</a></li>
                                    <li class="dropdown-item"><a href="#">Services: ${grailsApplication.serviceClasses.size()}</a></li>
                                    <li class="dropdown-item"><a href="#">Tag Libraries: ${grailsApplication.tagLibClasses.size()}</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#plugins" role="button" aria-haspopup="true" aria-expanded="false">Installed Plugins <span class="caret"></span></a>
                                <ul class="dropdown-menu dropdown-menu-right" id="plugins" style="background-color:#000000;">
                                    <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                                        <li class="dropdown-item"><a href="#">${plugin.name} - ${plugin.version}</a></li>
                                    </g:each>
                                </ul>
                            </li>
                        </div>
                    </ul>
                </div>
            </div>
        </nav>

        <% def drinkService = grailsApplication.mainContext.getBean('drinkService')  %>
        <div id="periodicTable" style="justify-content:center;display:inline-flex;padding:15em;margin:0;">
            <div id="column1" style="margin:0;padding:0;width:600px;">
                <div id="tequilaDrinks" style="margin:0;padding:0;">
                    <div class="card" style="">
                        <p style="text-align:center;margin-bottom:0;">Custom Tequila Drinks</p>
                        <div style="display:block;">
                            <%
                                List tequilaDrinks = drinkService.list().each { Drink d -> d.alcoholType==Alcohol.TEQUILA }
                                tequilaDrinks = tequilaDrinks.stream().limit(12).collect()
                                for (int i=0; i<tequilaDrinks.size(); i+=2) {
                                    Drink drink1 = (Drink)tequilaDrinks.get(i)
                                    if (i < tequilaDrinks.size()) {
                                        try { drink1 = (Drink)tequilaDrinks.get(i); if (!drink1.custom) drink1 = Drink.createFillerDrink(Alcohol.TEQUILA); drink1.id = i+1  }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.TEQUILA); drink1.id = i+1 }
                                    }
                                    Drink drink2 = null
                                    if (i+1 < tequilaDrinks.size()) {
                                        try { drink2 = (Drink)tequilaDrinks.get(i+1); if (!drink2.custom) drink2 = Drink.createFillerDrink(Alcohol.TEQUILA); drink1.id = i+1  }
                                        catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.TEQUILA); drink2.id = i+2 }
                                    }
                            %>
                            <div style="display:inline-flex;">
                                <div class="card" style="background-color:#ed969e;width:300px;height:300px;">
                                    <div style="display:inline-flex;">
                                        <div id="tequilaLeft1" style="height:200px;float:left;">
                                            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink1.drinkNumber}</b></p>
                                                <div style="overflow-y:auto;height:80px;">
                                                    <g:each in="${drink1.ingredients}" var="ingredientOption">
                                                        <p style="margin:0;">${ingredientOption}</p>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="tequilaRight1" style="text-align:center;height:200px;float:right;">
                                            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                <img width="75px" height="75px" title="${drink1.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink1.glassImage)}" alt="${drink1.glassImage}"/>
                                                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink1.drinkSymbol}</b></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="padding-left:10px;padding-top:10px;">
                                        <p style="margin:0;overflow-y:auto;height:45px;">${drink1.mixingInstructions}</p>
                                    </div>
                                    <div>
                                        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink1.drinkName}</b></p>
                                    </div>
                                </div>
                                <div class="card" style="background-color:#ed969e;width:300px;height:300px;">
                                    <div style="display:inline-flex;">
                                        <div id="tequilaLeft2" style="height:200px;float:left;">
                                            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink2.drinkNumber}</b></p>
                                                <div style="overflow-y:auto;height:80px;">
                                                    <g:each in="${drink2.ingredients}" var="ingredientOption">
                                                        <p style="margin:0;">${ingredientOption}</p>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="tequilaRight2" style="text-align:center;height:200px;float:right;">
                                            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                <img width="75px" height="75px" title="${drink2.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink2.glassImage)}" alt="${drink2.glassImage}"/>
                                                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink2.drinkSymbol}</b></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="padding-left:10px;padding-top:10px;">
                                        <p style="margin:0;overflow-y:auto;height:45px;">${drink2.mixingInstructions}</p>
                                    </div>
                                    <div>
                                        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink2.drinkName}</b></p>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                <div id="spacer" style="height:100px;"></div> <!-- for white space only -->
                <div id="key" style="text-align:center;padding:0;">
                    <h2><b>KEY</b></h2>
                    <div style="display:inline-flex;">
                        <div id="leftColumn" style="width:100px;height:280px;">
                            <p></p>
                            <p></p>
                            <h6>Cocktail</h6>
                            <h6 style="margin-bottom:-5px;">Number</h6>
                            <span class="arrow-right"></span>
                            <p style="height:20px;"></p>
                            <h6>Cocktail</h6>
                            <h6 style="margin-bottom:-5px;">Ingredients</h6>
                            <span class="arrow-right"></span>
                            <p style="height:30px;"></p>
                            <span class="arrow-right"></span>
                            <h6 style="margin-top:-5px;">Cocktail</h6>
                            <h6>Mixing</h6>
                            <h6>Instructions</h6>
                        </div>
                        <div id="middleColumn" style="text-align:center;display:inline-flex;">
                            <div class="card" style="border:double #000000;background-color:#95999c;width:300px;height:300px;">
                                <div style="display:inline-block;">
                                    <div id="innerLeft" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>20</b></p>
                                            <p style="margin:0;">1.5 oz Tequila</p>
                                            <p style="margin:0;">0.5 oz Triple Sec</p>
                                            <p style="margin:0;">1 oz Lemon Juice</p>
                                        </div>
                                    </div>
                                    <div id="innerRight" style="height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" src="${resource(dir:'../assets/images',file:'martiniGlass.png')}" alt="Cocktail Logo"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>Mg</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;"> <!-- text-align:left; looked funny. keep centered -->
                                    <p style="margin:0;height:45px;">Mix ingredients and enjoy!</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>MARGARITA</b></p>
                                </div>
                            </div>
                        </div>
                        <div id="rightColumn" style="width:100px;height:280px;">
                            <p></p>
                            <p></p>
                            <h6>Suggested</h6>
                            <h6 style="margin-bottom:-5px;">Glass</h6>
                            <span class="arrow-left"></span>
                            <p style="margin:50px;"></p>
                            <h6>Cocktail</h6>
                            <h6 style="margin-bottom:-5px;">Symbol</h6>
                            <span class="arrow-left"></span>
                            <p style="margin:60px;"></p>
                            <h6>Cocktail</h6>
                            <h6 style="margin-bottom:-5px;">Name</h6>
                            <span class="arrow-left"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div id="column2" style="margin:0;padding:0;width:2100px;">
                <div id="title" style="width:1200px;height:600px;">
                    <h1 id="chartTitle" style="width:2400px;font-size:180px;padding-left:35px;">Periodic Table of Mixology</h1>
                    <div id="reference" style="display:inline-flex;">
                        <div id="chart" style="margin-left:70px;margin-right:50px;width:1000px;">
                            <g:render template="customReferenceChart"/>
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
                            <div style="display:block;">
                                <%
                                    List vodkaDrinks = drinkService.list().each { Drink d -> d.alcoholType==Alcohol.VODKA}
                                    vodkaDrinks = vodkaDrinks.stream().limit(12).collect()
                                    for (int i=0; i<vodkaDrinks.size(); i+=4) {
                                        Drink drink1 = (Drink)vodkaDrinks.get(i)
                                        if (i < vodkaDrinks.size()) {
                                            try { drink1 = (Drink)vodkaDrinks.get(i); if (!drink1.custom) drink1 = Drink.createFillerDrink(Alcohol.VODKA); drink1.id = i+1  }
                                            catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.VOKDA); drink1.id = i+1 }
                                        }
                                        Drink drink2 = null
                                        if (i+1 < vodkaDrinks.size()) {
                                            try { drink2 = (Drink)vodkaDrinks.get(i); if (!drink2.custom) drink2 = Drink.createFillerDrink(Alcohol.VODKA); drink2.id = i+2  }
                                            catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.VOKDA); drink2.id = i+1 }
                                        }
                                        Drink drink3 = null
                                        if (i+2 < vodkaDrinks.size()) {
                                            try { drink3 = (Drink)vodkaDrinks.get(i); if (!drink3.custom) drink3 = Drink.createFillerDrink(Alcohol.VODKA); drink3.id = i+3  }
                                            catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.VOKDA); drink3.id = i+3 }
                                        }
                                        Drink drink4 = null
                                        if (i+3 < vodkaDrinks.size()) {
                                            try { drink4 = (Drink)vodkaDrinks.get(i); if (!drink4.custom) drink4 = Drink.createFillerDrink(Alcohol.VODKA); drink4.id = i+4  }
                                            catch (Exception e) { drink4 = Drink.createFillerDrink(Alcohol.VOKDA); drink4.id = i+4 }
                                        }
                                %>
                                <div style="display:inline-flex;">
                                    <div class="card" style="background-color:#ffdf7e;width:300px;height:300px;">
                                        <div style="display:inline-flex;">
                                            <div id="vokdaLeft1" style="height:200px;float:left;">
                                                <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                    <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink1.drinkNumber}</b></p>
                                                    <div style="overflow-y:auto;height:80px;">
                                                        <g:each in="${drink1.ingredients}" var="ingredientOption">
                                                            <p style="margin:0;">${ingredientOption}</p>
                                                        </g:each>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="vodkaRight1" style="text-align:center;height:200px;float:right;">
                                                <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                    <img width="75px" height="75px" title="${drink1.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink1.glassImage)}" alt="${drink1.glassImage}"/>
                                                    <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink1.drinkSymbol}</b></p>
                                                </div>
                                            </div>
                                        </div>
                                        <div style="padding-left:10px;padding-top:10px;">
                                            <p style="margin:0;overflow-y:auto;height:45px;">${drink1.mixingInstructions}</p>
                                        </div>
                                        <div>
                                            <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink1.drinkName}</b></p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#ffdf7e;width:300px;height:300px;">
                                        <div style="display:inline-flex;">
                                            <div id="vodkaLeft2" style="height:200px;float:left;">
                                                <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                    <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink2.drinkNumber}</b></p>
                                                    <div style="overflow-y:auto;height:80px;">
                                                        <g:each in="${drink2.ingredients}" var="ingredientOption">
                                                            <p style="margin:0;">${ingredientOption}</p>
                                                        </g:each>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="vodkaRight2" style="text-align:center;height:200px;float:right;">
                                                <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                    <img width="75px" height="75px" title="${drink2.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink2.glassImage)}" alt="${drink2.glassImage}"/>
                                                    <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink2.drinkSymbol}</b></p>
                                                </div>
                                            </div>
                                        </div>
                                        <div style="padding-left:10px;padding-top:10px;">
                                            <p style="margin:0;overflow-y:auto;height:45px;">${drink2.mixingInstructions}</p>
                                        </div>
                                        <div>
                                            <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink2.drinkName}</b></p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#ffdf7e;width:300px;height:300px;">
                                        <div style="display:inline-flex;">
                                            <div id="vodkaLeft3" style="height:200px;float:left;">
                                                <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                    <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink3.drinkNumber}</b></p>
                                                    <div style="overflow-y:auto;height:80px;">
                                                        <g:each in="${drink3.ingredients}" var="ingredientOption">
                                                            <p style="margin:0;">${ingredientOption}</p>
                                                        </g:each>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="vodkaRight3" style="text-align:center;height:200px;float:right;">
                                                <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                    <img width="75px" height="75px" title="${drink3.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink3.glassImage)}" alt="${drink3.glassImage}"/>
                                                    <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink3.drinkSymbol}</b></p>
                                                </div>
                                            </div>
                                        </div>
                                        <div style="padding-left:10px;padding-top:10px;">
                                            <p style="margin:0;overflow-y:auto;height:45px;">${drink3.mixingInstructions}</p>
                                        </div>
                                        <div>
                                            <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink3.drinkName}</b></p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#ffdf7e;width:300px;height:300px;">
                                        <div style="display:inline-flex;">
                                            <div id="vodkaLeft4" style="height:200px;float:left;">
                                                <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                    <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink4.drinkNumber}</b></p>
                                                    <div style="overflow-y:auto;height:80px;">
                                                        <g:each in="${drink4.ingredients}" var="ingredientOption">
                                                            <p style="margin:0;">${ingredientOption}</p>
                                                        </g:each>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="vodkaRight4" style="text-align:center;height:200px;float:right;">
                                                <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                    <img width="75px" height="75px" title="${drink2.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink4.glassImage)}" alt="${drink4.glassImage}"/>
                                                    <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink4.drinkSymbol}</b></p>
                                                </div>
                                            </div>
                                        </div>
                                        <div style="padding-left:10px;padding-top:10px;">
                                            <p style="margin:0;overflow-y:auto;height:45px;">${drink4.mixingInstructions}</p>
                                        </div>
                                        <div>
                                            <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink4.drinkName}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <div id="gin" style="margin:0;padding:0;width:905px;">
                        <div class="card">
                            <p style="text-align:center;margin-bottom:0;">Custom Gin Drinks</p>
                            <%
                                List ginDrinks = drinkService.list().each { Drink d -> d.alcoholType==Alcohol.GIN}
                                ginDrinks = ginDrinks.stream().limit(9).collect()
                                for (int i=0; i<ginDrinks.size(); i+=3) {
                                    Drink drink1 = (Drink)ginDrinks.get(i)
                                    if (i < tequilaDrinks.size()) {
                                        try { drink1 = (Drink)tequilaDrinks.get(i); if (!drink1.custom) drink1 = Drink.createFillerDrink(Alcohol.GIN); drink1.id = i+1  }
                                        catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.GIN); drink1.id = i+1 }
                                    }
                                    Drink drink2 = null
                                    if (i+1 < ginDrinks.size()) {
                                        try { drink2 = (Drink)tequilaDrinks.get(i); if (!drink2.custom) drink2 = Drink.createFillerDrink(Alcohol.GIN); drink2.id = i+2  }
                                        catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.GIN); drink2.id = i+1 }
                                    }
                                    Drink drink3 = null
                                    if (i+2 < ginDrinks.size()) {
                                        try { drink3 = (Drink)tequilaDrinks.get(i); if (!drink3.custom) drink3 = Drink.createFillerDrink(Alcohol.GIN); drink3.id = i+3  }
                                        catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.GIN); drink3.id = i+1 }
                                    }
                            %>
                            <div style="display:inline-flex;">
                                <div class="card" style="background-color:#46a5c8;width:300px;height:300px;">
                                    <div style="display:inline-flex;">
                                        <div id="ginLeft1" style="height:200px;float:left;">
                                            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink1.drinkNumber}</b></p>
                                                <div style="overflow-y:auto;height:80px;">
                                                    <g:each in="${drink1.ingredients}" var="ingredientOption">
                                                        <p style="margin:0;">${ingredientOption}</p>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="ginRight1" style="text-align:center;height:200px;float:right;">
                                            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                <img width="75px" height="75px" title="${drink1.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink1.glassImage)}" alt="${drink1.glassImage}"/>
                                                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink1.drinkSymbol}</b></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="padding-left:10px;padding-top:10px;">
                                        <p style="margin:0;overflow-y:auto;height:45px;">${drink1.mixingInstructions}</p>
                                    </div>
                                    <div>
                                        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink1.drinkName}</b></p>
                                    </div>
                                </div>
                                <div class="card" style="background-color:#46a5c8;width:300px;height:300px;">
                                    <div style="display:inline-flex;">
                                        <div id="ginLeft2" style="height:200px;float:left;">
                                            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink2.drinkNumber}</b></p>
                                                <div style="overflow-y:auto;height:80px;">
                                                    <g:each in="${drink2.ingredients}" var="ingredientOption">
                                                        <p style="margin:0;">${ingredientOption}</p>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="ginRight2" style="text-align:center;height:200px;float:right;">
                                            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                <img width="75px" height="75px" title="${drink2.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink2.glassImage)}" alt="${drink2.glassImage}"/>
                                                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink2.drinkSymbol}</b></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="padding-left:10px;padding-top:10px;">
                                        <p style="margin:0;overflow-y:auto;height:45px;">${drink2.mixingInstructions}</p>
                                    </div>
                                    <div>
                                        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink2.drinkName}</b></p>
                                    </div>
                                </div>
                                <div class="card" style="background-color:#46a5c8;width:300px;height:300px;">
                                    <div style="display:inline-flex;">
                                        <div id="ginLeft3" style="height:200px;float:left;">
                                            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                                <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink3.drinkNumber}</b></p>
                                                <div style="overflow-y:auto;height:80px;">
                                                    <g:each in="${drink3.ingredients}" var="ingredientOption">
                                                        <p style="margin:0;">${ingredientOption}</p>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="ginRight3" style="text-align:center;height:200px;float:right;">
                                            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                                <img width="75px" height="75px" title="${drink3.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink3.glassImage)}" alt="${drink3.glassImage}"/>
                                                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink3.drinkSymbol}</b></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="padding-left:10px;padding-top:10px;">
                                        <p style="margin:0;overflow-y:auto;height:45px;">${drink3.mixingInstructions}</p>
                                    </div>
                                    <div>
                                        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink3.drinkName}</b></p>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                <div id="shooterDrinks" style="width:2550px;padding:5em;">
                    <div class="card">
                        <p style="text-align:center;margin-bottom:0;">Custom Shooter Drinks</p>
                        <%
                            List shooterDrinks = drinkService.list().each { Drink d -> d.alcoholType==Alcohol.SHOOTER }
                            //shooterDrinks = shooterDrinks.stream().limit(16).collect()
                            for (int i=0; i<shooterDrinks.size(); i+=8) {
                                Drink drink1 = (Drink)shooterDrinks.get(i)
                                if (i < shooterDrinks.size()) {
                                    try { drink1 = (Drink)shooterDrinks.get(i); if (!drink1.custom) drink1 = Drink.createFillerDrink(Alcohol.SHOOTER); drink1.id = i+1 }
                                    catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.SHOOTER); drink1.id = i+1 }
                                }
                                Drink drink2 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink2 = (Drink)shooterDrinks.get(i); if (!drink2.custom) drink2 = Drink.createFillerDrink(Alcohol.SHOOTER); drink2.id = i+2 }
                                    catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.SHOOTER); drink2.id = i+2 }
                                }
                                Drink drink3 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink3 = (Drink)shooterDrinks.get(i); if (!drink3.custom) drink3 = Drink.createFillerDrink(Alcohol.SHOOTER); drink3.id = i+3 }
                                    catch (Exception e) { drink3 = Drink.createFillerDrink(Alcohol.SHOOTER); drink1.id = i+3 }
                                }
                                Drink drink4 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink4 = (Drink)shooterDrinks.get(i); if (!drink4.custom) drink4 = Drink.createFillerDrink(Alcohol.SHOOTER); drink4.id = i+4 }
                                    catch (Exception e) { drink4 = Drink.createFillerDrink(Alcohol.SHOOTER); drink4.id = i+1 }
                                }
                                Drink drink5 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink5 = (Drink)shooterDrinks.get(i); if (!drink5.custom) drink5 = Drink.createFillerDrink(Alcohol.SHOOTER); drink5.id = i+5 }
                                    catch (Exception e) { drink5 = Drink.createFillerDrink(Alcohol.SHOOTER); drink5.id = i+5 }
                                }
                                Drink drink6 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink6 = (Drink)shooterDrinks.get(i); if (!drink6.custom) drink6 = Drink.createFillerDrink(Alcohol.SHOOTER); drink6.id = i+6 }
                                    catch (Exception e) { drink6 = Drink.createFillerDrink(Alcohol.SHOOTER); drink6.id = i+6 }
                                }
                                Drink drink7 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink7 = (Drink)shooterDrinks.get(i); if (!drink7.custom) drink7 = Drink.createFillerDrink(Alcohol.SHOOTER); drink7.id = i+7 }
                                    catch (Exception e) { drink7 = Drink.createFillerDrink(Alcohol.SHOOTER); drink7.id = i+7 }
                                }
                                Drink drink8 = null
                                if (i+1 < shooterDrinks.size()) {
                                    try { drink8 = (Drink)shooterDrinks.get(i); if (!drink8.custom) drink8 = Drink.createFillerDrink(Alcohol.SHOOTER); drink8.id = i+8 }
                                    catch (Exception e) { drink8 = Drink.createFillerDrink(Alcohol.SHOOTER); drink8.id = i+8 }
                                }
                        %>
                        <div style="display:inline-flex;">
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft1" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink1.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink1.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight1" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink1.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink1.glassImage)}" alt="${drink1.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink1.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink1.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink1.drinkName}</b></p>
                                </div>
                            </div>
                            <% if (drink2 != null) { %>
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft2" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink2.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink2.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight2" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink2.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink2.glassImage)}" alt="${drink2.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink2.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink2.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink2.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                            <% if (drink3 != null) { %>
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft3" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink3.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink3.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight3" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink3.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink3.glassImage)}" alt="${drink3.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink3.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink3.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink3.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                            <% if (drink4 != null) { %>
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft4" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink4.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink4.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight4" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink4.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink4.glassImage)}" alt="${drink4.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink4.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink4.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink4.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                            <% if (drink5 != null) { %>
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft5" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink5.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink5.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight5" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink5.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink5.glassImage)}" alt="${drink5.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink5.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink5.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink5.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                            <% if (drink6 != null) { %>
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft6" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink6.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink6.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight6" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink6.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink6.glassImage)}" alt="${drink6.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink6.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink6.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink6.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                            <% if (drink7 != null) { %>
                            <div class="card" style="background-color:mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft7" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink7.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink7.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight7" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink7.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink7.glassImage)}" alt="${drink7.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink7.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink7.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink7.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                            <% if (drink8 != null) { %>
                            <div class="card" style="background-color: mediumpurple;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="shooterLeft8" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink8.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink8.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="shooterRight8" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink8.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink8.glassImage)}" alt="${drink8.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink8.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink8.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink8.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
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
                                List frozenDrinks = drinkService.list().each { Drink d -> d.alcoholType==Alcohol.FROZEN }
                                frozenDrinks = frozenDrinks.stream().limit(1).collect()
                                Drink drink1 = null
                                try { drink1 = (Drink)frozenDrinks.get(0); if (!drink1.custom) drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = 1 }
                                catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = 1 }
                            %>
                            <div class="card" style="background-color:mediumseagreen;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="frozenLeft1" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink1.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink1.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="frozenRight1" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink1.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink1.glassImage)}" alt="${drink1.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink1.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink1.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink1.drinkName}</b></p>
                                </div>
                            </div>
                        </div>
                        <%
                            frozenDrinks = drinkService.list().each { Drink d -> d.alcoholType==Alcohol.FROZEN }
                            frozenDrinks = frozenDrinks.stream().limit(9).collect()
                            for (int i=1; i<frozenDrinks.size(); i+=2) {
                                drink1 = (Drink)frozenDrinks.get(i)
                                if (i < frozenDrinks.size()) {
                                    try { drink1 = (Drink)frozenDrinks.get(i); if (!drink1.custom) drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = i+1 }
                                    catch (Exception e) { drink1 = Drink.createFillerDrink(Alcohol.FROZEN); drink1.id = i+1 }
                                }
                                Drink drink2 = null
                                if (i+1 < frozenDrinks.size()) {
                                    try { drink2 = (Drink)frozenDrinks.get(i); if (!drink2.custom) drink2 = Drink.createFillerDrink(Alcohol.FROZEN); drink2.id = i+1 }
                                    catch (Exception e) { drink2 = Drink.createFillerDrink(Alcohol.FROZEN); drink2.id = i+1 }
                                }
                        %>
                        <div style="display:inline-flex;">
                            <div class="card" style="background-color:mediumseagreen;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="frozenLeft2" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink1.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink1.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="frozenRight2" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink1.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink1.glassImage)}" alt="${drink1.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink1.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink1.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink1.drinkName}</b></p>
                                </div>
                            </div>
                            <% if (drink2 != null) { %>
                            <div class="card" style="background-color:mediumseagreen;width:300px;height:300px;">
                                <div style="display:inline-flex;">
                                    <div id="frozenLeft3" style="height:200px;float:left;">
                                        <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink2.drinkNumber}</b></p>
                                            <div style="overflow-y:auto;height:80px;">
                                                <g:each in="${drink2.ingredients}" var="ingredientOption">
                                                    <p style="margin:0;">${ingredientOption}</p>
                                                </g:each>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="frozenRight3" style="text-align:center;height:200px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" title="${drink2.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink2.glassImage)}" alt="${drink2.glassImage}"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink2.drinkSymbol}</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:10px;padding-top:10px;">
                                    <p style="margin:0;overflow-y:auto;height:45px;">${drink2.mixingInstructions}</p>
                                </div>
                                <div>
                                    <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink2.drinkName}</b></p>
                                </div>
                            </div>
                            <% } %>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>