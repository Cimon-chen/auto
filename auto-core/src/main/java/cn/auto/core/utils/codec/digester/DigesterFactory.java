/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.auto.core.utils.codec.digester;


/**
 * 摘要抽象工厂
 * 
 * @author zhoufengbo
 */
public interface DigesterFactory {
    enum TYPE {
        MD5, SHA256
    }

    public Digester getDigester(String algorithm);

}
