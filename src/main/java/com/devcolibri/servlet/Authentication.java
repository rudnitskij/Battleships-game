package com.devcolibri.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

import static com.devcolibri.servlet.AuthorizationServlet.usersMap;
import static com.devcolibri.servlet.AuthorizationServlet.xmlFilePath;
import static com.devcolibri.servlet.MainServlet.CONTENT_TYPE;

public class Authentication extends HttpServlet {
    PlayersList list;
    RequestDispatcher disp;
    File XMLfile;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        list = new PlayersList();
        int index = 0;
        resp.setContentType(CONTENT_TYPE);
        if (req.getParameter("userName").length() == 0 || req.getParameter("userPassword").length() == 0) {
            disp = req.getRequestDispatcher("/errorEmpty.jsp");
            disp.forward(req, resp);
        } else {
        try {
            XMLfile = new File(xmlFilePath);
            JAXBContext context = JAXBContext.newInstance(PlayersList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            list = (PlayersList) unmarshaller.unmarshal(XMLfile);
            for (Player pl : list.players) {
                if (pl.getName().equals(req.getParameter("userName")) &&
                        pl.getPassword().equals(req.getParameter("userPassword"))) {
                    HttpSession session = req.getSession();
                    session.setAttribute("userName", pl.getName());
                    usersMap.put(session.getId(), pl);
                    index = 1;
                    break;
                }
            }
            if (index != 1) {
                disp = req.getRequestDispatcher("/error.jsp");
                disp.forward(req, resp);
            } else {
                resp.sendRedirect("/createPlayerFleet");
            }
        } catch (JAXBException e) {
            disp = req.getRequestDispatcher("/error.jsp");
            disp.forward(req, resp);
        }
    }
    }

    public static PlayersList getPlayers(File file){
        try {
            JAXBContext context=JAXBContext.newInstance(PlayersList.class);
            Unmarshaller un=context.createUnmarshaller();
            return (PlayersList)un.unmarshal(file);
        } catch (JAXBException e) {
            return null;
        }
    }
}
