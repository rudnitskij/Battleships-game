<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Ошибка авторизации</title>
    <style>
        input {
            font-size: 20px;
            font-family: Garamond, Arial;
            background-color: #d0d9e0;
        }

        a {
            font-family: Verdana;
            font-size: 120%;
            color: blue;
        }
    </style>
</head>
<body bgcolor="#ffffc0">
<table align="center" width="800">
    <tr style="background-image: url(pacific); background-size: cover">
        <td align="center">
            <h1>ОШИБКА АВТОРИЗАЦИИ ПОЛЬЗОВАТЕЛЯ</h1>
            <h2>ПОЛЯ НЕ МОГУТ БЫТЬ ПУСТЫМИ</h2>
            <br><br><br><br><br>

            <p><font color="#f0ff0f">Войдите в свою учетную запись<br>или зарегистрируйтесь.<br>
                Регистрация означает только придумывание логина и пароля для своего профиля,<br>
                подтверждать аккаунт по СМС или электронной почте не нужно.</font></p><br><br><br>

            <form method="post">
                <input type="text" name="userName"><br>
                <input type="password" name="userPassword"><br>
                <input type="submit" value="Вход" formaction="/authentication">
                <input type="submit" value="Регистрация" formaction="/register">
            </form>
        </td>
    </tr>
</table>
<div align="center">
    <a href="/">К обработке таблиц</a><br>
    <a href="/newApp">На главную страницу</a>
</div>
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
