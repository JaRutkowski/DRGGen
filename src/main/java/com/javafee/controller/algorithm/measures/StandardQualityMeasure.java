package com.javafee.controller.algorithm.measures;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;

import lombok.Getter;

public class StandardQualityMeasure {
	protected boolean dynamicCalculation = false;

	protected List<Double> results = new ArrayList<>();

	public StandardQualityMeasure(Vector<Vector> data, List<LogicalExpression> decisionRules, LogicalExpression decisionRule) {
		this.data = data;
		this.decisionRules = decisionRules;
		this.decisionRule = decisionRule;
	}

	@Getter
	private Vector<Vector> data;

	@Getter
	private List<LogicalExpression> decisionRules;

	@Getter
	private LogicalExpression decisionRule;
}
