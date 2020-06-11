package cn.auto.core.utils.codec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密模块
 * 
 * @author majun
 */
public class ThreeDESUtil {

    static {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private static final String Algorithm = "DESede"; // 定义 加密算法,可用'DES','DESede','Blowfish'

    /**
     * encrypt data by 3des
     * 
     * @param key
     * @param src
     * @return
     */
    public static byte[] encryptData(byte[] key, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(key, Algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        }
        catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * @Title encryptDesData
     * @Description Des加密
     * @param key 加密因子
     * @param src 加密数据
     * @return byte[] 加密的数据
     * @throws
     */
    public static byte[] encryptDesData(byte[] key, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(key, "DES");
            // 加密
            Cipher c1 = Cipher.getInstance("DES");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        }
        catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * decrypt data by 3des
     *
     * @param key
     * @param src
     * @return
     */
    public static byte[] decryptData(byte[] key, byte[] src) {
        try {
            // 生成密钥
            SecretKey desKey = new SecretKeySpec(key, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, desKey);
            return c1.doFinal(src);
        }
        catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
