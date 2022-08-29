package com.techroof.reseau.Model;

public class CreatorHomeItems {

    private int icon,forwardIcon;
    private String itemName,itemDetail;

    public CreatorHomeItems() {
    }

    public CreatorHomeItems(int icon, int forwardIcon, String itemName, String itemDetail) {
        this.icon = icon;
        this.forwardIcon = forwardIcon;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
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

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }
}
