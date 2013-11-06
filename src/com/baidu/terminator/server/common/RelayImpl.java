/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.server.common;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.server.Relay;
import com.baidu.terminator.server.ServerContext;

public class RelayImpl implements Relay {

	private ServerContext context;

	public RelayImpl(ServerContext context) {
		this.context = context;
	}

	public ChannelFuture start(ChannelPipelineFactory factory) {
		ClientBootstrap clientBootstrap = new ClientBootstrap();
		clientBootstrap.setFactory(context.getRelaySocketChannelFactory());
		clientBootstrap.setPipelineFactory(factory);

		Link link = context.getLink();
		ChannelFuture relayChannelFuture = clientBootstrap
				.connect(new InetSocketAddress(link.getRemoteAddress(), link
						.getRemotePort()));
		return relayChannelFuture;
	}

}
