package com.example.bookportal;

import android.app.Application;

public class GlobalData extends Application {

    String collegePath;
    String combinationPath;
    String phone;
    String name;

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
