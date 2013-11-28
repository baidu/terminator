/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.plugin.signer;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.signer.algorithm.MD5;
import com.baidu.terminator.register.annotation.DefaultSigner;
import com.baidu.terminator.server.common.util.ChannelUtil;

@DefaultSigner(protocol = "HTTP", message = "对Http Header中的Version、Uri、Method以及整个Http Body拼接起来计算MD5值。")
public class DefaultHttpSigner extends BaseSigner {

	private Logger logger;

	public DefaultHttpSigner(Link link) {
		super(link);
		logger = LinkLogger.getLogger(DefaultHttpSigner.class,
				link.getId());
	}

	@Override
	public String sign(Object req) {
		HttpRequest httpRequest = (HttpRequest) req;
		StringBuilder sb = new StringBuilder();

		HttpVersion protocalVersion = httpRequest.getProtocolVersion();
		String method = httpRequest.getMethod().getName();
		String uri = httpRequest.getUri();

		sb.append(protocalVersion).append(method).append(uri);
		logger.info("the content of sign header is: " + sb.toString());

		ChannelBuffer content = httpRequest.getContent();
		if (content.readable()) {
			String stringContent = ChannelUtil
					.readChannelBufferAsString(content);
			logger.info("the content of sign body is: " + stringContent);
			sb.append(stringContent);
		}

		MD5 md5 = new MD5();
		return md5.getMD5ofStr(sb.toString());
	}

}
