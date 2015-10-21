<%@ page import="com.devcolibri.servlet.Player" %>
<%@ page import="static com.devcolibri.servlet.AuthorizationServlet.usersMap" %>
<%@ page import="static com.devcolibri.servlet.PlayServlet.setColor" %>
<%@ page import="static com.devcolibri.servlet.PlayServlet.createFleet" %>
<%@ page import="static com.devcolibri.servlet.PlayServlet.setCompColor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Player player = usersMap.get(session.getId()); %>
<% int[][] compField = createFleet();%>
<html>
<head>
    <title>Морской Бой</title>
</head>
<body bgcolor="#fffff0">
<table id="table" style="background-image: url(pacific); background-size: cover; width: 100%">
    <tr>
        <td width="450px" valign="center">
            Добро пожаловать, <%=player.getName()%><br>
        </td>
        <td width="450px" valign="center">
            Сыграно партий всего <%=player.getGamesPlayed()%><br>
            Одержано побед <%=player.getGamesWon() %>
        </td>
        <td></td>
    </tr>
    <tr>
        <td width="450px" style="font-family: Verdana, Arial; font-size: 30px; " height="210px"
            id="screen" align="center" valign="center"></td>
        <td width="450px" style="font-family: Garamond, Arial; font-size: 30px;" id="shotResult" align="center"
            valign="center"></td>
        <td></td>
    </tr>
    <tr>
        <td width="450px" align="left">
            Ваше поле здесь&dArr;<br>
            <table id="playerTable" style="font-size: 22px" cellspacing="1" border="2">
                <%
                    for (int i = 0; i < 10; i++) { %>
                <tr><%
                    for (int j = 0; j < 10; j++) {
                %>
                    <td abbr="<%=player.getField()[i][j]%>" bgcolor=<%=setColor(player.getField(), i, j)%>>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <% } %></tr>
                <%
                    }
                %>
            </table>
        </td>
        <td width="450px" align="right">
            Здесь поле компьютера, выбирайте цель&dArr;<br>
            <table id="compTable" style="font-size: 22px" cellspacing="1" border="2">
                <%
                    for (int i = 0; i < 10; i++) { %>
                <tr><%
                    for (int j = 0; j < 10; j++) {
                %>
                    <td abbr="<%=compField[i][j]%>" bgcolor="<%=setCompColor(compField, i, j)%>"
                        onclick="makeMove(<%=i%>,<%=j%>,event)">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <% } %></tr>
                <%
                    }
                %>
            </table>
        </td>
        <td></td>
    </tr>

