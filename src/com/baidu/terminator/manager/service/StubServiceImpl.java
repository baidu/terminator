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

import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.register.annotation.CustomizedExtractor;
import com.baidu.terminator.register.annotation.DefaultExtractor;
import com.baidu.terminator.storage.Storage;

@Service("stubService")
public class StubServiceImpl extends BaseLinkService implements StubService {

	@Override
	public List<StubData> getStubData(int linkId) {
		Storage storage = getActiveStorage(linkId);
		List<StubData> stubData = storage.getStubData();
		return stubData;
	}

	@Override
	public int addStubData(int linkId, StubData data) {
		Storage storage = getActiveStorage(linkId);
		storage.putStubData(data);
		return data.getId();
	}

	@Override
	public void deleteStubData(int linkId, int id) {
		Storage storage = getActiveStorage(linkId);
		storage.deleteStubData(id);
	}

	@Override
	public PluginInfo getExtractor(int linkId) {
		Link link = linkService.getLinkById(linkId);
		Class<?> extractClass = linkControlService.getPluginClass(link,
				WorkMode.STUB);

		PluginInfo pi = new PluginInfo();
		pi.setName(link.getExtractClass());

		DefaultExtractor defaultExtractor = extractClass
				.getAnnotation(DefaultExtractor.class);
		if (defaultExtractor != null) {
			pi.setProtocol(defaultExtractor.protocol());
			pi.setMessage(defaultExtractor.message());
		} else {
			CustomizedExtractor customizedExtractor = extractClass
					.getAnnotation(CustomizedExtractor.class);
			pi.setProtocol(customizedExtractor.protocol());
			pi.setMessage(customizedExtractor.message());
		}
		return pi;
	}

}
