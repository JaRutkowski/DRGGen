package com.javafee.controller.algorithm.measures;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.utils.Pair;
import com.javafee.controller.algorithm.process.VectorProcess;

public class StandardSupportMeasure extends StandardQualityMeasure implements QualityMeasure {

	public StandardSupportMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		super(data, decisionRules, decisionRule);
	}

	public StandardSupportMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule,
	                              boolean dynamicCalculation) {
		super(data, decisionRules, decisionRule);
		this.dynamicCalculation = dynamicCalculation;
		calculateDynamicForEachRow();
	}

	@Override
	public Double calculate() {
		Pair<Long> results = VectorProcess.countRowsWithEqualAttributesAndEqualDecisionValueAndRowsWithEqualDecisionValue(getData(), getDecisionRule());
		return results.getSecond().doubleValue() / results.getFirst().doubleValue();
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
		Pair<Long> results;
		for (LogicalExpression decisionRule : getDecisionRules()) {
			results = VectorProcess.countRowsWithEqualAttributesAndEqualDecisionValueAndRowsWithEqualDecisionValue(getData(), decisionRule);
			this.results.add(results.getSecond().doubleValue() / results.getFirst().doubleValue());
		}
	}
}
