/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import javax.annotation.Resource;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.ServerStatus;
import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.storage.Storage;

public class BaseLinkService {

	@Resource(name = "linkService")
	protected LinkService linkService;

	@Resource(name = "linkControlService")
	protected LinkControlService linkControlService;

	public Link getLink(int linkId) {
		Link link = linkControlService.getLink(linkId);
		return link;
	}

	public void checkLinkStatus(int linkId, ServerStatus status) {
		Link link = getLink(linkId);

		if (ServerStatus.running == status) {
			if (link == null) {
				throw new LinkStatusException(
						"This link has been closed, please start up first!");
			}
		}

		if (ServerStatus.stopped == status) {
			if (link != null) {
				throw new LinkStatusException(
						"This link is working now, please shut down first!");
			}
		}
	}

	public Storage getActiveStorage(int linkId) {
		checkLinkStatus(linkId, ServerStatus.running);
		Link link = linkControlService.getLink(linkId);
		Storage storage = link.getStorage();
		return storage;
	}

}
