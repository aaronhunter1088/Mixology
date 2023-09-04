<%--
  Created by IntelliJ IDEA.
  User: michaelball
  Date: 3/26/23
  Time: 9:33 PM
--%>
<%@ page import="mixology.User; java.time.LocalTime;" %>
<style>
    .home-bar {
        display:inline-flex;
    }
    .home-bar a, .dropdown-btn {
        padding: 6px 8px 6px 16px;
        text-decoration: none;
        font-size: 20px;
        color: #818181;
        display: inline-flex;
        border: none;
        background: none;
        width: 100%;
        text-align: left;
        cursor: pointer;
        outline: none;
    }
    .home-bar div.dropdown-container {
        display: inline-flex;
        background-color: #262626;
        padding-left: 8px;
    }
    </style>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<div class="navbar navbar-expand-lg navbar-dark" style="width:100%;">
    <div style="display:inline-flex;vertical-align:center;horiz-align:center;" class="container-fluid">
        <img style="width:200px;height:200px;" src="${resource(dir:'../assets/images',file:'martiniGlass.png')}" alt="Cocktail Logo"/>
        <sec:ifNotLoggedIn>
            <g:render template="/login/login"/>
            <g:link controller="user" action="create">Create User</g:link>
        </sec:ifNotLoggedIn>
        <sec:ifLoggedIn>
            <div id="nameAndNavigation" style="display: grid;" class="container-fluid">
                <div id="name" style="padding:50px 0;">
                    <%
                        def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
                        def user = User.findByUsername(springSecurityService.authentication.getPrincipal().username as String)
                        int hour = LocalTime.now().getHour()
                        String greeting = 'Good ';
                        if (0 <= hour && hour < 12 ) greeting += 'morning, ';
                        else if (12 <= hour && hour < 17) greeting += 'afternoon, ';
                        else greeting += 'evening, ';
                    %>
                    <g:set var="user" value="${user}"/>
                    <g:set var="greet" value="${greeting}"/>
                    <h1>${greet} ${user}</h1>
                </div>
                <div id="home-bar" style="padding:50px 0;">
                    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
                        <ul class="nav navbar-nav ml-auto">
                            <div tag="nav" style="display:inline-flex;">
                                <!-- Default homepage or custom homepage -->
                                <g:if test="${params.action == 'showCustomDrinks'}">
                                    <g:link class="dropdown-btn dropdown" url="${createLink(uri: '/')}">Default Drinks</g:link>
                                </g:if><g:else>
                                    <g:link class="dropdown-btn dropdown" controller="drink" action="showCustomDrinks">Your Drinks</g:link>
                                </g:else>
                                <li class="dropdown-btn dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#userMgmt" role="button" aria-haspopup="true" aria-expanded="false">User <span class="caret"></span></a>
                                    <ul class="dropdown-menu" id="userMgmt" style="background-color:#000000;">
                                        <li class="dropdown-header">User Mgmt</li>
                                        <sec:ifNotLoggedIn>
                                            <li class="dropdown-item"><g:link controller="user" action="create">Create User</g:link></li>
                                        </sec:ifNotLoggedIn>
                                        <sec:ifLoggedIn>
                                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                                <li class="dropdown-item"><g:link controller="user" action="index">Show all Users</g:link></li>
                                            </sec:ifAnyGranted>
                                            <li class="dropdown-item"><a href="${createLink(uri:"/user/show/${user.id}")}">User Details</a></li>
                                            <li class="dropdown-item"><g:link controller="logout" action="index">Logout</g:link></li>
                                        </sec:ifLoggedIn>
                                    </ul>
                                </li>
                                <li class="dropdown-btn dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#drinkMgmt" role="button" aria-haspopup="true" aria-expanded="false">Drinks <span class="caret"></span></a>
                                    <ul class="dropdown-menu" id="drinkMgmt" style="background-color:#000000;">
                                        <li class="dropdown-header">Drink Mgmt</li>
                                        <sec:ifLoggedIn>
                                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                                <li class="dropdown-item"><g:link controller="drink" action="index">Show Default Drinks</g:link></li>
                                                <li class="dropdown-item"><g:link controller="drink" action="customIndex">Show Your Drinks</g:link></li>
                                            </sec:ifAnyGranted>
                                            <sec:ifAnyGranted roles="ROLE_USER">
                                                <li class="dropdown-item"><g:link controller="drink" action="customIndex">Show Your Drinks</g:link></li>
                                            </sec:ifAnyGranted>
                                        </sec:ifLoggedIn>
                                    </ul>
                                </li>
                                <li class="dropdown-btn dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#ingredientMgmt" role="button" aria-haspopup="true" aria-expanded="false">Ingredients <span class="caret"></span></a>
                                    <ul class="dropdown-menu" id="ingredientMgmt" style="background-color:#000000;">
                                        <li class="dropdown-header">Ingredient Mgmt</li>
                                        <sec:ifAnyGranted roles="ROLE_ADMIN">
                                            <li class="dropdown-item"><g:link controller="ingredient" action="index">Show Default Ingredients</g:link></li>
                                            <li class="dropdown-item"><g:link controller="ingredient" action="customIndex">Show Your Ingredients</g:link></li>
                                        </sec:ifAnyGranted>
                                        <sec:ifAnyGranted roles="ROLE_USER">
                                            <li class="dropdown-item"><g:link controller="ingredient" action="customIndex">Show Your Ingredients</g:link></li>
                                        </sec:ifAnyGranted>
                                    </ul>
                                </li>
                                <sec:ifAnyGranted roles="ROLE_ADMIN">
                                    <li class="dropdown-btn dropdown">
                                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#appStatus" role="button" aria-haspopup="true" aria-expanded="false">Application Status <span class="caret"></span></a>
                                        <ul class="dropdown-menu" id="appStatus" style="background-color:#000000;">
                                            <li class="dropdown-item"><a href="/">Environment: ${grails.util.Environment.current.name}</a></li>
                                            <li class="dropdown-item"><a href="/">App profile: ${grailsApplication.config.getProperty('grails.profile')}</a></li>
                                            <li class="dropdown-item"><a href="/">App version:
                                                <g:meta name="info.app.version"/></a>
                                            </li>
                                            <li role="separator" class="dropdown-divider"></li>
                                            <li class="dropdown-item"><a href="/">Grails version:
                                                <g:meta name="info.app.grailsVersion"/></a>
                                            </li>
                                            <li class="dropdown-item"><a href="/">Groovy version: ${GroovySystem.getVersion()}</a></li>
                                            <li class="dropdown-item"><a href="/">JVM version: ${System.getProperty('java.version')}</a></li>
                                            <li role="separator" class="dropdown-divider"></li>
                                            <li class="dropdown-item"><a href="/">Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</a></li>
                                            <li class="dropdown-item"><a href="/">Domains: ${grailsApplication.domainClasses.size()}</a></li>
                                            <li class="dropdown-item"><a href="/">Services: ${grailsApplication.serviceClasses.size()}</a></li>
                                            <li class="dropdown-item"><a href="/">Tag Libraries: ${grailsApplication.tagLibClasses.size()}</a></li>

                                        </ul>
                                    </li>
                                    <li class="dropdown-btn dropdown">
                                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-target="#plugins" role="button" aria-haspopup="true" aria-expanded="false">Installed Plugins <span class="fa fa-caret-down"></span></a>
                                        <ul class="dropdown-menu dropdown-menu-right" id="plugins" style="background-color:#000000;">
                                            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                                                <li class="dropdown-item"><a href="/">${plugin.name} - ${plugin.version}</a></li>
                                            </g:each>
                                        </ul>
                                    </li>
                                </sec:ifAnyGranted>
                            </div>
                        </ul>
                    </div>
                </div>
            </div>
        </sec:ifLoggedIn>
    </div>
</div>

<script>
    /* Loop through all dropdown buttons to toggle between hiding and showing its dropdown content - This allows the user to have multiple dropdowns without any conflict */
    var dropdown = document.getElementsByClassName("dropdown-btn");
    var i;

    for (i = 0; i < dropdown.length; i++) {
        dropdown[i].addEventListener("click", function() {
            this.classList.toggle("active");
            var dropdownContent = this.nextElementSibling;
            if (dropdownContent.style.display === "block") {
                dropdownContent.style.display = "none";
            } else {
                dropdownContent.style.display = "block";
            }
        });
    }
</script>