package com.coderbunker.kioskapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

public class StatusBarLocker {


    private Context context;
    private CustomViewGroup view;

    public StatusBarLocker(Context context) {
        this.context = context;
    }

    public void lock() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)) {
            WindowManager manager = getWindowManager();

            WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY; // fix
            } else {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            }
            localLayoutParams.gravity = Gravity.TOP;
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

            localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            int result = 0;
            if (resId > 0) {
                result = context.getResources().getDimensionPixelSize(resId);
            } else {
                // Use Fallback size:
                result = 60; // 60px Fallback
            }

            localLayoutParams.height = result;
            localLayoutParams.format = PixelFormat.TRANSPARENT;

            view = new CustomViewGroup(context);
            manager.addView(view, localLayoutParams);
        }
    }

    private WindowManager getWindowManager() {
        return ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
    }

    public void release() {
        if (view != null) {
            getWindowManager().removeView(view);
        }
    }

    public static void askPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                context.startActivity(myIntent);
            }
        }
    }

    private static class CustomViewGroup extends ViewGroup {
        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }
}
