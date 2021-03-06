/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.auto.core.utils.codec.encrypter;

/**
 * 对称加密抽象工厂
 * 
 * @author zhoufengbo
 */
public interface EncrypterFactory {
    enum TYPE {
        PBEWithMD5AndDES, PBEWithMD5AndTripleDES
    }

    public Encrypter getEncrypter(String algorithm);

}
