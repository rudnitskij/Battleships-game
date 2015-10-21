package com.devcolibri.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.net.URLDecoder;

public class FileSharing extends HttpServlet {
    String name = MainServlet.fileNameSliced;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        int offset,i=0;
        File XLSfile=new File("C:\\Users\\Виталий\\apache-tomcat-8.0.18\\webapps\\ROOT\\"+name);
        byte[] bytes=new byte[(int)XLSfile.length()];
        InputStream in=new FileInputStream(XLSfile);
        while ( i < bytes.length ) {
            offset=in.read(bytes,0+i,bytes.length);
            i+=offset;
        }
        in.close();
        //response.setHeader( "Content-Disposition","attachment;filename=\""+ URLEncoder.encode(name,"cp866")+"\"");
        OutputStream bis=response.getOutputStream();
        bis.write(bytes);
        bis.close();
    }
}
