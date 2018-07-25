package com.coderbunker.kioskapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class KioskActivity extends Activity {

    private final Context context = this;
    private  WebView webView;
    private static String password = "1234";
    private static String URL = "https://naibaben.github.io/";

    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_HOME, KeyEvent.KEYCODE_POWER, KeyEvent.KEYCODE_APP_SWITCH));

    private boolean dialogPrompted = false;
    private boolean locked = false;
    private Dialog dialog;

    private Button b1, b2, b3, b4;
    private Button n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    private ArrayList<Button> numbers;

    private int cptPwd;

    private Timer timerLock, timerNav;

    @Override
    public void onBackPressed() {
     //Do nothing...
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_kiosk);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Get the webView and load the URL
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new KioskWebviewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);

        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSystemUI();

                if(!dialogPrompted && locked) {
                    askPassword();
                    return true;
                } else
                    return false;


            }
        });

        TimerTask lock = new TimerTask() {
            @Override
            public void run() {
                locked = true;
            }
        };

        timerLock = new Timer(true);
        timerLock.schedule(lock, 5000);

        numbers = new ArrayList<Button>();

    }


    // This snippet hides the system bars.
    private void hideSystemUI() {

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        webView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet hides the system bars.
    private void hideNavBar(View view) {

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
        if (hasFocus) {
            hideSystemUI();
        }else{
            hideSystemUI();
        }
    }


    public static void setPassword(String newPwd)
    {
        password = newPwd;
    }

    public static void setURL(String newURL)
    {
        URL = newURL;
    }

    public void enterNumber(String number) {

        switch (cptPwd) {
            case 0:
                b1.setText(number);
                break;
            case 1:
                b2.setText(number);
                break;
            case 2:
                b3.setText(number);
                break;
            case 3:
                b4.setText(number);
                break;
        }

        if (cptPwd == 3) {
            cptPwd = 0;
            checkPwd();
        } else
            cptPwd++;

    }

    private void checkPwd() {
        String pwd = b1.getText().toString() + b2.getText().toString() + b3.getText().toString() + b4.getText().toString();
        if (password.equals(pwd)) {
            finish();

        } else {
            dialog.dismiss();
            dialogPrompted = false;
        }

        cptPwd = 0;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(blockedKeys.contains(event.getKeyCode())){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void askPassword()
    {
        dialogPrompted = true;

        dialog = new Dialog(webView.getContext());
        View v = dialog.getWindow().getDecorView();
        hideNavBar(v);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogPrompted = false;
                cptPwd = 0;
            }
        });

        dialog.setContentView(R.layout.password_dialog);

        b1 = dialog.findViewById(R.id.b1);
        b2 = dialog.findViewById(R.id.b2);
        b3 = dialog.findViewById(R.id.b3);
        b4 = dialog.findViewById(R.id.b4);

        n0 = dialog.findViewById(R.id.number0);
        n1 = dialog.findViewById(R.id.number1);
        n2 = dialog.findViewById(R.id.number2);
        n3 = dialog.findViewById(R.id.number3);
        n4 = dialog.findViewById(R.id.number4);
        n5 = dialog.findViewById(R.id.number5);
        n6 = dialog.findViewById(R.id.number6);
        n7 = dialog.findViewById(R.id.number7);
        n8 = dialog.findViewById(R.id.number8);
        n9 = dialog.findViewById(R.id.number9);

        numbers.add(n0);
        numbers.add(n1);
        numbers.add(n2);
        numbers.add(n3);
        numbers.add(n4);
        numbers.add(n5);
        numbers.add(n6);
        numbers.add(n7);
        numbers.add(n8);
        numbers.add(n9);

        for (int i = 0; i < numbers.size(); i++) {
            numbers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = view.getTag().toString();
                    enterNumber(number);
                }
            });
        }

        dialog.show();
    }

}
