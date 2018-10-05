package com.coderbunker.kioskapp;

import android.hardware.Camera;
import android.webkit.WebView;
import android.widget.TextView;

import com.coderbunker.kioskapp.facerecognition.FaceDetectionListener;

import java.util.Observable;
import java.util.Observer;

public class ViewerObserver implements Observer {

    private DatabaseConnection dbc = new DatabaseConnection();
    private TextView faceCounterView;
    private WebView webView;
    private Camera.Face lastFace;
    private boolean sameFace=false;


    public ViewerObserver(TextView faceCounterView, WebView webView) {
        this.faceCounterView = faceCounterView;
        this.webView = webView;
    }

    @Override
    public void update(Observable observable, Object args) {
        if (observable instanceof FaceDetectionListener) {
            Camera.Face[] faces = ((Camera.Face[]) args);
            int faceCounter = faces.length;
            faceCounterView.setText("Current faces: " + faceCounter);

            if (faceCounter > 0) {
                if (!sameFace) {
                    dbc.addViewer(faceCounter);
                    sameFace = true;
                }else{
                    sameFace=true;
                }
            }else {
                sameFace=false;
            }

        }
    }

}
