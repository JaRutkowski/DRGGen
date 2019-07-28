package com.javafee.controller.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
}
