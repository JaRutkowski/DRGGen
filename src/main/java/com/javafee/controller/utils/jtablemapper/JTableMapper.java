package com.javafee.controller.utils.jtablemapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.Constants;

import lombok.Getter;

public class JTableMapper {
	@Getter
	private Map<Constants.SetType, DefaultTableModel> tableModels = new HashMap<>();

	public int trainingToIndex = 0;
	public Object[] columnNames = null;

	public void calculateIndexesForDataSubSets(double trainPercent, int dataSize) {
		trainingToIndex = (int) Math.ceil(Double.valueOf(dataSize) * (trainPercent / 100)) - 1;
	}

	public void prepareDefaultTableModels(List<List<Object>> data) {
		List<List<Object>> trainingData = data.subList(0, trainingToIndex + 1);
		List<List<Object>> testData = data.subList(trainingToIndex, data.size() - 1);
		tableModels.put(Constants.SetType.FULL, Common.fillDefaultTableModel(data, columnNames));
		tableModels.put(Constants.SetType.TRAINING, Common.fillDefaultTableModel(trainingData, columnNames));
		tableModels.put(Constants.SetType.TEST, Common.fillDefaultTableModel(testData, columnNames));
	}
}
