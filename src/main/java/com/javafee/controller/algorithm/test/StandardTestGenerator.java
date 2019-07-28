package com.javafee.controller.algorithm.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Named;

import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.process.VectorProcess;

import lombok.NonNull;

@Named("StandardTestGenerator")
public class StandardTestGenerator implements TestGenerator {
	@Override
	public List<RowPairsSet> generate(@NonNull Vector<Vector> data) {
		//TODO Extract to constants or utility method
		int decisionAttributeColumnIndex = data.get(0).size() - 1;
		List<RowPairsSet> rowPairsSetList = new ArrayList<>();
		//TODO Important! rowIndex _probably_ should be the attributeIndex!
		for (int rowIndex = 0; rowIndex <= decisionAttributeColumnIndex; rowIndex++) {
			RowPairsSet rowPairsSet = VectorProcess.findDistinctRowPairsWithVariousAttributeAndDecisionValue(data, rowIndex);
			rowPairsSet.setAttributeIndex(rowIndex);
			if (rowIndex == decisionAttributeColumnIndex)
				rowPairsSet.setAllReductSet(true);
			rowPairsSetList.add(rowPairsSet);
		}
		return rowPairsSetList;
	}
}
