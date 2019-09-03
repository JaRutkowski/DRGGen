package com.javafee.controller.algorithm.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.LogicalAttributeValuePair;
import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowPair;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.datastructure.utils.Pair;
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
			if (!rowToCompare.equals(row) &&
					!rowToCompareDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex)))
				rowIndexList.add(rowIndex);
			rowIndex++;
		}
		return rowIndexList;
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
