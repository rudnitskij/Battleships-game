package com.devcolibri.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static com.devcolibri.servlet.MainServlet.CONTENT_TYPE;

public class Checkfield extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.getWriter().println("Things gone bad...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        PrintWriter pw = resp.getWriter();
        if(!checking(req.getParameter("array"))){
            pw.println("Такое расположение<br>кораблей не является<br>допустимым!<br><br>");
        }
    }

    public boolean checking(String numbers) {
        int counter = 0, interimCounter = 0;
        int[][] position = new int[10][10];
        int[] shipsDrawn = new int[3], ships = new int[]{1, 2, 3};
        for (int i = 0; i < numbers.length(); i++) {
            position[i / 10][i % 10] = Integer.parseInt(String.valueOf(numbers.charAt(i)));
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (position[i][j] == 1) {
                    counter++;
                    interimCounter++;
                }
                if (position[i][j] == 0 || (j == 9)) {
                    switch (interimCounter) {
                        case 4:
                            shipsDrawn[0]++;
                            interimCounter = 0;
                            break;
                        case 3:
                            shipsDrawn[1]++;
                            interimCounter = 0;
                            break;
                        case 2:
                            shipsDrawn[2]++;
                            interimCounter = 0;
                            break;
                        case 1:
                            interimCounter = 0;
                    }
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (position[i][j] == 1) {
                    interimCounter++;
                }
                if (position[i][j] == 0 || (i == 9)) {
                    switch (interimCounter) {
                        case 4:
                            shipsDrawn[0]++;
                            interimCounter = 0;
                            break;
                        case 3:
                            shipsDrawn[1]++;
                            interimCounter = 0;
                            break;
                        case 2:
                            shipsDrawn[2]++;
                            interimCounter = 0;
                            break;
                        case 1:
                            interimCounter = 0;
                    }
                }
            }
        }
        return (counter == 20) && Arrays.equals(ships, shipsDrawn);
    }
}
