package com.devcolibri.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.devcolibri.servlet.MainServlet.CONTENT_TYPE;

public class AuthorizationServlet extends HttpServlet {
    static final String xmlFilePath="D:\\Java projects\\ServletExampl\\src\\main\\webapp\\players.xml";
    public static Map <String,Player> usersMap=new HashMap<String,Player>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(String.format("The page with battleships was accessed from " + request.getRemoteAddr() + " at %tT",Calendar.getInstance() ));
        response.setContentType(CONTENT_TYPE);
        HttpSession session=request.getSession();
        Player pl = usersMap.get(session.getId());
        if(session.getAttribute("userName")!=null && pl!=null){
            response.sendRedirect("/createPlayerFleet");
        }else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/battleships.jsp");
            dispatcher.forward(request, response);
        }
    }
}
