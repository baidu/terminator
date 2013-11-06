/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.register;

import java.util.List;
import java.util.Set;

import org.jboss.netty.channel.ChannelPipelineFactory;

import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.plugin.extractor.Extractor;
import com.baidu.terminator.plugin.signer.Signer;

public interface ProtocolRegister {

	public Set<String> getSupportedProtocols();

	public Class<? extends ChannelPipelineFactory> getChannelPipelineFactory(
			String protocal);

	public Class<? extends Signer> getDefaultSignerMap(
			String protocal);

	public List<PluginInfo> getDefaultSigners();

	public List<PluginInfo> getCustomizedSigners();

	public Class<? extends Extractor> getDefaultExtractorMap(
			String protocal);
	
	public List<PluginInfo> getDefaultExtractors();

	public List<PluginInfo> getCustomizedExtractors();

}
