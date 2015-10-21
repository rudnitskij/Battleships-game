package com.devcolibri.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.devcolibri.servlet.AuthorizationServlet.usersMap;
import static com.devcolibri.servlet.MainServlet.CONTENT_TYPE;


public class CreatePlayerFleet extends HttpServlet {
    RequestDispatcher disp;
    PlayersList list;
    public Player activePlayer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        HttpSession session = req.getSession();
        if (session.getAttribute("userName") == null && (activePlayer == null)) {
            disp = req.getRequestDispatcher("/error.jsp");
            disp.forward(req, resp);
        }
        activePlayer = usersMap.get(session.getId());
        req.setAttribute("player", usersMap.get(session.getId()).getName());
        disp = req.getRequestDispatcher("/createFleet.jsp");
        disp.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
