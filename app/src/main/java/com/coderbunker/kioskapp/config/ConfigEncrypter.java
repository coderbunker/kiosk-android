package com.coderbunker.kioskapp.config;

import android.content.Context;

import com.coderbunker.kioskapp.config.encryption.AesUtil;
import com.coderbunker.kioskapp.config.encryption.EncryptionException;
import com.coderbunker.kioskapp.lib.Base32;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ConfigEncrypter {
    //TODO remove constants ans hide them
    private int iterationCount = 1000;
    private int keySize = 128;
    private String salt = "9876";
    private String iv = "12345678901234561234567890123456";

    public String hashPassphrase(String passphrase) {
        AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String hashPassphrase = Base32.encode(aesUtil.hashPassphrase(passphrase, salt));
        return hashPassphrase;
    }

    public String encrypt(String passphrase, Configuration configuration) throws EncryptionException {
        try {
            AesUtil aesUtil = new AesUtil(keySize, iterationCount);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(configuration);
            return aesUtil.encrypt(iv, passphrase, json);
        } catch (Base32.DecodingException | IOException e) {
            e.printStackTrace();
            throw new EncryptionException();
        }
    }

    public Configuration decrypt(String passphrase, String value, Context context) throws EncryptionException {
        try {
            AesUtil aesUtil = new AesUtil(keySize, iterationCount);
            String decrypt = null;
            decrypt = aesUtil.decrypt(iv, passphrase, value);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decrypt, Configuration.ConfigurationBuilder.class).build(context);
        } catch (Base32.DecodingException | IOException e) {
            e.printStackTrace();
            throw new EncryptionException();
        }
    }
}
