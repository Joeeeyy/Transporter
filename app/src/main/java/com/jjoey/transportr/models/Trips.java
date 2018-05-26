package com.jjoey.transportr.models;

public class Trips {

    private String mapsIconImg;
    private String startLocationTxt;
    private String dropLocationTxt;
    private String tripFare;
    private String tripDateTimeTxt;

    public Trips() {
    }

    public Trips(String mapsIconImg, String startLocationTxt, String dropLocationTxt, String tripFare, String tripDateTimeTxt) {
        this.mapsIconImg = mapsIconImg;
        this.startLocationTxt = startLocationTxt;
        this.dropLocationTxt = dropLocationTxt;
        this.tripFare = tripFare;
        this.tripDateTimeTxt = tripDateTimeTxt;
    }

    public String getMapsIconImg() {
        return mapsIconImg;
    }

    public void setMapsIconImg(String mapsIconImg) {
        this.mapsIconImg = mapsIconImg;
    }

    public String getStartLocationTxt() {
        return startLocationTxt;
    }

    public void setStartLocationTxt(String startLocationTxt) {
        this.startLocationTxt = startLocationTxt;
    }

    public String getDropLocationTxt() {
        return dropLocationTxt;
    }

    public void setDropLocationTxt(String dropLocationTxt) {
        this.dropLocationTxt = dropLocationTxt;
    }

    public String getTripFare() {
        return tripFare;
    }

    public void setTripFare(String tripFare) {
        this.tripFare = tripFare;
    }

    public String getTripDateTimeTxt() {
        return tripDateTimeTxt;
    }

    public void setTripDateTimeTxt(String tripDateTimeTxt) {
        this.tripDateTimeTxt = tripDateTimeTxt;
    }
}
