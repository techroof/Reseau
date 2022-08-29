package com.techroof.reseau.Model;

import java.util.List;

public class Orders {

    private String orderId;
    private String quantity;
    private String supporterOrderTotal;
    private String creatorOrderTotal;
    private String creator;
    private String supporter;
    private String time;
    private String date;
    private String image;
    private String status;
    private List<Products> products;

    public Orders() {
    }

    public Orders(String orderId, String quantity, String supporterOrderTotal, String creatorOrderTotal, String creator, String supporter, String time, String date, String image, String status, List<Products> products) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.supporterOrderTotal = supporterOrderTotal;
        this.creatorOrderTotal = creatorOrderTotal;
        this.creator = creator;
        this.supporter = supporter;
        this.time = time;
        this.date = date;
        this.image = image;
        this.status = status;
        this.products = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSupporterOrderTotal() {
        return supporterOrderTotal;
    }

    public void setSupporterOrderTotal(String supporterOrderTotal) {
        this.supporterOrderTotal = supporterOrderTotal;
    }

    public String getCreatorOrderTotal() {
        return creatorOrderTotal;
    }

    public void setCreatorOrderTotal(String creatorOrderTotal) {
        this.creatorOrderTotal = creatorOrderTotal;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSupporter() {
        return supporter;
    }

    public void setSupporter(String supporter) {
        this.supporter = supporter;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
