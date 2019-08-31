package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.javafee.controller.utils.Constants;

public interface DefaultTableModelMapperStrategy {
	DefaultTableModel map(File inputData, Constants.SetType setType,
	                      Boolean shuffle, double trainingPercentage, double testPercentage) throws IOException, InvalidFormatException;

	Map<Constants.SetType, DefaultTableModel> getTableModels();
}
