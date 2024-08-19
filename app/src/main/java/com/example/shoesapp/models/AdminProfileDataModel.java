package com.example.shoesapp.models;

public class AdminProfileDataModel {

    String userName, email;

    public AdminProfileDataModel() {
    }

    public AdminProfileDataModel(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
