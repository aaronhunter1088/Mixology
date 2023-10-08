<%@ page import="grails.util.Environment; enums.*; mixology.*; java.time.LocalTime;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
    def user = User.findByUsername(springSecurityService.authentication.getPrincipal().username as String)
%>
<style>
    .navbar-default .navbar-nav .open .dropdown-menu>li>a, .navbar-default .navbar-nav .open
    .dropdown-menu {
        background-color: ghostwhite;
        color:#222;
    }
</style>                    <!-- navbar-dark OR navbar-inverse-->
<div id="home-bar" <g:if test="${user.darkMode}">class="navbar navbar-expand-sm navbar-dark" </g:if><g:else>class="navbar navbar-expand-sm navbar-inverse"</g:else> style="padding:30px 0;text-align:center;">
    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
        <ul <g:if test="${user.darkMode}">class="nav navbar-nav ml-auto navbar-dark" </g:if><g:else>class="nav navbar-nav ml-auto navbar-inverse" </g:else>>
            <div style="display:flex;margin: auto 10px;align-content:center;">
                <!-- Default homepage or custom homepage -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa fa-home dropdown-toggle" data-toggle="dropdown" data-target="#home" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;Home</a>
                    <ul class="dropdown-menu" id="home" style="background-color:#000000;">
                        <li class="dropdown-header" style="padding-left:10px;">Home</li><br/>
                        <g:link class="fa fa-home" url="${createLink(uri: '/')}">&nbsp;Default Drinks</g:link>
                        <g:link class="fa fa-home" controller="drink" action="showCustomDrinks">&nbsp;Your Drinks</g:link>
                    </ul>
                </li> <!-- Home -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa fa-user dropdown-toggle" data-toggle="dropdown" data-target="#userMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;User <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="userMgmt" style="background-color:#000000;">
                        <li class="dropdown-header" style="padding-left:10px;">User Mgmt</li><br/>
                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                            <g:link class="fa-solid fa-list" controller="user" action="index">&nbsp;Show all Users</g:link>
                            <g:link class="fa fa-user" controller="user" action="create">&nbsp;New User</g:link>
                        </sec:ifAnyGranted>
                        <g:link class="fa fa-circle-info" controller="user" action="show" params="[id:user.id]">&nbsp;User Details</g:link>
                        <g:link class="fa fa-circle-info" controller="user" action="darkMode" params="[uri:request.requestURI]">&nbsp;Enable <g:if test="${user.darkMode}">Light</g:if><g:else>Dark</g:else> Mode</g:link>
                        <g:link class="fa fa-light fa-arrow-right-from-bracket" controller="logout" action="index">&nbsp;Logout</g:link>
                    </ul>
                </li> <!-- Users -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa-solid fa-martini-glass-empty dropdown-toggle" data-toggle="dropdown" data-target="#drinkMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;Drinks <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="drinkMgmt" style="background-color:#000000;">
                        <li class="dropdown-header" style="padding-left:10px;">Drink Mgmt</li><br/>
                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                            <g:link class="fa-solid fa-list" controller="drink" action="index">&nbsp;Drinks</g:link>
                        </sec:ifAnyGranted>
                        <g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;Your Drinks</g:link>
                        <g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="create">&nbsp;New Drink</g:link>
                    </ul>
                </li> <!-- Drinks -->
                <li class="dropdown-btn dropdown">
                    <a href="#" class="fa-solid fa-jar-wheat dropdown-toggle" data-toggle="dropdown" data-target="#ingredientMgmt" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;Ingredients <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="ingredientMgmt" style="background-color:#000000;">
                        <li class="dropdown-header" style="padding-left:10px;">Ingredient Mgmt</li><br/>
                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                            <g:link class="fa-solid fa-list" controller="ingredient" action="index">&nbsp;Ingredients</g:link>
                        </sec:ifAnyGranted>
                        <g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;Your Ingredients</g:link>
                        <g:link class="fa fa-light fa-jar" controller="ingredient" action="create">&nbsp;New Ingredient</g:link>
                    </ul>
                </li> <!-- Ingredients -->
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-circle-info dropdown-toggle" data-toggle="dropdown" data-target="#appStatus" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;App Status <span class="caret"></span></a>
                        <ul class="dropdown-menu" id="appStatus" style="background-color:#000000;">
                            <g:link class="fa fa-thin fa-seedling" controller="mixology" action="env">&nbsp;Environment: ${Environment.current.name}</g:link>
                            <g:link class="fa fa-thin fa-globe" controller="mixology" action="profile">&nbsp;App profile: ${grailsApplication.config.getProperty('grails.profile')}</g:link>
                            <g:link class="fa fa-thin fa-code-branch" controller="mixology" action="appVersion">&nbsp;App version:<g:meta name="info.app.version"/></g:link>
                            <li role="separator" class="dropdown-divider"></li>
                            <g:link class="fa fa-thin fa-code-pull-request" controller="mixology" action="grailsVersion">&nbsp;Grails version:<g:meta name="info.app.grailsVersion"/></g:link>
                            <g:link class="fa fa-thin fa-code-pull-request" controller="mixology" action="groovyVersion">&nbsp;Groovy version: ${GroovySystem.getVersion()}</g:link>
                            <g:link class="fa fa-thin fa-code-pull-request" controller="mixology" action="jvmVersion">&nbsp;JVM version: ${System.getProperty('java.version')}</g:link>
                            <li role="separator" class="dropdown-divider"></li>
                            <g:link class="fa fa-circle-info" controller="mixology" action="reloadingActive">&nbsp;Reloading active: ${Environment.reloadingAgentEnabled}</g:link>
                            <g:link class="fa fa-circle-info" controller="mixology" action="domainsActive">&nbsp;Domains: ${grailsApplication.domainClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" controller="mixology" action="servicesActive">&nbsp;Services: ${grailsApplication.serviceClasses.size()}</g:link>
                            <g:link class="fa fa-circle-info" controller="mixology" action="tagLibsActive">&nbsp;Tag Libraries: ${grailsApplication.tagLibClasses.size()}</g:link>
                        </ul>
                    </li>
                    <li class="dropdown-btn dropdown">
                        <a href="#" class="fa fa-light fa-plug-circle-plus dropdown-toggle" data-toggle="dropdown" data-target="#plugins" role="button" aria-haspopup="true" aria-expanded="false">&nbsp;Plugins</a>
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