package com.flipkart.android.register;

/**
 * Created by VectoR on 17-09-2017.
 */


import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TuneEncryption {
    private IvParameterSpec f22116a;
    private SecretKeySpec f22117b;
    private Cipher f22118c;

    public TuneEncryption(String str, String str2) {
        this.f22116a = new IvParameterSpec(str2.getBytes());
        this.f22117b = new SecretKeySpec(str.getBytes(), "AES");
        try {
            this.f22118c = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        }
    }

    public byte[] encrypt(String str) throws Exception {
        if (str == null || str.length() == 0) {
            throw new Exception("Empty string");
        }
        try {
            this.f22118c.init(1, this.f22117b, this.f22116a);
            return this.f22118c.doFinal(m16491a(str).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
    }

    public byte[] decrypt(String str) throws Exception {
        if (str == null || str.length() == 0) {
            throw new Exception("Empty string");
        }
        try {
            this.f22118c.init(2, this.f22117b, this.f22116a);
            return this.f22118c.doFinal(hexToBytes(str));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
    }

    private static String m16491a(String str) {
        int length = 16 - (str.length() % 16);
        for (int i = 0; i < length; i++) {
            str = str + ' ';
        }
        return str;
    }

    public static byte[] hexToBytes(String str) {
        byte[] bArr = null;
        if (str != null && str.length() >= 2) {
            int length = str.length() / 2;
            bArr = new byte[length];
            for (int i = 0; i < length; i++) {
                bArr[i] = (byte) Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 16);
            }
        }
        return bArr;
    }
}