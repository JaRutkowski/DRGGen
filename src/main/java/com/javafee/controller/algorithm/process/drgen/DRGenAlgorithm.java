package com.javafee.controller.algorithm.process.drgen;

import java.util.List;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.RowsSet;

public interface DRGenAlgorithm {
	List<RowsSet> getResultCoverageRowsSet();

	LogicalExpression generateDecisionRules(RowsSet concernedRowsSet, List<RowsSet> rowsSetList);
}
