package com.jjoey.transportr.models;

/**
 * Created by JosephJoey on 5/24/2018.
 */

public class DrawerItems {

    public int iconImg;
    public String itemTitle;

    public DrawerItems() {
    }

    public DrawerItems(int iconImg, String itemTitle) {
        this.iconImg = iconImg;
        this.itemTitle = itemTitle;
    }

    public int getIconImg() {
        return iconImg;
    }

    public void setIconImg(int iconImg) {
        this.iconImg = iconImg;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
