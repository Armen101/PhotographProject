package com.example.student.userphotograph.models;

public class Pictures {

    private String title;
    private String imageUri;
    private String imageName;
    private int rating;

    public Pictures() {
    }

    public Pictures(String title, String imageUri, String imageName, int rating) {
        this.title = title;
        this.imageUri = imageUri;
        this.imageName = imageName;
        this.rating = rating;
    }

    public Pictures(String title, String imageUri, String imageName) {
        this.title = title;
        this.imageUri = imageUri;
        this.imageName = imageName;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImageName() {return imageName;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

}
