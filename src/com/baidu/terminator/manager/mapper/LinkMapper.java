/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.mapper;

import java.util.List;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.mybatis.MybatisMapper;

@MybatisMapper
public interface LinkMapper {

	public List<Link> selectAllLink();

	public Link selectLinkById(int id);

	public List<Link> selectLinkByName(String name);

	public int countValidPort(int port);

	public void insertLink(Link link);

	public void updateLink(Link link);

	public void deleteLink(int id);

}
