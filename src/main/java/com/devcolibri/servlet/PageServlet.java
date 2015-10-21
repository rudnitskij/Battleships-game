package com.devcolibri.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class PageServlet extends HttpServlet {

    final String path="C:\\Users\\Виталий\\Documents\\html-attempt.html";
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File page= new File(path);
        FileReader fis=new FileReader(page);
        resp.setContentType(MainServlet.CONTENT_TYPE);
        PrintWriter out=resp.getWriter();
        for (int i = 0; i < page.length(); i++) {
            out.write(fis.read());
        }
        fis.close();
        out.close();
    }
}
