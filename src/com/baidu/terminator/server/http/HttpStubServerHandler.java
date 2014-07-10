/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.http;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.stub.BaseStubServerHandler;

public class HttpStubServerHandler extends BaseStubServerHandler<HttpRequest> {

	public static final String DEFAULT_HTTP_BODY = "You see this message because the response to this request is not set!";

	public HttpStubServerHandler(ServerContext context) {
		super(context);
	}

	@Override
	public Object responseWhenHit(String content) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.setChunked(false);
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		response.setHeader("Content-Length", content.getBytes().length);//解决中文按照1个字符计算问题
		response.setContent(ChannelBuffers.copiedBuffer(content,
				CharsetUtil.UTF_8));
		return response;
	}

	@Override
	public Object responseWhenNotHit() {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.NOT_IMPLEMENTED);
		response.setChunked(false);
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		response.setHeader("Content-Length", 69);
		response.setContent(ChannelBuffers.copiedBuffer(DEFAULT_HTTP_BODY,
				CharsetUtil.UTF_8));
		return response;
	}

	@Override
	public ChannelPipelineFactory getRelayChannelPipelineFactory(
			RelayListener relayListener) {
		return new HttpRelayPipelineFactory();
	}

	class HttpRelayPipelineFactory implements ChannelPipelineFactory {

		@Override
		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline pipeline = pipeline();

			// use HttpClientCodec because it has additional state management
			// for HEAD and CONNECT
			pipeline.addLast("codec", new HttpClientCodec(8192, 8192 * 2,
					8192 * 2));
			pipeline.addLast("inflater", new HttpContentDecompressor());
			pipeline.addLast("aggregator", new HttpChunkAggregator(2048576));

			pipeline.addLast("handler", new HttpRelayHandler(context,
					inboundChannel, HttpStubServerHandler.this));
			return pipeline;
		}

	}

}
