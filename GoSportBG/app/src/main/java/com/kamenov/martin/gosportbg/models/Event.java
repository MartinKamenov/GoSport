package com.kamenov.martin.gosportbg.models;


import com.kamenov.martin.gosportbg.constants.Sport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Event {
    private List<User> players;
    private User admin;
    private Sport sport;
    private Date dateTime;
    private String name;
    private String location;

    public Event(User admin,Sport sport, String name, Date dateTime, int playerLimit, String location) {
        this.admin = admin;
        this.sport = sport;
        this.name = name;
        this.dateTime = dateTime;
        this.setPlayers(playerLimit);
        this.location = location;
    }


    public Event(User admin,Sport sport, String name, Date date, String location) {
        this(admin, sport, name, date, 0, location);
    }

    public void setPlayers(int playerLimit) {
        if (playerLimit <= 0) {
            this.players = new ArrayList<>();
            players.add(this.admin);
            return;
        }
        User[] users = new User[playerLimit];
        this.players = Arrays.asList(users);
        players.set(0, this.admin);
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return "Event{" +
                ", sport=" + sport +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                '}';
    }
}

