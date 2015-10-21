package com.devcolibri.servlet;

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
import java.io.PrintWriter;

import static com.devcolibri.servlet.Authentication.getPlayers;
import static com.devcolibri.servlet.AuthorizationServlet.usersMap;
import static com.devcolibri.servlet.AuthorizationServlet.xmlFilePath;
import static com.devcolibri.servlet.MainServlet.CONTENT_TYPE;


public class ProfileEdit extends HttpServlet {
    File XMLfile;
    PlayersList listOf;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        PrintWriter pw = resp.getWriter();
        pw.println("<body style=\"background-image: url(pacific); background-size: cover\">" +
                "<font size=100px color=\"#ff0\">МИР<br>ТРУД<br>МАЙ<br></font></body>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        HttpSession session = req.getSession();
        PrintWriter pw = resp.getWriter();
        int shots = Integer.parseInt(req.getParameter("shots"));
        int preciseShots = Integer.parseInt(req.getParameter("preciseShots"));
        String winner = req.getParameter("winner");

        XMLfile = new File(xmlFilePath);

        listOf = getPlayers(XMLfile);
        for (Player player : listOf.players) {
            if (player.getName().equals(usersMap.get(session.getId()).getName())) {


                player.setGamesPlayed(player.getGamesPlayed() + 1);
                if ("player".equalsIgnoreCase(winner)) {
                    player.setGamesWon(player.getGamesWon() + 1);
                }
                player.setShotsMade(player.getShotsMade() + shots);
                player.setShotsPrecise(player.getShotsPrecise() + preciseShots);
                pw.println("Игрок " + player.getName() + "<br>");
                pw.println("сыграно " + player.getGamesPlayed() + " боев<br>");
                pw.println("выиграно " + player.getGamesWon() + " боев<br>");
                pw.println("сделано " + player.getShotsMade() + " выстрелов<br>");
                pw.println("из них " + player.getShotsPrecise() + " в цель<br>");
            }
        }
        try {
            Marshaller marshaller = JAXBContext.newInstance(PlayersList.class).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(listOf, XMLfile);
            pw.println("Результат сохранен в профиль игрока");
        }catch(JAXBException e){
            pw.println("Ошибка записи результатов в профиль");
        }
    }
}
