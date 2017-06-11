package com.example.student.userphotograph.models;

import android.net.Uri;

public class Picture {

    private String title;
    private Uri imageUri;

    public Picture(String title, Uri imageUri) {
        this.title = title;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
