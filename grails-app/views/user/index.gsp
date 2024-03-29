<%@ page import="enums.*; mixology.*; static mixology.DrinkController.isOn;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title><g:message code="user.index.display.all.users" default="Display all users"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <g:include view="includeAll.gsp"/>
    <style>
    .btn-xs {
        padding: 1px 5px !important;
        font-size: 12px !important;
        line-height: 1.5 !important;
        border-radius: 3px !important;
    }
    #filterUsersFormDiv > a,
    #filterUsersFormDiv > input::placeholder,
    #filterUsersFormDiv > select {
        color:black;
    }
    #filterUsersFormDiv > input,
    #filterUsersFormDiv > select,option {
        background-color:white;
        border-color:black;
        margin: auto 10px;
    }
    #filterUsersFormDiv > label {
        margin: auto 10px;
    }
    a {
        color: black;
    }
    a:visited {
        color: rgb(128, 128, 128);
    }
    a:hover {
        color: rgb(128, 128, 128);
    }
    a:active {
        color: coral;
    }
    </style>
</head>
<g:set var="darkMode" value="${user?.darkMode}"/>
<g:set var="user" value="${g.message(code:'user.label', default:'User')}"/>
<g:set var="users" value="${g.message(code:'user.dynamic.label', args:'s', default:'Users')}"/>
<g:if test="${darkMode}">
    <style>
    #filterUsersFormDiv > a,
    #filterUsersFormDiv > input::placeholder,
    #filterUsersFormDiv > select {
        color:#e2e3e5;
    }
    #filterUsersFormDiv > input,
    #filterUsersFormDiv > select,option {
        background-color:black;
        border-color:white;
    }
    #usersHeaderRow > *{
        color:black;
        background-color: rgb(128, 128, 128) !important;
    }
    a, p {
        color: white;
    }
    a:visited {
        color: rgb(128, 128, 128);
    }
    a:hover {
        color: rgb(128, 128, 128);
    }
    a:active {
        color: coral;
    }
    </style>
</g:if>

<body style="padding:50px;margin:0;background-color:${darkMode?'black':'white'};">
<div id="content" class="" style="background-color:${darkMode?'black':'white'};">
    <section style="text-align:center;background-color:${darkMode?'black':'white'};">
        <div id="list-user">
            <div style="display:inline-flex;vertical-align:middle;">
                <div id="navigation">
                    <g:render template="../navigation" model="[user:user]"/>
                </div>
                <!--<div style="margin:auto;padding-top:10px;vertical-align:middle;">
                            <h1 style="color:$ {darkMode?'white':'black'};"><g :message code="default.list.label" args="[drink]" /></h1>
                        </div>-->
            </div>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div id="list" style="text-align:center;">
                <div id="filter" style="text-align:center;padding:10px;display:flex;justify-content:center;">
                    <h1 style="color:${darkMode?'white':'black'};"><g:message code="default.list.label" args="[users]" /></h1>
                    <g:form action="${params.action}" controller="user" name="filterUsers" method="get">
                        <div id="filterUsersFormDiv" style="display:inline-flex;margin: auto 10px;">
                            <label for="id"></label>
                            <input type="text" name="id" id="id" placeholder="id" value="${params.id}" style="width:50px;text-align:center;" class="form-control" />
                            <label for="name"></label>
                            <input type="text" name="name" id="name" placeholder="${g.message(code:'default.first.last.name', default:'first/last name')}" value="${params.firstName ?: params.lastName}" style="text-align:center;" class="form-control" />
                            <label for="username"></label>
                            <input type="text" name="username" id="username" placeholder="${g.message(code:'user.index.username', default:'user name')}" value="${params.username}" style="text-align:center;width:200px;" class="form-control"/>
                            <label for="email"></label>
                            <input type="text" name="email" id="email" placeholder="${g.message(code:'user.index.filter.email', default:'email')}" value="${params.email}" style="text-align:center;width:100px;" class="form-control" />
                            <button style="margin: auto 10px;" id="filterUserBtn" class="btn btn-primary btn-xs" type="submit" form="filterUsers"><g:message code="default.button.filter.label" default="Filter"/></button>
                            <g:link class="btn btn-outline-primary btn-xs" controller="user" action="index" style="text-align:center;margin-top:auto;margin-bottom:auto;"><g:message code="default.button.clear.label" default="Clear"/></g:link>
                            <label style="cursor:default;color:${darkMode?'black':'white'};" for="email"> Dont Show</label>
                        </div>
                    </g:form>
                </div>
                <p></p>
                <g:if test="${userCount <= 0}">
                    <p>No users found!</p>
                </g:if><g:else>
                <table id="usersTable">
                    <thead>
                    <tr id="usersHeaderRow">
                        <th><g:message code="user.index.count" default="Count"/> (${userCount})</th>
                        <th><g:message code="user.index.first.name" default="First Name"/></th>
                        <th><g:message code="user.index.last.name" default="Last Name"/></th>
                        <th><g:message code="user.index.username.capitalized" default="User Name"/></th>
                        <th><g:message code="user.index.email" default="Email"/></th>
                        <th><g:message code="user.index.mobile" default="Mobile"/></th>
                        <th><g:message code="user.index.password" default="Password"/></th>
                        <th><g:message code="user.index.photo" default="Photo"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:if test="${userList.totalCount > 0}">
                        <% int index = 1 %>
                        <g:each in="${userList}" var="user">
                            <g:if test="${params.offset && (params.offset as int) != 0}">
                                <g:set var="idx" value="${index + (params.offset as int)}"/>
                            </g:if><g:else>
                            <g:set var="idx" value="${index}"/>
                        </g:else>
                            <tr style="color:${darkMode?'white':'black'};background-color:${darkMode?'black':'white'};">
                                <td><g:link controller="user" action="show" params='[id:"${user.id}"]'>${idx}</g:link> </td>
                                <td>${((User)user).firstName}</td>
                                <td>${((User)user).lastName}</td>
                                <td>${((User)user).username}</td>
                                <td>${((User)user).email}</td>
                                <td>${((User)user).mobileNumber}</td>
                                <td>${((User)user).password ? '******' : ''}</td>
                                <td>${((User)user).photo ? 'Yes' : 'No'}</td>
                            </tr>
                            <% index += 1 %>
                        </g:each>
                    </g:if>
                    </tbody>
                </table>
                <div class="pagination">
                    <g:paginate controller="user"
                                action="index"
                                total="${userList.totalCount}"
                                max="5"
                                params="${params}" />
                </div>
            </g:else>
            </div>
        </div>
    </section>
</div>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function() {
        console.log("user index page loaded");
    });
</script>