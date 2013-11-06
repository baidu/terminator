/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.manager.common.exception.LinkStatusException;

public interface LinkControlService {

	public void startServer(int linkId);

	public void stopServer(int linkId);

	public void changeWorkMode(int linkId, WorkMode mode)
			throws LinkStatusException;

	public Link getLink(int linkId);

	public Class<?> getPluginClass(Link link, WorkMode mode);

}
