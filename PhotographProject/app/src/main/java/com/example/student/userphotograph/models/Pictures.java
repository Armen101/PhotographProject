package com.example.student.userphotograph.models;

public class Pictures {

    private String title;
    private String imageUri;

    public Pictures() {
    }

    public Pictures(String title, String imageUri) {
        this.title = title;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
