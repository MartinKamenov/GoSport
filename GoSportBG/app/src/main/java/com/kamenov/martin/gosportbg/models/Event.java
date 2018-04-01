package com.kamenov.martin.gosportbg.models;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Event implements Comparable {
    List<User> players;
    Admin admin;
    private Sport sport;
    private LocalDate date;
    private LocalTime time;
    private LocalDateTime dateTime;
    private String name;
    private String location;

    public Event(Sport sport, String name, int playerLimit, LocalDate date, LocalTime time, String location) {
        this.sport = sport;
        this.name = name;
        this.setPlayers(playerLimit);
        this.date = date;
        this.time = time;
        this.dateTime = LocalDateTime.of(date, time);
        this.location = location;
    }


    public Event(Sport sport, String name, LocalDate date, LocalTime time, String location) {
        this(sport, name, 0, date, time, location);
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

    public void setName(String var1) {
        this.name = var1;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        this.setDateTime(date, this.time);
    }

    public Sport getSport() {
        return sport;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
        this.setDateTime(this.date, time);
    }

    private void setDateTime(LocalDate date, LocalTime time) {
        this.dateTime = LocalDateTime.of(date, time);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return "Event{" +
                ", sport=" + sport +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime.format(formatter) +
                ", location='" + location + '\'' +
                '}';
    }

    public int compareTo(Object var1) {
        return this.getDateTime().isAfter(((Event) var1).getDateTime()) ? 1 : -1;
    }
}

