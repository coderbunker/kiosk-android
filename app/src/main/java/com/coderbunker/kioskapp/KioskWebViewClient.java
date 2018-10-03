package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.IntentFilter;
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

    private boolean locked = false;
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
                locked = true;
            }
        };

        Timer timerLock = new Timer(true);
        timerLock.schedule(lock, 5000);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        System.out.println("Test: " + request.getUrl().toString());

        if (enableCaching) {
            if (request.getUrl().toString().contains(".mp4") || request.getUrl().toString().contains(".wav")) {
                String[] url_parts = request.getUrl().toString().split("/");
                String file_name = url_parts[url_parts.length - 1];

                if (SaveAndLoad.readFromFile(file_name, activity).equals("")) {
                    URLRequest.startDownload(request.getUrl().toString(), file_name);
                }
                return new WebResourceResponse(SaveAndLoad.getMimeType(request.getUrl().toString()), "UTF-8", SaveAndLoad.readFromFileAndReturnInputStream(file_name, activity));

            }
        }
        return super.shouldInterceptRequest(view, request);
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

    public boolean isLocked() {
        return locked;
    }

}
