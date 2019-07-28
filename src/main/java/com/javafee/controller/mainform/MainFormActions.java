package com.javafee.controller.mainform;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.javafee.controller.Actions;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.decisionrules.DecisionRulesGenerator;
import com.javafee.controller.algorithm.decisionrules.GreedyDecisionRulesGenerator;
import com.javafee.controller.algorithm.test.StandardTestGenerator;
import com.javafee.controller.algorithm.test.TestGenerator;
import com.javafee.controller.parametrisationform.ParametrisationFormActions;
import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.databasemapper.MySQLMapper;
import com.javafee.controller.utils.jtablemapper.CSVFormatToDefaultTableModelDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.ExcelFormatToDefaultTableModelDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.FileToDefaultTableModelDefaultTableModelMapperService;
import com.javafee.controller.utils.params.Params;
import com.javafee.forms.mainform.MainForm;
import com.javafee.forms.mainform.utils.Utils;

@Stateless
public class MainFormActions implements Actions {
	private MainForm mainForm = new MainForm();

	@Inject
	private ParametrisationFormActions parametrisationFormActions;

	@Inject
	private FileToDefaultTableModelDefaultTableModelMapperService fileToDefaultTableModelMapper;

	private TestGenerator testGenerator = new StandardTestGenerator();
	private DecisionRulesGenerator greedyDecisionRulesGenerator = new GreedyDecisionRulesGenerator();

	public void control() {
		setComponentsVisibility();
		initializeListeners();
	}

	private void setComponentsVisibility() {
		setAndGetDataParametersPanelVisibility(mainForm.getCheckBoxShowDataParameters().isSelected());
		setAndGetCheckBoxShowDataParametersVisibility(Params.getInstance().contains("TABLE_NAME"));
	}

	private void initializeListeners() {
		mainForm.getMenuItemLoadData().addActionListener(e -> onClickMenuItemLoadData());
		mainForm.getMenuItemSaveToDatabase().addActionListener(e -> onClickMenuItemSaveToDatabase());
		mainForm.getMenuItemSettings().addActionListener(e -> onClickMenuParametrisation());

		mainForm.getCheckBoxShowDataParameters().addActionListener(e -> onClickCheckBoxDataParameters());

		mainForm.getBtnCheckData().addActionListener(e -> onClickBtnCheckData());
		mainForm.getBtnGenerateTest().addActionListener(e -> onClickBtnGenerateTest());
		mainForm.getBtnGenerateDecisionRules().addActionListener(e -> onClickBtnGenerateDecisionRules());
	}

	private void onClickMenuItemLoadData() {
		File file = Utils.displayFileChooserAndGetFile(null);
		try {
			if (file != null && file.toString().endsWith(Constants.CSV_EXTENSION))
				fileToDefaultTableModelMapper = new FileToDefaultTableModelDefaultTableModelMapperService(new CSVFormatToDefaultTableModelDefaultTableModelMapper());
			else if (file != null && (file.toString().endsWith(Constants.XLS_EXTENSION) || file.toString().endsWith(Constants.XLSX_EXTENSION)))
				fileToDefaultTableModelMapper = new FileToDefaultTableModelDefaultTableModelMapperService(new ExcelFormatToDefaultTableModelDefaultTableModelMapper());
			else {
				throw new InvalidObjectException("Invalid data format");
			}
			mainForm.getDecisionTable().setModel(fileToDefaultTableModelMapper.map(file));
			addTableNameToParams(FilenameUtils.removeExtension(file.getName()));
			setAndGetCheckBoxShowDataParametersVisibility(Params.getInstance().contains("TABLE_NAME"));
			fillDataParametersPanel(((DefaultTableModel) mainForm.getDecisionTable().getModel()));
		} catch (IOException | InvalidFormatException e) {
			Utils.displayErrorJOptionPaneAndLogError(SystemProperties.getResourceBundle().getString("errorOptionPaneTitle"), e.getMessage(), mainForm);
		}
	}

