package com.coderbunker.kioskapp.lib;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTP {

    private static int TIME_STEP_SECONDS = 30, NUM_DIGITS_OUTPUT = 6;

    //Source: https://github.com/airG/java-totp/blob/master/src/main/java/com/j256/totp/TwoFactorAuthUtil.java

    public static String generateCurrentNumber(String secret, long currentTimeMillis) {

        byte[] key = new byte[0];
        try {
            key = Base32.decode(secret);
        } catch (Base32.DecodingException e) {
            e.printStackTrace();
        }

        byte[] data = new byte[8];
        long value = currentTimeMillis / 1000 / TIME_STEP_SECONDS;
        for (int i = 7; value > 0; i--) {
            data[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        // encrypt the data with the key and return the SHA1 of it in hex
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        // if this is expensive, could put in a thread-local
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(signKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] hash = mac.doFinal(data);

        // take the 4 least significant bits from the encrypted string as an offset
        int offset = hash[hash.length - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = offset; i < offset + 4; ++i) {
            truncatedHash <<= 8;
            // get the 4 bytes at the offset
            truncatedHash |= (hash[i] & 0xFF);
        }
        // cut off the top bit
        truncatedHash &= 0x7FFFFFFF;

        // the token is then the last 6 digits in the number
        truncatedHash %= 1000000;

        return zeroPrepend(truncatedHash, NUM_DIGITS_OUTPUT);
    }

    private static String zeroPrepend(long num, int digits) {
        String hashStr = Long.toString(num);
        if (hashStr.length() >= digits) {
            return hashStr;
        } else {
            StringBuilder sb = new StringBuilder(digits);
            int zeroCount = digits - hashStr.length();
            sb.append(blockOfZeros, 0, zeroCount);
            sb.append(hashStr);
            return sb.toString();
        }
    }

    private static String blockOfZeros = null;

    {
        StringBuilder sb = new StringBuilder(NUM_DIGITS_OUTPUT);
        for (int i = 0; i < NUM_DIGITS_OUTPUT; i++) {
            sb.append('0');
        }
        blockOfZeros = sb.toString();
    }
}
