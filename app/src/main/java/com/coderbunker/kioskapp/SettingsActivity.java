package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class SettingsActivity extends Activity {

    private Context context = this;
    private EditText editPassword, editURL;
    private Button saveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editPassword = findViewById(R.id.editText_password);
        editURL = findViewById(R.id.editText_URL);

        saveChanges = findViewById(R.id.button_save);
        saveChanges.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                boolean changed = false;
                String url =  editURL.getText().toString();
                String password = editPassword.getText().toString();

                if(url != "" &&  URLUtil.isValidUrl(url)) {
                    KioskActivity.setURL(editURL.getText().toString());
                    changed = true;
                }
                if(password != "" && password.length()>0){
                    KioskActivity.setPassword(editPassword.getText().toString());
                    changed = true;
                }

               if(changed)
                Toast.makeText(context, "Changes saved!", LENGTH_LONG).show();

               finish();
            }
        });
    }
}
