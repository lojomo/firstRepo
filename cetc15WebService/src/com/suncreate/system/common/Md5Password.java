package com.suncreate.system.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

public class Md5Password {

    private static byte[] md5(String s) throws NoSuchAlgorithmException {
        byte abyte0[];
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update(s.getBytes());
        abyte0 = messagedigest.digest();
        return abyte0;
    }

    public static String doHash(String s) throws NoSuchAlgorithmException {
        return "{MD5}" + (new BASE64Encoder()).encode(md5(s));
    }
}
