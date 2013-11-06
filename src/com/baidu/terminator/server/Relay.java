/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.server;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;

public interface Relay {

	public ChannelFuture start(ChannelPipelineFactory factory);

}
