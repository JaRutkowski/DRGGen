package com.javafee.controller.algorithm.datastructure;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class LogicalExpression {
	@Getter
	private List<LogicalAttributeValuePair> logicalAttributeValuePairList = new ArrayList<>();
	private List<LogicalOperator> logicalOperatorList = new ArrayList<>();
	@Getter
	private String logicalValue;

	boolean logicalAttributeFlag = false;
	boolean logicalOperatorFlag = true;
	boolean logicalValueFlag = false;

	public void append(LogicalAttributeValuePair logicalAttributeValuePair) {
		logicalAttributeFlag = true;
		if (logicalOperatorFlag && !logicalValueFlag)
			logicalAttributeValuePairList.add(logicalAttributeValuePair);
		logicalOperatorFlag = false;
	}

	public void append(LogicalOperator logicalOperator) {
		logicalOperatorFlag = true;
		if (logicalAttributeFlag && !logicalValueFlag)
			logicalOperatorList.add(logicalOperator);
		logicalAttributeFlag = false;
	}

	public void append(String value) {
		logicalValue = value;
		logicalValueFlag = true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int index = 0;
		int operatorIndex = 0;
		for (LogicalAttributeValuePair logicalAttributeValuePair : logicalAttributeValuePairList) {
			result.append(logicalAttributeValuePair);
			if (index != logicalAttributeValuePairList.size() - 1)
				result.append(" " + logicalOperatorList.get(operatorIndex).getLogicalOperatorName() + " ");
			index++;
		}
		result.append(" -> ").append(logicalValue);
		return result.toString();
	}
}
