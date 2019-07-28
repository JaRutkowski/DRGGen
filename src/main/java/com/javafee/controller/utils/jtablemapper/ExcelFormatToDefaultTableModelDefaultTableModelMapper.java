package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFormatToDefaultTableModelDefaultTableModelMapper implements DefaultTableModelMapperStrategy {
	@Override
	public DefaultTableModel map(File file) throws IOException, InvalidFormatException {
		return processData(file);
	}

	private DefaultTableModel processData(File file) throws IOException, InvalidFormatException {
		Object[] columnNames;
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();

		List<Object> firstRow = new ArrayList<>();
		for (Cell cell1 : sheet.getRow(0)) {
			String cellValue = dataFormatter.formatCellValue(cell1);
			firstRow.add(cellValue);
		}

		columnNames = firstRow.toArray();
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, sheet.getLastRowNum());

		AtomicInteger rowIndex = new AtomicInteger(0);
		for (Row row : sheet) {
			if (row.getRowNum() != 0) {
				List<Object> rowData = new ArrayList<>();
				AtomicInteger columnNumber = new AtomicInteger(0);
				for (Cell cell : row) {
					String cellValue = dataFormatter.formatCellValue(cell);
					tableModel.setValueAt(cellValue, rowIndex.get() - 1, columnNumber.get());
					rowData.add(cellValue);
					columnNumber.getAndIncrement();
				}
			}
			rowIndex.getAndIncrement();
		}
		return tableModel;
	}
}
