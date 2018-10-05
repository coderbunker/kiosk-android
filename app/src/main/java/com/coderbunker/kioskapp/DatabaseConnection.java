package com.coderbunker.kioskapp;

import android.webkit.WebView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConnection {
    private DatabaseReference databaseRef;

    public DatabaseConnection() {
        databaseRef = FirebaseDatabase.getInstance().getReference("faceDetections");
    }

    public void addViewer(Viewer viewer) {
        String id = databaseRef.push().getKey();
        databaseRef.child(id).setValue(viewer);
    }
}
