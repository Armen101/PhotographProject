package com.example.student.userphotograph.models;

import com.example.student.userphotograph.fragments.PostFragment;

import java.util.Random;

public class PostModel {

    private long date;
    private String imageUrl;
    private long likes;
    private String title;
    private String userName;
    private String uid;
    private String userId;

    public PostModel() {
    }

    public String getUserId() {
        return userId;
    }

    public PostModel(String imageUrl, String title, String userName, String userId) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.userName = userName;
        this.userId = userId;
        this.likes = 0;
        this.date = System.currentTimeMillis();
        this.uid = getRandomPostId();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String getRandomPostId() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString() + System.currentTimeMillis();
    }

}
