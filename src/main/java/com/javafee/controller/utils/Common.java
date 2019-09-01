package com.javafee.controller.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

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

	public void initializeComboBoxSetType(JComboBox jComboBox) {
		jComboBox.addItem(Constants.SetType.FULL.getName());
		jComboBox.addItem(Constants.SetType.TRAINING.getName());
		jComboBox.addItem(Constants.SetType.TEST.getName());
	}
}
