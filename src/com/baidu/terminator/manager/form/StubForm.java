/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.manager.bo.StubData.Operator;

public class StubForm implements Comparable<StubForm>{
	
	@Size(min = 1, message = "conditions size should greater than zero!")
	private List<StubCondition> conditions = new ArrayList<StubCondition>();

	private int delay;
	
	private Operator operator;
	
	private int sequence;

	@NotNull(message = "response mode should be not null!")
	private String response;

	public List<StubCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<StubCondition> conditions) {
		this.conditions = conditions;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
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

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int compareTo(StubForm stubForm) {
		int s = stubForm.getSequence();
		return (this.sequence > s) ? 1 : ((this.sequence < s) ? -1 : 0);
	}
}
