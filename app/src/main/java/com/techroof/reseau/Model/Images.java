package com.techroof.reseau.Model;
public class Images {

    private String id;
    private String src,name,alt,sid;

    public Images() {
    }

    public Images(String id, String src, String name, String alt, String sid) {
        this.id = id;
        this.src = src;
        this.name = name;
        this.alt = alt;
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
