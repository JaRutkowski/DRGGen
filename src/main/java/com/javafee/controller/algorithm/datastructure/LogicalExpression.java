package com.javafee.controller.algorithm.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.Getter;

public class LogicalExpression implements Serializable {
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
		logicalAttributeValuePairList.sort(Comparator.comparing(LogicalAttributeValuePair::getAttributeIndex));
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

	public int length() {
		return logicalAttributeValuePairList.size();
	}

	public boolean isInconsistent(LogicalExpression logicalExpressionToCompare) {
		return (logicalAttributeValuePairList.containsAll(logicalExpressionToCompare.getLogicalAttributeValuePairList())
				&& logicalExpressionToCompare.getLogicalAttributeValuePairList().containsAll(logicalAttributeValuePairList))
				&& !this.logicalValue.equals(logicalExpressionToCompare.getLogicalValue());
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof LogicalExpression)) return false;
		LogicalExpression logicalExpressionObj = (LogicalExpression) obj;
		if (((LogicalExpression) obj).getLogicalAttributeValuePairList().size() != logicalAttributeValuePairList.size())
			return false;
		for (int index = 0; index < logicalExpressionObj.getLogicalAttributeValuePairList().size(); index++) {
			if (!logicalExpressionObj.getLogicalAttributeValuePairList().get(index).equals(this.logicalAttributeValuePairList.get(index)))
				return false;
		}
		if (!logicalExpressionObj.getLogicalValue().equals(logicalValue)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(logicalAttributeValuePairList, logicalOperatorList, logicalValue);
	}
}
