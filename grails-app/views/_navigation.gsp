<%@ page import="grails.util.Environment; enums.*; mixology.*; java.time.LocalTime;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
    def user = User.findByUsername(springSecurityService.authentication.getPrincipal().username as String)
%>
<g:set var="language" value="${user?.language ? user.language : (params.lang) ? params.lang : 'en'}"/>
<g:set var="darkMode" value="${user?.darkMode || (params.darkMode=='true') ?: false}"/>
<style>
    .navbar-dark {
        background-color: rgba(0,0,0,0) !important;
    }
    ::-webkit-scrollbar {
        -webkit-appearance:none;
        width: 5px;
        height: 5px;
    }
    .home::-webkit-scrollbar {
        display:none; /* exclude scrollbar from navbar only */
    }
    ::-webkit-scrollbar-thumb {
        border-radius: 4px;
        background-color: rgb(128, 128, 128);
        box-shadow: 0 0 1px rgba(255, 255, 255, .5);
    }
</style>                    <!-- navbar-dark OR navbar-inverse-->
<div id="home-bar" <g:if test="${darkMode}">class="navbar navbar-expand-sm navbar-dark"</g:if>
                   <g:else>class="navbar navbar-expand-sm navbar-inverse"</g:else>
     style="padding:30px 0;text-align:left;">
    <div id="navbarContent" class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;">
        <ul <g:if test="${darkMode}">class="nav navbar-nav ml-auto navbar-dark"</g:if>
                                  <g:else>class="nav navbar-nav ml-auto navbar-inverse"</g:else>
            style=""><!--width:90%;height:auto;overflow:hidden;overflow-x:auto;-->
            <div style="display:flex;margin: auto 10px;align-content:center;">
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa fa-home dropdown-toggle" data-toggle="dropdown" data-target="#home" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="default.home.label" default="Home"/></a>
                    <ul class="dropdown-menu" id="home" style="background-color:<g:if test="${darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                        <li class="dropdown-header" style="padding-left:10px;"><g:message code="default.home.label" default="Home"/></li><br/>
                        <g:link class="fa fa-home" controller="drink" action="showDrinks" params="[lang:language,darkMode:darkMode]">&nbsp;<g:message code="navigation.default.drinks" default="Default Drinks"/> </g:link>
                        <sec:ifLoggedIn>
                            <g:link class="fa fa-home" controller="drink" action="showCustomDrinks">&nbsp;<g:message code="navigation.your.drinks" default="Your Drinks"/></g:link>
                        </sec:ifLoggedIn>
                    </ul>
                </li> <!-- Home -->
                <sec:ifLoggedIn>
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-user dropdown-toggle" data-toggle="dropdown" data-target="#userMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="user.label" default="User"/> <span class="caret"></span></a>
                        <ul class="dropdown-menu" id="userMgmt" style="background-color:<g:if test="${darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                            <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.user.management" default="User Mgmt"/></li><br/>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:set var="userValue" value="${g.message(code:'user.label',default:'User')}"/>
                                <g:link class="fa-solid fa-list" controller="user" action="index">&nbsp;<g:message code="navigation.show.all.users" default="Show all Users"/></g:link>
                                <g:link class="fa fa-user" controller="user" action="create" params="[darkMode:user?.darkMode]">&nbsp;<g:message code="navigation.new.user" default="New User"/></g:link>
                            </sec:ifAnyGranted>
                            <g:link class="fa fa-circle-info" controller="user" action="show" params="[id:user?.id]">&nbsp;<g:message code="navigation.user.details" default="User Details"/></g:link>
                            <g:if test="${user?.darkMode}">
                                <g:link class="fa-solid fa-lightbulb" controller="user" action="darkMode" params="[uri:request.requestURI]">&nbsp;<g:message code="navigation.enable.dark.mode" default="Enable Dark Mode"/></g:link>
                            </g:if><g:else>
                                <g:link class="fa-regular fa-lightbulb" controller="user" action="darkMode" params="[uri:request.requestURI]">&nbsp;<g:message code="navigation.enable.light.mode" default="Enable Light Mode"/></g:link>
                            </g:else>
                            <g:link class="fa fa-light fa-arrow-right-from-bracket" controller="logout" action="index">&nbsp;<g:message code="springSecurity.logout" default="Logout"/></g:link>
                        </ul>
                    </li> <!-- Users -->
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa-solid fa-object-group dropdown-toggle" data-toggle="dropdown" data-target="#objectMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="default.objects.label" args="s" default="Objects"/> <span class="caret"></span></a>
                        <ul class="dropdown-menu" id="objectMgmt" style="background-color:<g:if test="${user?.darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                            <!-- Drink -->
                            <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.drink.management" default="Drink Mgmt"/></li><br/>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:link class="fa-solid fa-list" controller="drink" action="index">&nbsp;<g:message code="drink.label" args="s" default="Drinks"/></g:link>
                            </sec:ifAnyGranted>
                            <g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;<g:message code="navigation.your.drinks" default="Your Drinks"/></g:link>
                            <g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="create">&nbsp;<g:message code="navigation.new.drink" default="New Drink"/></g:link>
                            <!-- Ingredient -->
                            <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.ingredient.management" default="Ingredient Mgmt"/></li><br/>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:link class="fa-solid fa-list" controller="ingredient" action="index">&nbsp;<g:message code="navigation.ingredient.label" default="Ingredients"/></g:link>
                            </sec:ifAnyGranted>
                            <g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;<g:message code="navigation.your.ingredients" default="Your Ingredients"/></g:link>
                            <g:link class="fa fa-light fa-jar" controller="ingredient" action="create">&nbsp;<g:message code="navigation.new.ingredient" default="New Ingredient"/></g:link>
                        <!-- Auth Token -->
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.authToken.management" default="AuthToken Mgmt"/></li><br/>
                                <g:link class="fa-solid fa-list" controller="authToken" action="index">&nbsp;<g:message code="authToken.label" args="s" default="Auth Tokens"/></g:link>
                            </sec:ifAnyGranted>
                        </ul>
                    </li> <!-- Domain Objects -->
                </sec:ifLoggedIn>
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa fa-home dropdown-toggle" data-toggle="dropdown" data-target="#api" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="default.swagger.label" default="API & Swagger"/></a>
                    <ul class="dropdown-menu" id="api" style="background-color:<g:if test="${darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                        <li class="dropdown-header" style="padding-left:10px;"><g:message code="default.swagger.label" default="API & Swagger"/></li><br/>
                        <g:link class="fa fa-home" url="${createLink(uri: '/swagger-ui/index.html')}">&nbsp;<g:message code="navigation.default.swagger" default="API Swagger UI"/> </g:link>
                        <g:link class="fa fa-home" url="${createLink(uri: '/v3/api-docs')}">&nbsp;<g:message code="navigation.default.apidocs" default="OpenAPI Documentation"/></g:link>
                    </ul>
                </li>
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-circle-info dropdown-toggle" data-toggle="dropdown" data-target="#appStatus" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="navigation.app.status" default="App Status"/><span class="caret"></span></a>
                        <ul class="dropdown-menu" id="appStatus" style="background-color:#000000;">
                            <g:link class="fa fa-thin fa-seedling" view="/">&nbsp;<g:message code="navigation.environment" default="Environment"/>: ${Environment.current.name}</g:link>
                            <g:link class="fa fa-thin fa-globe" view="/">&nbsp;<g:message code="navigation.app.profile" default="App profile"/>: ${grailsApplication.config.getProperty('grails.profile')}</g:link>
                            <g:link class="fa fa-thin fa-code-branch" view="/">&nbsp;<g:message code="navigation.app.version" default="App version"/>:<g:meta name="info.app.version"/></g:link>
                            <li role="separator" class="dropdown-divider"></li>
                            <g:link class="fa fa-thin fa-code-pull-request" view="/">&nbsp;<g:message code="navigation.grails.version" default="Grails version"/>:<g:meta name="info.app.grailsVersion"/></g:link>
                            <g:link class="fa fa-thin fa-code-pull-request" view="/">&nbsp;<g:message code="navigation.groovy.version" default="Groovy version"/>: ${GroovySystem.getVersion()}</g:link>
                            <g:link class="fa fa-thin fa-code-pull-request" view="/">&nbsp;<g:message code="navigation.jvm.version" default="JVM version"/>: ${System.getProperty('java.version')}</g:link>
                            <li role="separator" class="dropdown-divider"></li>
                            <g:link class="fa fa-circle-info" view="/">&nbsp;<g:message code="navigation.reloading.active" default="Reloading active"/>: ${Environment.reloadingAgentEnabled}</g:link>
                            <g:link class="fa fa-circle-info" view="/">&nbsp;<g:message code="navigation.domain" args="s" default="Domains"/>: ${grailsApplication.domainClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" view="/">&nbsp;<g:message code="navigation.service" args="s" default="Services"/>: ${grailsApplication.serviceClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" view="/">&nbsp;<g:message code="navigation.tag.libraries" default="Tag Libraries"/>: ${grailsApplication.tagLibClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" view="/">&nbsp;Hello there, world</g:link>
                        </ul>
                    </li> <!-- App Status -->
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-light fa-plug-circle-plus dropdown-toggle" data-toggle="dropdown" data-target="#plugins" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="navigation.plugin" args="s" default="Plugins"/></a>
                        <ul class="dropdown-menu dropdown-menu-right" id="plugins" style="background-color:#000000;">
                            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                                <g:link class="fa fa-solid fa-plus" controller="mixology" action="plugin">&nbsp;${plugin.name} - ${plugin.version}</g:link>
                            </g:each>
                        </ul>
                    </li> <!-- Plugins -->
                </sec:ifAnyGranted>
            </div>
        </ul>
    </div>
</div>