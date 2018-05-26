package com.jjoey.transportr.models;

public class SavedPlaces {

    private String placeType;
    private String fullAddress;

    public SavedPlaces() {
    }

    public SavedPlaces(String placeType, String fullAddress) {
        this.placeType = placeType;
        this.fullAddress = fullAddress;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

}
