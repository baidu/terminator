/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.storage.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.common.Config;
import com.baidu.terminator.manager.common.SpringInit;
import com.baidu.terminator.manager.common.file.FileUtils;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.signer.Signer;
import com.baidu.terminator.storage.RequestResponseBundle;
import com.baidu.terminator.storage.Storage;
import com.baidu.terminator.storage.file.bo.DataIndex;
import com.baidu.terminator.storage.file.mapper.DataIndexMapper;
import com.baidu.terminator.storage.file.mapper.StubDataMapper;

/**
 * @author xingpei
 * @author zhangjunjun
 * 
 */
public class FileStorage implements Storage {

	private Logger logger;

	private static final String DEFAULT_DATA_FILE_NAME = "data";

	private Link link;

	private String filePath;

	private DataIndexMapper dataIndexMapper;
	private StubDataMapper stubDataMapper;

	public FileStorage(Link link) {
		this.link = link;
		dataIndexMapper = (DataIndexMapper) SpringInit
				.getBean("dataIndexMapper");
		stubDataMapper = (StubDataMapper) SpringInit.getBean("stubDataMapper");
		this.logger = LinkLogger
				.getLogger(FileStorage.class, this.link.getId());

		this.filePath = getStorageFileLocation();
		FileUtils.createFile(filePath);
	}

	private String getStorageFileLocation() {
		Config config = (Config) SpringInit.getBean("config");
		String baseStorageDir = config.getBaseStorageDir();
		if (StringUtils.isBlank(baseStorageDir)) {
			if (Config.getOperationSystem().startsWith("Windows")) {
				baseStorageDir = Config.DEFAULT_WIN_STORAGE_DIR;
			} else {
				baseStorageDir = Config.DEFAULT_LINUX_STORAGE_DIR;
			}
		}
		String linkDir = baseStorageDir + File.separator + link.getId();
		String filePath = linkDir + File.separator + DEFAULT_DATA_FILE_NAME;
		return filePath;
	}

	@Override
	public void signerChanged(Signer signer) {
		int linkId = this.link.getId();
		// get ids from database
		List<Long> dataidList = dataIndexMapper.selectDataIndexIDs(linkId);

		// load requestresponse from file
		// update database signature
		for (int i = 0; i < dataidList.size(); i++) {
			RequestResponseBundle bundles = (RequestResponseBundle) this
					.getbyID(dataidList.get(i));
			Object request = bundles.getRequest();
			String key = signer.sign(request);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", dataidList.get(i));
			map.put("signature", key);
			dataIndexMapper.updateDataIndexSign(map);
		}
	}

	public Object getbyID(Long dataId) {
		// load from database
		DataIndex recordData = dataIndexMapper.selectDataIndexByID(dataId);
		RequestResponseBundle bundles = new RequestResponseBundle();

		// read from file
		if (recordData != null) {
			Long offset;
			offset = recordData.getOffset();
			String filePath = this.filePath;
			try {
				FileInputStream dataStream = new FileInputStream(filePath);
				dataStream.skip(offset);
				ObjectInputStream objStream = new ObjectInputStream(dataStream);
				bundles = (RequestResponseBundle) objStream.readObject();
				objStream.close();
				dataStream.close();
			} catch (Exception e) {
				logger.error("[UPDATE] file data does not exists");
				System.out.println("[UPDATE] file data does not exists!");
				e.printStackTrace();
			}
		}

		return bundles;
	}

	@Override
	public synchronized void putRecordData(String key, RequestResponseBundle bundles) {
		int linkId = this.link.getId();
		File dataFile = new File(filePath);

		Date addtime = new Date();
		Long offset = dataFile.length();

		// write to file
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(dataFile, true);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(bundles);
		} catch (IOException e) {
			logger.error("[PUT] data record storage in file fail!", e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				logger.error("[PUT]	close file output stream error!", e);
			}
		}

		// write record offset to database
		DataIndex dataIndex = new DataIndex();
		dataIndex.setLinkId(linkId);
		dataIndex.setOffset(offset);
		dataIndex.setSignature((String) key);
		dataIndex.setAddTime(addtime);
		dataIndexMapper.insertDataIndex(dataIndex);
		logger.info("[PUT] data record storage in file success!");
	}

	@Override
	public RequestResponseBundle getRecordData(String key) {
		// load record offset from database
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("linkId", link.getId());
		parameters.put("signature", key);
		DataIndex recordData = dataIndexMapper.selectDataIndex(parameters);

		// read data record from file
		RequestResponseBundle bundles = new RequestResponseBundle();
		if (recordData != null) {
			logger.info("[GET] the request hit the file storage!");
			Long offset = recordData.getOffset();

			FileInputStream fis = null;
			ObjectInputStream ois = null;
			try {
				fis = new FileInputStream(filePath);
				fis.skip(offset);
				ois = new ObjectInputStream(fis);
				bundles = (RequestResponseBundle) ois.readObject();
			} catch (FileNotFoundException e) {
				logger.error("[GET] can not find data file: " + filePath, e);
			} catch (IOException e) {
				logger.error("[GET] data record from storage in file fail!", e);
			} catch (ClassNotFoundException e) {
				logger.error("[GET] data record from storage in file fail!", e);
			} finally {
				try {
					if (ois != null) {
						ois.close();
					}
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					logger.error("[GET]	close file output stream error", e);
				}
			}
		} else {
			logger.info("[GET] the request does not hit the file storage!");
		}

		return bundles;
	}

	@Override
	public void deleteRecordData(int version) {
		// delete database record
		dataIndexMapper.deleteDataIndex(link.getId());

		// delete file
		File dataFile = new File(filePath);
		if (dataFile.exists()) {
			if (dataFile.delete()) {
				logger.info("[DELETE] data file delete success!");
			} else {
				logger.info("[DELETE] data file delete fail!");
			}
		}
	}

	@Override
	public int putStubData(StubData stubData) {
		stubData.setLinkId(link.getId());
		stubDataMapper.insertStubData(stubData);
		int stubDataId = stubData.getId();
		for (StubCondition con : stubData.getConditions()) {
			con.setStubDataId(stubDataId);
			stubDataMapper.insertStubCondition(con);
		}
		return stubDataId;
	}

	@Override
	public List<StubData> getStubData() {
		List<StubData> stubData = stubDataMapper.selectStubData(link.getId());
		return stubData;
	}

	@Override
	public void deleteStubData(int id) {
		stubDataMapper.deleteStubData(id);
		stubDataMapper.deleteStubCondition(id);
	}

}
