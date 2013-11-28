package com.baidu.terminator.server.common.util;

import java.util.Comparator;

import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.bo.StubData.Operator;

public class StubMatcherComparator implements Comparator<StubData> {
	public int compare(StubData stubData1, StubData stubData2) {
		Operator operator1 =stubData1.getOperator();
		Operator operator2 = stubData2.getOperator();
		if (operator1.equals(Operator.not)) {// 若前面的元素为not，则不需要交换
			return -1;
		} else if (operator1.equals(Operator.and)
				&& operator2.equals(Operator.not)) {// 若前面元素为and，后面元素为not，则需要替换
			return 1;
		} else if (operator1.equals(Operator.or)) {// 若前面为or，则一定需要替换
			return 1;
		} else {// 否则不进行交互
			return 0;
		}
	}

}
