package com.example.walkofinterest.models.adapters;

import android.graphics.drawable.Drawable;

public class RouteInfoModel {
    String index;
    String time;
    String countSteps;
    Drawable color;

    public RouteInfoModel(String index, String time, String countSteps, Drawable color) {
        this.index = index;
        this.time = time;
        this.countSteps = countSteps;
        this.color = color;
    }

    public RouteInfoModel(int index, int time, int countSteps, Drawable color) {
        this.index = String.valueOf(index);
        this.time = String.valueOf(time);
        this.countSteps = String.valueOf(countSteps);
        this.color = color;
    }

    public String getIndex() {
        return index;
    }

    public String getTime(){
        return time;
    }

    public String getCountSteps(){
        return countSteps;
    }

    public Drawable getColor(){
        return color;
    }
}
