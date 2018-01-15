package com.flipkart.android.register;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by VectoR on 12-09-2017.
 */

public class Md5Gen {
        public static String md5(String str) {
            String str2 = null;
            if (str != null) {
                try {
                    MessageDigest instance = MessageDigest.getInstance("MD5");
                    instance.update(str.getBytes(Charset.forName("UTF-8")));
                    byte[] digest = instance.digest();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (byte b : digest) {
                        String toHexString = Integer.toHexString(b & 255);
                        while (toHexString.length() < 2) {
                            toHexString = "0" + toHexString;
                        }
                        stringBuilder.append(toHexString);
                    }
                    str2 = stringBuilder.toString();
                } catch (NoSuchAlgorithmException e) {
                }
            }
            return str2;
        }
    }
