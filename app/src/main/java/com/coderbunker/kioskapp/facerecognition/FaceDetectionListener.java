package com.coderbunker.kioskapp.facerecognition;

import android.hardware.Camera;

import java.util.Observable;

public class FaceDetectionListener extends Observable implements Camera.FaceDetectionListener {

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        setChanged();
        notifyObservers(faces);
    }

}
