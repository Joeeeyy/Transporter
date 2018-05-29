package com.jjoey.transportr.models;

public class Vehicle {

    public String vehicleIcon;
    public String vehicleType;
    public String estimatedTime;

    public Vehicle() {
    }

    public Vehicle(String vehicleIcon, String vehicleType, String estimatedTime) {
        this.vehicleIcon = vehicleIcon;
        this.vehicleType = vehicleType;
        this.estimatedTime = estimatedTime;
    }

    public String getVehicleIcon() {
        return vehicleIcon;
    }

    public void setVehicleIcon(String vehicleIcon) {
        this.vehicleIcon = vehicleIcon;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
