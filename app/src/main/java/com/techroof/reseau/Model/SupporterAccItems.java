package com.techroof.reseau.Model;

public class SupporterAccItems {

    private int icon,forwardIcon;
    private String itemName;

    public SupporterAccItems() {
    }

    public SupporterAccItems(int icon, int forwardIcon, String itemName) {
        this.icon = icon;
        this.forwardIcon = forwardIcon;
        this.itemName = itemName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getForwardIcon() {
        return forwardIcon;
    }

    public void setForwardIcon(int forwardIcon) {
        this.forwardIcon = forwardIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
