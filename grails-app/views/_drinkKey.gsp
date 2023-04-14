<%@ page import="enums.*; mixology.Drink;" %>
<h2><b>KEY</b></h2>
<div style="display:inline-flex;">
    <div id="leftColumn" style="width:100px;height:280px;">
        <p></p>
        <p></p>
        <h6>Cocktail</h6>
        <h6 style="margin-bottom:-5px;">Number</h6>
        <span class="arrow-right"></span>
        <p style="height:20px;"></p>
        <h6>Cocktail</h6>
        <h6 style="margin-bottom:-5px;">Ingredients</h6>
        <span class="arrow-right"></span>
        <p style="height:30px;"></p>
        <span class="arrow-right"></span>
        <h6 style="margin-top:-5px;">Cocktail</h6>
        <h6>Mixing</h6>
        <h6>Instructions</h6>
    </div>
    <div id="middleColumn" style="text-align:center;display:inline-flex;">
        <div class="card" style="border:double #000000;background-color:#95999c;width:300px;height:300px;">
            <div style="display:inline-block;">
                <div id="innerLeft" style="height:200px;float:left;">
                    <div style="text-align:left;padding-left:10px;padding-top:10px;width:140px;height:50px;">
                        <p style="text-align:center;font-size:5em;margin:0;color:navy;"><b>20</b></p>
                        <p style="margin:0;">1.5 oz Tequila</p>
                        <p style="margin:0;">0.5 oz Triple Sec</p>
                        <p style="margin:0;">1 oz Lemon Juice</p>
                    </div>
                </div>
                <div id="innerRight" style="height:200px;float:right;">
                    <div style="padding-left:10px;padding-top:25px;width:140px;height:75px;">
                        <img width="75px" height="75px" src="${resource(dir:'../assets/images',file:'martiniGlass.png')}" alt="Cocktail Logo"/>
                        <p style="font-size:5em;margin-top:10px;color:#155724;"><b>Mg</b></p>
                    </div>
                </div>
            </div>
            <div style="padding-left:10px;padding-top:10px;"> <!-- text-align:left; looked funny. keep centered -->
                <p style="margin:0;height:45px;">Mix ingredients and enjoy!</p>
            </div>
            <div>
                <p style="text-align:center;font-size:2em;margin:0;color:#a60000;"><b>MARGARITA</b></p>
            </div>
        </div>
    </div>
    <div id="rightColumn" style="width:100px;height:280px;">
        <p></p>
        <p></p>
        <h6>Suggested</h6>
        <h6 style="margin-bottom:-5px;">Glass</h6>
        <span class="arrow-left"></span>
        <p style="margin:50px;"></p>
        <h6>Cocktail</h6>
        <h6 style="margin-bottom:-5px;">Symbol</h6>
        <span class="arrow-left"></span>
        <p style="margin:60px;"></p>
        <h6>Cocktail</h6>
        <h6 style="margin-bottom:-5px;">Name</h6>
        <span class="arrow-left"></span>
    </div>
</div>