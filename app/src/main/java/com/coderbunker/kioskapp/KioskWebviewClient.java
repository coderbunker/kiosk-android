package com.coderbunker.kioskapp;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class KioskWebviewClient extends WebViewClient {

    private String URL = "https://naibaben.github.io/";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if(url.contains(url)) {
            view.loadUrl(url);
        }
        return true;

    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); //Ignore SSL certificate error
    }
}
