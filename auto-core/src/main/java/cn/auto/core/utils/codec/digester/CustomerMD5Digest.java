/*
 * Copyright 2012 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.auto.core.utils.codec.digester;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * CustomerMD5Digest.java <br/>
 * MD5签名服务类 提供了四个方法
 * <ul>
 * <li>左偏移产生的签名中含有小写的英文字母</li> *
 * <li>左偏移产生的签名中含有大写的英文字母</li>
 * <li>右偏移产生的签名中含有小写的英文字母</li>
 * <li>右偏移产生的签名中含有大写的英文字母</li>
 * </ul>
 * 默认的编码字符集 是 UTF-8
 * 
 * @author wangliming
 */
public class CustomerMD5Digest {
    private static final char[] UPPER_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};
    private static final char[] LOWER_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    public static String md5WithLeftLower(String plainText, String charset) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        byte[] bytes = getDegisteByte(plainText, charset);
        return leftOff(bytes, LOWER_ARRAY);
    }

    public static String md5WithLeftUpper(String plainText, String charset) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        byte[] bytes = getDegisteByte(plainText, charset);
        return leftOff(bytes, UPPER_ARRAY);
    }

    public static String md5WithRightLower(String plainText, String charset) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        byte[] bytes = getDegisteByte(plainText, charset);
        return rightOff(bytes, LOWER_ARRAY);
    }

    public static String md5WithRightUpper(String plainText, String charSet) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        byte[] bytes = getDegisteByte(plainText, charSet);
        return rightOff(bytes, UPPER_ARRAY);
    }

    public static byte[] getDegisteByte(String plainText, String charset) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest msgDigest = MessageDigest.getInstance("MD5");
        if (StringUtils.isNotEmpty(charset)) {
            msgDigest.update(plainText.getBytes(charset));
        }
        else {
            msgDigest.update(plainText.getBytes("UTF-8"));
        }
        byte[] bytes = msgDigest.digest();
        return bytes;
    }

    private static String leftOff(byte[] bytes, char[] sedArray) {
        int length = bytes.length;
        char[] out = new char[length << 1];
        for (int i = 0, j = 0; i < length; i++) {
            out[j++] = sedArray[(0xF0 & bytes[i]) >>> 4];
            out[j++] = sedArray[0xF0 & bytes[i]];
        }
        String md5Str = new String(out);
        return md5Str;
    }

    private static String rightOff(byte[] bytes, char[] sedArray) {
        char[] out = new char[16 * 2];
        for (int i = 0, j = 0; i < 16; i++) {
            out[j++] = sedArray[bytes[i] >>> 4 & 0xf];
            out[j++] = sedArray[bytes[i] & 0xf];
        }
        String md5Str = new String(out);
        return md5Str;
    }
}
