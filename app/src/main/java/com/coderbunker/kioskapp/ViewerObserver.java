package com.coderbunker.kioskapp;

import android.hardware.Camera;
import android.webkit.WebView;
import android.widget.TextView;

import com.coderbunker.kioskapp.facerecognition.FaceDetectionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

public class ViewerObserver implements Observer {

    private DatabaseConnection dbc = new DatabaseConnection();
    private TextView faceCounterView;
    private WebViewVideoReader webViewVideoReader;
    private Queue<Viewer> viewers = new LinkedList<>();
    private int currentFaceCounter;
    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public ViewerObserver(TextView faceCounterView, WebView webView) {
        this.faceCounterView = faceCounterView;
        webViewVideoReader = new WebViewVideoReader(webView);
    }

    @Override
    public void update(Observable observable, Object args) {

        if (observable instanceof FaceDetectionListener) {
            Camera.Face[] faces = ((Camera.Face[]) args);
            final int faceCounter = faces.length;
            faceCounterView.setText("Current faces: " + faceCounter);

            if (faceCounter > currentFaceCounter) {
                webViewVideoReader.runWithCurrentTime(new WebViewVideoReader.VideoProgressObserver() {
                    @Override
                    public void onProgressRead(WebViewVideoReader.VideoInfo video) {
                        Viewer viewer = new Viewer();
                        viewer.setStartTime(video.getCurrentTime());
                        viewer.setVideoUrl(video.getVideoSrc());
                        String currentDateTime = formatter.format( new Date());
                        viewer.setDateTime(currentDateTime);
                        viewers.add(viewer);

                    }
                });
                currentFaceCounter = faceCounter;
            } else if (faceCounter < currentFaceCounter) {
                webViewVideoReader.runWithCurrentTime(new WebViewVideoReader.VideoProgressObserver() {
                    @Override
                    public void onProgressRead(WebViewVideoReader.VideoInfo video) {
                        Viewer viewer = viewers.element();
                        viewer.setEndTime(video.getCurrentTime());
                        if (viewer.getStartTime() + 5 <= viewer.getEndTime()) {
                            dbc.addViewer(viewer);
                        }
                        viewers.remove(viewer);
                        currentFaceCounter = faceCounter;
                    }
                });
            }


        }

    }
}


