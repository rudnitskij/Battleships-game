package com.devcolibri.servlet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@XmlRootElement(name = "players")
public class PlayersList {

    public PlayersList(){}

    @XmlElement(name = "player")
    public List<Player> players = new CopyOnWriteArrayList<Player>();

    public void add(Player player) {
        players.add(player);
    }
}
