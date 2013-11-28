/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.plugin.signer.customized.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.signer.BaseSigner;
import com.baidu.terminator.plugin.signer.algorithm.MD5;
import com.baidu.terminator.register.annotation.CustomizedSigner;
import com.baidu.terminator.storage.RequestResponseBundle;

@CustomizedSigner(protocol = "HTTP", message = "移动凤巢与DR之间的签名类，主要解决登录时客户端每次发送RSA加密内容到DR的问题。")
public class FromAppToDrAPIHttpRequestSigner extends BaseSigner {

	private Logger logger;

	public FromAppToDrAPIHttpRequestSigner(Link link) {
		super(link);
		logger = LinkLogger.getLogger(FromAppToDrAPIHttpRequestSigner.class,
				link.getId());
	}
	
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAsD6ofIw1q6vVPwnmOL7UtrNDFoYNFFoFCIHXsXWMWuh8Pj6iWIAEmseoi34wgkeRx8z/zmp6GjF8vpR59PcUZnryuN0TWY2KiequBpiQuA5ak1tLzr4YY+8rzUSFIZtSWf3HnO4ZT1wDe3QGCc6MZ+TlQvkgvupjLdT0snLBAQIDAQAB";

	public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAICwPqh8jDWrq9U/CeY4vtS2s0MWhg0UWgUIgdexdYxa6Hw+PqJYgASax6iLfjCCR5HHzP/OanoaMXy+lHn09xRmevK43RNZjYqJ6q4GmJC4DlqTW0vOvhhj7yvNRIUhm1JZ/cec7hlPXAN7dAYJzoxn5OVC+SC+6mMt1PSycsEBAgMBAAECgYB97PjDYmzJMB3LZA5JzAlgmQ89fNLcBag4+KQl7q76ExMgUMB1mGhdGSB621U2HqXuDzeYseIbbXozeUJNPNpxFrWCbvZ1lUtX/Uom283Xuj4eFoZdxi57iRwkd74SU7gQ7uAF6H0r12RJlJ/NDH0TmeAWtw3vYy3xhLaUUNbuqQJBAPTp3y7+YrFPfjwBxMaK4KZ7YEF0zDkWY0FrTQGh8LO2VN0LJnOyHh1UBNYC6ABeummXazKgzFn7ChBPYfrmjSMCQQCGg4X10rI9LRfm+W3SM6kXKdR/iKv45HsyuTYtrHzGL4IaWisH7fyApaN72UyMmhP90ymz16MmXFkQlnBRMtWLAkEAu021U1JXljUuXKQZoqIJhykqB0VU/n/hruFqBkqcNETIiDiD0s2w/EsKW8XjOo030ZNs905EmrhEDQ845tK1swJAF6endixgRdvZOTgh9PY6xSUJBoh9XZ6of6oMegm8Q4n1QjcbHCDg56q5Tj1PdeZen6Nz0PmZ6lQRIZajF9RtswJAaOfGZs9P74H1lOskY7Gr+IRJCWY3fS9ROtDQY0/kqBaFGjeJThUql9/BdgLX5xhllKE4lhM5f3wiIbHj2P5cvQ==";

	private static final String LOGIN_URL = "/sem/common/LoginService";

	private static final Pattern PATTERN = Pattern
			.compile(".*password\":\"(.+?)\".*");

	@Override
	public String sign(Object req) {
		HttpRequest request = (HttpRequest) req;
		StringBuilder sb = new StringBuilder();

		HttpVersion protocalVersion = request.getProtocolVersion();
		String method = request.getMethod().getName();
		String uri = request.getUri();

		sb.append(protocalVersion).append(method).append(uri);
		logger.info("original header of http request is: " + sb.toString());

		ChannelBuffer content = request.getContent();
		if (content.readable()) {
			byte[] contentBytes = content.array();

			String unGzipContent = null;
			if (LOGIN_URL.equals(uri)) {
				unGzipContent = decryptRequest(contentBytes);
			} else {
				try {
					unGzipContent = GZipUtil.unGzipString(contentBytes);
				} catch (IOException e) {
					logger.info("request is not gzip: " + uri);
					unGzipContent = new String(contentBytes);
				}

			}
			logger.info("original body of http request is: " + unGzipContent);

			String contentExceptPassword = erasePassword(unGzipContent);
			sb.append(contentExceptPassword);
		}

		logger.info("content of http request to be signed is: " + sb.toString());
		MD5 md5 = new MD5();
		return md5.getMD5ofStr(sb.toString());
	}

	@Override
	public void logResponse(RequestResponseBundle bundle) {
		HttpRequest request = (HttpRequest) bundle.getRequest();
		HttpResponse response = (HttpResponse) bundle.getResponse();
		if (response != null) {
			ChannelBuffer content = response.getContent();
			if (content.readable()) {
				byte[] contentBytes = content.array();

				String unGzipContent = null;
				if (LOGIN_URL.equals(request.getUri())) {
					unGzipContent = decryptResponse(contentBytes);
				} else {
					try {
						unGzipContent = GZipUtil.unGzipString(contentBytes);
					} catch (IOException e) {
						unGzipContent = new String(contentBytes);
					}
				}
				logger.info("content of http response is:" + unGzipContent);
			}
		}
	}

	private String decryptRequest(byte[] content) {
		byte[] body = ArrayUtils.subarray(content, 8, content.length);

		String unGipString = "";
		try {
			byte[] gzipBytes = RSAUtil.decryptByPrivateKey(body, PRIVATE_KEY);
			unGipString = GZipUtil.unGzipString(gzipBytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (SignatureException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (ShortBufferException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return unGipString;
	}
	
	private String decryptResponse(byte[] content) {
		byte[] body = ArrayUtils.subarray(content, 8, content.length);

		String unGipString = "";
		try {
			byte[] gzipBytes = RSAUtil.decryptByPublicKey(body, PUBLIC_KEY);
			unGipString = GZipUtil.unGzipString(gzipBytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (SignatureException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (ShortBufferException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return unGipString;
	}

	private String erasePassword(String content) {
		Matcher matcher = PATTERN.matcher(content);
		if (matcher.matches()) {
			String password = matcher.group(1);
			content = content.replace(password, "");
		}
		return content;
	}

}
