package com.javafee.controller.utils.jtablemapper;

import java.io.File;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public interface DefaultTableModelMapperStrategy {
	DefaultTableModel map(File inputData) throws IOException, InvalidFormatException;
}
