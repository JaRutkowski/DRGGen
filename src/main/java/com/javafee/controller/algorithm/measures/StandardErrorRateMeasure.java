package com.javafee.controller.algorithm.measures;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.process.VectorProcess;

public class StandardErrorRateMeasure extends StandardQualityMeasure implements QualityMeasure {
	public StandardErrorRateMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		super(data, decisionRules, decisionRule);
	}

	public StandardErrorRateMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule,
	                                boolean dynamicCalculation) {
		super(data, decisionRules, decisionRule);
		this.dynamicCalculation = dynamicCalculation;
		calculateDynamicForEachRow();
	}

	@Override
	public Double calculate() {
		Long result = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), getDecisionRule());
		return result.doubleValue() / getData().size();
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
		Long result;
		for (LogicalExpression decisionRule : getDecisionRules()) {
			result = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), decisionRule);
			this.results.add(result / Double.valueOf(getData().size()));
		}
	}
}
