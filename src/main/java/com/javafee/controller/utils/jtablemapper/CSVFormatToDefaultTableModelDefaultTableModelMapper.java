package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.opencsv.CSVReader;

public class CSVFormatToDefaultTableModelDefaultTableModelMapper implements DefaultTableModelMapperStrategy {
	@Override
	public DefaultTableModel map(File file) throws IOException {
		return processData(file);
	}

	private DefaultTableModel processData(File file) throws IOException {
		Object[] columnNames;
		CSVReader reader = new CSVReader(new FileReader(file));
		List myEntries = reader.readAll();
		columnNames = (String[]) myEntries.get(0);
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, myEntries.size() - 1);
		int rowCount = tableModel.getRowCount();
		for (int row = 0; row < rowCount + 1; row++) {
			int columnNumber = 0;
			if (row > 0)
				for (String cellValue : (String[]) myEntries.get(row)) {
					tableModel.setValueAt(cellValue, row - 1, columnNumber);
					columnNumber++;
				}
		}
		return tableModel;
	}
}
