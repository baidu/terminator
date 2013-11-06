/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.manager.common.log.LinkLogger;

@Service("logService")
public class LogServiceImpl implements LogService {

	@Resource(name = "linkControlService")
	private LinkControlService linkControlService;

	@Override
	public void deleteLog(int linkId) throws LinkStatusException {
		Link link = linkControlService.getLink(linkId);
		if (link != null) {
			throw new LinkStatusException("Need_Link_Shut_Down");
		} else {
			LinkLogger.shutdownLogManager(linkId);

			String logFileLocation = LinkLogger.getLogFileLocation(linkId);
			File log = new File(logFileLocation);
			if (log.exists()) {
				log.delete();
			}
		}
	}

}
