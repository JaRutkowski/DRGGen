package com.javafee.controller.weka.utils;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import com.opencsv.CSVReader;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;

@Stateless
public class TestWekaIntegrationDataSplitService {
	public void trainTestSplit() {
		try {
			Object[] columnNames;
			CSVReader reader = new CSVReader(new FileReader("C:/Users/rutko/Desktop/cars — kopia.csv"));
			List listFromCSV = reader.readAll();

			CSVLoader csvLoader = new CSVLoader();
			csvLoader.setSource(new File("C:/Users/rutko/Desktop/cars — kopia.csv"));
			Instances instances = new ConverterUtils.DataSource(csvLoader).getDataSet();

			int trainSize = (int) Math.round(instances.numInstances() * 0.8);
			int testSize = instances.numInstances() - trainSize;

			Instances train = new Instances(instances, 0, trainSize);
			Instances test = new Instances(instances, trainSize, testSize);

			List<Object> instanceToList = Arrays.asList(instances.toArray());

			instanceToList.forEach(e ->
					System.out.println(e)
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
