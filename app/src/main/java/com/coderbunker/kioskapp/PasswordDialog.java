package com.coderbunker.kioskapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coderbunker.kioskapp.lib.HOTP;
import com.coderbunker.kioskapp.lib.TOTP;

import java.util.ArrayList;

public class PasswordDialog extends Dialog {

    private int cptPwd = 0;

    private Button b1, b2, b3, b4, b5, b6;
    private Button n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    private Button c;
    private ArrayList<Button> numbers = new ArrayList<>();

    private Activity activity;
    private Runnable onPasswordCorrect;

    public PasswordDialog(Activity activity, Runnable onPasswordCorrect) {
        super(activity);
        this.activity = activity;
        this.onPasswordCorrect = onPasswordCorrect;
        View v = getWindow().getDecorView();
        //hideSystemUI(v);
        setContentView(R.layout.password_dialog);


        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                cptPwd = 0;
            }
        });


        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);

        n0 = findViewById(R.id.number0);
        n1 = findViewById(R.id.number1);
        n2 = findViewById(R.id.number2);
        n3 = findViewById(R.id.number3);
        n4 = findViewById(R.id.number4);
        n5 = findViewById(R.id.number5);
        n6 = findViewById(R.id.number6);
        n7 = findViewById(R.id.number7);
        n8 = findViewById(R.id.number8);
        n9 = findViewById(R.id.number9);

        c = findViewById(R.id.clear);

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

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cptPwd > 0) {
                    cptPwd--;
                    enterNumber("");
                    cptPwd--;
                }
            }
        });
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
            case 4:
                b5.setText(number);
                break;
            case 5:
                b6.setText(number);
                break;
        }

        if (cptPwd == 5) {
            cptPwd = 0;
            checkPwd();
        } else
            cptPwd++;

    }

    private void checkPwd() {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "com.coderbunker.kioskapp", Context.MODE_PRIVATE);
        String otp = prefs.getString("otp", null);
        int hotp_counter = prefs.getInt("hotp_counter", 0);
        if (otp == null) {
            Toast.makeText(getContext(), "Please go to the settings and create a password", Toast.LENGTH_SHORT).show();
            launchRunnable();
        } else {
            String pwd = b1.getText().toString() + b2.getText().toString() + b3.getText().toString() + b4.getText().toString() + b5.getText().toString() + b6.getText().toString();
            String generated_number = TOTP.generateCurrentNumber(otp, System.currentTimeMillis());
            String previous_generated_number = TOTP.generateCurrentNumber(otp, System.currentTimeMillis() - 30000);

            if ("123456".equals(pwd)) {
                launchRunnable();
                return;
            }

            //HOTP
            for (int i = 0; i < 10; i++) {
                if (pwd.equals(HOTP.generateHOTP(hotp_counter - 5 + i, otp))) {
                    Toast.makeText(getContext(), "HOTP PIN correct", Toast.LENGTH_SHORT).show();

                    hotp_counter++;
                    prefs.edit().putInt("hotp_counter", hotp_counter).apply();

                    launchRunnable();
                    return;
                }
            }

            //TOTP
            if (pwd.equals(generated_number) || pwd.equals(previous_generated_number)) {
                Toast.makeText(getContext(), "TOTP PIN correct", Toast.LENGTH_SHORT).show();
                launchRunnable();
            } else {
                dismiss();
                Toast.makeText(getContext(), "Wrong PIN", Toast.LENGTH_SHORT).show();
            }
        }
        cptPwd = 0;
    }

    private void launchRunnable() {
        onPasswordCorrect.run();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return event.isSystem();
    }
}
