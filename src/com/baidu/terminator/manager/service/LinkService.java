/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import java.util.List;
import java.util.Set;

import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.manager.bo.Link;

public interface LinkService {

	public List<Link> getAllLink();

	public Link getLinkById(Integer id);

	public List<Link> getLinkByName(String name);

	public int addLink(Link link);

	public void modifyLink(Link link);

	public void deleteLink(int id);

	public boolean isPortAvailable(int port);

	public Set<String> getSupportedProtocols();

	public List<PluginInfo> getDefaultSigners();

	public List<PluginInfo> getCustomizedSigners();
	
	public List<PluginInfo> getDefaultExtractors();

	public List<PluginInfo> getCustomizedExtractors();

}
