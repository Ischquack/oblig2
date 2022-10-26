package com.example.oblig2;

public class Appointment {
    private int id;


    private String title;
    private String date;
    private String time;
    private String place;
    private String msg;

    public Appointment(String title, String date, String time, String place, String msg) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.place = place;
        this.msg = msg;
    }

    public Appointment (){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
