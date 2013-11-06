/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.storage.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.signer.Signer;
import com.baidu.terminator.storage.RequestResponseBundle;
import com.baidu.terminator.storage.Storage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheStorage implements Storage {

	private Logger logger;

	public CacheStorage(Link link) {
		this.logger = LinkLogger.getLogger(CacheStorage.class, link.getId());
	}

	private LoadingCache<Object, RequestResponseBundle> recordDataCache = CacheBuilder
			.newBuilder().maximumSize(1000).expireAfterWrite(7, TimeUnit.DAYS)
			.build(new CacheLoader<Object, RequestResponseBundle>() {
				public RequestResponseBundle load(Object key) {
					return null;
				}
			});

	private AtomicInteger id = new AtomicInteger(0);
	private List<StubData> stubDataCache = new ArrayList<StubData>(10);

	@Override
	public void signerChanged(Signer signer) {
		for (Entry<Object, RequestResponseBundle> entry : recordDataCache
				.asMap().entrySet()) {
			RequestResponseBundle bundle = entry.getValue();
			Object request = bundle.getRequest();
			String key = signer.sign(request);
			recordDataCache.put(key, bundle);
		}
	}

	@Override
	public void putRecordData(String key, RequestResponseBundle bundles) {
		recordDataCache.put(key, bundles);
		logger.info("[PUT] data record storage in cache success!");
	}

	@Override
	public RequestResponseBundle getRecordData(String key) {
		RequestResponseBundle bundle = recordDataCache.getIfPresent(key);
		if (bundle == null) {
			logger.info("[GET] the request does not hit the cache storage!");
			bundle = new RequestResponseBundle();
		} else {
			logger.info("[GET] the request hit the cache storage!");
		}
		return bundle;
	}

	@Override
	public void deleteRecordData(int version) {

	}

	@Override
	public synchronized int putStubData(StubData stubData) {
		if (stubDataCache.size() > 100) {
			logger.info("the cache storage is full, please use file storage if you want to storage more data!");
		}

		id.incrementAndGet();
		stubData.setId(id.intValue());
		stubDataCache.add(stubData);
		return id.intValue();
	}

	@Override
	public List<StubData> getStubData() {
		return stubDataCache;
	}

	@Override
	public void deleteStubData(int id) {
		Iterator<StubData> iterator = stubDataCache.iterator();
		while (iterator.hasNext()) {
			StubData data = iterator.next();
			if (data.getId() == id) {
				iterator.remove();
				break;
			}
		}
	}

}
