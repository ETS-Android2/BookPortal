package com.example.bookportal;

import android.app.Application;

import java.io.Serializable;

public class GlobalData extends Application {

    String collegePath;
    String combinationPath;
    String phone;
    String name;
    String sem;

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    String subject;

    String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




    public String getCollegePath() {
        return collegePath;
    }

    public void setCollegePath(String collegePath) {
        this.collegePath = collegePath;
    }

    public String getCombinationPath() {
        return combinationPath;
    }

    public void setCombinationPath(String combinationPath) {
        this.combinationPath = combinationPath;
    }
}
