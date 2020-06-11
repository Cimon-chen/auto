/**
 * $Id: CryptoUtils.java
 */
package cn.auto.core.utils;

import cn.auto.core.utils.codec.DefaultEncryptService;
import cn.auto.core.utils.codec.EncryptService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * 数字加解密
 */
public class CryptoUtils {
    public final static String ENCRYPT_ALGORITHM = "WDKFJS152P5kfjds7810YZQQqizdaqEG";

    enum PASSWORD {
        INSKYE
    }

    enum TYPE {
        PBEWithMD5AndDES, PBEWithMD5AndTripleDES
    }

    public static String e2(Long input) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(TYPE.PBEWithMD5AndDES.name());
        encryptor.setPassword(PASSWORD.INSKYE.name());
        String encrypted = encryptor.encrypt(input.toString());
        return Hex.encodeHexString(encrypted.getBytes());
    }

    public static long d2(String input) {

        String decrypted = null;
        try {
            decrypted = new String(Hex.decodeHex(input.toCharArray()));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(TYPE.PBEWithMD5AndDES.name());
        encryptor.setPassword(PASSWORD.INSKYE.name());
        return Long.valueOf(encryptor.decrypt(decrypted));
    }


    private static Log log = LogFactory.getLog(CryptoUtils.class);


    public static <T> String e1(T input) {
        // EncryptService encrypt = SpringContextUtils
        // .getBeanByClazz(DefaultEncryptService.class);
        EncryptService encrypt = new DefaultEncryptService();
        return encrypt.encrypt(StringUtils.trimToEmpty(input.toString()),
                EncryptService.ENCRYPT_ALGORITHM.PBEWithMD5AndDES.name());
    }

    public static <T> long d1(T input) {
        // EncryptService encrypt = SpringContextUtils
        // .getBeanByClazz(DefaultEncryptService.class);
        EncryptService encrypt = new DefaultEncryptService();
        return Long.valueOf(encrypt.decrypt(StringUtils.trimToEmpty(input.toString()),
                EncryptService.ENCRYPT_ALGORITHM.PBEWithMD5AndDES.name()));
    }

    /**
     * 针对前台ID加密
     */
    public static <T> String encrypt(T input) {
        String input0 = Long.toHexString(Long.valueOf(input.toString()));
        String input1 = StringUtils.leftPad(input0, input0.length() + 2,
                DigestUtils.md5Hex(input0).substring(0, 2));
        return Crypto5b.encode(input1.getBytes());
    }


    /**
     */
    public static <T> long decrypt(T input) {
        try {
            String output = new String(Crypto5b.decode(input.toString()));
            long output0 = Long.valueOf(output.substring(2), 16);
            if (DigestUtils.md5Hex(output.substring(2)).substring(0, 2).equals(output.substring(0, 2))) {
                return output0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 用于cookie加密
     *
     * @param <T>
     * @param input
     * @return
     */
    public static <T> String eStr(T input) {
        String input0 = input.toString();
        String input1 = StringUtils.leftPad(input0, input0.length() + 2,
                DigestUtils.md5Hex(input0).substring(0, 2));
        return Crypto5b.encode(input1.getBytes());
    }

    /**
     * 用于cookie解密
     *
     * @param <T>
     * @param input
     * @return
     */
    public static <T> String dStr(T input) {
        try {
            String output = new String(Crypto5b.decode(input.toString()));
            String output0 = output.substring(2);
            if (DigestUtils.md5Hex(output.substring(2)).substring(0, 2).equals(output.substring(0, 2))) {
                return output0;
            }
        } catch (InvalidCrypto5b e) {
            log.error("cookie解密错误： 密文为括号内文本（" + input.toString() + "）");
        }
        return "";
    }

    /**
     * 用于加密不太重要的信息
     *
     * @param <T>
     * @param input
     * @return
     */
    public static <T> String eKey(T input) {
        String input0 = input.toString();
        String input1 = StringUtils.leftPad(input0, input0.length() + 2,
                DigestUtils.md5Hex(input0).substring(0, 2));
        return Crypto5b.encode(input1.getBytes());
    }

    /**
     * 用于解密不太重要的信息
     *
     * @param <T>
     * @param input
     * @return
     */
    public static <T> String dKey(T input) {
        try {
            String output = new String(Crypto5b.decode(input.toString()));
            String output0 = output.substring(2);
            if (DigestUtils.md5Hex(output.substring(2)).substring(0, 2).equals(output.substring(0, 2))) {
                return output0;
            }
        } catch (InvalidCrypto5b e) {
            log.error("解密错误： 密文为括号内文本（" + input.toString() + "）");
        }
        return "";
    }

    public static <T> String irreverEncrypt(T input) {
        String input0 = null == input ? ENCRYPT_ALGORITHM : input.toString() + ENCRYPT_ALGORITHM;
        return DigestUtils.md5Hex(input0);
    }


    public static void main(String[] args) throws DecoderException {
        System.out.println(encrypt("3037800"));
        System.out.println(decrypt("pl4wh3m3j3wgv"));

        System.out.println(encrypt("190918630216") + "-" + irreverEncrypt("190918630216"));
    }
}
