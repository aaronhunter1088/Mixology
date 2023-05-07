<div class="card" style="background-color:${backgroundColor};width:300px;height:300px;opacity:${opacity}">
    <div style="display:inline-flex;">
        <div id="tequilaLeft1" style="height:200px;float:left;">
            <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>${drink.drinkNumber}</b></p>
                <div style="overflow-y:auto;height:80px;">
                    <g:each in="${drink.ingredients}" var="ingredient">
                        <g:if test="${opacity == 0.5}">
                            <p style="margin:0;color:black;">${ingredient}</p>
                        </g:if><g:else>
                            <g:link controller="ingredient" action="show" params="[id:ingredient.id]">
                                <p style="margin:0;color:black;">${ingredient.prettyName()}</p>
                            </g:link>
                        </g:else>
                    </g:each>
                </div>
            </div>
        </div>
        <div id="tequilaRight1" style="text-align:center;height:200px;float:right;">
            <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                <img width="75px" height="75px" title="${drink.suggestedGlass.glassName}" src="${resource(dir:'../assets/images',file:drink.glassImage)}" alt="${drink.glassImage}"/>
                <p style="font-size:5em;margin-top:10px;color:#155724;"><b>${drink.drinkSymbol}</b></p>
            </div>
        </div>
    </div>
    <div style="padding-left:10px;padding-top:10px;">
        <p style="margin:0;overflow-y:auto;height:45px;color:black;">${drink.mixingInstructions}</p>
    </div>
    <div>
        <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>${drink.drinkName}</b></p>
    </div>
</div>