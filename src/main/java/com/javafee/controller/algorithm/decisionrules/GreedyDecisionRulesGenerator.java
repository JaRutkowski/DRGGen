package com.javafee.controller.algorithm.decisionrules;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Named;

import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.process.VectorProcess;
import com.javafee.controller.utils.SystemProperties;

@Named("GreedyDecisionRulesGenerator")
public class GreedyDecisionRulesGenerator implements DecisionRulesGenerator {
	//TODO Should return List<LogicStatement>
	@Override
	public List<List<Object>> generate(Vector<Vector> data) {
		// RowsSet and List<RowsSet>
		List<List<Object>> result = new ArrayList<>();

		int rowIndex = 1;
		List<RowsSet> rowsSetList = new ArrayList<>();
		// For each rows r_1, r_2 to r_n
		for (Vector row : data) {
			// Initialization
			List<Object> partialResultConsistedOfRowsSetAndRowsSetForEachAttributes = new ArrayList<>();
			Row concernedRow = new Row(rowIndex);

			// First step - generate A(T, r_x)
			RowsSet rowsSet = VectorProcess.findDistinctRowsWithVariousAttributesAndDecisionValue(data, row);
			rowsSet.setConcernedRow(concernedRow);
			rowsSetList.add(rowsSet);
			partialResultConsistedOfRowsSetAndRowsSetForEachAttributes.add(rowsSet);

			// Second step - generate A(T, r_1, f_1)
			// For each attributes f_1, f_2 to f_n
			int decisionAttributeColumnIndex = row.size() - 1;//SystemProperties.getSystemParameterDecisionAttributeIndex();
			List<RowsSet> rowsSetForEachAttributesList = new ArrayList<>();
			for (int attributeIndex = 0; attributeIndex <= decisionAttributeColumnIndex; attributeIndex++) {
				if (attributeIndex != SystemProperties.getSystemParameterDecisionAttributeIndex()) {
					RowsSet rowsSetForEachAttributes = VectorProcess.findDistinctRowsWithVariousAttributesAndDecisionValue(rowsSet, row, attributeIndex);
					rowsSetForEachAttributes.setConcernedRow(concernedRow);
					rowsSetForEachAttributes.setAttributeIndex(attributeIndex);
					rowsSetForEachAttributes.setConcernedRow(new Row(rowIndex));
					rowsSetForEachAttributesList.add(rowsSetForEachAttributes);
				}
			}
			partialResultConsistedOfRowsSetAndRowsSetForEachAttributes.add(rowsSetForEachAttributesList);

			result.add(partialResultConsistedOfRowsSetAndRowsSetForEachAttributes);
			rowIndex++;
		}

		return result;
	}
}
