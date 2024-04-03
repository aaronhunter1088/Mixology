<%@ page import="enums.*; mixology.*; java.time.LocalTime;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    .home-bar {
        display:inline-flex;
        text-align:left;
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

<%
    def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
    def user = User.findByUsername(springSecurityService.authentication.getPrincipal().username as String)
    int hour = LocalTime.now().getHour()
    String greeting = g.message(code:'default.greeting.good', default:'Good ')
    if (0 <= hour && hour < 12 ) greeting += g.message(code:'default.greeting.morning', default:'morning, ')
    else if (12 <= hour && hour < 17) greeting += g.message(code:'default.greeting.afternoon', default:'afternoon, ')
    else greeting += g.message(code:'default.greeting.evening', default:'evening, ')
%>
<g:set var="user" value="${user}"/>
<g:set var="greet" value="${greeting}"/>
<!-- class navbar navbar-expand-lg navbar-dark -->
<div id="topBar" class="container-fluid">
    <sec:ifNotLoggedIn>
        <div id="loginDiv" style="display:inline-flex;">
            <img style="width:auto;height:100px;" src="${resource(dir:'../assets/images',file:'martiniGlass.png')}" alt="Cocktail Logo"/>
            <g:render template="/login/login" model="[darkMode:darkMode]"/>
        </div>
    </sec:ifNotLoggedIn>
    <sec:ifLoggedIn>
        <div id="loggedInDiv" style="display:inline-flex;">
            <img style="width:100px;height:100px;" src="${resource(dir:'../assets/images',file:'martiniGlass.png')}" alt="Cocktail Logo"/>
            <div id="nameAndNav" style="display:inline-block;color:${darkMode?'white':'black'};">
                <h1>${greet} ${user}</h1>
                <g:render template="/navigation" />
            </div>
        </div>
    </sec:ifLoggedIn>
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