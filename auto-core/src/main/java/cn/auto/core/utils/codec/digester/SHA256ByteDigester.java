/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.auto.core.utils.codec.digester;

import cn.auto.core.utils.codec.DigestedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.digest.StandardByteDigester;


/**
 * SHA256算法,256位摘要,针对Byte[]
 * 
 * @author zhoufengbo
 */
public class SHA256ByteDigester implements Digester {

    private static final Log logger = LogFactory.getLog(SHA256ByteDigester.class);
    private final StandardByteDigester digester;

    public SHA256ByteDigester() {
        digester = new StandardByteDigester();
        digester.setAlgorithm("SHA-256");
        digester.setIterations(100000);
        digester.setSaltSizeBytes(16);
        digester.initialize();
    }

    public byte[] digest(byte[] message) throws DigestedException {

        try {
            return digester.digest(message);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    public String digest(String message) throws DigestedException {

        throw new DigestedException("this is String Digester, not support byte[]");
    }

    public boolean matches(byte[] message, byte[] digest) throws DigestedException {

        try {
            return digester.matches(message, digest);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    public boolean matches(String message, String digest) throws DigestedException {
        throw new DigestedException("this is String Digester, not support byte[]");
    }

}
