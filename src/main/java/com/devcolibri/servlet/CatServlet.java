package com.devcolibri.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class CatServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        int i=0,offset;
        File catFile=new File("D:\\Рисунки\\Фото с телефона\\20150724-0001.jpeg");
        byte[] bytes=new byte[(int)catFile.length()];
        InputStream in=new FileInputStream(catFile);
        while ( i < bytes.length ) {
            offset=in.read(bytes,i,bytes.length);
            i+=offset;
        }
        //in.read(MainServlet.statCounter.getBytes());
        in.close();
        OutputStream bis=response.getOutputStream();
        bis.write(bytes);
        bis.close();
        response.setContentType("text/html");
        PrintWriter pw=response.getWriter();
        pw.print("<html><body>");
        pw.print(MainServlet.statCounter);
        pw.print("</body></html>");
    }
}
