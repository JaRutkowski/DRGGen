package com.javafee.controller.algorithm.process;

import java.util.Vector;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InconsistencyProcess {
	public boolean checkIfInconsistencyExists(Vector<Vector> data) {
		return VectorProcess.checkIfRowsWithSameAttributesAndVariousDecisionValueExists(data);
	}
}
