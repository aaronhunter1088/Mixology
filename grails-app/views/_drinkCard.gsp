<div id="card" class="card" style="background-color:${backgroundColor};width:300px;height:300px;opacity:${opacity}">
    <div id="topSection" style="display:inline-flex;">
        <div id="leftSide" style="height:200px;float:left;">
            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                <p style="text-align:center;font-size:5em;margin:0;color:navy;">
                    <b>
                        <g:if test="${drink.id}">
                            <g:link controller="drink" action="show" params="[id:drink.id,lang:language,darkMode:darkMode]">
                                <font color="navy">${drink.number}</font>
                            </g:link>
                        </g:if><g:else>
                            <font color="navy">0</font> <!-- Filler Drink -->
                        </g:else>
                    </b>
                </p>
                <div style="overflow-y:auto;height:80px;">
                    <g:if test="${drink?.fillerDrink}">
                        <g:each in="${ drink.ingredients.sort{it.givenId} }" var="ingredient">
                            <g:if test="${opacity == 0.5}">
                                <p style="margin:0;color:black;">${ingredient}</p>
                            </g:if><g:else>
                            <g:link controller="ingredient" action="show" params="[id:ingredient.id,lang:language,darkMode:darkMode]">
                                <p style="margin:0;color:black;">${ingredient.prettyName()}</p>
                            </g:link>
                        </g:else>
                        </g:each>
                    </g:if>
                    <g:else>
                        <g:each in="${ drink.ingredients.sort{it.id} }" var="ingredient">
                            <g:if test="${opacity == 0.5}">
                                <p style="margin:0;color:black;">${ingredient}</p>
                            </g:if><g:else>
                            <g:link controller="ingredient" action="show" params="[id:ingredient.id,lang:language,darkMode:darkMode]">
                                <p style="margin:0;color:black;">${ingredient.prettyName()}</p>
                            </g:link>
                        </g:else>
                        </g:each>
                    </g:else>
                </div>
            </div>
        </div>
        <div id="rightSide" style="text-align:right;height:200px;float:right;">
            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                <img width="75px" height="75px" title="${drink.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink.glassImage)}" alt="${drink.glassImage}"/>
                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink.symbol}</b></p>
            </div>
        </div>
    </div>
    <div id="mixingSection" style="padding-left:10px;padding-top:10px;">
        <p style="margin:0;overflow-y:auto;height:45px;color:black;">${drink.mixingInstructions}</p>
    </div>
    <div id="nameSection">
        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink.name}</b></p>
    </div>
</div>