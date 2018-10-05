package com.coderbunker.kioskapp;

import android.webkit.WebView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConnection {
    private DatabaseReference databaseRef;

    public DatabaseConnection() {
        databaseRef = FirebaseDatabase.getInstance().getReference("faceDetections");
    }

    public void addViewer(int faceCounter) {
        String id = databaseRef.push().getKey();
        Viewer viewer = new Viewer("TestWert", "TestWert","TestURL", faceCounter);
        databaseRef.child(id).setValue(viewer);
    }
}
