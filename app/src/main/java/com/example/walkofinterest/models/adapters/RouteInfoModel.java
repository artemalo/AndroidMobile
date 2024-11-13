package com.example.walkofinterest.models.adapters;

public class RouteInfoModel {
    String index;
    String time;
    String countSteps;

    public RouteInfoModel(String index, String time, String countSteps) {
        this.index = index;
        this.time = time;
        this.countSteps = countSteps;
    }

    public RouteInfoModel(int index, int time, int countSteps) {
        this.index = String.valueOf(index);
        this.time = String.valueOf(time);
        this.countSteps = String.valueOf(countSteps);
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
}
