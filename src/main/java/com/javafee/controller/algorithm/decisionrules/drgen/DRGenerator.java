package com.javafee.controller.algorithm.decisionrules.drgen;

import java.util.List;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.RowsSet;

public interface DRGenerator {
	List<RowsSet> getResultCoverageRowsSet();

	LogicalExpression generateDecisionRules(RowsSet concernedRowsSet, List<RowsSet> rowsSetList);
}
