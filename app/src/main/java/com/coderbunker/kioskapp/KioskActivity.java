package com.coderbunker.kioskapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coderbunker.kioskapp.facerecognition.CameraPreview;
import com.coderbunker.kioskapp.facerecognition.FaceDetectionListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;


public class KioskActivity extends Activity  {

    private final Context context = this;
    private WebView webView;
    private TextView faceCounterView;
    private static String url = "";

    private Dialog passwordDialog;

    private SharedPreferences prefs;

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private KioskWebViewClient webViewClient;
    private AutoWebViewReloader autoWebViewReloader;
    private StatusBarLocker statusBarLocker;

    @Override
    public void onBackPressed() {
        //on back pressed
    }

    @SuppressWarnings("deprecation")
    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        removeTitleBar();
        doNotLockScreen();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        statusBarLocker = new StatusBarLocker(this);
        statusBarLocker.lock();

        setContentView(R.layout.activity_kiosk);

        Configuration configuration = Configuration.loadFromPreferences(context);

        url = configuration.getUrl();
        String otp = configuration.getPassphrase();

        if (otp == null) {
            Intent intent = new Intent(KioskActivity.this, SettingsActivity.class);
            Toast.makeText(context, "Please setup first the One-Time-Passwords on your phone before you use the kiosk mode.", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }

        webView = findViewById(R.id.webview);
        faceCounterView = findViewById(R.id.face_counter);

        webViewClient = new KioskWebViewClient(this);
        webView.setWebViewClient(webViewClient);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCacheMaxSize(200 * 1024 * 1024);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.loadUrl(url);
        hideSystemUI(webView);

        Toast.makeText(this, "Loading " + url, Toast.LENGTH_SHORT).show();

        webView.post(new Runnable() {
            @Override
            public void run() {
                autoWebViewReloader = new AutoWebViewReloader(webView);
                autoWebViewReloader.register(KioskActivity.this);
            }
        });

        if (checkCameraHardware(this)) {

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    try {
                        mCamera.unlock();
                    } catch (Exception e) {
                        //do nothing
                    }
                    mCamera = getCameraInstance();
                    if (mCamera != null) {
                        FaceDetectionListener faceDetectionListener = new FaceDetectionListener();
                        faceDetectionListener.addObserver(new ViewerObserver(faceCounterView,webView));
                        mCamera.setFaceDetectionListener(faceDetectionListener);

                        mCameraPreview = new CameraPreview(context, mCamera);

                        FrameLayout preview = findViewById(R.id.camera_preview);
                        preview.addView(mCameraPreview);

                        mCamera.startPreview();
                        Toast.makeText(context, "Face recognition started", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Due a camera issue the face recognition can not be started.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(context, "Face recognition not active due denied permissions.", Toast.LENGTH_SHORT).show();
                }

            };

            TedPermission.with(context).setPermissionListener(permissionlistener).setPermissions(Manifest.permission.CAMERA).check();

            webView.setOnTouchListener(new View.OnTouchListener() {

                private long lastTouchTime;
                private int count;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                        long touchTime = System.currentTimeMillis();
                        if (touchTime - lastTouchTime >= 400) {
                            count = 0;
                        }
                        count++;
                        if (count >= 4) {
                            askPassword();
                            count = 0;
                        }
                        lastTouchTime = touchTime;
                    }
                    return false;
                }
            });

            passwordDialog = new PasswordDialog(this, new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(KioskActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void doNotLockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    private void removeTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    // This snippet hides the system bars.
    private void hideSystemUI(View view) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI(webView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return blockKeys(keyCode, event);
    }

    private boolean blockKeys(int keyCode, KeyEvent event) {
        return event.isSystem();
    }

    private void askPassword() {
        passwordDialog.show();
    }

    @SuppressWarnings("deprecation")
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        if (autoWebViewReloader != null) {
            autoWebViewReloader.deregister(this);
        }
        statusBarLocker.release();
        passwordDialog.dismiss();
        super.onDestroy();
    }
}
