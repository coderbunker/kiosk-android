package com.coderbunker.kioskapp.config;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coderbunker.kioskapp.R;
import com.coderbunker.kioskapp.config.ConfigEncrypter;
import com.coderbunker.kioskapp.config.Configuration;
import com.coderbunker.kioskapp.config.encryption.EncryptionException;

public class ClaimDialog extends Dialog {
    public ClaimDialog(@NonNull Context context) {
        super(context, false, null);
        setContentView(R.layout.claim_dialog);

        Configuration configuration = Configuration.loadFromPreferences(context);
        final EditText passphraseView = findViewById(R.id.passphrase);
        final EditText groupLabelView = findViewById(R.id.groupLabel);
        groupLabelView.setText(configuration.getGroupLabel());
        final EditText deviceLabelView = findViewById(R.id.deviceLabel);
        deviceLabelView.setText(configuration.getDeviceLabel());

        Button claimBtn = findViewById(R.id.claim);
        Button cancelBtn = findViewById(R.id.cancel);

        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String passphrase = passphraseView.getText().toString();
                    Configuration configuration = Configuration.loadFromPreferences(getContext());
                    configuration.setGroupLabel(groupLabelView.getText().toString());
                    configuration.setDeviceLabel(deviceLabelView.getText().toString());
                    configuration.setPassphrase(new ConfigEncrypter().hashPassphrase(passphrase));
                    configuration.save();
                } catch (EncryptionException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Claim failed!", Toast.LENGTH_LONG).show();
                } finally {
                    dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
