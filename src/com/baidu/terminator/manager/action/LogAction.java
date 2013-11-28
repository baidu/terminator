/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.action;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.baidu.terminator.manager.bo.Log;
import com.baidu.terminator.manager.service.LogService;

@Controller("logAction")
@RequestMapping(value = "/log")
public class LogAction {

	@Resource(name = "logService")
	private LogService logService;

	@RequestMapping(value = "/{linkId}/{offset}", method = RequestMethod.GET)
	public ResponseEntity<Log> getLog(@PathVariable int linkId,
			@PathVariable long offset) throws IOException {
		Log log = logService.readLog(linkId, offset);
		return new ResponseEntity<Log>(log, HttpStatus.OK);
	}

	@RequestMapping(value = "/{linkId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLog(@PathVariable int linkId) {
		logService.deleteLog(linkId);
	}

}
