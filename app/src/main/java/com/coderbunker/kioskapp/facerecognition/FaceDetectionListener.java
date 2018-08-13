package com.coderbunker.kioskapp.facerecognition;

import android.hardware.Camera;

import java.util.Observable;

public class FaceDetectionListener extends Observable implements Camera.FaceDetectionListener {

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length > 0) {
            for (Camera.Face face : faces) {
                try {
                    /*System.out.println("--------------------------");
                    System.out.println(face.score);*/
                    setChanged();
                    notifyObservers(face);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
