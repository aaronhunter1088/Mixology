<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    def springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
    def user = User.findByUsername(springSecurityService.authentication.getPrincipal().username as String)
%>
<g:set var="user" value="${user}"/>
<div class="nav" role="navigation">
    <ul>
        <li><a class="fa fa-home" href="${createLink(uri: '/')}">&nbsp;<g:message code="default.home.label"/></a></li>
        <li><g:link class="fa fa-home" controller="drink" action="showCustomDrinks">&nbsp;Custom Home</g:link></li>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
            <li><g:link class="fa-solid fa-list" controller="drink" action="index">&nbsp;Drinks</g:link></li>
            <li><g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;Your Drinks</g:link></li>
            <li><g:link class="fa-solid fa-list" controller="ingredient" action="index">&nbsp;Ingredients</g:link></li>
            <li><g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;Your Ingredients</g:link></li>
            <li><g:link class="fa-solid fa-list" controller="user" action="index">&nbsp;Users</g:link></li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_USER">
            <li><g:link class="fa-solid fa-list" controller="drink" action="customIndex">&nbsp;Your Drinks</g:link></li>
            <li><g:link class="fa-solid fa-list" controller="ingredient" action="customIndex">&nbsp;Your Ingredients</g:link> </li>
        </sec:ifAnyGranted>
        <li><g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="create">&nbsp;New Drink</g:link></li>
        <li><g:link class="fa-solid fa-jar-wheat" controller="ingredient" action="create">&nbsp;New Ingredient</g:link></li>
        <li><g:link class="fa fa-user" controller="user" action="create">&nbsp;New User</g:link></li>
        <li><g:link class="fa-solid fa-list" controller="user" action="show" params="[id:user.id]">&nbsp;Yourself</g:link></li>
    </ul>
</div>