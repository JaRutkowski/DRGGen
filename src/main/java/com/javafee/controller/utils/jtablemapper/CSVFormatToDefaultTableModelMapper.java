package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.javafee.controller.utils.Constants;
import com.opencsv.CSVReader;

public class CSVFormatToDefaultTableModelMapper extends JTableMapper implements DefaultTableModelMapperStrategy {
	@Override
	public DefaultTableModel map(File file, Constants.SetType setType,
	                             Boolean shuffle, double trainingPercentage, double testPercentage) throws IOException, InvalidFormatException {
		return processData(file, setType, shuffle, trainingPercentage, testPercentage);
	}

	private DefaultTableModel processData(File file, Constants.SetType setType,
	                                      boolean shuffle, double trainingPercentage, double testPercentage) throws IOException, InvalidFormatException {
		// Load data from csv file
		CSVReader reader = new CSVReader(new FileReader(file));

		List data = reader.readAll();

		// First row preparation
		columnNames = (String[]) data.get(0);

		// Data from reader to List loading
		data = data.subList(1, (int) reader.getLinesRead());

		// Data shuffling
		if (shuffle)
			Collections.shuffle(data);

		// Calculate train and test sub sets
		calculateIndexesForDataSubSets(trainingPercentage, data.size());

		// DefaultTableModels preparation
		prepareDefaultTableModels(data);

		return getTableModels().get(setType);
	}
}
