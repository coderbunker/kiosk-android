package com.coderbunker.kioskapp;

public class Viewer {
    private String startDate;
    private String endDate;
    private int faceCounter;

    public Viewer() {
        //Default constructor
    }

    public Viewer(String startDate, String endDate, int faceCounter) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.faceCounter = faceCounter;
    }

    public String getStartDate() {
        return startDate;
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
