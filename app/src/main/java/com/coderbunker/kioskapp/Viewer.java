package com.coderbunker.kioskapp;

public class Viewer {
    private double startTime;
    private double endTime;
    private String videoUrl;

    public Viewer() {
        //Default constructor for Firebase
    }


    public double getStartTime() {
        return startTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public double getEndTime() {
        return endTime;
    }


    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }


}
