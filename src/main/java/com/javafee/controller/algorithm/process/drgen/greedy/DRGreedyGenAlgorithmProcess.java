package com.javafee.controller.algorithm.process.drgen.greedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.javafee.controller.algorithm.datastructure.LogicalAttributeValuePair;
import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.LogicalOperator;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.process.drgen.DRGenAlgorithm;
import com.javafee.controller.utils.SystemProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DRGreedyGenAlgorithmProcess implements DRGenAlgorithm {
	@Setter
	private int initialCoverage = 0;
	@Setter
	private double goalCoverage = 0;
	private int minIterations = 1;
	private int maxIterations = 0;

	private int currentIteration = 1;
	private double currentCoverage = 0;
	private double maxCoverage = 0;
	private double currentCoveragePercentage = 0.0;
	private int amountOfConcernedSets = 1;

	private List<RowsSet[]> allCombinationsForCoefficient = null;
	private boolean stopAlgorithm = false;
	private List<RowsSet> resultCoverageRowsSet = null;

	private RowsSet concernedRowsSet = null;
	private List<RowsSet> rowsSetList = null;

	@Override
	public LogicalExpression generateDecisionRules(RowsSet concernedRowsSet, List<RowsSet> rowsSetList) {
		initializeParameters(concernedRowsSet, rowsSetList);
		findCoverageUsingGreedyMethod();
		return mapResultCoverageRowsSetToLogicalStatement();
	}

	private void initializeParameters(RowsSet concernedRowsSet, List<RowsSet> rowsSetList) {
		this.concernedRowsSet = concernedRowsSet;
		this.rowsSetList = rowsSetList;

		this.initialCoverage = 0;
		this.goalCoverage = 0;
		this.minIterations = 1;
		this.maxIterations = 0;

		this.currentIteration = 1;
		this.currentCoverage = 0;
		this.maxCoverage = 0;
		this.currentCoveragePercentage = 0.0;
		this.amountOfConcernedSets = 1;

		this.goalCoverage = concernedRowsSet.getRows().size();
		this.stopAlgorithm = false;
		//TODO Max iterations calculation
		//maxIterations =
	}

	private void findCoverageUsingGreedyMethod() {
		allCombinationsForCoefficient = new ArrayList<>();

		for (amountOfConcernedSets = 1; amountOfConcernedSets <= rowsSetList.size(); amountOfConcernedSets++) {
			if (!stopAlgorithm) {
				checkCoversForAllCombinations(rowsSetList.stream().toArray(RowsSet[]::new), rowsSetList.size(), amountOfConcernedSets);
				currentIteration++;
			} else
				break;
		}
	}

	private void checkCoversForAllCombinations(RowsSet[] rowsSetArray, int rowsSetArraySize, int coefficient) {
		RowsSet currentCombinations[] = new RowsSet[coefficient];
		generateCombinations(rowsSetArray, currentCombinations, 0, rowsSetArraySize - 1, 0, coefficient);
	}

	private void generateCombinations(RowsSet[] rowsSetArray, RowsSet[] currentCombinationsArray,
	                                  int start, int rowsSetArraySize, int index, int coefficient) {
		if (index == coefficient) {
			for (int j = 0; j < coefficient; j++) {
				System.out.print(currentCombinationsArray[j] + " "); //split & check coverage for combinate set
			}
			calculateCoverage(currentCombinationsArray);
			allCombinationsForCoefficient.add(currentCombinationsArray);
			if (stopAlgorithm) resultCoverageRowsSet = Arrays.asList(currentCombinationsArray);
			System.out.println();
			return;
		}

		for (int i = start; i <= rowsSetArraySize && rowsSetArraySize - i + 1 >= coefficient - index; i++) {
			if (!stopAlgorithm) {
				currentCombinationsArray[index] = rowsSetArray[i];
				generateCombinations(rowsSetArray, currentCombinationsArray, i + 1, rowsSetArraySize, index + 1, coefficient);
			} else return;
		}
	}

	private void calculateCoverage(RowsSet[] rowsSetCombinationArray) {
		currentCoverage = concernedRowsSet.coverage(rowsSetCombinationArray);
		currentCoveragePercentage = (currentCoverage / goalCoverage) * 100;
		if (currentCoverage > maxCoverage) {
			maxCoverage = currentCoverage;
		}
		stopAlgorithm = currentCoverage == goalCoverage;
	}

	private LogicalExpression mapResultCoverageRowsSetToLogicalStatement() {
		LogicalExpression logicalExpression = new LogicalExpression();
		LogicalAttributeValuePair logicalAttributeValuePair;

		int index = 0;
		for (RowsSet rowsSet : resultCoverageRowsSet) {
			logicalAttributeValuePair = new LogicalAttributeValuePair();
			logicalAttributeValuePair.setAttributeIndex(rowsSet.getAttributeIndex());
			logicalAttributeValuePair.setValue(rowsSet.getConcernedRow().getValues().get(rowsSet.getAttributeIndex()).toString());
			logicalExpression.append(logicalAttributeValuePair);
			if (index != resultCoverageRowsSet.size() - 1)
				logicalExpression.append(LogicalOperator.CONJUNCTION);
			else
				logicalExpression.append(rowsSet.getConcernedRow().getValues().get(SystemProperties.getSystemParameterDecisionAttributeIndex()).toString());

			index++;
		}
		return logicalExpression;
	}
}
