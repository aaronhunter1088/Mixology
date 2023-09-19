<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Display All Users</title>
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
        </style>
    </head>
    <g:set var="user" value="${message(code: 'user.label', default: 'User')}" />
    <body style="overflow-x:scroll;">
        <div id="content">
            <div class="container">
                <section class="row" id="navigation">
                    <g:render template="../navigation"/>
                </section>
                <section class="row">
                    <div id="list-user" class="col-12 content scaffold-list">
                        <h1><g:message code="default.list.label" args="['User']" /></h1>
                        <g:if test="${flash.message}">
                            <div class="message" role="status">${flash.message}</div>
                        </g:if>
                        <g:if test="${userCount <= 0}">
                            <p>No users found!</p>
                        </g:if><g:else>
                            <div id="filter" style="text-align:center;width:auto;display:flex;justify-content:center;">
                                <g:form action="index" controller="user" name="filterUsers" method="get">
                                    <div id="filterUsersFormDiv" style="display:flex;">
                                        <label for="id"></label>
                                        <input type="text" name="id" id="id" placeholder="id" value="${params.id}" style="width:50px;text-align:center;" class="form-control" />
                                        <label for="name"></label>
                                        <input type="text" name="name" id="name" placeholder="first/last name" value="${params.firstName ?: params.lastName}" style="text-align:center;width:200px;" class="form-control" />
                                        <label for="username"></label>
                                        <input type="text" name="username" id="username" placeholder="user name" value="${params.username}" style="text-align:center;width:200px;" class="form-control" />
                                        <label for="email"></label>
                                        <input type="text" name="email" id="email" placeholder="email" value="${params.email}" style="text-align:center;width:200px;" class="form-control" />
                                        <button style="margin: auto 10px;" id="filterUserBtn" class="btn btn-primary btn-xs" type="submit" form="filterUsers">Filter</button>
                                        <g:link class="btn btn-outline-primary btn-xs" controller="user" action="index" style="text-align:center;margin-top:auto;margin-bottom:auto;">Clear</g:link>
                                    </div>
                                </g:form>
                            </div>
                            <p></p>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Count</th>
%{--                                        <th>ID</th>--}%
                                        <th>First Name</th>
                                        <th>Last Name</th>
                                        <th>Username</th>
                                        <th>Email</th>
                                        <th>Mobile Phone</th>
                                        <th>Password</th>
                                        <th>Photo</th>
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
                                            <tr>
                                                <td><g:link controller="user" action="show" params='[id:"${user.id}"]'>${idx}</g:link> </td>
%{--                                                <td>${user.id}</td>--}%
                                                <td>${user.firstName}</td>
                                                <td>${user.lastName}</td>
                                                <td>${user.username}</td>
                                                <td>${user.email}</td>
                                                <td>${user.mobileNumber}</td>
                                                <td>${user.password ? '******' : ''}</td>
                                                <td>${user.photo ? 'Yes' : 'No'}</td>
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
                </section>
            </div>
        </div>
    </body>
</html>