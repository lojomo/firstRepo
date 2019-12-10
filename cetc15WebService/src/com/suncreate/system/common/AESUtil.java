package com.suncreate.system.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.provider.SecureRandom;

public class AESUtil {

	 public static String encrypt(String strKey, String strIn) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(strIn.getBytes());

        return new BASE64Encoder().encode(encrypted);
    }
	 
	 public static String decrypt(String strKey, String strIn) throws Exception {
	        SecretKeySpec skeySpec = getKey(strKey);
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(strIn);

	        byte[] original = cipher.doFinal(encrypted1);
	        String originalString = new String(original);
	        return originalString;
	    }

	    private static SecretKeySpec getKey(String strKey) throws Exception {
	        byte[] arrBTmp = strKey.getBytes();
	        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

	        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
	            arrB[i] = arrBTmp[i];
	        }

	        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

	        return skeySpec;
	    }

	    public static void main(String[] args) throws Exception {
	        String Code = "中文ABc123";
	        String key = "1q2w3e4r";
	        String codE;

	        codE = AESUtil.encrypt(key, Code);

	        System.out.println("原文：" + Code);
	        System.out.println("密钥：" + key);
	        System.out.println("密文：" + codE);
	        System.out.println("解密：" + AESUtil.decrypt(key, codE));
	    }




}
