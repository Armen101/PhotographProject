package com.example.student.userphotograph.models;

public class AboutModel {
    private String imgMember;
    private String tvMemberName;


    public AboutModel() {
    }

    public AboutModel(String imgMember, String tvMemberName) {
        this.imgMember = imgMember;
        this.tvMemberName = tvMemberName;
    }

    public String getImgMember() {
        return imgMember;
    }

    public void setImgMember(String imgMember) {
        this.imgMember = imgMember;
    }

    public String getTvMemberName() {
        return tvMemberName;
    }

    public void setTvMemberName(String tvMemberName) {
        this.tvMemberName = tvMemberName;
    }
}
