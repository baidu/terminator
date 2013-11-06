/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.action;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.form.StubForm;
import com.baidu.terminator.manager.service.StubService;

@Controller("stubAction")
@RequestMapping(value = "/stub", produces = MediaType.APPLICATION_JSON_VALUE)
public class StubAction extends BaseAction {

	@Resource(name = "stubService")
	private StubService stubService;

	@RequestMapping(value = "/{linkId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<StubData>> listStubData(@PathVariable int linkId) {
		List<StubData> stubData = stubService.getStubData(linkId);
		return new ResponseEntity<List<StubData>>(stubData, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{linkId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> addStubData(@PathVariable int linkId,
			@Valid @RequestBody StubForm stubForm) {
		List<StubCondition> conditions = stubForm.getConditions();
		String response = stubForm.getResponse();

		StubData stubData = new StubData();
		stubData.setConditions(conditions);
		stubData.setResponse(response);
		stubData.setDelay(stubForm.getDelay());

		int id = stubService.addStubData(linkId, stubData);
		StringBuilder uri = new StringBuilder(20);
		uri.append("/stub/").append(linkId).append("/").append(id);
		return new ResponseEntity(setLocationHeaders(uri.toString()),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{linkId}/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void listStubData(@PathVariable int linkId, @PathVariable int id) {
		stubService.deleteStubData(linkId, id);
	}

	@RequestMapping(value = "/{linkId}/extractor", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PluginInfo> getExtractorMessage(
			@PathVariable int linkId) {
		PluginInfo pluginInfo = stubService.getExtractor(linkId);
		return new ResponseEntity<PluginInfo>(pluginInfo, HttpStatus.OK);
	}

}
