package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

@Stateless
public class FileToDefaultTableModelDefaultTableModelMapperService implements DefaultTableModelMapperStrategy {
	private DefaultTableModelMapperStrategy defaultTableModelMapperStrategy;

	public FileToDefaultTableModelDefaultTableModelMapperService(DefaultTableModelMapperStrategy defaultTableModelMapperStrategy) {
		this.defaultTableModelMapperStrategy = defaultTableModelMapperStrategy;
	}

	@Override
	public DefaultTableModel map(File inputData) throws IOException, InvalidFormatException {
		return defaultTableModelMapperStrategy.map(inputData);
	}
}
