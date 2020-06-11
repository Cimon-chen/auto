/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.auto.core.utils.codec.encrypter;


import cn.auto.core.utils.codec.EncryptException;

/**
 * 加密/解密器，采用固定的基于口令的密码
 * 
 * @author zhoufengbo
 * @since 2010-09-17
 * @version 1.0
 */
public interface Encrypter {

    enum PASSWORD {
        INSKYE
    }

    enum TYPE {
        PBEWithMD5AndDES, PBEWithMD5AndTripleDES
    }

    enum ALGORITHM {
        PBEWithMD5AndDES("PBEWithMD5AndDES");
        //PBEWithMD5AndTripleDES("PBEWithMD5AndTripleDES"), 需要jce_policy支持
        //AES("PBEWITHSHA256AND128BITAES-CBC-BC"); 需要bouncyCastle支持

        private String value;


        private ALGORITHM(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static ALGORITHM value2Enum(String value){
            for (ALGORITHM algorithm: ALGORITHM.values()){
                if(algorithm.getValue().equals(value)){
                    return algorithm;
                }
            }
            throw new IllegalArgumentException("参数传递错误,未能匹配");
        }
    }

    public byte[] encrypt(byte[] message) throws EncryptException;;

    public byte[] decrypt(byte[] encryptedMessage) throws EncryptException;;

    public String encrypt(String message) throws EncryptException;

    public String decrypt(String encryptedMessage) throws EncryptException;;
}
