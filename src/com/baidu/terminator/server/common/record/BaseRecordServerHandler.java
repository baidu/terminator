/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common.record;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;

import com.baidu.terminator.server.Relay;
import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.BaseServerHandler;
import com.baidu.terminator.server.common.RelayImpl;
import com.baidu.terminator.storage.RequestResponseBundle;

public abstract class BaseRecordServerHandler<Request> extends
		BaseServerHandler<Request> implements RelayListener {

	public BaseRecordServerHandler(ServerContext context) {
		super(context);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		inboundChannel = e.getChannel();
		inboundChannel.setReadable(false);

		Relay relay = new RelayImpl(context);
		ChannelFuture relayChannelFuture = relay
				.start(getRelayChannelPipelineFactory(this));
		outboundChannel = relayChannelFuture.getChannel();

		context.addChannel(inboundChannel);
		context.addChannel(outboundChannel);

		relayChannelFuture.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future)
					throws Exception {
				if (future.isSuccess()) {
					inboundChannel.setReadable(true);
				} else {
					inboundChannel.close();
				}
			}
		});
	}

	@Override
	public void onRelayResponse(Object response) {
		RequestResponseBundle bundle = new RequestResponseBundle(request,
				response);
		storage.putRecordData(signer.sign(request), bundle);
		signer.logResponse(bundle);
	}

	public abstract ChannelPipelineFactory getRelayChannelPipelineFactory(
			RelayListener relayListener);

}
