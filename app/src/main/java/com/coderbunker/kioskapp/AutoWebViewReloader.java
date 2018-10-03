package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;
import android.widget.Toast;

public class AutoWebViewReloader extends BroadcastReceiver {


    private WebView webView;

    public AutoWebViewReloader(WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
            webView.reload();
        } else {
            Toast.makeText(context, "Lost connection", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

    public void register(Activity activity) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        activity.registerReceiver(this, filter);
    }
}
