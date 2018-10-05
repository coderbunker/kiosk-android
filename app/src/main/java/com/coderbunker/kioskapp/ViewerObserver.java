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
    private boolean sameFace = false;
    private WebViewVideoReader webViewVideoReader;


    public ViewerObserver(TextView faceCounterView, WebView webView) {
        this.faceCounterView = faceCounterView;
        this.webView = webView;
        webViewVideoReader = new WebViewVideoReader(webView);
    }

    @Override
    public void update(Observable observable, Object args) {
        if (observable instanceof FaceDetectionListener) {
            Camera.Face[] faces = ((Camera.Face[]) args);
            final int faceCounter = faces.length;
            faceCounterView.setText("Current faces: " + faceCounter);

            if (faceCounter > 0) {
                if (!sameFace) {
                    webViewVideoReader.runWithCurrentTime(new WebViewVideoReader.VideoProgressObserver() {
                        @Override
                        public void onProgressRead(WebViewVideoReader.VideoInfo video) {
                            dbc.addViewer(faceCounter, video);
                        }
                    });
                    sameFace = true;
                } else {
                    sameFace = true;
                }
            } else {
                sameFace = false;
            }

        }
    }

}
