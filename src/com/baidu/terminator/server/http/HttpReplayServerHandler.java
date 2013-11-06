/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.http;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.replay.BaseReplayServerHandler;

/**
 * HttpReplayServerHandler response for replaying the response when the sign
 * value of request is as same as which was recorded.<br>
 * If there is not an response to the request, then HttpReplayServerHandler will
 * return a default HttpResponse: <br>
 * <br>
 * HTTP/1.1 501 Not Implemented<br>
 * Content-Type:text/html; charset=utf-8<br>
 * Content-Length: 74<br>
 * <br>
 * You see this message because the response to this request is not recorded!<br>
 * 
 * @author zhangjunjun
 * 
 */
public class HttpReplayServerHandler extends
		BaseReplayServerHandler<HttpRequest> {

	public static final String DEFAULT_HTTP_BODY = "You see this message because the response to this request is not recorded!";

	public HttpReplayServerHandler(ServerContext context) {
		super(context);
	}

	@Override
	public Object responseWhenNotHit() {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.NOT_IMPLEMENTED);
		response.setChunked(false);
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		response.setHeader("Content-Length", 74);
		response.setContent(ChannelBuffers.copiedBuffer(DEFAULT_HTTP_BODY,
				CharsetUtil.UTF_8));
		return response;
	}

}
