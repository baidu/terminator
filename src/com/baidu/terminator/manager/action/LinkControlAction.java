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

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.manager.service.LinkControlService;

@Controller("linkControlAction")
@RequestMapping(value = "/control")
public class LinkControlAction {

	@Resource(name = "messageSource")
	protected MessageSource messageSource;

	@Resource(name = "linkControlService")
	private LinkControlService linkControlService;

	@RequestMapping(value = "/{workMode}/{linkId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<?> changeWorkMode(@PathVariable Integer linkId,
			@PathVariable WorkMode workMode) {
		try {
			linkControlService.changeWorkMode(linkId, workMode);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch (LinkStatusException e) {
			return new ResponseEntity<String>(e.getMessage(),
					HttpStatus.METHOD_NOT_ALLOWED);
		}
	}

	@RequestMapping(value = "/start/{linkId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> startServer(@PathVariable Integer linkId) {
		linkControlService.startServer(linkId);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/stop/{linkId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void stopServer(@PathVariable Integer linkId) {
		linkControlService.stopServer(linkId);
	}

}
