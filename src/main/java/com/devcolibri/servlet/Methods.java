package com.devcolibri.servlet;


import java.io.File;

import static com.devcolibri.servlet.Authentication.getPlayers;
import static com.devcolibri.servlet.AuthorizationServlet.xmlFilePath;

public class Methods {

    public static Player getPlayerByName(String name){
        Player player=null;
        PlayersList list;
        list = getPlayers(new File(xmlFilePath));
        for (Player p : list.players) {
            if (p.getName().equals(name)) {
                player = p;
                break;
            }
        }
        return player;
    }


}
