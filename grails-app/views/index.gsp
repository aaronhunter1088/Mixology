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
        img {
            mix-blend-mode: color-burn;
            background-blend-mode: soft-light;
        }
    </style>
    <body style="overflow-x:scroll;">
        <nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
            <div class="container-fluid">
    %{--            <a class="navbar-brand" href="/#"><asset:image src="cocktail.ico" alt="Grails Logo"/></a>--}%
    %{--            <img src="../assets/images/cocktail.ico" alt="Cocktail Logo"/>--}%
                <img src="${resource(dir:'../assets/images',file:'cocktail.ico')}" alt="Cocktail Logo"/>
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

        <div id="periodicTable" style="display:inline-flex;">
            <div id="column1">
                <div id="tequilaDrinks" style="padding:2em;width:700px;">
                <p style="text-align:center;margin-bottom:0px;">Tequila Drinks</p>
                <g:each var="card" in="{1..6}">
                    <div style="display:inline-flex">
                        <div class="card" style="">
                            <div class="card" style="background-color:#ed969e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                <div class="card" style="width:50%;">
                                    <p>hello i'm on the left</p>
                                </div>
                            </div>
                            <div class="card" style="background-color:#ed969e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                <div class="card" style="width:50%;">
                                    <p>hello i'm on the right</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </g:each>
            </div>
            </div>
            <div id="column2" style="width:2400px;">
                <div id="title" style="padding:2em;width:1200px;height:600px;">
                    <h1 style="width:2400px;font-size:180px;">Periodic Table of Mixology</h1>
                </div>
                <div style="display:inline-flex;">
                    <div id="vodka" style="padding:2em;width:1200px;">
                        <div class="card" style="">
                            <p style="text-align:center;margin-bottom:0px;">Vodka Drinks</p>
                            <g:each var="card" in="{1}">
                                <div style="display:inline-flex">
                                    <div class="card" style="background-color:#ffdf7e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm on the left</p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#ffdf7e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm on the right</p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#ffdf7e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm on the left</p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#ffdf7e;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm on the right</p>
                                        </div>
                                    </div>
                                </div>
                            </g:each>
                        </div>
                    </div>
                    <div id="gin" style="padding:2em;width:900px;">
                        <div class="card" style="">
                            <p style="text-align:center;margin-bottom:0px;">Gin Drinks</p>
                            <g:each var="card" in="{1}">
                                <div style="display:inline-flex;">
                                    <div class="card" style="background-color:#006dba;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm on the left</p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#006dba;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm in the middle</p>
                                        </div>
                                    </div>
                                    <div class="card" style="background-color:#006dba;padding-left:5px;padding-top:5px;width:300px;height:300px;">
                                        <div class="card" style="width:50%;">
                                            <p>hello i'm on the right</p>
                                        </div>
                                    </div>
                                </div>
                            </g:each>
                        </div>
                    </div>
                </div>
            </div>
            <div id="column3">

            </div>
        </div>



%{--        <div id="controllers" style="text-align:center;">--}%
%{--            <h2>Available Controllers:</h2>--}%
%{--            <ul>--}%
%{--                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">--}%
%{--                    <li class="controller">--}%
%{--                        <g:link controller="${c.logicalPropertyName}">${c.name}</g:link>--}%
%{--                    </li>--}%
%{--                </g:each>--}%
%{--            </ul>--}%
%{--        </div>--}%
        <script type="text/javascript">
        $(document).ready(function() {
            console.log("index loaded");
            //$("#controllers").show(); // by default
        });
        // function showControllers() {
        //     $("#controllers").show();
        // }
    </script>
    </body>
</html>