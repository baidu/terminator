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

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.common.mybatis.MybatisMapper;

@MybatisMapper
public interface StubDataMapper {

	public int insertStubData(StubData stubData);

	public List<StubData> selectStubData(int linkId);

	public void insertStubCondition(StubCondition stubCondition);

	public void deleteStubData(int id);

	public void deleteStubCondition(int stubDataId);

}
