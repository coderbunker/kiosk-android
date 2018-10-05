package com.coderbunker.kioskapp;

import android.webkit.WebView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConnection {
    private DatabaseReference databaseRef;

    public DatabaseConnection() {
        databaseRef = FirebaseDatabase.getInstance().getReference("faceDetections");
    }

    public void addViewer(int faceCounter, WebViewVideoReader.VideoInfo video) {
        String id = databaseRef.push().getKey();
        Viewer viewer = new Viewer(String.valueOf(video.getCurrentTime()), "TestWert", video.getVideoSrc(), faceCounter);
        databaseRef.child(id).setValue(viewer);
    }
}
