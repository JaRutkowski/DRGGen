package com.javafee.controller.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import com.javafee.controller.algorithm.datastructure.RowsSet;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Common {
	public String[] getColumnNamesFromDefaultTableModel(DefaultTableModel defaultTableModel) {
		String[] result = new String[]{};
		int dfmColumnCount = defaultTableModel.getColumnCount();
		List<String> columnNames = IntStream.range(0, dfmColumnCount).mapToObj(defaultTableModel::getColumnName)
				.collect(Collectors.toList());
		return columnNames.toArray(result);
	}

	public DefaultTableModel fillDefaultTableModel(List testData, Object[] columnNames) {
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, testData.size());
		AtomicInteger rowIndex = new AtomicInteger(0);
		testData.forEach(row -> {
			AtomicInteger columnNumber = new AtomicInteger(0);
			if (row instanceof List) {
				for (Object cell : (List<Object>) row) {
					tableModel.setValueAt(cell.toString(), rowIndex.get(), columnNumber.get());
					columnNumber.getAndIncrement();
				}
				rowIndex.getAndIncrement();
			} else if (row instanceof String[]) {
				for (String cell : (String[]) row) {
					tableModel.setValueAt(cell, rowIndex.get(), columnNumber.get());
					columnNumber.getAndIncrement();
				}
				rowIndex.getAndIncrement();
			}
		});
		return tableModel;
	}

	public Vector prepareColumnForGivenAttributeIndexFromData(Vector<Vector> data, long indexOfAttribute) {
		Vector columnForGivenAttributeIndex = new Vector();
		data.forEach(row -> columnForGivenAttributeIndex.add(row.get(Math.toIntExact(indexOfAttribute))));
		return columnForGivenAttributeIndex;
	}

	public Vector prepareColumnForGivenAttributeIndexFromData(RowsSet data, long indexOfAttribute) {
		Vector columnForGivenAttributeIndex = new Vector();
		data.getRows().forEach(row -> columnForGivenAttributeIndex.add(row.getValues().get(Math.toIntExact(indexOfAttribute))));
		return columnForGivenAttributeIndex;
	}

	public Vector prepareVectorOnlyWithConditionalAttributesValuesForGivenRow(Vector row, long decisionAttributeIndex) {
		Vector clone = (Vector) row.clone();
		clone.remove(Math.toIntExact(decisionAttributeIndex));
		return clone;
	}

	public <T> T[] concatenateArrays(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public long[] concatenateArrays(long[] first, long[] second) {
		long[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public void refreshSystemParameterDecisionAttributeIndexAfterAttributesRemoval(long[] removedAttributesIndexes) {
		int amountOfRemovedAttributesWithLowerIndexWhenDecisionAttribute =
				Arrays.stream(removedAttributesIndexes).boxed().collect(Collectors.toList())
						.stream().filter(e -> e < SystemProperties.getSystemParameterDecisionAttributeIndex())
						.collect(Collectors.toList()).size();

		SystemProperties.setSystemParameterDecisionAttributeIndex(
				SystemProperties.getSystemParameterDecisionAttributeIndex() - amountOfRemovedAttributesWithLowerIndexWhenDecisionAttribute
		);
	}

	public void initializeComboBoxAlgorithm(JComboBox jComboBox) {
		jComboBox.addItem(Constants.Algorithm.GREEDY_FOR_CONSISTENT_DATA.getName());
		jComboBox.addItem(Constants.Algorithm.GREEDY_FOR_INCONSISTENT_DATA.getName());
	}

	public void initializeComboBoxSetType(JComboBox jComboBox) {
		jComboBox.addItem(Constants.SetType.FULL.getName());
		jComboBox.addItem(Constants.SetType.TRAINING.getName());
		jComboBox.addItem(Constants.SetType.TEST.getName());
	}

	public void initializeComboBoxSetTypeConsistency(JComboBox jComboBox) {
		jComboBox.addItem(Constants.SetTypeConsistency.NOT_CHECKED.getName());
		jComboBox.addItem(Constants.SetTypeConsistency.CONSISTENT.getName());
		jComboBox.addItem(Constants.SetTypeConsistency.INCONSISTENT.getName());
	}
}
