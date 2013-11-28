/**
 * RSAUtil.java
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
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/**
 * RSAUtil是一个运用RSA加密算法进行签名和加密、解密的工具类
 * 
 * 目前提供的功能如下： 1. public static String sign(byte[] data, String privateKey)
 * 用私钥对信息生成数字签名
 * 
 * 2. public static boolean verify(byte[] data, String publicKey, String sign)
 * 校验数字签名的正确性
 * 
 * 3. public static byte[] decryptByPrivateKey(byte[] data, String key)
 * 用私钥解密通过公钥加密的数据
 * 
 * 4. public static byte[] decryptByPublicKey(byte[] data, String key)
 * 用公钥解密通过私钥加密的数据
 * 
 * 5. public static byte[] encryptByPublicKey(byte[] data, String key) 用公钥加密数据
 * 
 * 6. public static byte[] encryptByPrivateKey(byte[] data, String key) 用私钥加密数据
 * 
 * 7. public static String getPrivateKey(Map<String, Object> keyMap) 取得私钥
 * 
 * 8. public static String getPublicKey(Map<String, Object> keyMap) 取得公钥
 * 
 * 9. public static Map<String, Object> initKey() 初始化密钥,产生1024bit的密钥对
 * 
 * @used 暂无项目使用
 * @category veyron code -> 公共库 -> 编码加密
 * @author <a href="mailto:xiejiyun@baidu.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class RSAUtil {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            需要签名的数据
	 * @param privateKey
	 *            用来签名的私钥
	 * 
	 * @return 对数据的签名
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 */
	public static String sign(byte[] data, String privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException {
		// 解密由base64编码的私钥
		byte[] keyBytes = Base64ForServer.base64toByte(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return Base64ForServer.byteToBase64(signature.sign());
	}

	/**
	 * 校验数字签名的正确性
	 * 
	 * @param data
	 *            需要校验的数据
	 * @param publicKey
	 *            用来检验数字签名的公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException {
		// 解密由base64编码的公钥
		byte[] keyBytes = Base64ForServer.base64toByte(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		// 验证签名是否正常
		return signature.verify(Base64ForServer.base64toByte(sign));
	}

	/**
	 * 用私钥解密通过公钥加密的数据
	 * 
	 * @param data
	 *            通过公钥加密的数据
	 * @param key
	 *            用来解密的私钥
	 * @return 解密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 对密钥解密
		byte[] keyBytes = Base64ForServer.base64toByte(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return CipherUtil.process(cipher, 128, data);
	}

	/**
	 * 用私钥解密通过公钥加密的数据
	 * 
	 * @param data
	 *            通过公钥加密的数据
	 * @param key
	 *            用来解密的私钥
	 * @return 解密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] decryptByPrivateKey(byte[] data, Key privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 对数据解密
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return CipherUtil.process(cipher, 128, data);
	}

	/**
	 * 用公钥解密通过私钥加密的数据
	 * 
	 * @param data
	 *            通过私钥加密的数据
	 * @param key
	 *            用来解密的公钥
	 * @return 解密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 对密钥解密
		byte[] keyBytes = Base64ForServer.base64toByte(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return CipherUtil.process(cipher, 128, data);
	}

	/**
	 * 用公钥加密数据
	 * 
	 * @param data
	 *            等待加密的原始数据
	 * @param key
	 *            用来加密的公钥
	 * @return 加密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 对公钥解密
		byte[] keyBytes = Base64ForServer.base64toByte(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return CipherUtil.process(cipher, 117, data);
	}

	/**
	 * 用公钥加密数据
	 * 
	 * @param data
	 *            等待加密的原始数据
	 * @param key
	 *            用来加密的公钥
	 * @return 加密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] encryptByPublicKey(byte[] data, Key publicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 取得公钥
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return CipherUtil.process(cipher, 117, data);
	}

	/**
	 * 用私钥加密数据
	 * 
	 * @param data
	 *            等待加密的原始数据
	 * @param key
	 *            用来加密的私钥
	 * @return 加密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 对密钥解密
		byte[] keyBytes = Base64ForServer.base64toByte(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return CipherUtil.process(cipher, 117, data);
	}

	/**
	 * 用私钥加密数据
	 * 
	 * @param data
	 *            等待加密的原始数据
	 * @param key
	 *            用来加密的私钥
	 * @return 加密后的数据
	 * 
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 * @throws InvalidKeyException
	 *             假如输入的RSA私钥不合法
	 * @throws SignatureException
	 *             假如根据privateKey生成密钥失败
	 * @throws UnsupportedEncodingException
	 *             假如privateKey不是使用UTF-8进行编码
	 * @throws NoSuchPaddingException
	 *             假如产生的密钥对有问题
	 * @throws BadPaddingException
	 *             假如输入的加密的数据填充数据错误
	 * @throws IllegalBlockSizeException
	 *             假如输入的加密的数据字节数不是BlockSize的整数倍
	 * @throws ShortBufferException
	 */
	public static byte[] encryptByPrivateKey(byte[] data, Key privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			ShortBufferException {
		// 取得私钥
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return CipherUtil.process(cipher, 117, data);
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 *            密钥对Map
	 * @return 私钥
	 * @throws UnsupportedEncodingException
	 *             假如用户的环境不支持UTF-8
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 * @throws InvalidKeySpecException
	 *             假如根据privateKey生成密钥失败
	 */
	public static Key getPrivateKey(String key)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// 对密钥解密
		byte[] keyBytes = Base64ForServer.base64toByte(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		return keyFactory.generatePrivate(pkcs8KeySpec);
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 *            密钥对Map
	 * @return 私钥
	 * @throws UnsupportedEncodingException
	 *             假如用户的环境不支持UTF-8
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws UnsupportedEncodingException {
		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return Base64ForServer.byteToBase64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 *            密钥对Map
	 * @return 公钥
	 * @throws UnsupportedEncodingException
	 *             假如用户的环境不支持UTF-8
	 */
	public static String getPublicKey(Map<String, Object> keyMap)
			throws UnsupportedEncodingException {
		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return Base64ForServer.byteToBase64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 *            密钥对Map
	 * @return 公钥
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static Key getPublicKey(String key) throws NoSuchAlgorithmException,
			UnsupportedEncodingException, InvalidKeySpecException {
		// 解密由base64编码的公钥
		byte[] keyBytes = Base64ForAPP.decode(key);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * 初始化密钥,产生1024bit的密钥对
	 * 
	 * @return 密钥对Map
	 * @throws NoSuchAlgorithmException
	 *             假如用户的JDK不支持RSA
	 */
	public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);

		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
}
