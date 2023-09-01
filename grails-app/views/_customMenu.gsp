<a href="#show-ingredient" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<p>HEYYYYY!</p>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <g:if test="${drink.isCustom}">
            <li><g:link class="home" controller="drink" action="showCustomDrinks">Custom Drinks</g:link></li>
        </g:if>
        <li><g:link class="list" action="index">Ingredient List</g:link></li>
        <li><g:link class="create" controller="drink" action="create">New Drink</g:link></li>
        <li><g:link class="create" controller="ingredient" action="create">New Ingredient</g:link></li>
    </ul>
</div>