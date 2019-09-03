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

	@Override
	public Double calculate() {
		Pair<Long> results = VectorProcess.countRowsWithEqualAttributesAndEqualDecisionValueAndRowsWithEqualDecisionValue(getData(), getDecisionRule());
		return results.getSecond().doubleValue() / results.getFirst().doubleValue();
	}

	@Override
	public Double calculateAverage() {
		Double sum = 0.0;
		Pair<Long> results;
		for (LogicalExpression decisionRule : getDecisionRules()) {
			results = VectorProcess.countRowsWithEqualAttributesAndEqualDecisionValueAndRowsWithEqualDecisionValue(getData(), decisionRule);
			sum += results.getSecond().doubleValue() / results.getFirst().doubleValue();
		}
		return sum / getDecisionRules().size();
	}
}
