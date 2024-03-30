<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><g:message code="drink.show.a.drink" default="Show A Drink"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>
        <g:include view="includeAll.gsp"/>
        <style>
            .formfield {
                display: table;
                width: 100%;
                padding-bottom: 25px;
                padding-right: 10px;
            }
            .formfield label,
            .formfield .input-wrapper {
                display: table-cell;
                vertical-align: top;
            }
            .formfield label {
                width: 150px;
                padding-left: 10px;
            }
            .formfield .input-wrapper .table-form {
                width: 100%;
            }
            .btn-xs {
                padding: 1px 5px !important;
                font-size: 12px !important;
                line-height: 1.5 !important;
                border-radius: 3px !important;
            }
            hr {
                margin-bottom: 10px;
            }
            fieldset {
                position: relative;
            }
            fieldset::before {
                content: "";
                position: absolute;
                margin-top: -45px;
                right: 10px;
                background: #fff;
                padding: 0 5px;
            }
            a.fa fa-clone::before {
                background-color: #efefef;
                overflow: hidden;
                padding: 0.3em;
                -moz-box-shadow: 0 0 3px 1px #aaaaaa;
                -webkit-box-shadow: 0 0 3px 1px #aaaaaa;
                box-shadow: 0 0 3px 1px #aaaaaa;
                margin: 0.1em 0 0 0;
                border: none;
                content: "\f24d  Copy"
            }
            /*These two webkit scrollbars control size and color*/
            ::-webkit-scrollbar {
                -webkit-appearance:none;
                width: 5px;
            }
            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgb(128, 128, 128);
                box-shadow: 0 0 1px rgba(255, 255, 255, .5);
            }
        </style>
    </head>
    <g:set var="darkMode" value="${user?.darkMode ?: false}"/>
    <g:if test="${darkMode}">
        <style>
            .input-wrapper > input,textarea {
                background-color:black;
                color:white;
            }
            .buttons {
                background-color: black;
                overflow: hidden;
                padding: 0.3em;
                -moz-box-shadow: 0 0 3px 1px black;
                -webkit-box-shadow: 0 0 3px 1px black;
                box-shadow: 0 0 3px 1px black;
                margin: 0.1em 0 0 0;
                border: none;
            }
            .buttons > a {
                color:ghostwhite;
            }
        </style>
    </g:if>
    <body style="padding:50px;background-color:${darkMode?'black':'white'};">
        <div id="showDrink" style="background-color:${darkMode?'black':'white'};">
            <section style="background-color:${darkMode?'black':'white'};">
                <div class="container">
                    <div id="navigation" style="display:flex;justify-content:center;">
                        <g:render template="../navigation" model="[user:user]"/>
                    </div>
                </div>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.drink}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.drink}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>
                <div id="show-drink" class="container">
                    <div style="display:flex;justify-content:center;">
                        <div style="display:block;">
                            <fieldset style="border:thick solid #000080;" class="no-before">
                                <legend style="margin-left:25px;padding-left:10px;width:auto;color:${darkMode?'white':'black'};">
                                    ${drink.name} &emsp14;
                                    <hr style="height:1px;background-color:#000080">
                                </legend>
                                <div id="drink" style="width:auto;color:${darkMode?'white':'black'};">
                                    <div class="formfield" id="drinkName">
                                        <label for='name'><g:message code="drink.show.name" default="Drink Name"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="name" value="${drink.name}" required="" id="name" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="drinkNumber">
                                        <label for='number'><g:message code="drink.show.number" default="Drink Number"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="number" value="${drink.number}" required="" id="number" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="alcoholType">
                                        <label for='alcohol'><g:message code="drink.show.type" default="Drink Type"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="alcohol" value="${drink.alcohol}" required="" id="alcohol" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="drinkSymbol">
                                        <label for='symbol'><g:message code="drink.show.symbol" default="Drink Symbol"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="symbol" value="${drink.symbol}" required="" id="symbol" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="glassType">
                                        <label for="glass"><g:message code="drink.show.glass" default="Suggested Glass"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <input type="text" disabled name="glass" value="${drink.suggestedGlass}" required="" id="glass" />
                                        </div>
                                    </div>
                                    <div class="formfield" id="instructions">
                                        <label for='instructions'><g:message code="drink.show.mixing" default="Mixing Instructions"/></label>
                                        <div class="input-wrapper" style="text-align:right;">
                                            <textarea disabled readonly name="instructions" rows="5" cols="40">${drink.mixingInstructions}</textarea>
                                        </div>
                                    </div>
                                    <div class="formfield" id="ingredients">
                                        <g:if test="${!drink.ingredients || drink.ingredients.size() == 0}">
                                            <label><g:message code="drink.show.no.ingredients" default="No Ingredients"/></label>
                                            <p>Once you add this drink to an ingredient, they'll show here</p>
                                        </g:if><g:else>
                                            <label><g:message code="drink.show.ingredients" default="Ingredients"/></label><br/>
                                            <div style="margin-top:-25px;height:100px;padding-right:10px;overflow-y:auto;text-align:right;">
                                                <g:each in="${drink.ingredients.sort{ it.id }}" var="ingredient" status="i">
                                                    <div style="display:block;">
                                                        <input hidden type="checkbox" disabled name="ingredients" id="ingredient${ingredient.id}" checked value="${ingredient}"/>
                                                        <g:link action="show" controller="ingredient" params='[id:"${ingredient.id}"]'>${ingredient}</g:link>
                                                    </div>
                                                </g:each>
                                            </div>
                                        </g:else>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset class="buttons" style="text-align:center;">
                                <sec:ifLoggedIn>
                                    <g:if test="${!drink.custom}">
                                        <g:link class="fa fa-clone" action="copy" resource="${this.drink}">&nbsp;<g:message code="default.button.addToYourDrinks.label" default="Add To Your Drinks"/></g:link>
                                    </g:if>
                                    <g:if test="${drink.custom || adminIsLoggedIn}">
                                        <g:link class="fa-solid fa-pen-to-square" action="edit" resource="${this.drink}">&nbsp;<g:message code="default.button.edit.label" default="Edit"/></g:link>
                                        <a href="#sendEmailDiv" rel="modal:open" class="fa fa-solid fa-share" id="share">&nbsp;<g:message code="default.button.share" default="Share"/></a> %{--<a href="#sendEmailDiv" rel="modal:open" class="fa fa-solid fa-share">&nbsp;Share</a>--}%
                                    </g:if>
                                    <g:if test="${drink.canBeDeleted}">
                                        <div style="display:block;">
                                            <g:form controller="drink" action="delete" method="DELETE">
                                                <input type="hidden" name="id" value="${drink.id}">
                                                <button class="fa-solid fa-trash-can btn btn-danger" type="submit"
                                                        onclick="return confirm('${message(code:'default.button.delete.confirm.message', default:'Are you sure?')}');">&nbsp;<g:message code="default.button.delete.label" default="Delete"/></button>
                                            </g:form>
                                        </div>
