package com.javafee.controller.algorithm.datapreprocessing;

import java.util.Vector;

public class StandardDataPreparator implements DataPreparator {
	@Override
	public boolean prepare(Vector<Vector> data) {
		//TODO check for (1) inconsistency, (2) sames rows, generate exceptions
		// System.err.println("inconsistency between " + row.toString() + " and " + rowToCompare.toString());
		// if(row.equals(rowToCompare) && !rowToCompareDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex))
		return false;
	}
}
