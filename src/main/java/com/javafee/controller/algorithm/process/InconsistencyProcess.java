package com.javafee.controller.algorithm.process;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InconsistencyProcess {
	public boolean checkIfInconsistencyExists(Vector<Vector> data) {
		return VectorProcess.checkIfRowsWithSameAttributesAndVariousDecisionValueExists(data);
	}

	public boolean checkIfInconsistencyExists(List<LogicalExpression> decisionRulesSet) {
		for (LogicalExpression decisionRule : decisionRulesSet)
			for (LogicalExpression dR : decisionRulesSet)
				if (decisionRule.isInconsistent(dR))
					return true;
		return false;
	}
}
