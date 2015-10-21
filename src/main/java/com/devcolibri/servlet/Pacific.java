package com.devcolibri.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class Pacific extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");
        int i=0,offset;
        File seaFile=new File("D:\\Java projects\\ServletExampl\\src\\main\\webapp\\Pacific.jpg");
        byte[] bytes=new byte[(int)seaFile.length()];
        InputStream in=new FileInputStream(seaFile);
        while ( i < bytes.length ) {
            offset=in.read(bytes,i,bytes.length);
            i+=offset;
        }
        in.close();
        OutputStream bis=resp.getOutputStream();
        bis.write(bytes);
        bis.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