	private void onClickMenuItemSaveToDatabase() {
		if (validateTableDataLoaded()) {
			TableModel decisionTableModel = mainForm.getDecisionTable().getModel();
			MySQLMapper mySQLMapper = new MySQLMapper();
			String tableName = (String) Params.getInstance().get("TABLE_NAME");
			try {
				mySQLMapper.map(decisionTableModel, tableName);
			} catch (SQLException e) {
				Utils.displayErrorJOptionPaneAndLogError(SystemProperties.getResourceBundle().getString("errorOptionPaneTitle"), e.getMessage(), mainForm);
			}
		} else {
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("validationOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("mainFormActions.tableDataLoadedValidationMessage"),
					JOptionPane.ERROR_MESSAGE, mainForm);
		}
	}

	private void onClickMenuParametrisation() {
		if (parametrisationFormActions == null)
			parametrisationFormActions = new ParametrisationFormActions();
		parametrisationFormActions.control();
	}

	private void onClickCheckBoxDataParameters() {
		mainForm.getDataParametersPanel().setVisible(mainForm.getCheckBoxShowDataParameters().isSelected());
	}

	private void onClickBtnCheckData() {

	}

	private void onClickBtnGenerateTest() {
		List<RowPairsSet> rowPairsSetList = testGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		StringBuilder result = new StringBuilder();
		for (RowPairsSet rowPairsSet : rowPairsSetList)
			result.append(rowPairsSet.toString() + "\n");
		mainForm.getTextAreaTest().setText(result.toString());
	}

	private void onClickBtnGenerateDecisionRules() {
		List<List<Object>> resultObjectListOfObject = greedyDecisionRulesGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		StringBuilder result = new StringBuilder();
		for (List<Object> partialResultConsistedOfRowsSetAndRowsSetForEachAttributes : resultObjectListOfObject) {
			result.append(partialResultConsistedOfRowsSetAndRowsSetForEachAttributes.get(0).toString() + "\n");
			for (RowsSet rowsSet : (List<RowsSet>) partialResultConsistedOfRowsSetAndRowsSetForEachAttributes.get(1))
				result.append((rowsSet).toString() + "\n");
			result.append("\n");
		}
		mainForm.getTextAreaDecisionRules().setText(result.toString());
	}

	private void fillDataParametersPanel(DefaultTableModel defaultTableModel) {
		Vector<Vector> dataVector = defaultTableModel.getDataVector();

		StringBuilder attributes = new StringBuilder();
		int columnIndex = 0;
		for (; columnIndex < defaultTableModel.getColumnCount() - 1; columnIndex++)
			attributes.append(defaultTableModel.getColumnName(columnIndex) + " ");

		mainForm.getTextFieldDecisionAttributeIndex().setText(Integer.toString(dataVector.get(0).size() - 1));
		mainForm.getTextFieldConditionalAttributes().setText(attributes.toString());
		mainForm.getTextFieldNumberOfConditionalAttributes().setText(Integer.toString(dataVector.get(0).size() - 1));
		mainForm.getTextFieldNumberOfRows().setText(Integer.toString(dataVector.size()));
		mainForm.getTextFieldNumberOfColumns().setText(Integer.toString(dataVector.get(0).size()));
	}

	private void addTableNameToParams(String tableName) {
		if (Params.getInstance().contains("TABLE_NAME")) {
			Params.getInstance().remove("TABLE_NAME");
			Params.getInstance().add("TABLE_NAME", tableName);
		} else
			Params.getInstance().add("TABLE_NAME", tableName);
	}

	private boolean setAndGetDataParametersPanelVisibility(boolean visibility) {
		mainForm.getDataParametersPanel().setVisible(visibility);
		return mainForm.getDataParametersPanel().isVisible();
	}

	private boolean setAndGetCheckBoxShowDataParametersVisibility(boolean visibility) {
		mainForm.getCheckBoxShowDataParameters().setVisible(visibility);
		return mainForm.getCheckBoxShowDataParameters().isVisible();
	}

	private boolean validateTableDataLoaded() {
		return mainForm.getDecisionTable().getModel().getRowCount() > 0 && Params.getInstance().get("TABLE_NAME") != null;
	}
}
