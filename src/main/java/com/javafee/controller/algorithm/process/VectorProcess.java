package com.javafee.controller.algorithm.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.javafee.controller.algorithm.datastructure.LogicalAttributeValuePair;
import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowPair;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.datastructure.utils.Pair;
import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.SystemProperties;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VectorProcess {
	/**
	 * Finds distinct pairs of the various rows with various attributeIndex and decision value in given data set. Uses
	 * the <code>RowPairSet</code> data structure to filter redundant row pairs.
	 *
	 * @param data                  data which contains rows set
	 * @param variousAttributeIndex index of the attributeIndex to be compared
	 * @return {@link RowPairsSet}
	 */
	public RowPairsSet findDistinctRowPairsWithVariousAttributeAndDecisionValue(
			Vector<Vector> data, int variousAttributeIndex) {
		RowPairsSet rowPairsSet = new RowPairsSet();
		int rowIndex = 1;
		for (Vector row : data) {
			List<Integer> notEqualsRowsIndexes = findRowPairsIndexesWithVariousAttributeAndDecisionValue(
					row, data, variousAttributeIndex);
			for (Integer notEqualsRowIndex : notEqualsRowsIndexes)
				rowPairsSet.add(new RowPair(rowIndex, notEqualsRowIndex), true);
			rowIndex++;
		}
		return rowPairsSet;
	}

	/**
	 * Finds distinct pairs of the rows with various decision value in given data set. Uses
	 * the <code>RowPairSet</code> data structure to filter redundant row pairs.
	 *
	 * @param data data which contains rows set
	 * @return {@link RowPairsSet}
	 */
	public RowPairsSet findDistinctRowPairsWithVariousDecisionValue(
			RowsSet data) {
		RowPairsSet rowPairsSet = new RowPairsSet();
		int rowIndex = 1;
		for (Row row : data.getRows()) {
			List<Integer> rowsIndexes = findRowPairsIndexesWithVariousDecisionValue(
					row, data);
			for (Integer notEqualsRowIndex : rowsIndexes)
				rowPairsSet.add(new RowPair(row.getIndex(), notEqualsRowIndex), true);
			rowIndex++;
		}
		return rowPairsSet;
	}

	/**
	 * Finds all the pairs of various rows with the various attributeIndex and decision value in given data set.
	 *
	 * @param rowToCompare          row to be compared with
	 * @param data                  data which contains rows set
	 * @param variousAttributeIndex index of the attributeIndex to be compared
	 * @return list of the rows pair numbers
	 */
	public List<Integer> findRowPairsIndexesWithVariousAttributeAndDecisionValue(
			Vector rowToCompare, Vector<Vector> data, int variousAttributeIndex) {
		List<Integer> rowIndexList = new ArrayList<>();
		Integer rowIndex = 1;

		Integer rowToCompareDecisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		String rowToCompareAttributeValue = (String) rowToCompare.get(variousAttributeIndex);
		String rowToCompareDecisionAttributeValue = (String) rowToCompare.get(rowToCompareDecisionAttributeIndex);

		for (Vector row : data) {
			if (!rowToCompareAttributeValue.equals(row.get(variousAttributeIndex)) &&
					!rowToCompareDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex)))
				rowIndexList.add(rowIndex);
			rowIndex++;
		}
		return rowIndexList;
	}

	public List<Integer> findRowPairsIndexesWithVariousDecisionValue(
			Row rowToCompare, RowsSet data) {
		List<Integer> rowIndexList = new ArrayList<>();

		Integer rowToCompareDecisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		String rowToCompareDecisionAttributeValue = (String) rowToCompare.getValues().get(rowToCompareDecisionAttributeIndex);

		for (Row row : data.getRows()) {
			if (!rowToCompareDecisionAttributeValue.equals(row.getValues().get(rowToCompareDecisionAttributeIndex)))
				rowIndexList.add(row.getIndex());
		}
		return rowIndexList;
	}

	/**
	 * Finds distinct row indexes of the rows with various attributes and decision value in given data set. Uses
	 * the <code>RowsSet</code> data structure to filter redundant row pairs.
	 *
	 * @param data data which contains rows set
	 * @return {@link RowsSet}
	 */
	public RowsSet findDistinctRowsWithVariousAttributesAndDecisionValue(
			Vector<Vector> data, Vector row) {
		RowsSet rowsSet = new RowsSet();
		List<Integer> notEqualsRowsIndexes = findRowsIndexesWithVariousAttributesAndDecisionValue(
				row, data);
		for (Integer notEqualsRowIndex : notEqualsRowsIndexes)
			rowsSet.add(new Row(notEqualsRowIndex, data.get(notEqualsRowIndex - 1)), true);
		return rowsSet;
	}

	/**
	 * Finds all the rows in rowSet with various attribute and decision value.
	 *
	 * @param rowToCompare          row to be compared with
	 * @param rowsSet               rows set to be searched
	 * @param variousAttributeIndex index of the attributeIndex to be compared
	 * @return {@link RowsSet}
	 */
	public RowsSet findDistinctRowsWithVariousAttributeAndDecisionValue(RowsSet rowsSet, Vector rowToCompare, int variousAttributeIndex) {
		RowsSet resultRowsSet = new RowsSet();
		for (Row row : rowsSet.getRows()) {
			Integer rowToCompareDecisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
			String rowToCompareAttributeValue = (String) rowToCompare.get(variousAttributeIndex);
			String rowToCompareDecisionAttributeValue = (String) rowToCompare.get(rowToCompareDecisionAttributeIndex);

			if (!rowToCompareAttributeValue.equals(row.getValues().get(variousAttributeIndex)) &&
					!rowToCompareDecisionAttributeValue.equals(row.getValues().get(rowToCompareDecisionAttributeIndex)))
				resultRowsSet.add(row, true);
		}
		return resultRowsSet;
	}

	/**
	 * Finds all the rows in rowSet with same attribute value.
	 *
	 * @param rowToCompare row to be compared with
	 * @return {@link RowsSet}
	 */
	public RowsSet findDistinctRowsWithSameAttribute(Vector<Vector> data, Vector rowToCompare, ArrayList<Integer> sameAttributeIndexes) {
		RowsSet rowsSet = new RowsSet();
		List<Integer> rowsWithSameAttributesValuesIndexes = findRowsIndexesWithSameAttributes(rowToCompare,
				data, sameAttributeIndexes);
		//TODO optimized
		rowsWithSameAttributesValuesIndexes.stream().forEach(notEqualsRowIndex -> rowsSet.add(new Row(notEqualsRowIndex, data.get(notEqualsRowIndex - 1)), true));
		return rowsSet;
	}

	/**
	 * Finds the most common decision in given rowSet.
	 *
	 * @param rowsSet rows set to be searched
	 * @return {@link RowsSet}
	 */
	public static String findTheMostCommonDecision(RowsSet rowsSet) {
		List<Object> values = Collections.list(Common.prepareColumnForGivenAttributeIndexFromData(rowsSet, SystemProperties.getSystemParameterDecisionAttributeIndex()).elements());
		Map<Object, Long> countByValues = values.stream()
				.collect(Collectors.groupingBy(Object::toString, Collectors.counting()));
		return (String) Collections.max(countByValues.entrySet(), Map.Entry.comparingByValue()).getKey();
	}

	/**
	 * Counts rows in the data with equal attributes and equal decision value and number of rows with equal
	 * decision value.
	 *
	 * @param data         data which contains rows set
	 * @param decisionRule decisionRule which contains attributes and decision value to be compared
	 * @return {@link Pair}
	 * @see Pair
	 * @see LogicalExpression
	 * @see LogicalAttributeValuePair
	 */
	public Pair<Long> countRowsWithEqualAttributesAndEqualDecisionValueAndRowsWithEqualDecisionValue(
			Vector<Vector> data, LogicalExpression decisionRule) {
		long rowsWithEqualAttributesAndEqualDecisionValue = 0;
		long rowsWithEqualDecisionValue = 0;

		Integer rowToCompareDecisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		String decisionRuleDecisionAttributeValue = decisionRule.getLogicalValue();

		List<Integer> attributesIndexes = new ArrayList<>();
		Vector conditionalAttributesValues = new Vector();
		decisionRule.getLogicalAttributeValuePairList().forEach(attributeValuePair -> attributesIndexes.add(attributeValuePair.getAttributeIndex()));
		decisionRule.getLogicalAttributeValuePairList().forEach(attributeValuePair -> conditionalAttributesValues.add((attributeValuePair.getValue())));

		for (Vector row : data) {
			if (decisionRuleDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex))) {
				rowsWithEqualDecisionValue++;
				Vector rowCopyOnlyWithAppropriateConditionalAttributes = prepareRowCopyOnlyWithAppropriateConditionalAttributes(row, rowToCompareDecisionAttributeIndex, attributesIndexes);
				if (rowCopyOnlyWithAppropriateConditionalAttributes.equals(conditionalAttributesValues))
					rowsWithEqualAttributesAndEqualDecisionValue++;
			}
		}
		return new Pair<>(rowsWithEqualDecisionValue, rowsWithEqualAttributesAndEqualDecisionValue);
	}

	/**
	 * Counts rows in the data with equal attributes and various decision value.
	 *
	 * @param data         data which contains rows set
	 * @param decisionRule decisionRule which contains attributes and decision value to be compared
	 * @return {@link Long}
	 * @see LogicalExpression
	 * @see LogicalAttributeValuePair
	 */
	public Long countRowsWithEqualAttributesAndVariousDecisionValue(
			Vector<Vector> data, LogicalExpression decisionRule) {
		long rowsWithEqualAttributesAndVariousDecisionValue = 0;

		Integer rowToCompareDecisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		String decisionRuleDecisionAttributeValue = decisionRule.getLogicalValue();

		List<Integer> attributesIndexes = new ArrayList<>();
		Vector conditionalAttributesValues = new Vector();
		decisionRule.getLogicalAttributeValuePairList().forEach(attributeValuePair -> attributesIndexes.add(attributeValuePair.getAttributeIndex()));
		decisionRule.getLogicalAttributeValuePairList().forEach(attributeValuePair -> conditionalAttributesValues.add((attributeValuePair.getValue())));

		for (Vector row : data) {
			Vector rowCopyOnlyWithAppropriateConditionalAttributes = prepareRowCopyOnlyWithAppropriateConditionalAttributes(row, rowToCompareDecisionAttributeIndex, attributesIndexes);
			if (rowCopyOnlyWithAppropriateConditionalAttributes.equals(conditionalAttributesValues))
				if (!decisionRuleDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex)))
					rowsWithEqualAttributesAndVariousDecisionValue++;
		}
		return rowsWithEqualAttributesAndVariousDecisionValue;
	}

	/**
	 * Counts max occurrences of each types of values for each conditional attributes except decision values. Returns
	 * map of attribute indexes and its max value occurrences.
	 *
	 * @param data data which contains rows set
	 * @return {@link Map}
	 */
	public Map<Long, Long> countMaxOccurrencesOfEachTypesOfValuesForEachConditionalAttributesExceptDecisionValues(
			Vector<Vector> data) {
		Map<Long, Long> mapOfMaxCountOfValueForEachAttributeExceptDecisionValues = new HashMap<>();

		Integer decisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		AtomicInteger attributeIndex = new AtomicInteger(0);
		if (!data.isEmpty()) {
			data.get(0).forEach(attribute -> {
				if (attributeIndex.get() != decisionAttributeIndex) {
					List<Object> values = Collections.list(Common.prepareColumnForGivenAttributeIndexFromData(data, attributeIndex.get()).elements());
					Map<Object, Long> countByValues = values.stream()
							.collect(Collectors.groupingBy(Object::hashCode, Collectors.counting()));
					Long maxCountForValue = Collections.max(countByValues.entrySet(), Map.Entry.comparingByValue()).getValue();
					mapOfMaxCountOfValueForEachAttributeExceptDecisionValues.put((long) attributeIndex.get(), maxCountForValue);
				}
				attributeIndex.getAndIncrement();
			});
		}

		return mapOfMaxCountOfValueForEachAttributeExceptDecisionValues;
	}

	/**
	 * Checks if same rows (with same conditional attributes) but not equal decision value exists for given data set.
	 * Using <code>systemParameterDecisionAttributeIndex</code> parameter to get its index.
	 *
	 * @param data data which contains rows set
	 * @return true if same row with not equal decision value exists, otherwise false
	 * @see SystemProperties
	 */
	public boolean checkIfRowsWithSameAttributesAndVariousDecisionValueExists(Vector<Vector> data) {
		boolean rowsWithSameAttributesAndVariousDecisionValueExists = false;
		for (Vector row : data)
			if (checkIfRowsWithSameAttributesAndVariousDecisionValueExists(row, data)) {
				rowsWithSameAttributesAndVariousDecisionValueExists = true;
				break;
			}
		return rowsWithSameAttributesAndVariousDecisionValueExists;
	}

	/**
	 * Checks if same rows (with same conditional attributes). Using <code>systemParameterDecisionAttributeIndex</code>
	 * parameter to get its index.
	 *
	 * @param data data which contains rows set
	 * @return true if all rows are same, otherwise false
	 * @see SystemProperties
	 */
	public boolean checkIfRowsAreWithSameAttributes(RowsSet data) {
		boolean rowsWithSameAttributes = true;
		Vector firstRowAttributes = (Vector) data.getRows().get(0).getValues().clone();
		firstRowAttributes.removeElementAt(SystemProperties.getSystemParameterDecisionAttributeIndex());
		for (int index = 1; index < data.getRows().size(); index++) {
			Vector rowToCompareAttributes = (Vector) data.getRows().get(index).getValues().clone();
			rowToCompareAttributes.removeElementAt(SystemProperties.getSystemParameterDecisionAttributeIndex());
			if (!firstRowAttributes.equals(rowToCompareAttributes)) {
				rowsWithSameAttributes = false;
				break;
			}
		}
		return rowsWithSameAttributes;
	}

	/**
	 * Checks if same decision attributes. Using <code>systemParameterDecisionAttributeIndex</code>
	 * parameter to get its index.
	 *
	 * @param data data which contains rows set
	 * @return true if all rows are same, otherwise false
	 * @see SystemProperties
	 */
	public boolean checkIfRowsAreWithSameDecisionAttributes(RowsSet data) {
		boolean rowsWithSameDecision = true;
		String firstRowDecisionAttributeValue = (String) data.getRows().get(0).getValues()
				.get(SystemProperties.getSystemParameterDecisionAttributeIndex());
		for (int index = 1; index < data.getRows().size(); index++) {
			String rowToCompareDecisionAttribute = (String) data.getRows().get(index).getValues()
					.get(SystemProperties.getSystemParameterDecisionAttributeIndex());
			if (!firstRowDecisionAttributeValue.equals(rowToCompareDecisionAttribute)) {
				rowsWithSameDecision = false;
				break;
			}
		}
		return rowsWithSameDecision;
	}

	public static void main(String[] args) {
		Vector v = new Vector();
		v.add("kuba");
		v.add("sylwia");
		v.add("sylwia");
		v.add("ala");
		v.add("ala");
		v.add("ala");
		v.add("ala");

		List<Object> objects = Collections.list(v.elements());
		Map<Object, Long> countForId = objects.stream()
				.collect(Collectors.groupingBy(Object::hashCode, Collectors.counting()));
		Long key = (Long) Collections.max(countForId.entrySet(), Map.Entry.comparingByValue()).getValue();

		System.out.println("end");
	}

	/**
	 * Finds all the rows with various attributes and decision value in given data set.
	 *
	 * @param rowToCompare row to be compared with
	 * @param data         data which contains rows set
	 * @return list of the rows indexes
	 */
	private List<Integer> findRowsIndexesWithVariousAttributesAndDecisionValue(Vector rowToCompare, Vector<Vector> data) {
		List<Integer> rowIndexList = new ArrayList<>();
		Integer rowIndex = 1;

		Integer rowToCompareDecisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		String rowToCompareDecisionAttributeValue = (String) rowToCompare.get(rowToCompareDecisionAttributeIndex);

		for (Vector row : data) {
			Vector rowAttributes = (Vector) row.clone(), rowToCompareAttributes = (Vector) rowToCompare.clone();
			rowAttributes.removeElementAt(SystemProperties.getSystemParameterDecisionAttributeIndex());
			rowToCompareAttributes.removeElementAt(rowToCompareDecisionAttributeIndex);
			if (!rowToCompareAttributes.equals(rowAttributes) &&
					!rowToCompareDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex)))
				rowIndexList.add(rowIndex);
			rowIndex++;
		}
		return rowIndexList;
	}

	/**
	 * Finds all the rows with various attributes and decision value in given data set.
	 *
	 * @param rowToCompare row to be compared with
	 * @param data         data which contains rows set
	 * @return list of the rows indexes
	 */
	private List<Integer> findRowsIndexesWithSameAttributes(Vector rowToCompare, Vector<Vector> data, ArrayList<Integer> indexes) {
		List<Integer> rowIndexList = new ArrayList<>();
		Integer rowIndex = 1;
		for (Vector row : data) {
			boolean areSameAttributes = true;
			for (Integer index : indexes) {
				if (!rowToCompare.get(index).equals(row.get(index)))
					areSameAttributes = false;
			}
			if (areSameAttributes) rowIndexList.add(rowIndex);
			rowIndex++;
		}
		return rowIndexList;
	}

	/**
	 * Checks if same rows (with same conditional attributes) but not equal decision value exists for given row. Using
	 * <code>systemParameterDecisionAttributeIndex</code> parameter to get its index.
	 *
	 * @param rowToCompare row to be compared with
	 * @param data         data which contains rows set
	 * @return true if same row with not equal decision value exists, otherwise false
	 */
	private boolean checkIfRowsWithSameAttributesAndVariousDecisionValueExists(Vector rowToCompare, Vector<Vector> data) {
		boolean rowsWithSameAttributesAndVariousDecisionValueExists = false;

		Integer decisionAttributeIndex = SystemProperties.getSystemParameterDecisionAttributeIndex();
		String rowToCompareDecisionAttributeValue = (String) rowToCompare.get(decisionAttributeIndex);

		for (Vector row : data) {
			Vector rowOfAttributes = Common.prepareVectorOnlyWithConditionalAttributesValuesForGivenRow(row, decisionAttributeIndex);
			Vector rowToCompareOfAttributes = Common.prepareVectorOnlyWithConditionalAttributesValuesForGivenRow(rowToCompare, decisionAttributeIndex);
			if (rowToCompareOfAttributes.equals(rowOfAttributes) &&
					!rowToCompareDecisionAttributeValue.equals(row.get(decisionAttributeIndex))) {
				rowsWithSameAttributesAndVariousDecisionValueExists = true;
				break;
			}
		}

		return rowsWithSameAttributesAndVariousDecisionValueExists;
	}

	private Vector prepareRowCopyOnlyWithAppropriateConditionalAttributes(Vector concernedRow, int rowToCompareDecisionAttributeIndex, List<Integer> attributesIndexes) {
		Vector rowCopyOnlyWithConditionalAttributes;
		rowCopyOnlyWithConditionalAttributes = new Vector(concernedRow);
		rowCopyOnlyWithConditionalAttributes.remove(rowToCompareDecisionAttributeIndex);
		for (int index = 0; index < rowCopyOnlyWithConditionalAttributes.size(); index++)
			if (!attributesIndexes.contains(index))
				rowCopyOnlyWithConditionalAttributes.set(index, null);
		List<Object> rowCopyOnlyWithConditionalAttributesList = Collections.list(rowCopyOnlyWithConditionalAttributes.elements());
		rowCopyOnlyWithConditionalAttributesList.removeIf(Objects::isNull);
		rowCopyOnlyWithConditionalAttributes = new Vector(rowCopyOnlyWithConditionalAttributesList);
		return rowCopyOnlyWithConditionalAttributes;
	}
}
