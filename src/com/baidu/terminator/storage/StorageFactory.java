/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.storage;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.StorageType;
import com.baidu.terminator.storage.cache.CacheStorage;
import com.baidu.terminator.storage.file.FileStorage;

public class StorageFactory {

	public static Storage getStorage(Link link) {
		StorageType type = link.getStorageType();

		Storage storage = new CacheStorage(link);
		if (StorageType.FILE == type) {
			storage = new FileStorage(link);
		}

		return storage;
	}

}
