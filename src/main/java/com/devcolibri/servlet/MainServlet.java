package com.devcolibri.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.Calendar;

public class MainServlet extends HttpServlet {
    Reviewer r;
    static String fileNameSliced;
    static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final int CR = (int) '\r';
    private static final int LF = (int) '\n';
    static final String linkMainPage = "<p><a href=\"http://rudnitskij.ddns.net/newApp/\">На главную страницу</a></p>";
    static final String weatherKiev = "<table cellpadding=0 cellspacing=0 width=206 align=right style=\"border:solid 1px #f6f5f4;font-family:Tahoma;font-size:10px;background-color:#32d228\"><tr><td><table width=100% cellpadding=0 cellspacing=0><tr><td width=8 height=30 background=\"http://rp5.ru/informer/htmlinfa/topshl.png\"  bgcolor=#f6f5f4> </td><td width=* align=center background=\"http://rp5.ru/informer/htmlinfa/topsh.png\" bgcolor=#f6f5f4><a style=\"color:#302227; font-family:Tahoma;font-size: 10px;\" href=\"http://rp5.ru/509938/ru\"><b>Киев</b></a></td><td width=8 height=30 background=\"http://rp5.ru/informer/htmlinfa/topshr.png\" bgcolor=#f6f5f4> </td></tr></table></td></tr><tr><td valign=top style=\"padding:0;\"><iframe src=\"http://rp5.ru/htmla.php?id=509938&lang=ru&um=00000&bg=%2332d228&ft=%23302227&fc=%23f6f5f4&c=%231c1717&f=Tahoma&s=10&sc=4\" width=100% height=254 frameborder=0 scrolling=no style=\"margin:0;\"></iframe></td></tr></table>";
    static final String weather = "<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" WIDTH=\"160\" HEIGHT=\"298\" id=\"loader\" ALIGN=\"right\"> <PARAM NAME=movie VALUE=\"http://rp5.ru/informer/group/1/19.swf\"> <PARAM NAME=quality VALUE=high> <PARAM NAME=scale VALUE=noscale> <PARAM NAME=salign VALUE=LT> <PARAM NAME=wmode VALUE=transparent> <PARAM NAME=bgcolor VALUE=#FFFFFF> <PARAM NAME=flashvars VALUE=\"points=7285,509938,259183, 5028,341401,3478&lang=ru&um=00000\"><EMBED src=\"http://rp5.ru/informer/group/1/19.swf\" quality=high scale=noscale salign=LT wmode=transparent bgcolor=#FFFFFF  WIDTH=\"160\" HEIGHT=\"253\" NAME=\"loader\" flashvars=\"points=7285,509938,259183,341401,3478&lang=ru&um=00000\" ALIGN=\"\" TYPE=\"application/x-shockwave-flash\" PLUGINSPAGE=\"http://www.macromedia.com/go/getflashplayer\"></EMBED></OBJECT>";
    static final String statCounter = "<!--LiveInternet counter--><script type=\"text/javascript\"><!--\n" +
            "document.write(\"<a href='//www.liveinternet.ru/click' \"+\n" +
            "\"target=_blank><img src='//counter.yadro.ru/hit?t16.4;r\"+\n" +
            "escape(document.referrer)+((typeof(screen)==\"undefined\")?\"\":\n" +
            "\";s\"+screen.width+\"*\"+screen.height+\"*\"+(screen.colorDepth?\n" +
            "screen.colorDepth:screen.pixelDepth))+\";u\"+escape(document.URL)+\n" +
            "\";\"+Math.random()+\n" +
            "\"' alt='' title='LiveInternet: показано число просмотров за 24\"+\n" +
            "\" часа, посетителей за 24 часа и за сегодня' \"+\n" +
            "\"border='0' width='88' height='31'><\\/a>\")\n" +
            "//--></script><!--/LiveInternet-->";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(String.format("The site was accessed from " + request.getRemoteAddr() + " at %tT", Calendar.getInstance()));
        response.setContentType(CONTENT_TYPE);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MainPage.jsp");
        dispatcher.forward(request,response);
        /*PrintWriter out = response.getWriter();

// HTML форма, отправляемая методом post
        out.println("<html>");
        out.println("<head><title>Проверка xls-файлов</title></head>");
        out.println("<body bgcolor=\"#FFFFD0\">");
        out.print(weatherKiev);
        out.print(weather);
        out.print("<form action=\"inspectingXLSfiles\" method=\"post\" enctype=\"multipart/form-data\">");
        out.print("<input type=file name=XLSfile>");
        out.print("<input type=submit value=\"Отправить файл\">");
        out.print("</form>");
        out.println("<form action=\"inspectZIP\" method=\"post\" enctype=\"multipart/form-data\" >" +
                "<input type=file name=ZIPfile>" +
                "<input type=submit value=\"Отправить zip-архив\">" +
                "</form>");
        out.println(linkMainPage);
        out.println("<br><p><a href=\"http://rudnitskij.ddns.net/battleships\">Морской бой</a></p>");
        out.println("<div id=\"disqus_thread\"></div>\n" +
                "    <script type=\"text/javascript\">\n" +
                "        *//* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * *//*\n" +
                "        var disqus_shortname = 'rudnitskij'; // required: replace example with your forum shortname\n" +
                "\n" +
                "        *//* * * DON'T EDIT BELOW THIS LINE * * *//*\n" +
                "        (function() {\n" +
                "            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;\n" +
                "            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';\n" +
                "            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);\n" +
                "        })();\n" +
                "    </script>\n" +
                "    <noscript>Please enable JavaScript to view the <a href=\"https://disqus.com/?ref_noscript\">comments powered by Disqus.</a></noscript>\n" +
                "   \n" +
                "\n" +
                "\n" +
                " ");
        out.println(statCounter);
        out.println("</body></html>");
        out.close();*/
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        r = new Reviewer();

//Поток, в который будет писаться содержимое (в принципе может быть любой OutputStream)
        byte[] dataSlice = extractData(request);
        String path = "C:\\Users\\Виталий\\apache-tomcat-8.0.18\\webapps\\ROOT\\" + fileNameSliced;
        FileOutputStream fos = new FileOutputStream(path);
        int i;
        for (i = 0; i < dataSlice.length; i++) {
            fos.write(dataSlice[i]);
        }
        fos.flush();
        fos.close();
        File receivedFile = new File(path);
// HTML форма отправляемая методом post
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Проверка xls-файлов</title></head>");
        out.println("<body bgcolor=\"#FFFFD0\">");
        out.print(weatherKiev);
        out.print(weather);
        if (r.cellsFilledProperly(receivedFile)) {
            out.println("<h1>Файл соответствует требованиям.</h1>");
            out.println("<p style=\"font-family:century gothic; font-size:medium\">" + r.result.toString() + "</p>");
        } else {
            out.println("<h1>Файл не соответствует требованиям.</h1>");
            out.println("<p style=\"font-family:century gothic; font-size:medium\">" + r.result.toString() + "</p>");
        }
        out.println("<a href=\""+ URLEncoder.encode(fileNameSliced, "utf-8")+"\"><button>Нажмите, чтобы ска-<br>чать свой файл обратно</button></a>\n" +
                "\n");
        out.print("<a href=\"blackCat.png\"><button>Нажмите, чтобы <br>посмотреть на кота.</button></a>");
        out.println(linkMainPage);
        out.println("<div id=\"disqus_thread\"></div>\n" +
                "    <script type=\"text/javascript\">\n" +
                "        /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */\n" +
                "        var disqus_shortname = 'rudnitskij'; // required: replace example with your forum shortname\n" +
                "\n" +
                "        /* * * DON'T EDIT BELOW THIS LINE * * */\n" +
                "        (function() {\n" +
                "            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;\n" +
                "            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';\n" +
                "            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);\n" +
                "        })();\n" +
                "    </script>\n" +
                "    <noscript>Please enable JavaScript to view the <a href=\"https://disqus.com/?ref_noscript\">comments powered by Disqus.</a></noscript>");
        out.println(statCounter);
        out.println("</body></html>");
        out.close();
    }

    public static byte[] extractData(HttpServletRequest request) throws IOException {
// Содержимое пришедших байтов их запроса (содержимое приходящего файла)
        String fileName;
        int index= -2;
        InputStream is = request.getInputStream();
        byte[] data = new byte[request.getContentLength()];
        byte[] name = new byte[512];
        int bytes;
        int counter = 0;
        while ((bytes = is.read()) != -1) {
            if (bytes == 61) index++;
            data[counter] = (byte) bytes;
            if (index == 0 && counter < 512) {
                name[counter] = (byte) bytes;
                if (bytes == 46) index = 1;
            }
            counter++;
        }
        fileName = new String(name,"UTF-8");
        fileNameSliced = fileName.substring(fileName.lastIndexOf("\"")+1,fileName.lastIndexOf(".")+1) + "xls";
        is.close();

// Определение индексов срезки
        int i;
        int beginSliceIndex = 0;
// Конечный индекс срезки - длина границы + доп. символы.
        int endSliceIndex = data.length - getBoundary(request).length() - 9;

        for (i = 0; i < data.length; i++) {
// Начальный индекс срезки: после того как встретятся 2 раза подряд \r\n
            if (data[i] == CR && data[i + 1] == LF && data[i + 2] == CR && data[i + 3] == LF) {
                beginSliceIndex = i + 4;
                break;
            }
        }

        byte[] dataSlice = new byte[endSliceIndex - beginSliceIndex + 1];
        for (i = beginSliceIndex; i <= endSliceIndex; i++) {
            dataSlice[i - beginSliceIndex] = data[i];
        }

        return dataSlice;
    }

    // Поиск границы
    private static String getBoundary(HttpServletRequest request) {
        String cType = request.getContentType();
        return cType.substring(cType.indexOf("boundary=") + 9);
    }
}