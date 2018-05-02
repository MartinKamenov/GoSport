package com.kamenov.martin.gosportbg.date;

/**
 * Created by Martin on 29.4.2018 г..
 */

public class DateTime {
    private int minutes;
    private int hour;
    private int day;
    private int month;
    private int year;

    public DateTime(int year, int month, int day, int hour, int minutes) {
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinutes(minutes);
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
