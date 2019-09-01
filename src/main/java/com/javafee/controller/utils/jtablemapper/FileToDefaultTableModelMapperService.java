package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.ejb.Stateless;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.javafee.controller.utils.Constants;

@Stateless
public class FileToDefaultTableModelMapperService implements DefaultTableModelMapperStrategy {
	private DefaultTableModelMapperStrategy defaultTableModelMapperStrategy;

	public FileToDefaultTableModelMapperService(DefaultTableModelMapperStrategy defaultTableModelMapperStrategy) {
		this.defaultTableModelMapperStrategy = defaultTableModelMapperStrategy;
	}

	@Override
	public DefaultTableModel map(File inputData, Constants.SetType setType,
	                             Boolean shuffle, double trainingPercentage, double testPercentage) throws IOException, InvalidFormatException {
		return defaultTableModelMapperStrategy.map(inputData, setType, shuffle, trainingPercentage, testPercentage);
	}

	@Override
	public Map<Constants.SetType, DefaultTableModel> getTableModels() {
		return defaultTableModelMapperStrategy.getTableModels();
	}
}
