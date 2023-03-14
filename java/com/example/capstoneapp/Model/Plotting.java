package com.example.capstoneapp.Model;

public class Plotting {
    private int plotId;
    private int latitude;
    private int longitude;
    private String type;

    public Plotting(int plotId, int latitude, int longitude, String type) {
        this.plotId = plotId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public int getId() {
        return plotId;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setId(int plotId) {
        this.plotId= plotId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }
}

