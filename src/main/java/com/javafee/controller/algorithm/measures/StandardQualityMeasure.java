package com.javafee.controller.algorithm.measures;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StandardQualityMeasure {
	@Getter
	private Vector<Vector> data = null;

	@Getter
	private List<LogicalExpression> decisionRules = null;

	@Getter
	private LogicalExpression decisionRule = null;
}