%{--                                        <g:link class="fa-solid fa-trash-can" action="delete" resource="${this.drink}" onclick="return confirm('${message(code:'default.button.delete.confirm.message', default:'Are you sure?')}');">&nbsp;<g:message code="default.button.delete.label" default="Delete"/></g:link>--}%
                                    </g:if>
                                </sec:ifLoggedIn>
                            </fieldset>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <!-- Modal HTML embedded directly into document -->
        <div id="sendEmailDiv" class="modal" style="width:300px;height:150px;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <g:form controller="emails" action="shareDrinkEmail" method="POST" name="emailDrink" useToken="true">
                <input name="drinkId" id="drinkId" type="hidden" value="${this.drink.id}"/>
                <input name="recipientName" id="recipientName" type="text" placeholder="${g.message(code:'enter.recipient.name', default:'Enter recipient name')}" required="required"/>
                <input name="recipientEmail" id="recipientEmail" type="text" placeholder="${g.message(code:'enter.recipient.email', default:'Enter recipient email')}" required="required"/>
                <br/>
                <input class="btn btn-outline-success btn-xs" type="submit" form="emailDrink" value="${message(code: 'default.share.label', default: 'Share')}" />
            </g:form>
        </div>
        <div id="ex1" class="modal">
            <p>Thanks for clicking. That felt good.</p>
            <a href="#" rel="modal:close">Close</a>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                console.log("show drink loaded");

                var deleteTag = document.getElementsByClassName("fa-solid fa-trash-can");

                // $("#share").click(function() {
                //     console.log('share btn clicked');
                //     $("#sendEmailDiv").modal('show');
                // });
            });
        </script>
    </body>
</html>
