package com.javafee.controller.algorithm.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Named;

import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.process.VectorProcess;
import com.javafee.controller.utils.SystemProperties;

import lombok.NonNull;

@Named("StandardTestGenerator")
public class StandardTestGenerator implements TestGenerator {
	@Override
	public List<RowPairsSet> generate(@NonNull Vector<Vector> data) {
		//TODO Extract to constants or utility method
		int lastAttributeIndex = data.get(0).size() - 1;
		List<RowPairsSet> rowPairsSetList = new ArrayList<>();
		for (int attributeIndex = 0; attributeIndex <= lastAttributeIndex; attributeIndex++) {
			RowPairsSet rowPairsSet = VectorProcess.findDistinctRowPairsWithVariousAttributeAndDecisionValue(data, attributeIndex);
			rowPairsSet.setAttributeIndex(attributeIndex);
			// all reducts set for processing decision attribute
			if (attributeIndex == SystemProperties.getSystemParameterDecisionAttributeIndex())
				rowPairsSet.setAllReductSet(true);
			rowPairsSetList.add(rowPairsSet);
		}
		return rowPairsSetList;
	}
}
