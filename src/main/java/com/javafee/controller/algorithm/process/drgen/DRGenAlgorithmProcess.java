package com.javafee.controller.algorithm.process.drgen;

import java.util.List;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.RowsSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DRGenAlgorithmProcess {
	private RowsSet rowsSet;
	private List<RowsSet> rowsSetList;
	private DRGenAlgorithm drGenAlgorithmStrategy;

	private List<RowsSet> resultCoverageRowsSet = null;

	public DRGenAlgorithmProcess(RowsSet rowsSet, List<RowsSet> rowsSetList, DRGenAlgorithm drGenAlgorithmStrategy) {
		this.rowsSet = rowsSet;
		this.rowsSetList = rowsSetList;
		this.drGenAlgorithmStrategy = drGenAlgorithmStrategy;
	}

	public LogicalExpression generateDecisionRules() {
		LogicalExpression logicalExpression = drGenAlgorithmStrategy.generateDecisionRules(rowsSet, rowsSetList);
		resultCoverageRowsSet = drGenAlgorithmStrategy.getResultCoverageRowsSet();
		return logicalExpression;
	}
}
