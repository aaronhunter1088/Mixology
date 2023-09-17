<%@ page import="enums.*; mixology.*;" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<p style="font-size:35px;margin:0;color:#a60000;"><b>NUMBER, SYMBOL, AND NAME QUICK REFERENCE CHART</b></p>
<div style="display:inline-flex;">
    <div style="display:block;">
        <!-- Change from i=1; i<13 i++ when done. Remove if-->
        <% int count = 0; for (int i=0; i<=30; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
        <% count++; if (count == 12) break; } %>
    </div>
    <!-- Uncomment when done creating all drinks -->
        <div style="display:block;">
            <% for (int i=13; i<25; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
            <% } %>
        </div>
    <!-- Uncomment when done creating all drinks -->
        <div style="display:block;">
            <% for (int i=25; i<37; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
            <% } %>
        </div>
    <!-- Uncomment when done creating all drinks -->
        <div style="display:block;">
            <% for (int i=37; i<49; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
            <% } %>
        </div>
    <!-- Uncomment when done creating all drinks -->
        <div style="display:block;">
            <% for (int i=49; i<59; i++) { Drink drink = drinks[i]; if (drink == null) drink = Drink.createFillerDrink(Alcohol.TEQUILA) %>
            <div style="display:inline-flex;padding:0;">
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>${drink?.number ?: 0}</b></p><br/>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>${drink.symbol}</b></p>
                <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">${drink.name} (${drink.alcoholType.alcoholName.charAt(0)})</p>
            </div><br/>
            <% } %>
        </div>

%{--    <div style="display:block;">--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>13</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cc</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Cape Codder (V)</p>--}%
%{--        </div><br/> <!-- 13 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>14</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Af</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Alabama Fizz (G)</p>--}%
%{--        </div><br/> <!-- 14 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>15</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cs</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Chelsea Sidecar (G)</p>--}%
%{--        </div><br/> <!-- 15 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>16</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Df</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Diamond Fizz (G)</p>--}%
%{--        </div><br/> <!-- 16 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>17</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cq</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Canyon Quake</p>--}%
%{--        </div><br/> <!-- 17 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>18</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ca</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Cranberry Cooler (F)</p>--}%
%{--        </div><br/> <!-- 18 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>19</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Hp</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Hot Pants (T)</p>--}%
%{--        </div><br/> <!-- 19 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>20</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Mg</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Margarita (T)</p>--}%
%{--        </div><br/> <!-- 20 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>21</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cl</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Citronella Cooler (V)</p>--}%
%{--        </div><br/> <!-- 21 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>22</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cr</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Crocodile Cooler (V)</p>--}%
%{--        </div><br/> <!-- 22 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>23</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ds</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Desert Sunrise (V)</p>--}%
%{--        </div><br/> <!-- 23 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>24</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ej</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Electric Jam (V)</p>--}%
%{--        </div><br/> <!-- 24 -->--}%
%{--    </div>--}%
%{--    <div style="display:block;">--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>25</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Dm</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Dry Martini (G)</p>--}%
%{--        </div><br/> <!-- 25 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>26</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Fa</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Fallen Angel (G)</p>--}%
%{--        </div><br/> <!-- 26 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>27</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Fc</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Floradora Cooler (G)</p>--}%
%{--        </div><br/> <!-- 27 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>28</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cg</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Creamy Gin Sour (F)</p>--}%
%{--        </div><br/> <!-- 28 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>29</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cn</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Citron Neon (F)</p>--}%
%{--        </div><br/> <!-- 29 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>30</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Mm</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Mexican Madras (T)</p>--}%
%{--        </div><br/> <!-- 30 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>31</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Pp</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Purple Pancho (T)</p>--}%
%{--        </div><br/> <!-- 31 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>32</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Hc</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Handball Cooler (V)</p>--}%
%{--        </div><br/> <!-- 32 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>33</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Hw</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Harvey Wallbanger (V)</p>--}%
%{--        </div><br/> <!-- 33 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>34</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Li</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Long Island Iced Tea (V)</p>--}%
%{--        </div><br/> <!-- 34 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>35</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Pl</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Pink Lemonade (V)</p>--}%
%{--        </div><br/> <!-- 35 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>36</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Fh</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Fog Horn (G)</p>--}%
%{--        </div><br/> <!-- 36 -->--}%
%{--    </div>--}%
%{--    <div style="display:block;">--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>37</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Gs</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Gin and Sin (G)</p>--}%
%{--        </div><br/> <!-- 37 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>38</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Rr</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Rum Runner (G)</p>--}%
%{--        </div><br/> <!-- 38 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>39</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Fd</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Frozen Daiquiri (F)</p>--}%
%{--        </div><br/> <!-- 39 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>40</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ff</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Frozen Fuzzy (F)</p>--}%
%{--        </div><br/> <!-- 40 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>41</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ss</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Silk Stocking (T)</p>--}%
%{--        </div><br/> <!-- 41 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>42</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Tt</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Tijuana Taxi (T)</p>--}%
%{--        </div><br/> <!-- 42 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>43</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ar</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Affair (S)</p>--}%
%{--        </div><br/> <!-- 43 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>44</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Bt</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">B-52 (S)</p>--}%
%{--        </div><br/> <!-- 44 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>45</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Bs</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Between-The-Sheets (S)</p>--}%
%{--        </div><br/> <!-- 45 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>46</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Bz</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Buzzard's Breath (S)</p>--}%
%{--        </div><br/> <!-- 46 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>47</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Cz</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">C.C. Kazi (S)</p>--}%
%{--        </div><br/> <!-- 47 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>48</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ga</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Galactic Ale (S)</p>--}%
%{--        </div><br/> <!-- 48 -->--}%
%{--    </div>--}%
%{--    <div style="display:block;">--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>49</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Gd</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Green Demon (S)</p>--}%
%{--        </div><br/> <!-- 49 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>50</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Jb</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Johnny Beach (S)</p>--}%
%{--        </div><br/> <!-- 50 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>51</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Kz</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Kamikaze (S)</p>--}%
%{--        </div><br/> <!-- 51 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>52</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ld</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Lemon Drop (S)</p>--}%
%{--        </div><br/> <!-- 52 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>53</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Pb</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Parisian Blonde (S)</p>--}%
%{--        </div><br/> <!-- 53 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>54</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ph</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Purple Hooter (S)</p>--}%
%{--        </div><br/> <!-- 54 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>55</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Rm</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Rocky Mountain (S)</p>--}%
%{--        </div><br/> <!-- 55 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>56</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Sb</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Sex On The Beach (S)</p>--}%
%{--        </div><br/> <!-- 56 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>57</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Sa</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Sour Apple (S)</p>--}%
%{--        </div><br/> <!-- 57 -->--}%
%{--        <div style="display:inline-flex;padding:0;">--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:navy;"><b>58</b></p><br/>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#155724;"><b>Ww</b></p>--}%
%{--            <p style="font-size:1em;margin-left:5px;margin-bottom:-5px;color:#000000;">Woo Woo (S)</p>--}%
%{--        </div><br/> <!-- 58 -->--}%
%{--    </div>--}%
</div>