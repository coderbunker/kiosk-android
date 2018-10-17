package com.coderbunker.kioskapp;

import android.support.annotation.NonNull;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class WebViewVideoReader {

    private String videoId = "video";
    private WebView webView;

    private String script = "" +
            "var video = document.getElementById(\"" + videoId + "\");" +
            "if(video === undefined || video === null) {0 + \";\" + 0 + \";\" + window.location;}else{video.currentTime + \";\" + video.duration + \";\" + window.location;}";

    public WebViewVideoReader(WebView webView) {
        this.webView = webView;
    }

    public void runWithCurrentTime(final VideoProgressObserver observer) {
        webView.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                System.out.println(value);
                String[] strings = splitValues(value);
                observer.onProgressRead(new VideoInfo(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]), strings[2]));
            }
        });
    }

    @NonNull
    private String[] splitValues(String value) {
        return removeSuperfluousChars(value).split(";", 3);
    }

    @NonNull
    private String removeSuperfluousChars(String value) {
        return value.substring(1, value.length() - 1);
    }

    public static class VideoInfo {
        private double currentTime;
        private double duration;
        private String videoSrc;

        public VideoInfo(double currentTime, double duration, String videoSrc) {
            this.currentTime = currentTime;
            this.duration = duration;
            this.videoSrc = videoSrc;
        }

        public double getCurrentTime() {
            return currentTime;
        }

        public double getDuration() {
            return duration;
        }

        public String getVideoSrc() {
            return videoSrc;
        }

        @Override
        public String toString() {
            return "VideoInfo{" +
                    "currentTime=" + currentTime +
                    ", duration=" + duration +
                    ", videoSrc='" + videoSrc + '\'' +
                    '}';
        }
    }

    interface VideoProgressObserver {
        void onProgressRead(VideoInfo video);
    }
}
