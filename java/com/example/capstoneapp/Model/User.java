package com.example.capstoneapp.Model;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;


public class User {
    private String userID;
    private String email;
    private String userPassword;
    private String date;
    private String token;

    public User(String userID, String email, String password, String token){
        this.userID = userID;
        this.email = email;
        this.userPassword = password;
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", password='" + userPassword + '\'' +
                ", date='" + date + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return userPassword;
    }

    public void setPassword(String password) {
        this.userPassword = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
