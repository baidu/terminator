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

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.extractor.Extractor;
import com.baidu.terminator.plugin.signer.Signer;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.util.ChannelUtil;
import com.baidu.terminator.storage.Storage;

public abstract class BaseServerHandler<Request> extends
		SimpleChannelUpstreamHandler {

	private Logger logger;

	protected ServerContext context;
	protected Storage storage;
	protected Signer signer;
	protected Extractor extractor;

	protected final Object trafficLock = new Object();
	protected volatile Channel inboundChannel;
	protected volatile Channel outboundChannel;

	protected Request request;

	public BaseServerHandler(ServerContext context) {
		this.context = context;
		Link link = context.getLink();
		this.storage = link.getStorage();
		this.signer = link.getRequestSigner();
		this.extractor = link.getRequestExtractor();
		this.logger = LinkLogger.getLogger(BaseServerHandler.class, context
				.getLink().getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		request = (Request) e.getMessage();
		writeObject(e, request);
	}

	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		// If inboundChannel is not saturated anymore, continue accepting
		// the incoming traffic from the outboundChannel.
		synchronized (trafficLock) {
			Channel inboundChannel = e.getChannel();
			if (inboundChannel.isWritable()) {
				if (outboundChannel != null) {
					outboundChannel.setReadable(true);
				}
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		if (outboundChannel != null) {
			ChannelUtil.closeOnFlush(outboundChannel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		Channel channel = e.getChannel();
		logger.info("Caught an exception on client to Terminator! ",
				e.getCause());

		ChannelUtil.closeOnFlush(channel);
	}

	protected void writeObject(MessageEvent e, Object data) {
		synchronized (trafficLock) {
			Channel inboundChannel = e.getChannel();
			outboundChannel.write(data);
			// If outboundChannel is saturated, do not read until notified in
			// OutboundHandler.channelInterestChanged().
			if (!outboundChannel.isWritable()) {
				inboundChannel.setReadable(false);
			}
		}
	}

}
