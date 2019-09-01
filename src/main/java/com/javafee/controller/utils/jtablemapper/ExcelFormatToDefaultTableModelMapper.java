package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

import com.javafee.controller.utils.Constants;

public class ExcelFormatToDefaultTableModelMapper extends JTableMapper implements DefaultTableModelMapperStrategy {
	@Override
	public DefaultTableModel map(File file, Constants.SetType setType,
	                             Boolean shuffle, double trainingPercentage, double testPercentage) throws IOException, InvalidFormatException {
		return processData(file, setType, shuffle, trainingPercentage, testPercentage);
	}

	private DefaultTableModel processData(File file, Constants.SetType setType,
	                                      boolean shuffle, double trainingPercentage, double testPercentage) throws IOException, InvalidFormatException {
		// Load data sheet from xls/xlsx file
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();

		// First row preparation
		List<Object> firstRow = new ArrayList<>();
		for (Cell cell : sheet.getRow(0)) {
			String cellValue = dataFormatter.formatCellValue(cell);
			firstRow.add(cellValue);
		}
		columnNames = firstRow.toArray();

		// Data from sheet to List loading
		List<List<Object>> data = loadDataFromSheetToList(sheet);

		// Data shuffling
		if (shuffle)
			Collections.shuffle(data);

		// Calculate train and test sub sets
		calculateIndexesForDataSubSets(trainingPercentage, data.size());

		// DefaultTableModels preparation
		prepareDefaultTableModels(data);

		return getTableModels().get(setType);
	}

	private List<List<Object>> loadDataFromSheetToList(Sheet sheet) {
		DataFormatter dataFormatter = new DataFormatter();
		List<List<Object>> data = new ArrayList<>();
		AtomicInteger rowIndex = new AtomicInteger(0);
		for (Row row : sheet) {
			if (row.getRowNum() != 0) {
				List<Object> rowData = new ArrayList<>();
				AtomicInteger columnNumber = new AtomicInteger(0);
				for (Cell cell : row) {
					String cellValue = dataFormatter.formatCellValue(cell);
					rowData.add(cellValue);
				}
				data.add(rowData);
				columnNumber.getAndIncrement();
			}
			rowIndex.getAndIncrement();
		}
		return data;
	}
}
