package com.javafee.controller.algorithm.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowPair;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;

public final class VectorProcess {
	/**
	 * Finds distinct pairs of the various rows with various attributeIndex and decision value in given data set. Uses
	 * the <code>RowPairSet</code> data structure to filter redundant row pairs.
	 *
	 * @param data                  data which contains rows set
	 * @param variousAttributeIndex index of the attributeIndex to be compared
	 * @return {@link RowPairsSet}
	 */
	public static RowPairsSet findDistinctRowPairsWithVariousAttributeAndDecisionValue(
			Vector<Vector> data, int variousAttributeIndex) {
		RowPairsSet rowPairsSet = new RowPairsSet();
		int rowIndex = 1;
		for (Vector row : data) {
			List<Integer> notEqualsRowsIndexes = findRowsPairsIndexesWithVariousAttributeAndDecisionValue(
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
	public static List<Integer> findRowsPairsIndexesWithVariousAttributeAndDecisionValue(
			Vector rowToCompare, Vector<Vector> data, int variousAttributeIndex) {
		List<Integer> rowIndexList = new ArrayList<>();
		Integer rowIndex = 1;

		Integer rowToCompareDecisionAttributeIndex = rowToCompare.size() - 1;
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
	public static RowsSet findDistinctRowsWithVariousAttributesAndDecisionValue(
			Vector<Vector> data, Vector row) {
		RowsSet rowsSet = new RowsSet();
		List<Integer> notEqualsRowsIndexes = findRowsIndexesWithVariousAttributesAndDecisionValue(
				row, data);
		for (Integer notEqualsRowIndex : notEqualsRowsIndexes)
			rowsSet.add(new Row(notEqualsRowIndex, data.get(notEqualsRowIndex - 1)), true);
		return rowsSet;
	}

	/**
	 * Finds all the rows with various attributes and decision value in given data set.
	 *
	 * @param rowToCompare row to be compared with
	 * @param data         data which contains rows set
	 * @return list of the rows indexes
	 */
	private static List<Integer> findRowsIndexesWithVariousAttributesAndDecisionValue(Vector rowToCompare, Vector<Vector> data) {
		List<Integer> rowIndexList = new ArrayList<>();
		Integer rowIndex = 1;

		Integer rowToCompareDecisionAttributeIndex = rowToCompare.size() - 1;
		String rowToCompareDecisionAttributeValue = (String) rowToCompare.get(rowToCompareDecisionAttributeIndex);

		for (Vector row : data) {
			if (!rowToCompare.equals(row) &&
					!rowToCompareDecisionAttributeValue.equals(row.get(rowToCompareDecisionAttributeIndex)))
				rowIndexList.add(rowIndex);
			rowIndex++;
		}
		return rowIndexList;
	}

	/**
	 * Finds all the rows in rowSet with various attribute and decision value in given data set.
	 *
	 * @param rowToCompare          row to be compared with
	 * @param rowsSet               rows set to be searched
	 * @param variousAttributeIndex index of the attributeIndex to be compared
	 * @return {@link RowsSet}
	 */
	public static RowsSet findDistinctRowsWithVariousAttributesAndDecisionValue(RowsSet rowsSet, Vector rowToCompare, int variousAttributeIndex) {
		RowsSet resultRowsSet = new RowsSet();
		for (Row row : rowsSet.getRows()) {
			Integer rowToCompareDecisionAttributeIndex = rowToCompare.size() - 1;
			String rowToCompareAttributeValue = (String) rowToCompare.get(variousAttributeIndex);
			String rowToCompareDecisionAttributeValue = (String) rowToCompare.get(rowToCompareDecisionAttributeIndex);

			if (!rowToCompareAttributeValue.equals(row.getValues().get(variousAttributeIndex)) &&
					!rowToCompareDecisionAttributeValue.equals(row.getValues().get(rowToCompareDecisionAttributeIndex)))
				resultRowsSet.add(row, true);
		}
		return resultRowsSet;
	}
}
