<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="static com.devcolibri.servlet.AuthorizationServlet.usersMap" %>
<html>
<head>
    <title>Разместите корабли</title>
    <script type="text/javascript">
        var counter=0;
        var sb;
        var ajax=getXmlHttp();
        var field = new Array(10);
        for (var z = 0; z < 10; z++) {
            field[z] = new Array(10);
            for (var a = 0; a < 10; a++) {
                field[z][a] = 0;
            }
        }
        function getSB(){
            return sb;
        }
        function getXmlHttp(){
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
            if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
                xmlhttp = new XMLHttpRequest();
            }
            return xmlhttp;
        }
        function switchColor(x, y, event) {
            var target = event.target;
            switch (field[x][y]) {
                case 0:
                    target.style.backgroundColor = "#036";
                    field[x][y] = 1;
                    break;
                case 1:
                    target.style.backgroundColor = "#fffff0";
                    field[x][y] = 0;
            }
        }
        function send() {
            sb="";
            counter++;
            for(var i=0;i<10;i++){
                for(var j=0;j<10;j++){
                    sb=sb+field[i][j];
                }
            }
            ajax.open("POST","http://rudnitskij.ddns.net/checkfield",false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            ajax.onreadystatechange = function() {
                if (ajax.readyState == 4) {
                    if(ajax.status == 200) {
                        document.getElementById("upper").style.textBlink=true;
                    }
                }
            };
            ajax.send("array="+sb);
            if(ajax.responseText){
                document.getElementById("answer").innerHTML=ajax.responseText;
                document.getElementById("answer").style.border="none";
                document.getElementById("answer").style.backgroundColor="#fffff0";
                document.getElementById("answer").onclick = false;
                sb="";
            } else {
                document.getElementById("answer").innerHTML="Нажмите здесь для игры<br>этим расположением<br>кораблей против<br>компьютера";
                document.getElementById("answer").style.border="solid";
                document.getElementById("answer").style.borderBottomWidth="3px";
                document.getElementById("answer").style.borderBottomLeftRadius="20px";
                document.getElementById("answer").style.borderTopLeftRadius="20px";
                document.getElementById("answer").style.borderBottomRightRadius="20px";
                document.getElementById("answer").style.borderTopRightRadius="20px";
                document.getElementById("answer").style.backgroundColor="#ffffd0";
                document.getElementById("answer").onclick = function(){
                    document.forms.sendarray.array.value=sb/*getSB()*/;
                    document.forms.sendarray.submit();
                };
            }
        }
    </script>
</head>
<body bgcolor="#fffff0">
<table style="background-image: url(pacific); background-size: cover; width: 100%">
    <tr>
        <td width="40%">Добро пожаловать, ${player}</td>
        <td align="right">
            <div align="justify" id="upper">
                Здесь, как и в обычном морском бое, вам нужно разместить свои корабли,<br>
                (1 четырехпалубный, 2 трехпалубных, 3 двухпалубных и 4 однопалубных),<br>
                таким образом, чтобы они не касались друг друга
            </div>
        </td>
    </tr>
</table>
<table style="font-size: 25px" cellspacing="1" border="2">
    <%
        for (int i = 0; i < 10; i++) { %>
    <tr><%
        for (int j = 0; j < 10; j++) {
    %>
        <td style="background-color:#fffff0;" onclick="switchColor(<%=i%>,<%=j%>,event)">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        <% } %></tr>
    <%
        }
    %>
</table>
<table width="100%">
    <tr>
        <td align="center" valign="center" width="40%">
            <a href="/battleships">
                <button onclick="<%session.setAttribute("userName",null); %>">
                    Выйти из учетной записи
                </button>
            </a>
        </td>
        <td align="center" valign="center" width="40%">
                <button onclick="send()">
                    Утвердить расположение<br>кораблей
                </button>
        </td>
        <td align="center"><div id="answer"><br>&nbsp;<br>&nbsp;<br><br></div></td>
    </tr>
</table>
<form name="sendarray" method="post" action="/play">
    <input type="hidden" name="array">
</form>
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
