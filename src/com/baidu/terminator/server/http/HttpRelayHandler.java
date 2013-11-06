/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.http;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.BaseRelayHandler;

public class HttpRelayHandler extends BaseRelayHandler<HttpResponse> {

	private volatile boolean readingChunks;

	public HttpRelayHandler(ServerContext context, Channel inboundChannel,
			RelayListener httpRelayListener) {
		super(context, inboundChannel, httpRelayListener);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
			throws Exception {
		if (!readingChunks) {
			response = (HttpResponse) e.getMessage();
			if (response.isChunked()) {
				readingChunks = true;
			} else {
				writeObject(e, response);
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();

			if (chunk.isLast()) {
				readingChunks = false;
				writeObject(e, chunk);
			}
		}
	}

}
