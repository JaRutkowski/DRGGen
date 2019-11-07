package com.javafee.controller.algorithm.measures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;

public class StandardNumberOfVariousDecisionRulesMeasure extends StandardQualityMeasure {
	public StandardNumberOfVariousDecisionRulesMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		super(data, decisionRules, decisionRule);
	}

	public Integer calculate() {
		Set distinctLogicalExpression = new HashSet();
		for (LogicalExpression logicalExpression : getDecisionRules())
			distinctLogicalExpression.add(logicalExpression);
		getDecisionRules().get(2).equals(getDecisionRules().get(3));
		return distinctLogicalExpression.size();
	}
}
