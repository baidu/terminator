/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.bo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class StubCondition implements Comparable<StubCondition> {

	public enum Operator {
		and
	};

	private Integer stubDataId;

	private String key;

	private String value;

	private int sequence;

	private Operator operator;

	public Integer getStubDataId() {
		return stubDataId;
	}

	public void setStubDataId(Integer stubDataId) {
		this.stubDataId = stubDataId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int compareTo(StubCondition condition) {
		int s = condition.getSequence();
		return (this.sequence > s) ? 1 : ((this.sequence < s) ? -1 : 0);
	}

}
