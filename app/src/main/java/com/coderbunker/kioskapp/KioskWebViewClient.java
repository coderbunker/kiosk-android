package com.coderbunker.kioskapp;

import android.app.Activity;
import android.net.http.SslError;
import android.support.annotation.Nullable;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.coderbunker.kioskapp.lib.SaveAndLoad;
import com.coderbunker.kioskapp.lib.URLRequest;

import java.util.Timer;
import java.util.TimerTask;

class KioskWebViewClient extends WebViewClient {

    private boolean enableCaching = false;
    private Activity activity;

    public KioskWebViewClient(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        TimerTask lock = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(view.getContext(), "Kioskmode locked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        Timer timerLock = new Timer(true);
        timerLock.schedule(lock, 5000);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        System.out.println("Test: " + url);

        if (enableCaching) {
            if (url.contains(".mp4") || url.contains(".wav")) {
                String[] url_parts = url.split("/");
                String file_name = url_parts[url_parts.length - 1];

                if (SaveAndLoad.readFromFile(file_name, activity).equals("")) {
                    URLRequest.startDownload(url, file_name);
                }
                return new WebResourceResponse(SaveAndLoad.getMimeType(url), "UTF-8", SaveAndLoad.readFromFileAndReturnInputStream(file_name, activity));

            }
        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); //Ignore SSL certificate error
    }
}
