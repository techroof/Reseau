package com.techroof.reseau.Model;

public class Cart {

    private int ID;
    private String PID, NAME, PRICE, QTY, IMAGE, CATEGORY,CREATOR,STATUS;

    public Cart() {
    }

    public Cart(int ID, String PID, String NAME, String PRICE, String QTY, String IMAGE, String CATEGORY, String CREATOR, String STATUS) {
        this.ID = ID;
        this.PID = PID;
        this.NAME = NAME;
        this.PRICE = PRICE;
        this.QTY = QTY;
        this.IMAGE = IMAGE;
        this.CATEGORY = CATEGORY;
        this.CREATOR = CREATOR;
        this.STATUS = STATUS;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }
}
