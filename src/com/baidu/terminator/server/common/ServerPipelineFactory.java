/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common;

import java.lang.reflect.Constructor;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.manager.common.SpringInit;
import com.baidu.terminator.register.ProtocolRegister;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.tunnel.TunnelServerHandler;

public class ServerPipelineFactory implements ChannelPipelineFactory {

	private ServerContext context;

	private ProtocolRegister protocolRegister;

	public ServerPipelineFactory(ServerContext context) {
		this.context = context;
		this.protocolRegister = (ProtocolRegister) SpringInit
				.getBean("protocolRegister");
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		Link link = context.getLink();

		// regardless of protocol, so long as the work mode is tunnel just use
		// the same pipeline factory
		if (WorkMode.TUNNEL == link.getWorkMode()) {
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("handler", new TunnelServerHandler(context));
			return pipeline;
		}

		String protocol = link.getProtocolType();
		Class<? extends ChannelPipelineFactory> pipelineFactoryClass = protocolRegister
				.getChannelPipelineFactory(protocol);
		Constructor<? extends ChannelPipelineFactory> constructor = pipelineFactoryClass
				.getConstructor(ServerContext.class);
		ChannelPipelineFactory pipelineFactory = constructor
				.newInstance(context);

		return pipelineFactory.getPipeline();
	}

}
