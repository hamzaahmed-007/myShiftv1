package com.hamza.firestonev1.models;

public class Shifts {


    String date;
    String id;
    String username;
    String name;
    String location;
    String ShiftID;


    public String getShiftID() {
        return ShiftID;
    }

    public void setShiftID(String shiftID) {
        ShiftID = shiftID;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Shifts(String id, String name, String location, String date, String ShiftID) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.ShiftID = ShiftID;
    }


    public Shifts()
    {


    }
}
