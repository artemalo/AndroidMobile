package com.example.walkofinterest.models.adapters;

import android.graphics.drawable.Drawable;

import com.yandex.mapkit.directions.driving.DrivingRoute;

public class RouteInfoModel {
    /*DrivingRoute route;*/
    String index;
    String time;
    String countSteps;
    Drawable color;

    public RouteInfoModel(/*DrivingRoute route, */String index, String time, String countSteps, Drawable color) {
        /*this.route = route;*/
        this.index = index;
        this.time = time;
        this.countSteps = countSteps;
        this.color = color;
    }

    public RouteInfoModel(/*DrivingRoute route,*/ int index, int time, int countSteps, Drawable color) {
       /* this.route = route;*/
        this.index = String.valueOf(index);
        this.time = String.valueOf(time);
        this.countSteps = String.valueOf(countSteps);
        this.color = color;
    }

   /* public DrivingRoute getRoute() {
        return route;
    }*/

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
