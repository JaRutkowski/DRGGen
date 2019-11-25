package com.javafee.controller.algorithm.measures;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.process.VectorProcess;

import lombok.Getter;
import lombok.Setter;

public class StandardErrorRateMeasure extends StandardQualityMeasure implements QualityMeasure {
	@Getter
	@Setter
	/**
	 * Calculate support by dividing on decision class set cardinality (non absolute)
	 * or decision attributes set cardinality (absolute).
	 */
	private TYPE type;

	public StandardErrorRateMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		super(data, decisionRules, decisionRule);
	}

	public StandardErrorRateMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule,
	                                boolean dynamicCalculation) {
		super(data, decisionRules, decisionRule);
		this.dynamicCalculation = dynamicCalculation;
		calculateDynamicForEachRow();
	}

	public StandardErrorRateMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule,
	                                boolean dynamicCalculation, TYPE type) {
		super(data, decisionRules, decisionRule);
		this.type = type;
		this.dynamicCalculation = dynamicCalculation;
		if (dynamicCalculation) calculateDynamicForEachRow();
	}

	@Override
	public Double calculate() {
		Double result = null;
		switch (type) {
			case OBJECT_SET:
				Long resultsLong = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), getDecisionRule());
				result = resultsLong.doubleValue() / getData().size();
				break;
			case ABSOLUTE:
				Long resultsLongAbsolute = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), getDecisionRule());
				result = resultsLongAbsolute.doubleValue();
				break;
		}
		return result;
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
		switch (type) {
			case OBJECT_SET:
				Long resultsLong;
				for (LogicalExpression decisionRule : getDecisionRules()) {
					resultsLong = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), decisionRule);
					this.results.add(resultsLong / Double.valueOf(getData().size()));
				}
				break;
			case ABSOLUTE:
				Long resultsLongAbsolute;
				for (LogicalExpression decisionRule : getDecisionRules()) {
					resultsLongAbsolute = VectorProcess.countRowsWithEqualAttributesAndVariousDecisionValue(getData(), decisionRule);
					this.results.add(resultsLongAbsolute.doubleValue());
				}
				break;
		}
	}

	public enum TYPE {
		OBJECT_SET, ABSOLUTE;
	}
}
