/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common.tunnel;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.BaseRelayHandler;
import com.baidu.terminator.server.common.record.BaseRecordServerHandler;

public class TunnelServerHandler extends BaseRecordServerHandler<ChannelBuffer> {

	public TunnelServerHandler(ServerContext context) {
		super(context);
	}

	@Override
	public void onRelayResponse(Object response) {
		// DO Nothing
	}

	@Override
	public ChannelPipelineFactory getRelayChannelPipelineFactory(
			RelayListener relayListener) {
		return new TunnelRelayPipelineFactory();
	}

	public class TunnelRelayPipelineFactory implements ChannelPipelineFactory {

		@Override
		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline pipeline = pipeline();

			pipeline.addLast("handler", new BaseRelayHandler<ChannelBuffer>(
					context, inboundChannel, TunnelServerHandler.this));
			return pipeline;
		}

	}

}
