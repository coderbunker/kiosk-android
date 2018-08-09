   /*
    * HOTP.java
    * OATH Initiative,
    * HOTP one-time password algorithm
    *
    */

   /* Copyright (C) 2004, OATH.  All rights reserved.
    *
    * License to copy and use this software is granted provided that it
    * is identified as the "OATH HOTP Algorithm" in all material
    * mentioning or referencing this software or this function.
    *
    * License is also granted to make and use derivative works provided
    * that such works are identified as
    *  "derived from OATH HOTP algorithm"
    * in all material mentioning or referencing the derived work.
    *
    * OATH (Open AuTHentication) and its members make no
    * representations concerning either the merchantability of this
    * software or the suitability of this software for any particular
    * purpose.
    *
    * It is provided "as is" without express or implied warranty
    * of any kind and OATH AND ITS MEMBERS EXPRESSaLY DISCLAIMS
    * ANY WARRANTY OR LIABILITY OF ANY KIND relating to this software.
    *
    * These notices must be retained in any copies of any part of this
    * documentation and/or software.
    */

   package com.coderbunker.kioskapp.lib;

   import java.lang.reflect.UndeclaredThrowableException;
   import java.security.GeneralSecurityException;

   import javax.crypto.Mac;
   import javax.crypto.spec.SecretKeySpec;

   /**
    * This class contains static methods that are used to calculate the
    * One-Time Password (OTP) using
    * JCE to provide the HMAC-SHA-1.
    *
    * @author Loren Hart
    * @version 1.0
    */
   public class HOTP {
       public static final int[] DIGITS_POWER
               //  0  1   2     3    4        5        6       7         8
               = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

       public static String generateHOTP(long count, String seedString) {

           byte[] counter = new byte[8];
           long movingFactor = count;

           for (int i = counter.length - 1; i >= 0; i--) {
               counter[i] = (byte) (movingFactor & 0xff);
               movingFactor >>= 8;
           }
           byte[] seed = new byte[0];
           try {
               seed = Base32.decode(seedString);
           } catch (Base32.DecodingException e) {
               e.printStackTrace();
           }


           byte[] hash = HMAC(seed, counter);
           int offset = hash[hash.length - 1] & 0xf;

           int otpBinary = ((hash[offset] & 0x7f) << 24)
                   | ((hash[offset + 1] & 0xff) << 16)
                   | ((hash[offset + 2] & 0xff) << 8)
                   | (hash[offset + 3] & 0xff);

           int otp = otpBinary % DIGITS_POWER[6];
           String result = Integer.toString(otp);

           while (result.length() < 6) {
               result = "0" + result;
           }
           return result;

       }

       private static byte[] HMAC(byte[] seed, byte[] counter) {

           try {
               Mac hmac = Mac.getInstance("HmacSHA1");
               SecretKeySpec macKey = new SecretKeySpec(seed, "RAW");
               hmac.init(macKey);
               return hmac.doFinal(counter);

           } catch (GeneralSecurityException ex) {
               throw new UndeclaredThrowableException(ex);
           }
       }
   }