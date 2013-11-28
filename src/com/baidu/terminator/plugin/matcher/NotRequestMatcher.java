package com.baidu.terminator.plugin.matcher;

import java.util.List;

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.plugin.extractor.RequestElement;

public class NotRequestMatcher implements RequestMatcher {

	@Override
	public boolean isMatch(List<StubCondition> conditions,
			List<RequestElement> elements) {
		int i = 0;
		boolean isMatch = false;
		for (; i < conditions.size(); i++) {
			StubCondition condition = conditions.get(i);
			for (RequestElement re : elements) {
				if (condition.getKey().equals(re.getKey())
						&& condition.getValue().equals(re.getValue())) {
					isMatch = true;
					break;
				}
			}
			if (isMatch) {
				break;
			}
		}
		return !isMatch;
	}

}
