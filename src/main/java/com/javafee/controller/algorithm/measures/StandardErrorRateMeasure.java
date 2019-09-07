package com.javafee.controller.algorithm.measures;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.process.VectorProcess;

public class StandardErrorRateMeasure extends StandardQualityMeasure implements QualityMeasure {
	public StandardErrorRateMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		super(data, decisionRules, decisionRule);
	}

	@Override
	public Double calculate() {
		Long result = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), getDecisionRule());
		return result.doubleValue() / getData().size();
	}

	@Override
	public Double calculateAverage() {
		Double sum = 0.0;
		Long result;
		for (LogicalExpression decisionRule : getDecisionRules()) {
			result = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), decisionRule);
			sum += result / Double.valueOf(getData().size());
		}
		return sum / getDecisionRules().size();
	}
}
