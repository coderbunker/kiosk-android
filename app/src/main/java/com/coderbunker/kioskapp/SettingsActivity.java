package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderbunker.kioskapp.lib.Base32;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static android.widget.Toast.LENGTH_LONG;

public class SettingsActivity extends Activity {

    private Context context = this;
    private EditText editURL;
    private ImageView imgQRCode, imgQRCodeHOTP;
    private Button btnSave;

    private SharedPreferences prefs;

    private String otp_uri, hotp_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = this.getSharedPreferences(
                "com.coderbunker.kioskapp", Context.MODE_PRIVATE);

        imgQRCode = findViewById(R.id.imgQRCode);
        imgQRCodeHOTP = findViewById(R.id.imgQRCodeHOTP);
        editURL = findViewById(R.id.editText_URL);
        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean changed = false;
                String url = editURL.getText().toString();

                if (url != "" && URLUtil.isValidUrl(url)) {
                    prefs.edit().putString("url", url).apply();
                    changed = true;
                }

                if (changed)
                    Toast.makeText(context, "Changes saved!", LENGTH_LONG).show();
            }
        });

        String otp = prefs.getString("otp", null);
        String url = prefs.getString("url", "https://naibaben.github.io/");
        int hotp_counter = prefs.getInt("hotp_counter", 0);

        editURL.setText(url);

        if (otp == null) {

            byte key_1 = (byte) Math.floor(Math.random() * 10);
            byte key_2 = (byte) Math.floor(Math.random() * 10);
            byte key_3 = (byte) Math.floor(Math.random() * 10);
            byte key_4 = (byte) Math.floor(Math.random() * 10);
            byte key_5 = (byte) Math.floor(Math.random() * 10);
            byte key_6 = (byte) Math.floor(Math.random() * 10);

            byte[] key = {key_1, key_2, key_3, key_4, key_5, key_6, (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF};

            otp = Base32.encode(key);

            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("otp", otp);
            editor.apply();
        }

        otp_uri = "otpauth://totp/Time%20based?secret=" + otp + "&issuer=Kiosk%20Coderbunker";
        hotp_uri = "otpauth://hotp/Hash%20based?secret=" + otp + "&issuer=Kiosk%20Coderbunker&counter=" + hotp_counter + "&algorithm=SHA1";

        generateQRCodeTOTP(otp_uri);
        generateQRCodeHOTP(hotp_uri);
    }

    private void generateQRCodeTOTP(String uri) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(uri, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imgQRCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void generateQRCodeHOTP(String uri) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(uri, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imgQRCodeHOTP.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
