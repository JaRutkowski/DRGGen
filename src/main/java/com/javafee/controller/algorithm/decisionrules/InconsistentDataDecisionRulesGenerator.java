package com.javafee.controller.algorithm.decisionrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Named;

import org.paukov.combinatorics3.Generator;

import com.javafee.controller.algorithm.datastructure.LogicalAttributeValuePair;
import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.LogicalOperator;
import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.process.VectorProcess;
import com.javafee.controller.utils.SystemProperties;

import lombok.Getter;
import lombok.Setter;

@Named("InconsistentDataDecisionRulesGenerator")
public class InconsistentDataDecisionRulesGenerator implements DecisionRulesGenerator {
	@Getter
	@Setter
	private long timeMeasure = 0L;

	@Override
	public List<List<Object>> generate(Vector<Vector> data) {
		List<List<Object>> result = new ArrayList<>();

		int rowIndex = 1, rowIndexLogger = 1;
		// For each rows r_1, r_2 to r_n
		for (Vector row : data) {
			// Initialization
			List<Object> resultConsistedOfDecisionRules = new ArrayList<>();
			Row concernedRow = new Row(rowIndex);
			concernedRow.setValues(data.get(rowIndex - 1));

			// First step - generate subtables T(f_x, r_y) ... (f_n, r_y)
			// For each combinations of attributes
			List<Integer> list = IntStream.rangeClosed(0, data.get(0).size() - 2).boxed().collect(Collectors.toList());
			Map p = new HashMap();
			List<Integer> q = new ArrayList<>();
			RowsSet.DEGENERATION_TYPE isDegenerated = RowsSet.DEGENERATION_TYPE.NOT_DEGENERATED;
			for (int combinationCurrentSize = 1; combinationCurrentSize < data.size(); combinationCurrentSize++) {
				if (isDegenerated == RowsSet.DEGENERATION_TYPE.NOT_DEGENERATED) {
					List combinationIndexesForGivenCombinationSize = Generator.combination(list.toArray(new Integer[list.size()]))
							.simple(combinationCurrentSize)
							.stream().collect(Collectors.toList());
					int iterationNumber = 0, currentMinCardinality = 0;
					RowsSet subTable = null;
					for (Object attributesIndexesCombination : combinationIndexesForGivenCombinationSize) {
						ArrayList<Integer> attributesIndexesCombinationList = (ArrayList<Integer>) attributesIndexesCombination;
						subTable = VectorProcess.findRowsWithSameAttributes(data, row, attributesIndexesCombinationList);
						RowPairsSet rowPairsSet = VectorProcess.findDistinctRowPairsWithVariousDecisionValue(subTable);
						if (iterationNumber == 0) {
							currentMinCardinality = rowPairsSet.cardinality();
							for (int attributeIndex : attributesIndexesCombinationList)
								p.put(attributeIndex, rowPairsSet.cardinality());
						}

						// Second step - add calculated cardinality of P(T(f_x, r_y)) value to P set
						if (rowPairsSet.cardinality() < currentMinCardinality) {
							p.clear();
							currentMinCardinality = rowPairsSet.cardinality();
							for (int attributeIndex : attributesIndexesCombinationList)
								p.put(attributeIndex, rowPairsSet.cardinality());
						}

						// Third step - if degenerated generate Q, break, calculate decision rule
						isDegenerated = subTable.isDegenerated();
						if (isDegenerated != RowsSet.DEGENERATION_TYPE.NOT_DEGENERATED) {
							q.addAll(p.keySet());
							break;
						}

						iterationNumber++;
					}

					// Fourth step - generate decision rule base on Q set
					if (isDegenerated != RowsSet.DEGENERATION_TYPE.NOT_DEGENERATED) {
						LogicalExpression logicalExpression = new LogicalExpression();
						LogicalAttributeValuePair logicalAttributeValuePair;

						int index = 0;
						for (Integer attributeIndex : q) {
							logicalAttributeValuePair = new LogicalAttributeValuePair();
							logicalAttributeValuePair.setAttributeIndex(attributeIndex);
							logicalAttributeValuePair.setValue((String) row.get(attributeIndex));
							logicalExpression.append(logicalAttributeValuePair);
							if (index != q.size() - 1)
								logicalExpression.append(LogicalOperator.CONJUNCTION);
							else
								logicalExpression.append(isDegenerated == RowsSet.DEGENERATION_TYPE.SAME_ROWS && subTable != null
										? subTable.getTheMostCommonDecision()
										: subTable.getRows().get(0).getValues().get(
										SystemProperties.getSystemParameterDecisionAttributeIndex()).toString());

							index++;
						}

						System.out.println("[" + rowIndexLogger + "]" + " " + logicalExpression.toString());
						resultConsistedOfDecisionRules.add(logicalExpression);

						result.add(resultConsistedOfDecisionRules);
					}
				}
			}
			rowIndexLogger++;
		}

		return result;
	}
}
