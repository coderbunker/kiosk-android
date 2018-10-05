package com.coderbunker.kioskapp;

public class Viewer {
    private String startTime;
    private String endTime;
    private int faceCounter;
    private String videoUrl;

    public Viewer() {
        //Default constructor for Firebase
    }

    public Viewer(String startTime, String endTime, String videoUrl, int faceCounter) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.faceCounter = faceCounter;
        this.videoUrl = videoUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getFaceCounter() {
        return faceCounter;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setFaceCounter(int faceCounter) {
        this.faceCounter = faceCounter;
    }

}
