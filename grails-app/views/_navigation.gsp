<%@ page import="grails.util.Environment; enums.*; mixology.*; java.time.LocalTime;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
    def user = User.findByUsername(springSecurityService.authentication.getPrincipal().username as String)
%>
<style>
    .navbar-dark {
        background-color: rgba(0,0,0,0) !important;
    }
</style>                    <!-- navbar-dark OR navbar-inverse-->
<div id="home-bar" <g:if test="${user.darkMode}">class="navbar navbar-expand-sm navbar-dark"</g:if>
                   <g:else>class="navbar navbar-expand-sm navbar-inverse"</g:else>
     style="padding:30px 0;text-align:center;">
    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
        <ul <g:if test="${user.darkMode}">class="nav navbar-nav ml-auto navbar-dark" </g:if>
                                  <g:else>class="nav navbar-nav ml-auto navbar-inverse" </g:else>>
            <div style="display:flex;margin: auto 10px;align-content:center;">
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa fa-home dropdown-toggle" data-toggle="dropdown" data-target="#home" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="default.home.label" default="Home"/></a>
                    <ul class="dropdown-menu" id="home" style="background-color:<g:if test="${user.darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                        <li class="dropdown-header" style="padding-left:10px;"><g:message code="default.home.label" default="Home"/></li><br/>
                        <g:link class="fa fa-home" url="${createLink(uri: '/')}">&nbsp;<g:message code="navigation.default.drinks" default="Default Drinks"/> </g:link>
                        <g:link class="fa fa-home" controller="drink" action="showCustomDrinks">&nbsp;<g:message code="navigation.your.drinks" default="Your Drinks"/></g:link>
                    </ul>
                </li> <!-- Home -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa fa-user dropdown-toggle" data-toggle="dropdown" data-target="#userMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="user.label" default="User"/> <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="userMgmt" style="background-color:<g:if test="${user.darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                        <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.user.management" default="User Mgmt"/></li><br/>
                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                            <g:set var="userValue" value="${g.message(code:'user.label',default:'User')}"/>
                            <g:link class="fa-solid fa-list" controller="user" action="index">&nbsp;<g:message code="navigation.show.all.users" default="Show all Users"/></g:link>
                            <g:link class="fa fa-user" controller="user" action="create">&nbsp;<g:message code="navigation.new.user" default="New User"/></g:link>
                        </sec:ifAnyGranted>
                        <g:link class="fa fa-circle-info" controller="user" action="show" params="[id:user.id]">&nbsp;<g:message code="navigation.user.details" default="User Details"/></g:link>
                        <g:if test="${user.darkMode}">
                            <g:link class="fa-solid fa-lightbulb" controller="user" action="darkMode" params="[uri:request.requestURI]">&nbsp;<g:message code="navigation.enable.dark.mode" default="Enable Dark Mode"/></g:link>
                        </g:if><g:else>
                            <g:link class="fa-regular fa-lightbulb" controller="user" action="darkMode" params="[uri:request.requestURI]">&nbsp;<g:message code="navigation.enable.light.mode" default="Enable Light Mode"/></g:link>
                        </g:else>
                        <g:link class="fa fa-light fa-arrow-right-from-bracket" controller="logout" action="index">&nbsp;<g:message code="springSecurity.logout" default="Logout"/></g:link>
                    </ul>
                </li> <!-- Users -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa-solid fa-martini-glass-empty dropdown-toggle" data-toggle="dropdown" data-target="#drinkMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="drink.label" args="s" default="Drinks"/> <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="drinkMgmt" style="background-color:<g:if test="${user.darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                        <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.drink.management" default="Drink Mgmt"/></li><br/>
                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                            <g:link class="fa-solid fa-list" controller="drink" action="index">&nbsp;<g:message code="drink.label" args="s" default="Drinks"/></g:link>
                        </sec:ifAnyGranted>
                        <g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;<g:message code="navigation.your.drinks" default="Your Drinks"/></g:link>
                        <g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="create">&nbsp;<g:message code="navigation.new.drink" default="New Drink"/></g:link>
                    </ul>
                </li> <!-- Drinks -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa-solid fa-jar-wheat dropdown-toggle" data-toggle="dropdown" data-target="#ingredientMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="ingredient.label" args="s" default="Ingredients"/><span class="caret"></span></a>
                    <ul class="dropdown-menu" id="ingredientMgmt" style="background-color:<g:if test="${user.darkMode}">#000000;</g:if><g:else>ghostwhite;</g:else>">
                        <li class="dropdown-header" style="padding-left:10px;"><g:message code="navigation.ingredient.management" default="Ingredient Mgmt"/></li><br/>
                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                            <g:link class="fa-solid fa-list" controller="ingredient" action="index">&nbsp;<g:message code="ingredient.label" args="s" default="Ingredients"/></g:link>
                        </sec:ifAnyGranted>
                        <g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;<g:message code="navigation.your.ingredients" default="Your Ingredients"/></g:link>
                        <g:link class="fa fa-light fa-jar" controller="ingredient" action="create">&nbsp;<g:message code="navigation.new.ingredient" default="New Ingredient"/></g:link>
                    </ul>
                </li> <!-- Ingredients -->
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-circle-info dropdown-toggle" data-toggle="dropdown" data-target="#appStatus" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="navigation.app.status" default="App Status"/><span class="caret"></span></a>
                        <ul class="dropdown-menu" id="appStatus" style="background-color:#000000;">
                            <g:link class="fa fa-thin fa-seedling" controller="mixology" action="env">&nbsp;<g:message code="navigation.environment" default="Environment"/>: ${Environment.current.name}</g:link>
                            <g:link class="fa fa-thin fa-globe" controller="mixology" action="profile">&nbsp;<g:message code="navigation.app.profile" default="App profile"/>: ${grailsApplication.config.getProperty('grails.profile')}</g:link>
                            <g:link class="fa fa-thin fa-code-branch" controller="mixology" action="appVersion">&nbsp;<g:message code="navigation.app.version" default="App version"/>:<g:meta name="info.app.version"/></g:link>
                            <li role="separator" class="dropdown-divider"></li>
                            <g:link class="fa fa-thin fa-code-pull-request" controller="mixology" action="grailsVersion">&nbsp;<g:message code="navigation.grails.version" default="Grails version"/>:<g:meta name="info.app.grailsVersion"/></g:link>
                            <g:link class="fa fa-thin fa-code-pull-request" controller="mixology" action="groovyVersion">&nbsp;<g:message code="navigation.groovy.version" default="Groovy version"/>: ${GroovySystem.getVersion()}</g:link>
                            <g:link class="fa fa-thin fa-code-pull-request" controller="mixology" action="jvmVersion">&nbsp;<g:message code="navigation.jvm.version" default="JVM version"/>: ${System.getProperty('java.version')}</g:link>
                            <li role="separator" class="dropdown-divider"></li>
                            <g:link class="fa fa-circle-info" controller="mixology" action="reloadingActive">&nbsp;<g:message code="navigation.reloading.active" default="Reloading active"/>: ${Environment.reloadingAgentEnabled}</g:link>
                            <g:link class="fa fa-circle-info" controller="mixology" action="domainsActive">&nbsp;<g:message code="navigation.domain" args="s" default="Domains"/>: ${grailsApplication.domainClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" controller="mixology" action="servicesActive">&nbsp;<g:message code="navigation.service" args="s" default="Services"/>: ${grailsApplication.serviceClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" controller="mixology" action="tagLibsActive">&nbsp;<g:message code="navigation.tag.libraries" default="Tag Libraries"/>: ${grailsApplication.tagLibClasses.size()}</g:link>
                        </ul>
                    </li>
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-light fa-plug-circle-plus dropdown-toggle" data-toggle="dropdown" data-target="#plugins" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;<g:message code="navigation.plugin" args="s" default="Plugins"/></a>
                        <ul class="dropdown-menu dropdown-menu-right" id="plugins" style="background-color:#000000;">
                            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                                <g:link class="fa fa-solid fa-plus" controller="mixology" action="plugin">&nbsp;${plugin.name} - ${plugin.version}</g:link>
                            </g:each>
                        </ul>
                    </li>
                </sec:ifAnyGranted>
            </div>
        </ul>
    </div>
</div>