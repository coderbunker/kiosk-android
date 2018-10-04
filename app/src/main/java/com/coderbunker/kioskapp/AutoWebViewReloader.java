package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.webkit.WebView;
import android.widget.Toast;

public class AutoWebViewReloader extends BroadcastReceiver {


    private WebView webView;
    private NoInternetConnectionPopup noInternetConnectionPopup;

    public AutoWebViewReloader(WebView webView) {
        this.webView = webView;
        noInternetConnectionPopup = new NoInternetConnectionPopup(webView);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
            webView.reload();
            noInternetConnectionPopup.dismiss();
        } else {
            noInternetConnectionPopup.show();
            turnOnWifi(context);
        }
    }

    private void turnOnWifi(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
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

    public void deregister(Activity activity) {
        activity.unregisterReceiver(this);
    }
}
