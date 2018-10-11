package com.coderbunker.kioskapp;

public class Viewer {
    private double startTime;
    private double endTime;
    private String videoUrl;
    private String dateTime;

    public Viewer() {
        //Default constructor for Firebase
    }

    public String getDateTime() { return dateTime; }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public double getStartTime() {
        return startTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }


}
