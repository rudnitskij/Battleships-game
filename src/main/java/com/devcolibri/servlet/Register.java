package com.devcolibri.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;

import static com.devcolibri.servlet.Authentication.getPlayers;
import static com.devcolibri.servlet.AuthorizationServlet.usersMap;
import static com.devcolibri.servlet.AuthorizationServlet.xmlFilePath;

public class Register extends HttpServlet {
    File XMLfile;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = 0;
        XMLfile = new File(xmlFilePath);
        PlayersList list;
        RequestDispatcher disp;
        if (req.getParameter("userName").length() == 0 || req.getParameter("userPassword").length() == 0) {
            disp = req.getRequestDispatcher("/errorEmpty.jsp");
            disp.forward(req, resp);
        } else {
        try {
            try {
                list = getPlayers(XMLfile);
                for (Player pl : list.players) {
                    if (pl.getName().equals(req.getParameter("userName"))) {
                        disp = req.getRequestDispatcher("/errorRegister.jsp");
                        disp.forward(req, resp);
                        index = 1;
                        break;
                    }
                }
            } catch (Exception e) {
                list = new PlayersList();
            }
            if (index != 1) {
                Player newPlayer = new Player(req.getParameter("userName"), req.getParameter("userPassword"));
                list.add(newPlayer);
                Marshaller marshaller = JAXBContext.newInstance(PlayersList.class).createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                marshaller.marshal(list, XMLfile);
                HttpSession session = req.getSession();
                session.setAttribute("userName", newPlayer.getName());
                usersMap.put(session.getId(), newPlayer);
                resp.sendRedirect("/createPlayerFleet");
            }
        } catch (JAXBException e) {
            e.printStackTrace();
            disp = req.getRequestDispatcher("/error.jsp");
            disp.forward(req, resp);
        }
    }
    }
}
