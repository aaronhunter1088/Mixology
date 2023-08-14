<div class="nav" role="navigation">
    <ul>
        <li><a class="fa fa-home" href="${createLink(uri: '/')}">&nbsp;<g:message code="default.home.label"/></a></li>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
            <li class="fa fa-home"><g:link controller="drink" action="index">&nbsp;Show Default Drinks</g:link></li>
            <li><g:link class="fa-solid fa-jar-wheat" controller="ingredient" action="index">&nbsp;Show Default Ingredients</g:link></li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_USER">
            <li><g:link class="fa fa-home" controller="drink" action="showCustomDrinks">&nbsp;Your Drinks</g:link></li>
            <li><g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="customIndex">&nbsp;Drink List</g:link></li>
            <li><g:link class="fa-solid fa-jar-wheat" controller="ingredient" action="showCustomIngredients">&nbsp;Ingredient List</g:link></li>
        </sec:ifAnyGranted>
        <li><g:link class="fa-solid fa-martini-glass-empty" controller="drink" action="create">&nbsp;New Drink</g:link></li>
        <li><g:link class="fa-solid fa-jar-wheat" controller="ingredient" action="create">&nbsp;New Ingredient</g:link></li>
    </ul>
</div>