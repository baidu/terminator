package com.baidu.terminator.plugin.matcher;

import java.util.Collections;
import java.util.List;

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.plugin.extractor.RequestElement;

public class AndRequestMatcher implements RequestMatcher {

	@Override
	public boolean isMatch(List<StubCondition> conditions,
			List<RequestElement> elements) {
		Collections.sort(conditions);
		int i = 0;
		for (; i < conditions.size(); i++) {
			StubCondition condition = conditions.get(i);

			boolean isMatch = false;
			for (RequestElement re : elements) {
				if (condition.getKey().equals(re.getKey())
						&& condition.getValue().equals(re.getValue())) {
					isMatch = true;
					break;
				}
			}

			if (!isMatch) {
				break;
			}
		}

		return conditions.size() == i;
	}

}
