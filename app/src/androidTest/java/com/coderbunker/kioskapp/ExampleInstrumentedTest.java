package com.coderbunker.kioskapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        String passphrase = "123456";
        int iterationCount = 1000;
        int keySize = 128;
        String salt = "9876";
        String ciphertext = "uj9QkQg7gdhZxRjUcqRt3Q==";
        String iv = "12345678901234561234567890123456";

        AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String encrypt = aesUtil.encrypt(salt, iv, passphrase, "Hello World, i hope this works.");

        System.out.println(encrypt);

        String plaintext = aesUtil.decrypt(salt, iv, passphrase, encrypt);
        System.out.println(plaintext);
    }
}
