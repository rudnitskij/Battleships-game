package com.devcolibri.servlet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@XmlRootElement(name = "player")
public class Player {
    private String name;
    private String password;
    private long gamesPlayed;
    private long gamesWon;
    private long shotsMade;
    private long shotsPrecise;
    private Date registerDate;
    private int[][] Field;

    public int[][] getField() {
        return Field;
    }

    public void setField(int[][] field) {
        Field = field;
    }

    public Player(){}

    public Player(String name,String password){
        this.name=name;
        this.password=password;
        registerDate=new Date(System.currentTimeMillis());
    }

    public Date getRegisterDate() {
        return registerDate;
    }
    @XmlElement(name = "register_date")
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public long getShotsPrecise() {
        return shotsPrecise;
    }
    @XmlElement(name = "shots_precise")
    public void setShotsPrecise(long shotsPrecise) {
        this.shotsPrecise = shotsPrecise;
    }

    public long getShotsMade() {
        return shotsMade;
    }
    @XmlElement(name="shots_made")
    public void setShotsMade(long shotsMade) {
        this.shotsMade = shotsMade;
    }

    public long getGamesWon() {
        return gamesWon;
    }
    @XmlElement(name="games_won")
    public void setGamesWon(long gamesWon) {
        this.gamesWon = gamesWon;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }
    @XmlElement(name="games_played")
    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getPassword() {
        return password;
    }
    @XmlElement
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
}
