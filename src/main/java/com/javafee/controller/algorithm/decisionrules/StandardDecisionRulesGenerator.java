package com.javafee.controller.algorithm.decisionrules;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Named;

import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.process.VectorProcess;
import com.javafee.controller.algorithm.process.drgen.DRGenAlgorithmProcess;
import com.javafee.controller.algorithm.process.drgen.DRGeneratorProcess;
import com.javafee.controller.algorithm.process.drgen.greedy.DRGreedyGenAlgorithmProcess;
import com.javafee.controller.utils.SystemProperties;

@Named("StandardDecisionRulesGenerator")
public class StandardDecisionRulesGenerator implements DecisionRulesGenerator {

	private DRGeneratorProcess drGeneratorProcess = new DRGeneratorProcess();

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
			List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributesAndCoverageAndDecisionRules = new ArrayList<>();
			Row concernedRow = new Row(rowIndex);
			concernedRow.setValues(data.get(rowIndex - 1));

			// First step - generate A(T, r_x)
			RowsSet rowsSet = VectorProcess.findDistinctRowsWithVariousAttributesAndDecisionValue(data, row);
			rowsSet.setConcernedRow(concernedRow);
			rowsSetList.add(rowsSet);
			resultConsistedOfRowsSetAndRowsSetForEachAttributesAndCoverageAndDecisionRules.add(rowsSet);

			// Second step - generate A(T, r_1, f_1)
			// For each attributes f_1, f_2 to f_n
			int lastColumnIndex = row.size() - 1;
			List<RowsSet> rowsSetForEachAttributesList = new ArrayList<>();
			for (int attributeIndex = 0; attributeIndex <= lastColumnIndex; attributeIndex++) {
				if (attributeIndex != SystemProperties.getSystemParameterDecisionAttributeIndex()) {
					RowsSet rowsSetForEachAttributes = VectorProcess.findDistinctRowsWithVariousAttributeAndDecisionValue(rowsSet, row, attributeIndex);
					rowsSetForEachAttributes.setConcernedRow(concernedRow);
					rowsSetForEachAttributes.setAttributeIndex(attributeIndex);
					rowsSetForEachAttributesList.add(rowsSetForEachAttributes);
				}
			}
			resultConsistedOfRowsSetAndRowsSetForEachAttributesAndCoverageAndDecisionRules.add(rowsSetForEachAttributesList);

			// Third step - calculate cover and decision rules generation
			DRGenAlgorithmProcess drGenAlgorithmProcess = new DRGenAlgorithmProcess(rowsSet, rowsSetForEachAttributesList, new DRGreedyGenAlgorithmProcess());
			LogicalExpression decisionRules = drGenAlgorithmProcess.generateDecisionRules();
			List<RowsSet> resultCoverageRowsSet = drGenAlgorithmProcess.getResultCoverageRowsSet();
			resultConsistedOfRowsSetAndRowsSetForEachAttributesAndCoverageAndDecisionRules.add(resultCoverageRowsSet);
			resultConsistedOfRowsSetAndRowsSetForEachAttributesAndCoverageAndDecisionRules.add(decisionRules);

			result.add(resultConsistedOfRowsSetAndRowsSetForEachAttributesAndCoverageAndDecisionRules);
			rowIndex++;
		}

		return result;
	}
}
