package com.coderbunker.kioskapp;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private DatabaseReference databaseRef;


    public DatabaseConnection() {
        databaseRef = FirebaseDatabase.getInstance().getReference("faceDetections");
    }

    public void addViewer(Viewer viewer) {
        String id = databaseRef.push().getKey();
        databaseRef.child(id).setValue(viewer);
    }

    public void readViewer(final OnViewersReceived onViewersReceived) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Viewer> viewers = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Viewer viewer = dsp.getValue(Viewer.class);
                    viewers.add((Viewer) viewer); //add result into array list
                }
                onViewersReceived.onViewersReceived(viewers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }

    interface OnViewersReceived {
        void onViewersReceived(List<Viewer> viewers);
    }
}