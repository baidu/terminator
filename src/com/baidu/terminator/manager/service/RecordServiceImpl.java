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

import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.storage.Storage;

@Service("recordService")
public class RecordServiceImpl implements RecordService {

	@Resource(name = "linkControlService")
	private LinkControlService linkControlService;

	@Override
	public void deleteRecord(int linkId, int version) throws LinkStatusException {
		Link link = linkControlService.getLink(linkId);
		if (link != null) {
			Storage storage = link.getStorage();
			storage.deleteRecordData(version);
		} else {
			throw new LinkStatusException(
					"The link have not working yet, please start up first!");
		}
	}
	
}
