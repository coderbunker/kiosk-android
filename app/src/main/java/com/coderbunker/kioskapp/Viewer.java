package com.coderbunker.kioskapp;

import java.net.URL;

public class Viewer {
    private String startDate;
    private String endDate;
    private int faceCounter;
    private String videoUrl;

    public Viewer() {
        //Default constructor
    }

    public Viewer(String startDate, String endDate,String videoUrl, int faceCounter) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.faceCounter = faceCounter;
        this.videoUrl = videoUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getFaceCounter() {
        return faceCounter;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setFaceCounter(int faceCounter) {
        this.faceCounter = faceCounter;
    }

}
