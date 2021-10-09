package com.example.artixspy;

public class callLogModel {
    String number,name,type,date,time,duration;

    public callLogModel(String number, String name, String type, String date, String time, String duration) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }
}
