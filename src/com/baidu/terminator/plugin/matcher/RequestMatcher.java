package com.baidu.terminator.plugin.matcher;

import java.util.List;

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.plugin.extractor.RequestElement;

public interface RequestMatcher {

	public boolean isMatch(List<StubCondition> conditions,
			List<RequestElement> elements);

}
