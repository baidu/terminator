/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.action;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.manager.service.RecordService;

@Controller("recordAction")
@RequestMapping(value = "/record", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordAction {

	@Resource(name = "recordService")
	private RecordService recordService;

	@RequestMapping(value = "/{linkId}/{version}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRecord(@PathVariable int linkId,
			@PathVariable int version) {
		try {
			recordService.deleteRecord(linkId, version);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch (LinkStatusException e) {
			return new ResponseEntity<String>(e.getMessage(),
					HttpStatus.METHOD_NOT_ALLOWED);
		}
	}

}
