package com.example.bookportal;

import android.app.Application;

public class GlobalData extends Application {

    String collegePath;
    String combinationPath;


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
