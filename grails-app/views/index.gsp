<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/9/23
  Time: 9:43 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    %{--    <meta name="layout" content="main"/>--}%
        <title>Mixology</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
%{--        <asset:link rel="icon" href="cocktailRed.png" type="image/png" id="icon"/>--}%
%{--        <link rel="icon" type="image/x-ico" href="../../assets/images/cocktail.ico">--}%
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'cocktail.ico')}" />
    </head>
    <style>
        /*img {*/
        /*    mix-blend-mode: color-burn;*/
        /*    background-blend-mode: soft-light;*/
        /*}*/
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
                <img style="mix-blend-mode:color-burn;background-blend-mode:soft-light;" src="${resource(dir:'../assets/images',file:'cocktail.ico')}" alt="Cocktail Logo"/>
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
                                        <li class="dropdown-item"><g:link controller="${c.logicalPropertyName}">${c.name}</g:link></li>
                                    </g:each>
                                    <li role="separator" class="dropdown-divider"></li>
%{--                                    <li class="dropdown-item"><a href="javascript:showControllers()">Controllers: ${grailsApplication.controllerClasses.size()}</a></li>--}%
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

        <div id="periodicTable" style="justify-content:center;display:inline-flex;padding:15em;margin:0;">
            <div id="column1" style="margin:0;padding:0;width:600px;">
                <div id="tequilaDrinks" style="margin:0;padding:0;">
                    <div class="card">
                        <p style="text-align:center;margin-bottom:0;">Tequila Drinks</p>
                        <g:each var="card" in="${(1..6)}">
                            <div style="display:inline-flex;">
                                <g:each var="card2" in="${(1..2)}">
                                    <div class="card" style="background-color:#ed969e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>i'm some tequila drink</p>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                        </g:each>
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
                                    <div id="innerLeft" style="height:260px;float:left;">
                                        <div style="padding-left:10px;padding-top:10px;width:140px;height:50px;">
                                            <p style="font-size:5em;margin:0;color:navy;"><b>20</b></p>
                                            <p style="margin:0;">1.5 oz Tequila</p>
                                            <p style="margin:0;">0.5 oz Triple Sec</p>
                                            <p style="margin:0;">1 oz Lemon Juice</p>
                                            <p style="margin:10px;"></p>
                                            <p style="margin:0;">Mix ingredients and enjoy!</p>
                                        </div>
                                    </div>
                                    <div id="innerRight" style="height:260px;float:right;">
                                        <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                                            <img width="75px" height="75px" src="${resource(dir:'../assets/images',file:'exampleGlass.png')}" alt="Cocktail Logo"/>
                                            <p style="font-size:5em;margin-top:10px;color:#155724;"><b>Mg</b></p>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <p style="font-size:2em;margin:0;color:#a60000;"><b>MARGARITA</b></p>
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
            <div id="column2" style="margin:0;padding:0;width:2099px;">
                <div id="title" style="width:1200px;height:600px;">
                    <h1 style="width:2400px;font-size:180px;padding-left:35px;">Periodic Table of Mixology</h1>
                    <div id="reference" style="display:inline-flex;">
                        <div id="chart" style="margin-left:70px;margin-right:50px;width:1000px;">
                            <g:render template="referenceChart"/>
                        </div>
                        <div id="measurements" style="margin-left:50px;margin-right:50px;">
                            <g:render template="measurementsCard"/>
                        </div>
                        <div id="glasses">

                        </div>
                    </div>
                </div>
                <div id="vodkaAndGin" style="display:inline-flex;">
                    <div id="vodkaDrinks" style="margin:0;padding:0;width:1200px;">
                        <div class="card" style="">
                            <p style="text-align:center;margin-bottom:0;">Vodka Drinks</p>
                            <g:each var="card" in="${(1..3)}">
                                <div style="display:inline-flex">
                                    <g:each var="card2" in="${(1..4)}">
                                        <div class="card" style="background-color:#ffdf7e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                            <div class="card" style="width:50%;">
                                                <p>i'm some vodka drink</p>
                                            </div>
                                        </div>
                                    </g:each>
                                </div>
                            </g:each>
                        </div>
                    </div>
                    <div id="gin" style="margin:0;padding:0;width:900px;">
                        <div class="card" style="">
                            <p style="text-align:center;margin-bottom:0px;">Gin Drinks</p>
                            <g:each var="card" in="${(1..3)}">
                                <div style="display:inline-flex;">
                                    <g:each var="card2" in="${(1..3)}">
                                        <div class="card" style="background-color:#006dba;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                            <div class="card" style="width:50%;">
                                                <p>i'm some gin drink</p>
                                            </div>
                                        </div>
                                    </g:each>
                                </div>
                            </g:each>
                        </div>
                    </div>
                </div>
                <div id="shooterDrinks" style="width:2550px;padding:5em;">
                    <div class="card">
                        <p style="text-align:center;margin-bottom:0;">Shooter Drinks</p>
                        <g:each var="card" in="${(1..2)}">
                            <div style="display:inline-flex;">
                                <g:each var="card2" in="${(1..8)}">
                                    <div class="card" style="background-color:#6610f2;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>i'm some shooter drink</p>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                        </g:each>
                    </div>
                </div>
            </div>
            <div id="column3" style="margin:0;padding:0;width:600px;">
                <div id="frozenDrinks" style="">
                    <div class="card">
                        <p style="text-align:center;margin-bottom:0;">Frozen Drinks</p>
                        <div style="display:inline-flex;">
                            <div style="width:300px;height:300px;">
                                <img title="Don't Drink And Drive!" width="290px" height="290px" src="${resource(dir:'../assets/images',file:'dontDrinkAndDrive.png')}" alt="Don't Drink and Drive"/>
                            </div>
                            <div class="card" style="background-color:#19692c;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                <div class="card" style="width:50%;">
                                    <p>i'm a frozen drink</p>
                                </div>
                            </div>
                        </div>
                        <g:each var="card" in="${(1..4)}">
                            <div style="display:inline-flex;">
                                <g:each var="card2" in="${(1..2)}">
                                    <div class="card" style="background-color:#19692c;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>i'm some frozen drink</p>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                        </g:each>
                    </div>
                </div>
            </div>
        </div>


        <script type="text/javascript">
        $(document).ready(function() {
            console.log("index loaded");
        });
    </script>
    </body>
</html>