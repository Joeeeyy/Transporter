package com.jjoey.transportr.models;

import java.util.List;

public class ClientUser {

    public String fullName;
    public String emailAddr;
    public String imgURL;
    public String phoneNumber;
    public PaymentOptions paymentOptions;
    public List<Trips> numTrips;

    public ClientUser() {
    }

    public ClientUser(String fullName, String emailAddr, String imgURL, String phoneNumber, PaymentOptions paymentOptions, List<Trips> numTrips) {
        this.fullName = fullName;
        this.emailAddr = emailAddr;
        this.imgURL = imgURL;
        this.phoneNumber = phoneNumber;
        this.paymentOptions = paymentOptions;
        this.numTrips = numTrips;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PaymentOptions getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(PaymentOptions paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<Trips> getNumTrips() {
        return numTrips;
    }

    public void setNumTrips(List<Trips> numTrips) {
        this.numTrips = numTrips;
    }
}
