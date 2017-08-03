package com.example.student.userphotograph.models;

public class RatingModel {
    private String name;
    private String avatarUri;
    private long rating;
    private String uid;

    public RatingModel() {

    }

    public RatingModel(String name, String avatarUri, long rating, String uid) {
        this.name = name;
        this.avatarUri = avatarUri;
        this.rating = rating;
        this.uid = uid;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getAvatarUri() {
        return avatarUri;
    }
}
