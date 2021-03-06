/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.auto.core.utils.codec.digester;


import cn.auto.core.utils.codec.DigestedException;

/**
 * 摘要通用接口
 * 
 * @author zhoufengbo
 * @since 2010-09-17
 * @version 1.0
 */
public interface Digester {

    public byte[] digest(byte[] message) throws DigestedException;

    public boolean matches(byte[] message, byte[] digest) throws DigestedException;

    public String digest(String message) throws DigestedException;

    public boolean matches(String message, String digest) throws DigestedException;

}
