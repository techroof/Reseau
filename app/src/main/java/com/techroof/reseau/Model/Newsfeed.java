package com.techroof.reseau.Model;

import java.util.List;

public class Newsfeed {

    String newsfeedId,creatorId,newsfeedText,newsfeedImg,creatorName,creatorImg;
    long timestamp;
    List<String> supports;

    public Newsfeed() {
    }

    public Newsfeed(String newsfeedId, String creatorId, String newsfeedText, String newsfeedImg, String creatorName, String creatorImg, long timestamp, List<String> supports) {
        this.newsfeedId = newsfeedId;
        this.creatorId = creatorId;
        this.newsfeedText = newsfeedText;
        this.newsfeedImg = newsfeedImg;
        this.creatorName = creatorName;
        this.creatorImg = creatorImg;
        this.timestamp = timestamp;
        this.supports = supports;
    }

    public List<String> getSupports() {
        return supports;
    }

    public void setSupports(List<String> supports) {
        this.supports = supports;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNewsfeedId() {
        return newsfeedId;
    }

    public void setNewsfeedId(String newsfeedId) {
        this.newsfeedId = newsfeedId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getNewsfeedText() {
        return newsfeedText;
    }

    public void setNewsfeedText(String newsfeedText) {
        this.newsfeedText = newsfeedText;
    }

    public String getNewsfeedImg() {
        return newsfeedImg;
    }

    public void setNewsfeedImg(String newsfeedImg) {
        this.newsfeedImg = newsfeedImg;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorImg() {
        return creatorImg;
    }

    public void setCreatorImg(String creatorImg) {
        this.creatorImg = creatorImg;
    }
}
