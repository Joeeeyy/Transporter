package com.jjoey.transportr.models;

public class PaymentOptions {

    public int paymentIcon;
    public String paymentType;
    public String otherDetails;
    public boolean isCard;
    public boolean isProvider;
    public boolean isCash;

    public PaymentOptions() {
    }

    public PaymentOptions(int paymentIcon, String paymentType, String otherDetails, boolean isCard, boolean isProvider, boolean isCash) {
        this.paymentIcon = paymentIcon;
        this.paymentType = paymentType;
        this.otherDetails = otherDetails;
        this.isCard = isCard;
        this.isProvider = isProvider;
        this.isCash = isCash;
    }

    public int getPaymentIcon() {
        return paymentIcon;
    }

    public void setPaymentIcon(int paymentIcon) {
        this.paymentIcon = paymentIcon;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean card) {
        isCard = card;
    }

    public boolean isProvider() {
        return isProvider;
    }

    public void setProvider(boolean provider) {
        isProvider = provider;
    }

    public boolean isCash() {
        return isCash;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }
}
