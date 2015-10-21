package com.devcolibri.servlet;

/*import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;*/

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This servlet class is supposed to process zip-files received from the user
 */
public class ZipServlet extends HttpServlet {
    Reviewer r;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] bytes, emptyBytes = new byte[1024];
        r=new Reviewer();
        StringBuilder inspectionResult = new StringBuilder();
        String[] dirs;
        File[] files;
        int malformedFiles = 0;
        boolean endsWithxls = true, endsWithXLS = true;
        String path = "C:\\Users\\Виталий\\Documents\\revisionZIP.zip";
        File receivedZIPfile = new File(path);
        FileOutputStream fos = new FileOutputStream(receivedZIPfile);
        byte[] dataSlice = MainServlet.extractData(request);
        int i, read;
        for (i = 0; i < dataSlice.length; i++) {
            fos.write(dataSlice[i]);
        }
        fos.flush();
        fos.close();
        Charset cSet=Charset.forName("IBM866");
        String resultPath = "C:\\Users\\Виталий\\Documents\\extractingZIPfiles\\";
        File file, resultDir = new File(resultPath);
        ZipFile zf = new ZipFile(receivedZIPfile,cSet);
        Enumeration<ZipEntry> entryEnum = (Enumeration<ZipEntry>) zf.entries();
        while (entryEnum.hasMoreElements()) {
            bytes = emptyBytes;
            ZipEntry entry = null;
            try {
                entry = entryEnum.nextElement();
            } catch (IllegalArgumentException e) {
                malformedFiles++;
                continue;
            }
            if (entry.isDirectory()) {
                continue;
            }
            dirs = entry.getName().split("/");
            file = new File(resultPath + dirs[dirs.length - 1]);
            InputStream is = zf.getInputStream(entry);
            FileOutputStream foss = new FileOutputStream(file);
            while ((read = is.read(bytes)) > -1) {
                foss.write(bytes, 0, read);
            }
            foss.close();
            is.close();
        }
        files = resultDir.listFiles();
        for (File f : files) {
            endsWithxls = (f.getName().endsWith(".xls"));
            endsWithXLS = (f.getName().endsWith(".XLS"));
            if (!(endsWithxls || endsWithXLS)) {
                inspectionResult.append("не файл .xls - ").append(f.getName()).append("<br>");
                f.delete();
                continue;
            }
            if (!(r.cellsFilledProperly(f))) {
                inspectionResult.append("Файл нуждается в проверке: ");
            }
            inspectionResult.append(r.result.toString()).append("<br><br>");
            f.delete();
        }
        switch (malformedFiles) {
            case 0:
                break;
            case 1:
                inspectionResult.append("Ошибка чтения из архива 1 файла");
                break;
            default:
                inspectionResult.append("Ошибка чтения из архива ").append(malformedFiles).append(" файлов");
                break;
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Проверка xls-файлов</title></head>");
        out.println("<body bgcolor=\"#FFFFD0\">");
        out.print(MainServlet.weatherKiev);
        out.print(MainServlet.weather);
        out.print("<p style=\"font-family:century gothic; font-size:medium\">");
        out.print(inspectionResult.toString());
        inspectionResult.delete(0, inspectionResult.length());
        out.print("</p><br>");
        out.println("<a href=\"blackCat.png\"><button>Нажмите, чтобы <br>посмотреть на кота.</button></a><br>\n" +
                "\n");
        out.println(MainServlet.linkMainPage);
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
        out.println(MainServlet.statCounter);
        out.println("</body></html>");
        out.close();
    }

}