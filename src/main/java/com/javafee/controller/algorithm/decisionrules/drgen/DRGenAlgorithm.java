package com.javafee.controller.algorithm.decisionrules.drgen;

import java.util.List;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.RowsSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DRGenAlgorithm {
	private RowsSet rowsSet;
	private List<RowsSet> rowsSetList;
	private DRGenerator drGeneratorStrategy;

	private List<RowsSet> resultCoverageRowsSet = null;

	public DRGenAlgorithm(RowsSet rowsSet, List<RowsSet> rowsSetList, DRGenerator drGeneratorStrategy) {
		this.rowsSet = rowsSet;
		this.rowsSetList = rowsSetList;
		this.drGeneratorStrategy = drGeneratorStrategy;
	}

	public LogicalExpression generateDecisionRules() {
		LogicalExpression logicalExpression = drGeneratorStrategy.generateDecisionRules(rowsSet, rowsSetList);
		resultCoverageRowsSet = drGeneratorStrategy.getResultCoverageRowsSet();
		return logicalExpression;
	}
}
