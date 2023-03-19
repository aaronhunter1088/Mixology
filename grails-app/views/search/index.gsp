<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/18/23
  Time: 2:09 PM
--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Search</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"/>
        <link rel="icon" type="image/x-ico" href="${resource(dir:'../assets/images',file:'martiniGlass.png')}" />
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <style>
            input[type="text"]
            {
                font-size:40px;
            }
        </style>
    </head>

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

        <div id="searchContent" style="padding:6em;text-align:center;">
            <div style="display:inline-flex;">
                <div id="title">
                    <h1 style="font-size:10em;">Search</h1>
                </div>
                <div id="search" style="display:flex;margin:40px;">
                    <input type="text" title="Enter a drink or ingredient value to search" style="width:300px;height:50px;margin:20px;"/>
                </div>
                <div id="submit" style="display:flex;margin-top:40px;">
                    <input type="button" class="btn btn-primary" style="font-size:2em;font-weight:bold;width:110px;height:50px;margin:20px;" onclick="search();" value="Search"/>
                </div>
                <div id="total" hidden>
                    <p>Total: </p>
                </div>
            </div>

            <div style="display:inline-flex;">
                <div id="results" hidden>
                    <div id="drinkResults">

                    </div>
                    <div id="ingredientResults">

                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            function search() {
                let searchingFor = $("#search > input").val();
                $.ajax({
                    headers: {
                        accept: "application/json",
                        contentType: "application/json"
                    },
                    async: false,
                    type: "GET",
                    url: "${createLink(controller:'search', action:'search')}",
                    data: {
                        searchingFor: searchingFor
                    },
                    statusCode: {
                        200: function(data) {
                            console.log(JSON.stringify(data))

                        },
                        400: function(data) {
                            failCount += 1;
                            let response = JSON.parse(JSON.stringify(data['responseJSON']))
                            let message = response.message;;
                            console.log("FAILED: " + message);
                            //row.addClass("errors");
                            //let errorMessage = $("#ingredientErrorMessage")
                            //errorMessage.addClass("errors");
                            //errorMessage.html(message);
                        },
                        404: function(data) {
                            console.log(JSON.stringify(data));
                        },
                        500: function(data) {
                            console.log(JSON.stringify(data));
                        }
                    }
                });
            }
        </script>
    </body>
</html>