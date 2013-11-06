/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common.replay;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

import com.baidu.terminator.server.NotHitHandler;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.BaseServerHandler;
import com.baidu.terminator.storage.RequestResponseBundle;

public abstract class BaseReplayServerHandler<Request> extends
		BaseServerHandler<Request> implements NotHitHandler {

	public BaseReplayServerHandler(ServerContext context) {
		super(context);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		inboundChannel = e.getChannel();
		context.addChannel(inboundChannel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		request = (Request) e.getMessage();

		RequestResponseBundle bundle = storage.getRecordData(signer
				.sign(request));
		Object response = bundle.getResponse();
		if (response == null) {
			// set default response, or it will take a long time to wait
			response = responseWhenNotHit();
		}
		signer.logResponse(bundle);
		inboundChannel.write(response);
	}

	@Override
	public abstract Object responseWhenNotHit();

}
