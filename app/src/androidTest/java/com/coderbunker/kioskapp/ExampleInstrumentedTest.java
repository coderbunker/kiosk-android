package com.coderbunker.kioskapp;

import android.support.test.runner.AndroidJUnit4;

import com.coderbunker.kioskapp.config.encryption.AesUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        System.out.println("DoUdbrcBa6Tv3REujq4NCODN1ubJbLqVit0LY/xex4HDWwxXD/B0PtDHbGN3DLaGaPkR+eiMiqZRw705WlhHFBincqvXjPmlsVQ6WnqkdLYCgLOyGtpr5mKjDnpd4+ITsLwwzyzAtyqw7D9sdMo2+8M0RhChzAZlfD5+ym08nSeA1Ou2jAjKGgul92+8f7Dobbx5E4W2yvFzfdCqFxJ2gQ==".length());
        String passphrase = "123456";
        int iterationCount = 1000;
        int keySize = 128;
        String salt = "9876";
        String ciphertext = "uj9QkQg7gdhZxRjUcqRt3Q==";
        String iv = "12345678901234561234567890123456";

        AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String encrypt = aesUtil.encrypt(iv, passphrase, "{\"hotpCounter\":0,\"passphrase\":\"AADASBACADPK3PXP\",\"url\":\"https://coderbunker.github.io/kiosk-web/\",\"uuid\":\"c75a8198-7081-436d-83a8-d55651886934\"}");

        System.out.println(encrypt);

        String plaintext = aesUtil.decrypt(iv, passphrase, encrypt);
        System.out.println(plaintext);
    }
}
