package com.kamenov.martin.gosportbg.models;

import java.util.List;

public class Event {
    public int id;
    public String name;
    public String sport;
    public Location location;
    public String admin;
    public DateTime datetime;
    public int neededPlayers;
    public List<String> players;
}

