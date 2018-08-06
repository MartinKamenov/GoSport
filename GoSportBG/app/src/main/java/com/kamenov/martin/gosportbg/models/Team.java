package com.kamenov.martin.gosportbg.models;

/**
 * Created by Martin on 21.7.2018 г..
 */

public class Team {
    public int id;
    public String name;
    public String sport;
    public User[] users;
    public User[] requestingPlayers;
    public String pictureUrl;
    public DateTime dateTime;
}
