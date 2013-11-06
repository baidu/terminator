/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.util.ChannelUtil;

public class BaseRelayHandler<Response> extends SimpleChannelUpstreamHandler {

	private Logger logger;

	protected final Object trafficLock = new Object();

	protected RelayListener httpRelayListener;

	protected Channel inboundChannel;

	protected Response response;

	public BaseRelayHandler(ServerContext context, Channel inboundChannel,
			RelayListener httpRelayListener) {
		this.inboundChannel = inboundChannel;
		this.httpRelayListener = httpRelayListener;
		this.logger = LinkLogger.getLogger(BaseRelayHandler.class, context
				.getLink().getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
			throws Exception {
		response = (Response) e.getMessage();
		writeObject(e, response);
	}

	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		// If outboundChannel is not saturated anymore, continue accepting
		// the incoming traffic from the inboundChannel.
		synchronized (trafficLock) {
			Channel outboundChannel = e.getChannel();
			if (outboundChannel.isWritable()) {
				inboundChannel.setReadable(true);
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		ChannelUtil.closeOnFlush(inboundChannel);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		Channel channel = e.getChannel();
		logger.info("Caught an exception on Terminator to Remote Server! ",
				e.getCause());

		ChannelUtil.closeOnFlush(channel);
	}

	protected void writeObject(MessageEvent e, Object response) {
		synchronized (trafficLock) {
			httpRelayListener.onRelayResponse(response);
			Channel outboundChannel = e.getChannel();
			inboundChannel.write(response);
			// If inboundChannel is saturated, do not read until
			// notified in
			// HexDumpProxyInboundHandler.channelInterestChanged().
			if (!inboundChannel.isWritable()) {
				outboundChannel.setReadable(false);
			}
		}
	}

}