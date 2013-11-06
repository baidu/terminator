/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.baidu.terminator.manager.bo.Log;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.manager.service.LogService;

@Controller("logAction")
@RequestMapping(value = "/log")
public class LogAction {

	private static final long MAX_READ_BYTES = 1024 * 512;

	private final static org.apache.commons.logging.Log LOGGER = LogFactory
			.getLog(LogAction.class);

	@Resource(name = "logService")
	private LogService logService;

	@RequestMapping(value = "/{linkId}/{offset}", method = RequestMethod.GET)
	public ResponseEntity<Log> getLog(@PathVariable int linkId,
			@PathVariable long offset) {
		String logFileLocation = LinkLogger.getLogFileLocation(linkId);

		RandomAccessFile raf = null;
		List<String> lines = new ArrayList<String>();
		long length = 0;

		try {
			raf = new RandomAccessFile(logFileLocation, "r");
			raf.seek(offset);
			length = raf.length();

			long point = raf.getFilePointer();
			while (point < length) {
				String line = raf.readLine();
				String utf8Line = new String(line.getBytes("8859_1"), "utf-8");
				lines.add(utf8Line);

				if (point - offset >= MAX_READ_BYTES) {
					length = point;
					break;
				}
				point = raf.getFilePointer();
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Can not find the file: " + logFileLocation, e);
			throw new RuntimeException("LogFile_Not_Found");
		} catch (IOException e) {
			LOGGER.error("Can not read the file: " + logFileLocation, e);
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					LOGGER.error("Can not close the random access resource: "
							+ logFileLocation, e);
				}
			}
		}

		Log log = new Log();
		log.setLogLocation(logFileLocation);
		log.setOffset(length);
		log.setContent(lines);

		return new ResponseEntity<Log>(log, HttpStatus.OK);
	}

	@RequestMapping(value = "/{linkId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLog(@PathVariable int linkId) {
		logService.deleteLog(linkId);
	}

}
