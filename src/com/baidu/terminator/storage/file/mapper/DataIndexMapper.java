/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.storage.file.mapper;

import java.util.List;
import java.util.Map;

import com.baidu.terminator.manager.common.mybatis.MybatisMapper;
import com.baidu.terminator.storage.file.bo.DataIndex;

@MybatisMapper
public interface DataIndexMapper {

	public DataIndex selectDataIndex(Map<String, Object> parameters);

	public DataIndex selectDataIndexByID(long id);

	public List<Long> selectDataIndexIDs(int linkId);

	public void insertDataIndex(DataIndex recordData);

	public void updateDataIndexSign(Map<String, Object> parameters);

	public void deleteDataIndex(int linkId);

}