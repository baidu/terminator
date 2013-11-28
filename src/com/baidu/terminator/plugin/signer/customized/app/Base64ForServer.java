/**
 * Base64Util.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.baidu.terminator.plugin.signer.customized.app;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;


/**
 * Base64Util是一个用来实现在byte数组和64进制字符串之间相互转换的工具类
 * Base64Util自己并不实现Base64的功能，而是封装了apache的commons库里的相关功能。
 * 本类依赖于commons-codec-1.3.jar
 * 
 * 目前提供的功能如下：
 * 1. public static final String byteToBase64(byte[] bytes)
 * 将byte数组转换成64进制字符串
 * 
 * 2. public static final byte[] base64toByte(String str)
 * 将64进制字符串转换成byte数组
 * 
 * @see org.apache.commons.codec.binary.Base64
 * @used RSAUtil
 * @category veyron code -> 公共库 -> 编码加密
 * @author <a href="mailto:xiejiyun@baidu.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class Base64ForServer {

    /**
     * 将byte数组转换成64进制字符串
     * Encode Byte Array into Base64 String
     * 
     * @param data 待转换的byte数组
     * @return 转换后的64进制字符串
     * @throws UnsupportedEncodingException 假如用户的环境不支持UTF-8编码
     */
    public static String byteToBase64(byte[] data) throws UnsupportedEncodingException {
        if (data == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        return new String(Base64.encodeBase64(data), "UTF-8");
    }

    /**
     * 将64进制字符串转换成byte数组
     * Decode Base64 String into Byte Array
     * 
     * @param str 待转换的64进制字符串
     * @return 转换后的byte数组
     * @throws UnsupportedEncodingException 假如用户的环境不支持UTF-8编码
     */
    public static byte[] base64toByte(String str) throws UnsupportedEncodingException {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        return Base64.decodeBase64(str.getBytes("UTF-8"));
    }
}
