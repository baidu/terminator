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
import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.record.BaseRecordServerHandler;

/**
 * HttpRecordServerHandler response for recording the pair of HTTP request and
 * response.<br>
 * Each Request have a sign value which is calculated by RequestSigner. When
 * terminator change work mode to replay, it will return the recorded response
 * if the sign value of request is same as recorded requests'.<br>
 * Once RequestSigner changed, the sign value storage in Terminator will be
 * recalculate.
 * 
 * @author zhangjunjun
 * 
 */
public class HttpRecordServerHandler extends
		BaseRecordServerHandler<HttpRequest> {

	private volatile boolean readingChunks;

	public HttpRecordServerHandler(ServerContext context) {
		super(context);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!readingChunks) {
			request = (HttpRequest) e.getMessage();

			if (is100ContinueExpected(request)) {
				send100Continue(e);
			}

			if (request.isChunked()) {
				readingChunks = true;
			} else {
				writeObject(e, request);
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				readingChunks = false;
				writeObject(e, chunk);
			}
		}
	}

	private void send100Continue(MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
		e.getChannel().write(response);
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
					inboundChannel, HttpRecordServerHandler.this));
			return pipeline;
		}

	}

}
