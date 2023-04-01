<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/26/23
  Time: 9:33 PM
--%>

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
                            <li class="dropdown-header">User Mgmt</li>
                            <li class="dropdown-item"><g:link controller="user" action="index">Show Users</g:link></li>
                            <li class="dropdown-item"><g:link controller="user" action="create">Create User</g:link></li>
                            <li role="separator" class="dropdown-divider"></li>
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
                            <li class="dropdown-header">Drinks</li>
                            <li class="dropdown-item"><g:link url="${createLink(uri: '/')}">Default Drinks</g:link></li>
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