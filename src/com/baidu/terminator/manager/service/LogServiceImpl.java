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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.Log;
import com.baidu.terminator.manager.bo.ServerStatus;
import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.manager.common.file.FileUtils;
import com.baidu.terminator.manager.common.log.LinkLogger;

@Service("logService")
public class LogServiceImpl extends BaseLinkService implements LogService {

	private static final long MAX_READ_BYTES = 1024 * 512;

	@Override
	public Log readLog(int linkId, long offset) throws IOException {
		String logFileLocation = LinkLogger.getLogFileLocation(linkId);
		FileUtils.createFile(logFileLocation);

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
		} finally {
			if (raf != null) {
				raf.close();
			}
		}

		Log log = new Log();
		log.setLogLocation(logFileLocation);
		log.setOffset(length);
		log.setContent(lines);
		return log;
	}

	@Override
	public void deleteLog(int linkId) throws LinkStatusException {
		checkLinkStatus(linkId, ServerStatus.stopped);
		LinkLogger.shutdownLogManager(linkId);

		String logFileLocation = LinkLogger.getLogFileLocation(linkId);
		File log = new File(logFileLocation);
		if (log.exists()) {
			log.delete();
		}
	}

}
