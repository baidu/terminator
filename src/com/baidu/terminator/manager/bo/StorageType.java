/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.bo;

public enum StorageType {
	// add DATABASE type for page at time, TODO: optimistic
	CACHE("com.baidu.terminator.storage.cache.CacheStorage"), 
	FILE("com.baidu.terminator.storage.file.FileStorage"), 
	DATABASE("com.baidu.terminator.storage.file.FileStorage"), 
	STUBFILE("com.baidu.terminator.storage.stubstorage.StubFileStorage");
	
    private String cls;  
    private StorageType(String cls) {  
        this.setCls(cls);  
    }  
    /** 
     * @param cls the cls to set 
     */  
    private void setCls(String cls) {  
        this.cls = cls;  
    }  
    /** 
     * @return the cls 
     */  
    public String getCls() {  
        return cls;  
    }  
}