</table>
<script type="text/javascript">
    var x, y, showI, showJ, playerShotResult = -1, compShotResult = -1, prevMoveI = -1, prevMoveJ = -1;
    var shipIsNotSinked = false;
    var shots = 0, preciseShots = 0;
    var target;
    var playerShips = document.getElementById("playerTable");
    playerShipsArray = playerShips.getElementsByTagName("td");
    var playerField = new Array(10);
    for (x = 0; x < 10; x++) {
        playerField[x] = new Array(10);
        for (y = 0; y < 10; y++) {
            playerField[x][y] = parseInt(playerShipsArray[x * 10 + y].abbr.valueOf());
        }
    }
    var compShips = document.getElementById("compTable");
    compShipsArray = compShips.getElementsByTagName("td");
    var compField = new Array(10);
    for (x = 0; x < 10; x++) {
        compField[x] = new Array(10);
        for (y = 0; y < 10; y++) {
            compField[x][y] = parseInt(compShipsArray[x * 10 + y].abbr.valueOf());
        }
    }
    function makeMove(i, j, event) {
        if (shipsAreSinked(compField) || shipsAreSinked(playerField)) {
            return false;
        }
        target = event.target;
        switch (compField[i][j]) {
            case 1:
                shots++;
                preciseShots++;
                document.getElementById("screen").innerHTML = "";
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#fffff0")', 100);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#00ffff")', 200);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#00ff00")', 300);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#ffff00")', 400);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#ffa500")', 500);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#ffff00")', 600);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#00ff00")', 700);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#00ffff")', 800);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#fffff0")', 900);
                setTimeout('document.getElementById("screen").innerHTML = "ЕСТЬ ПОПАДАНИЕ!".fontcolor("#fffff0")', 1000);
                compField[i][j] = 5;
                target.abbr = "5";
                target.style.backgroundColor = "#f00";
                if (shipsAreSinked(compField)) {
                    document.getElementById("shotResult").innerHTML =
                            "Вы победили!<br>Примите искренние поздравления".fontcolor("#ff0");
                    document.getElementById("screen").innerHTML = "";
                    message = "Бой окончен!" + "\n" + "Сделано " + shots + " выстрелов,\n" + "из них " + preciseShots +
                    " в цель.\n" + "Нажмите для продолжения.";
                    document.getElementById("table").style.backgroundImage = "url('salut')";
                    reDraw();
                    reDrawPlayer();
                    setTimeout('alert(message);sendResults("player");shots=0;preciseShots=0;', 2000);
                    return false;
                }
                playerShotResult = getShotResult(i, j, compField);
                switch (getShotResult(i, j, compField)) {
                    case 1:
                        document.getElementById("shotResult").innerHTML = "УБИЛ!".fontcolor("#ff0");
                        for (k = 0; k < 10; k++) {
                            for (l = 0; l < 10; l++) {
                                if (compField[k][l] == 5) {
                                    try {
                                        if (compField[k][l - 1] == 0) {
                                            compField[k][l - 1] = 2;
                                            compShipsArray[k * 10 + (l - 1)].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k][l + 1] == 0) {
                                            compField[k][l + 1] = 2;
                                            compShipsArray[k * 10 + (l + 1)].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k + 1][l] == 0) {
                                            compField[k + 1][l] = 2;
                                            compShipsArray[(k + 1) * 10 + l].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k - 1][l] == 0) {
                                            compField[k - 1][l] = 2;
                                            compShipsArray[(k - 1) * 10 + l].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k - 1][l - 1] == 0) {
                                            compField[k - 1][l - 1] = 2;
                                            compShipsArray[(k - 1) * 10 + (l - 1)].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k - 1][l + 1] == 0) {
                                            compField[k - 1][l + 1] = 2;
                                            compShipsArray[(k - 1) * 10 + (l + 1)].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k + 1][l + 1] == 0) {
                                            compField[k + 1][l + 1] = 2;
                                            compShipsArray[(k + 1) * 10 + (l + 1)].style.backgroundColor = "#0ff";
                                        }
                                        if (compField[k + 1][l - 1] == 0) {
                                            compField[k + 1][l - 1] = 2;
                                            compShipsArray[(k + 1) * 10 + (l - 1)].style.backgroundColor = "#0ff";
                                        }
                                    } catch (ee) {
                                        continue;
                                    }
                                }
                            }
                        }
                        break;
                    case 0:
                        document.getElementById("shotResult").innerHTML = "РАНИЛ...".fontcolor("#ff0");
                }
                break;
            case 0:
                shots++;
                document.getElementById("screen").innerHTML = "";
                document.getElementById("shotResult").innerHTML = "Сейчас ход компьютера.".fontcolor("#ff0");
                document.getElementById("screen").innerHTML = "МИМО...".fontcolor("#ff0");
                compField[i][j] = 2;
                target.abbr = "2";
                target.style.backgroundColor = "#0ff";
                do {
                    if (shipsAreSinked(playerField)) {
                        document.getElementById("shotResult").innerHTML =
                                "Вы проиграли!<br>Ничего, на воде всякое бывает".fontcolor("#ff0");
                        document.getElementById("screen").innerHTML = "";
                        message = "Бой окончен!" + "\n" + "Сделано " + shots + " выстрелов,\n" + "из них " + preciseShots +
                        " в цель.\n" + "Нажмите для продолжения.";
                        reDraw();
                        reDrawPlayer();
                        document.getElementById("table").style.backgroundImage = "url('explosion')";
                        setTimeout('alert(message);reDrawPlayer();reDraw();sendResults("comp");shots=0;preciseShots=0;', 2000);
                        return false;
                    } else {
                        alert('сейчас мой ход...');
                        compMove(playerField);
                    }
                } while (compShotResult > -1)
        }
        reDrawPlayer();
    }
    function shipsAreSinked(table) {
        var counter = 0, i, j;
        for (i = 0; i < 10; i++) {
            for (j = 0; j < 10; j++) {
                if (table[i][j] == 5) counter++;// Подбитый корабль обозначим пятеркой
            }
        }
        return (counter == 20);
    }
    function getShotResult(i, j, table) {
        if (table[i][j] == 0)return -1;
        x = i;
        y = j;
        var sumUp = true, sumDown = true, sumLeft = true, sumRight = true;
        while (y >= 0 && table[x][y] != 0 && table[x][y] != 2) {
            if (table[x][y] == 1 || table[x][y] == 0) {
                sumUp = false;
                break;
            }
            y--;
        }
        x = i;
        y = j;
        while (y <= 9 && table[x][y] != 0 && table[x][y] != 2) {
            if (table[x][y] == 1 || table[x][y] == 0) {
                sumDown = false;
                break;
            }
            y++;
        }
        x = i;
        y = j;
        while (x >= 0 && table[x][y] != 0 && table[x][y] != 2) {
            if (table[x][y] == 1 || table[x][y] == 0) {
                sumLeft = false;
                break;
            }
            x--;
        }
        x = i;
        y = j;
        while (x <= 9 && table[x][y] != 0 && table[x][y] != 2) {
            if (table[x][y] == 1 || table[x][y] == 0) {
                sumRight = false;
                break;
            }
            x++;
        }

        return (sumUp && sumDown && sumLeft && sumRight) ? 1 : 0;
    }
    function compMove(table) {
        var i, j, k, l;
        var shot = new Array(2);
        document.getElementById("screen").innerHTML = "";
        while (true) {
            try {
                if (compShotResult == 0 || shipIsNotSinked) {
                    shot = lookAround(prevMoveI, prevMoveJ, table);
                    i = shot[0];
                    j = shot[1];
                } else {
                    i = Math.round(Math.random() * 1000000 % 10);
                    j = Math.round(Math.random() * 1000000 % 10);
                    if (i == 0 || i == 9 || j == 0 || j == 9) {
                        if (Math.round(Math.random() * 1000000 % 2) != 0) continue;
                    }
                }
                if (table[i][j] == 2 || table[i][j] == 5) continue;
                showI = i;//для отображения выстрела на доске
                showJ = j;
                reDraw();
                function blinking() {
                    timeID1 = setTimeout('playerShipsArray[showI*10+showJ].style.backgroundColor = "#f0f"', 250);
                    timeID2 = setTimeout('playerShipsArray[showI*10+showJ].style.backgroundColor = "#009"', 500);
                }

                if (table[i][j] == 0) {
                    intervalID = setInterval(blinking, 500);
                    if (compShotResult == 0) {
                        shipIsNotSinked = true;
                    }
                    compShotResult = -1;
                    table[i][j] = 2;
                    document.getElementById("screen").innerHTML = "Сейчас ваш ход".fontcolor("#fffff0");
                    document.getElementById("shotResult").innerHTML = "Компьютер\nпромахнулся...".fontcolor("#fffff0");
                    break;
                }
                if (table[i][j] == 1) {
                    playerShipsArray[showI * 10 + showJ].style.backgroundColor = "#f0f";
                    table[i][j] = 5;
                    /*if(shipsAreSinked(playerField)){
                     document.getElementById("shotResult").innerHTML =
                     "Вы проиграли!<br>Ничего, на воде всякое бывает".fontcolor("#ff0");
                     document.getElementById("screen").innerHTML = "";
                     message = "Бой окончен!" +"\n" + "Сделано "+shots+" выстрелов,\n" + "из них "+preciseShots+
                     " в цель.\n" + "Нажмите для продолжения.";
                     reDraw();reDrawPlayer();
                     setTimeout('alert(message);reDrawPlayer();reDraw();sendResults("comp")',2000);
                     return false;
                     }*/
                    compShotResult = getShotResult(i, j, table);
                    switch (compShotResult) {
                        case 1:
                            document.getElementById("shotResult").innerHTML = "ВАШ КОРАБЛЬ\nПОТОПЛЕН".fontcolor("#fffff0");
                            alert('потопил...');
                            shipIsNotSinked = false;
                            for (k = 0; k < 10; k++) {
                                for (l = 0; l < 10; l++) {
                                    if (table[k][l] == 5) {
                                        try {
                                            if (table[k][l - 1] == 0) {
                                                table[k][l - 1] = 2;
                                                playerShipsArray[k * 10 + (l - 1)].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k][l + 1] == 0) {
                                                table[k][l + 1] = 2;
                                                playerShipsArray[k * 10 + (l + 1)].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k + 1][l] == 0) {
                                                table[k + 1][l] = 2;
                                                playerShipsArray[(k + 1) * 10 + l].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k - 1][l] == 0) {
                                                table[k - 1][l] = 2;
                                                playerShipsArray[(k - 1) * 10 + l].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k - 1][l - 1] == 0) {
                                                table[k - 1][l - 1] = 2;
                                                playerShipsArray[(k - 1) * 10 + (l - 1)].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k - 1][l + 1] == 0) {
                                                table[k - 1][l + 1] = 2;
                                                playerShipsArray[(k - 1) * 10 + (l + 1)].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k + 1][l + 1] == 0) {
                                                table[k + 1][l + 1] = 2;
                                                playerShipsArray[(k + 1) * 10 + (l + 1)].style.backgroundColor = "#0ff";
                                            }
                                            if (table[k + 1][l - 1] == 0) {
                                                table[k + 1][l - 1] = 2;
                                                playerShipsArray[(k + 1) * 10 + (l - 1)].style.backgroundColor = "#0ff";
                                            }
                                        } catch (ee) {
                                            continue;
                                        }
                                    }
                                }
                            }
                            break;
                        case 0:
                            alert('ранил...');
                            shipIsNotSinked = true;
                            prevMoveI = i;
                            prevMoveJ = j;
                            document.getElementById("shotResult").innerHTML = "ВАШ КОРАБЛЬ\nПОДБИТ!".fontcolor("#fffff0");
                    }
                    reDraw();
                    reDrawPlayer();
                    break;
                }
            } catch (e) {
                continue;
            }
        }
    }

    function reDraw() {
        for (i = 0; i < 10; i++) {
            for (j = 0; j < 10; j++) {
                switch (playerField[i][j]) {
                    case 0:
                        playerShipsArray[i * 10 + j].style.backgroundColor = "#ddddd0";
                        playerShipsArray[i * 10 + j].abbr = 0;
                        break;
                    case 1:
                        playerShipsArray[i * 10 + j].style.backgroundColor = "#036";
                        playerShipsArray[i * 10 + j].abbr = 1;
                        break;
                    case 2:
                        playerShipsArray[i * 10 + j].style.backgroundColor = "#0ff";
                        playerShipsArray[i * 10 + j].abbr = 2;
                        break;
                    case 5:
                        playerShipsArray[i * 10 + j].style.backgroundColor = "#f00";
                        playerShipsArray[i * 10 + j].abbr = 5;
                        break;
                }
            }
        }
    }
    function reDrawPlayer() {
        for (i = 0; i < 10; i++) {
            for (j = 0; j < 10; j++) {
                switch (compField[i][j]) {
                    case 0:
                        compShipsArray[i * 10 + j].abbr = 0;
                        if (shipsAreSinked(playerField)) {
                            compShipsArray[i * 10 + j].style.backgroundColor = "#0ff";
                        }
                        break;
                    case 1:
                        compShipsArray[i * 10 + j].abbr = 1;
                        if (shipsAreSinked(playerField)) {
                            compShipsArray[i * 10 + j].style.backgroundColor = "#f00";
                        }
                        break;
                    case 2:
                        compShipsArray[i * 10 + j].style.backgroundColor = "#0ff";
                        compShipsArray[i * 10 + j].abbr = 2;
                        break;
                    case 5:
                        compShipsArray[i * 10 + j].style.backgroundColor = "#f00";
                        compShipsArray[i * 10 + j].abbr = 5;
                        break;
                }
            }
        }
    }
    function lookAround(x, y, table) {
        while (true) {
            var shot = new Array(2);
            var i = x, j = y;
            if (table[x - 1][y] == 5 && (table[x + 1][y] != 5 && table[x + 1][y] != 2)) {
                return [x + 1, y];
            }
            if (table[x + 1][y] == 5 && (table[x - 1][y] != 5 && table[x - 1][y] != 2)) {
                return [x - 1, y];
            }
            if (table[x][y - 1] == 5 && (table[x][y + 1] != 5 && table[x][y + 1] != 2)) {
                return [x, y + 1];
            }
            if (table[x][y + 1] == 5 && (table[x][y - 1] != 5 && table[x][y - 1] != 2)) {
                return [x, y - 1];
            }
            left = [x - 1, y];
            right = [x + 1, y];
            up = [x, y + 1];
            down = [x, y - 1];
            leftIndex = (table[x - 1][y] == 2 || table[x][y - 1] == 5 || table[x][y + 1] == 5) ? 0 : Math.round(Math.random() * 100);
            rightIndex = (table[x + 1][y] == 2 || table[x][y - 1] == 5 || table[x][y + 1] == 5) ? 0 : Math.round(Math.random() * 100);
            upIndex = (table[x][y + 1] == 2 || table[x - 1][y] == 5 || table[x + 1][y] == 5) ? 0 : Math.round(Math.random() * 100);
            downIndex = (table[x][y - 1] == 2 || table[x - 1][y] == 5 || table[x + 1][y] == 5) ? 0 : Math.round(Math.random() * 100);
            choice = new Array();
            choice[leftIndex] = left;
            choice[rightIndex] = right;
            choice[upIndex] = up;
            choice[downIndex] = down;
            if (choice.length > 1) {
                return choice[choice.length - 1];
            } else {
                if (table[--i][j] == 5) {
                    while (true) {
                        try {
                            --i;
                            if (table[i][j] != 5) {
                                return [i, j];
                            }
                        } catch (e) {
                            break;
                        }
                    }
                }
                i = x;
                j = y;
                if (table[++i][j] == 5) {
                    while (true) {
                        try {
                            ++i;
                            if (table[i][j] != 5) {
                                return [i, j];
                            }
                        } catch (e) {
                            break;
                        }
                    }
                }
                i = x;
                j = y;
                if (table[i][--j] == 5) {
                    while (true) {
                        try {
                            --j;
                            if (table[i][j] != 5) {
                                return [i, j];
                            }
                        } catch (e) {
                            break;
                        }
                    }
                }
                i = x;
                j = y;
                if (table[i][++j] == 5) {
                    while (true) {
                        try {
                            ++j;
                            if (table[i][j] != 5) {
                                return [i, j];
                            }
                        } catch (e) {
                            break;
                        }
                    }
                }
            }
        }
    }
    function getXmlHttp() {
        var xmlhttp;
        try {
            xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (E) {
                xmlhttp = false;
            }
        }
        if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
            xmlhttp = new XMLHttpRequest();
        }
        return xmlhttp;
    }
    function sendResults(winner) {
        ajax = getXmlHttp();
        ajax.open("POST", "http://rudnitskij.ddns.net/profile_edit", false);
        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        ajax.onreadystatechange = function () {
            if (ajax.readyState == 4) {
                if (ajax.status == 200) {
                    document.getElementById("upper").style.textBlink = true;
                }
            }
        };
        ajax.send("shots=" + shots + "&preciseShots=" + preciseShots + "&winner=" + winner);
        document.getElementById("screen").style.textAlign = "left";
        document.getElementById("screen").innerHTML = ajax.responseText.fontcolor("#f00");
        document.getElementById("shotResult").innerHTML = "<a href=\"/newApp\"><button style=\"width:300px\">" +
        "Вернуться на главную</button></a><br>" +
        "<a href=\"/battleships\"><button style=\"width:300px\">Сыграть еще раз</button></a>";
    }
</script>
<script type="text/javascript">

    <!--
    document.write("<a href='//www.liveinternet.ru/click' " +
    "target=_blank><img src='//counter.yadro.ru/hit?t16.4;r" +
    escape(document.referrer) + ((typeof(screen) == "undefined") ? "" :
    ";s" + screen.width + "*" + screen.height + "*" + (screen.colorDepth ?
            screen.colorDepth : screen.pixelDepth)) + ";u" + escape(document.URL) +
    ";" + Math.random() +
    "' alt='' title='LiveInternet: показано число просмотров за 24" +
    " часа, посетителей за 24 часа и за сегодня' " +
    "border='0' width='88' height='31'><\/a>")
    //--></script>
</body>
</html>

