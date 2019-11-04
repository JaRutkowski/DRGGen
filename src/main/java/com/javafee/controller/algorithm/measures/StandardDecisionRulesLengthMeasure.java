package com.javafee.controller.algorithm.measures;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;

public class StandardDecisionRulesLengthMeasure extends StandardQualityMeasure implements QualityMeasure {
	public StandardDecisionRulesLengthMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		super(data, decisionRules, decisionRule);
	}

	public StandardDecisionRulesLengthMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule,
	                                          boolean dynamicCalculation) {
		super(data, decisionRules, decisionRule);
		this.dynamicCalculation = dynamicCalculation;
		calculateDynamicForEachRow();
	}

	@Override
	public Double calculate() {
		return Double.valueOf(getDecisionRule().length());
	}

	@Override
	public Double calculateAverage() {
		if (!dynamicCalculation) calculateDynamicForEachRow();
		return results.stream().mapToDouble(val -> val).average().orElse(0.0);
	}

	@Override
	public Double calculateMax() {
		if (!dynamicCalculation) calculateDynamicForEachRow();
		return results.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
	}

	@Override
	public Double calculateMin() {
		if (!dynamicCalculation) calculateDynamicForEachRow();
		return results.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
	}

	private void calculateDynamicForEachRow() {
		for (LogicalExpression decisionRule : getDecisionRules()) {
			this.results.add(Double.valueOf(decisionRule.length()));
		}
	}
}
