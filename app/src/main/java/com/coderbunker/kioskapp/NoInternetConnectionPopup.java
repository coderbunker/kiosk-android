package com.coderbunker.kioskapp;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.PopupWindow;

public class NoInternetConnectionPopup extends PopupWindow {
    private View view;

    public NoInternetConnectionPopup(View view) {
        super(View.inflate(view.getContext(), R.layout.no_internet_connection_dialog, null), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.view = view;
    }

    public void show() {
        showAtLocation(view, Gravity.CENTER ,0,0);
    }
}
