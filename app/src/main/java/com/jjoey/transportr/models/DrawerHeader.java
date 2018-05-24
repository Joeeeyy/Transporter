package com.jjoey.transportr.models;

/**
 * Created by JosephJoey on 5/24/2018.
 */

public class DrawerHeader {

    public String profileIcon;
    public String profileName;
    public String phoneNum;

    public DrawerHeader() {
    }

    public DrawerHeader(String profileIcon, String profileName, String phoneNum) {
        this.profileIcon = profileIcon;
        this.profileName = profileName;
        this.phoneNum = phoneNum;
    }

    public String getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(String profileIcon) {
        this.profileIcon = profileIcon;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
