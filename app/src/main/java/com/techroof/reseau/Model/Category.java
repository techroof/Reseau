package com.techroof.reseau.Model;

public class Category {

    private String id, imgUrl,name;

    public Category(String name) {
        this.name = name;
    }

    public Category() {
    }

    public Category(String id, String imgUrl, String name) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
