/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.manager.mapper.LinkMapper;
import com.baidu.terminator.register.ProtocolRegister;

@Service("linkService")
public class LinkServiceImpl implements LinkService {

	@Resource(name = "linkMapper")
	private LinkMapper linkMapper;

	@Resource(name = "protocolRegister")
	private ProtocolRegister protocolRegister;

	@Override
	public List<Link> getAllLink() {
		List<Link> allLink = linkMapper.selectAllLink();
		return allLink;
	}

	@Override
	public Link getLinkById(Integer id) {
		Link link = linkMapper.selectLinkById(id);
		return link;
	}

	@Override
	public List<Link> getLinkByName(String name) {
		if (StringUtils.isNotBlank(name)) {
			return linkMapper.selectLinkByName("%" + name + "%");
		} else {
			return getAllLink();
		}
	}

	@Override
	public void addLink(Link link) {
		if (link.getLocalPort() == null) {
			link.setLocalPort(distributeLocalPort());
		}

		linkMapper.insertLink(link);
	}

	@Override
	public void modifyLink(Link link) {
		linkMapper.updateLink(link);
	}

	@Override
	public void deleteLink(int id) {
		linkMapper.deleteLink(id);
	}

	@Override
	public boolean isPortAvailable(int port) {
		int count = linkMapper.countValidPort(port);
		if (count >= 1) {
			return false;
		} else {
			try {
				ServerSocket s = new ServerSocket(port);
				s.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<String> getSupportedProtocols() {
		return protocolRegister.getSupportedProtocols();
	}

	@Override
	public List<PluginInfo> getDefaultSigners() {
		List<PluginInfo> signers = protocolRegister.getDefaultSigners();
		return signers;
	}

	@Override
	public List<PluginInfo> getCustomizedSigners() {
		List<PluginInfo> signers = protocolRegister.getCustomizedSigners();
		return signers;
	}

	@Override
	public List<PluginInfo> getDefaultExtractors() {
		List<PluginInfo> extractors = protocolRegister.getDefaultExtractors();
		return extractors;
	}

	@Override
	public List<PluginInfo> getCustomizedExtractors() {
		List<PluginInfo> extractors = protocolRegister
				.getCustomizedExtractors();
		return extractors;
	}

	private int distributeLocalPort() {
		for (int i = 8001; i < 50000; i++) {
			if (isPortAvailable(i)) {
				return i;
			}
		}
		return -1;
	}

}
