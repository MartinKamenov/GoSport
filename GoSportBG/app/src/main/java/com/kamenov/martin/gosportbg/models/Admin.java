package com.kamenov.martin.gosportbg.models;


import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Admin extends User {

    private List<Event> events = new ArrayList<>();


    public Admin(String username, String address, String email, String name, String password) {
        super(username, address, email, name, password);
    }/*



    public void createEvent(Sport sport, String name, int playerLimit, int year, int month, int day, int hour, int minute, String location) {
        Event event = new Event(sport, name, playerLimit, LocalDate.of(year, month, day), LocalTime.of(hour, minute), location);
        event.admin = this;
        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid Date");
        }
        events.add(event);
    }

    public void createEvent(Sport sport, String name, int year, int month, int day, int hour, int minute, String location) {
        Event event = new Event(sport, name, LocalDate.of(year, month, day), LocalTime.of(hour, minute), location);
        event.admin = this;
        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid Date");
        }
        events.add(event);

    }

    public void addPlayer(String name, User user) {
        Event event = events.stream().filter(e -> e.getName().equals(name)).findAny().orElse(null);
        try {
            if (event.players instanceof ArrayList) {
                ((ArrayList) event.players).add(user);
                return;
            }
            int idx = event.players.indexOf(null);
            event.players.set(idx, user);

        } catch (NullPointerException ex) {
            System.out.println("No match found!");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Player full package");
        }
    }

    public void banPlayer(String name, User user){
        Event event = events.stream().filter(e -> e.getName().equals(name)).findAny().orElse(null);
        try {
            if (event.players instanceof ArrayList) {
                ((ArrayList) event.players).remove(user);
                return;
            }
            int idx = event.players.indexOf(user);
            event.players.set(idx, null);

        } catch (NullPointerException ex) {
            System.out.println("No match found!");
        }
    }

    public void changeDate(String name, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Event event = events.stream().filter(e -> e.getName().equals(name)).findAny().orElse(null);
        if (event.getDate().isAfter(date)) {
            throw new IllegalArgumentException("Invalid Date");

        }
        event.setDate(date);

    }

    public void changeTime(String name, int hour, int minute) {
        LocalTime time = LocalTime.of(hour, minute);
        Event event = events.stream().filter(e -> e.getName().equals(name)).findAny().orElse(null);
        if (event.getTime().isAfter(time)) {
            throw new IllegalArgumentException("Invalid date");
        }
        event.setTime(time);
    }

    public void showUpcomingEvents() {
        Collections.sort(events);
        events.forEach(e -> {
            System.out.println(e);
        });

    }

    public void showEventsByType(Sport... sports) {
        Collections.sort(events);
        for (Sport sport:sports) {
            Predicate<Sport> p = s -> s.getValue() == sport.getValue();
            events.forEach(event -> {
                if (p.test(event.getSport())) System.out.println(event.toString());
            });
        }

    }*/


}

