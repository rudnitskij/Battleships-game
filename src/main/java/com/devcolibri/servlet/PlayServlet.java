package com.devcolibri.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

import static com.devcolibri.servlet.AuthorizationServlet.usersMap;
import static com.devcolibri.servlet.MainServlet.CONTENT_TYPE;


public class PlayServlet extends HttpServlet {
    String postedArray;
    RequestDispatcher disp;
    HttpSession session;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession();
        response.setContentType(CONTENT_TYPE);
        postedArray=request.getParameter("array");
        Player pl= usersMap.get(session.getId());
        pl.setField(getField(postedArray));
        disp= request.getRequestDispatcher("/play.jsp");
        disp.forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter pw=response.getWriter();
        pw.println("Запрос GET не поддерживается!");
    }

    public static String setColor(int[][] array,int i,int j){
        switch (array[i][j]){
            case 0: return "#ddddd0";
            case 1: return "#036";
            case 2: return "#0ff";
            case 5: return "#f00";
        }
        return null;
    }

    public static String setCompColor(int[][] array,int i,int j){
        switch (array[i][j]){
            case 2: return "#0ff";
            case 5: return "#f00";
            default: return "#036";
        }
    }

    private int[][] getField(String data){
        int[][] result = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                result[i][j]=Integer.parseInt(String.valueOf(data.charAt(i*10 + j)));
            }
        }
        return result;
    }

    public static int[][] createFleet() {
        long time, time2;
        boolean tooLong;
        Random random;
        int number, vertical, threeDeckShips, twoDeckShips, oneDeckShips, counter, interimCounter;
        int[][] result;
        int[] shipsDrawn = new int[3];
        int[] ships = new int[]{1, 2, 3};
        while (true) {
            tooLong = false;
            random = new Random();
            threeDeckShips = twoDeckShips = oneDeckShips = counter = interimCounter = 0;
            result = new int[10][10];
            while (true) {
                try {
                    time = System.currentTimeMillis();
                    number = random.nextInt() % 9 + 1;
                    if (number % 2 == 0) {
                        vertical = random.nextInt() % 5 + 1;
                        result[number][vertical] = 1;
                        result[number][vertical + 1] = 1;
                        result[number][vertical + 2] = 1;
                        result[number][vertical + 3] = 1;
                    } else {
                        vertical = random.nextInt() % 5 + 1;
                        result[vertical][number] = 1;
                        result[vertical + 1][number] = 1;
                        result[vertical + 2][number] = 1;
                        result[vertical + 3][number] = 1;
                    }//установлен 4-палубный корабль
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
                break;
            }
            do {
                try {
                    number = random.nextInt() % 9 + 1;
                    vertical = random.nextInt() % 8 + 1;
                    if (number % 2 == 0) {
                        if (result[number][vertical] == 1 || result[number][vertical + 1] == 1 || result[number][vertical + 2] == 1 ||
                                result[number - 1][vertical - 1] == 1 || result[number + 1][vertical - 1] == 1 ||
                                result[number - 1][vertical + 3] == 1 || result[number + 1][vertical + 3] == 1 ||
                                result[number + 3][vertical - 1] == 1 || result[number + 3][vertical + 1] == 1) {
                            continue;
                        }
                        result[number][vertical] = 1;
                        result[number][vertical + 1] = 1;
                        result[number][vertical + 2] = 1;
                    } else {
                        if (result[vertical][number] == 1 || result[vertical + 1][number] == 1 || result[vertical + 2][number] == 1 ||
                                result[vertical - 1][number - 1] == 1 || result[vertical - 1][number + 1] == 1 ||
                                result[vertical + 3][number + 1] == 1 || result[vertical + 3][number + 1] == 1) {
                            continue;
                        }
                        result[vertical][number] = 1;
                        result[vertical + 1][number] = 1;
                        result[vertical + 2][number] = 1;
                    }
                    threeDeckShips++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            } while (threeDeckShips < 2);

            do {
                try {
                    number = random.nextInt() % 9 + 1;
                    vertical = random.nextInt() % 9 + 1;
                    if (number % 2 == 0) {
                        if (result[number][vertical] == 1 || result[number][vertical + 1] == 1 || result[number - 1][vertical - 1] == 1 ||
                                result[number - 1][vertical] == 1 || result[number - 1][vertical + 1] == 1 || result[number - 1][vertical + 2] == 1 ||
                                result[number][vertical + 2] == 1 || result[number + 1][vertical + 2] == 1) {
                            continue;
                        }
                        result[number][vertical] = 1;
                        result[number][vertical + 1] = 1;
                    } else {
                        if (result[vertical][number] == 1 || result[vertical + 1][number] == 1 || result[vertical - 1][number - 1] == 1 ||
                                result[vertical - 1][number] == 1 || result[vertical - 1][number + 1] == 1 || result[vertical + 2][number - 1] == 1 ||
                                result[vertical + 2][number] == 1 || result[vertical + 2][number + 1] == 1) {
                            continue;
                        }
                        result[vertical][number] = 1;
                        result[vertical + 1][number] = 1;
                    }
                    twoDeckShips++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            } while (twoDeckShips < 3);
            while (oneDeckShips < 4) {
                time2 = System.currentTimeMillis();
                if ((time2 - time) > 150) {
                    tooLong = true;
                    break;
                }
                number = random.nextInt() % 10;
                vertical = random.nextInt() % 10;
                try {
                    if (result[number][vertical] == 1 || result[number - 1][vertical] == 1 || result[number + 1][vertical] == 1 ||
                            result[number][vertical - 1] == 1 || result[number - 1][vertical - 1] == 1 || result[number + 1][vertical - 1] == 1 ||
                            result[number][vertical + 1] == 1 || result[number - 1][vertical + 1] == 1 || result[number + 1][vertical + 1] == 1) {
                        continue;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
                result[number][vertical] = 1;
                oneDeckShips++;
            }
            if (tooLong) {
                continue;
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (result[i][j] == 1) {
                        counter++;
                        interimCounter++;
                    }
                    if (result[i][j] == 0 || (j == 9)) {
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
                    if (result[i][j] == 1) {
                        interimCounter++;
                    }
                    if (result[i][j] == 0 || (i == 9)) {
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
            if (counter != 20) {
                shipsDrawn = new int[3];
                continue;
            }
            if (Arrays.equals(ships, shipsDrawn)) {
                break;
            } else {
                shipsDrawn = new int[3];
            }
        }
        return result;
    }
}
