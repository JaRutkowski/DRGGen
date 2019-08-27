package com.javafee.controller.mainform;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

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
import com.javafee.controller.algorithm.decisionrules.StandardDecisionRulesGenerator;
import com.javafee.controller.algorithm.test.StandardTestGenerator;
import com.javafee.controller.algorithm.test.TestGenerator;
import com.javafee.controller.parametrisationform.ParametrisationFormActions;
import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.cache.Cache;
import com.javafee.controller.utils.databasemapper.MySQLMapper;
import com.javafee.controller.utils.jtablemapper.CSVFormatToDefaultTableModelDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.ExcelFormatToDefaultTableModelDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.FileToDefaultTableModelDefaultTableModelMapperService;
import com.javafee.controller.utils.params.Params;
import com.javafee.forms.mainform.MainForm;
import com.javafee.forms.mainform.utils.Utils;

import net.coderazzi.filters.gui.TableFilterHeader;

@Stateless
public class MainFormActions implements Actions {
	private MainForm mainForm = new MainForm();

	@Inject
	private ParametrisationFormActions parametrisationFormActions;

	@Inject
	private FileToDefaultTableModelDefaultTableModelMapperService fileToDefaultTableModelMapper;

	private TestGenerator testGenerator = new StandardTestGenerator();
	private DecisionRulesGenerator greedyDecisionRulesGenerator = new StandardDecisionRulesGenerator();

	public void control() {
		setComponentsVisibility();
		initializeListeners();
	}

	private void setComponentsVisibility() {
		setAndGetDataParametersPanelVisibility(mainForm.getCheckBoxShowDataParameters().isSelected());
		setAndGetCheckBoxShowDataParametersVisibility(Params.getInstance().contains("TABLE_NAME"));
		setAndGetCheckBoxShowCacheVisibility(Params.getInstance().contains("TABLE_NAME"));
		setAndGetSplitPaneCacheVisibility(mainForm.getCheckBoxShowCache().isSelected());
	}

	private void initializeListeners() {
		mainForm.getMenuItemLoadData().addActionListener(e -> onClickMenuItemLoadData());
		mainForm.getMenuItemSaveToDatabase().addActionListener(e -> onClickMenuItemSaveToDatabase());
		mainForm.getMenuItemSettings().addActionListener(e -> onClickMenuParametrisation());

		mainForm.getCheckBoxShowDataParameters().addActionListener(e -> onClickCheckBoxDataParameters());
		mainForm.getCheckBoxShowCache().addActionListener(e -> onClickCheckBoxShowCache());

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
			DefaultTableModel defaultTableModel = fileToDefaultTableModelMapper.map(file);

			buildAndRefreshViewWithDecisionTable(defaultTableModel);
			addTableNameToParams(FilenameUtils.removeExtension(file.getName()));
			setAndGetCheckBoxShowDataParametersVisibility(Params.getInstance().contains("TABLE_NAME"));
			setAndGetCheckBoxShowCacheVisibility(Params.getInstance().contains("TABLE_NAME"));
			fillDataParametersPanel(defaultTableModel);

			SystemProperties.setSystemParameterDecisionAttributeIndex(
					SystemProperties.getSystemParameterDecisionAttributeIndex(((Vector) defaultTableModel.getDataVector().get(0)).size()));
			if (Params.getInstance().contains("PARAM_FORM_ACTIONS_COMPONENTS_REFRESH") && Params.getInstance().contains("PARAM_FORM_ACTIONS_INITIALIZE_PARAMETERS")) {
				((Consumer) Params.getInstance().get("PARAM_FORM_ACTIONS_COMPONENTS_REFRESH")).accept(null);
				((Consumer) Params.getInstance().get("PARAM_FORM_ACTIONS_INITIALIZE_PARAMETERS")).accept(null);

				Consumer refreshTextFieldDecisionAttributeIndex = (e) -> refreshTextFieldDecisionAttributeIndex();
				Params.getInstance().add("MAIN_FORM_ACTIONS_REFRESH_TEXT_FIELD_DEC_ATTR_IDX", refreshTextFieldDecisionAttributeIndex);
			}
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

	private void onClickCheckBoxShowCache() {
		setAndGetSplitPaneCacheVisibility(mainForm.getCheckBoxShowCache().isSelected());
		mainForm.pack();
	}

	private void onClickBtnCheckData() {

	}

	private void onClickBtnGenerateTest() {
		List<RowPairsSet> rowPairsSetList = testGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		Cache.getInstance().cache("TEST", rowPairsSetList);
		refreshTextAreaTest(rowPairsSetList, true);
	}

	private void onClickBtnGenerateDecisionRules() {
		List<List<Object>> resultObjectListOfObject = greedyDecisionRulesGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		Cache.getInstance().cache("DECISION_RULES", resultObjectListOfObject);
		refreshTextAreaDecisionRulesBaseOnSysParameters(resultObjectListOfObject, true);
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

	private void refreshTextAreaTest(List<RowPairsSet> rowPairsSetList, boolean withCache) {
		StringBuilder result = new StringBuilder("<b>Reduct set:</b><br>");
		for (RowPairsSet rowPairsSet : rowPairsSetList)
			result.append(rowPairsSet.toString() + "<br>");
		mainForm.getEditorPaneTest().setText(result.toString());

		if (withCache && Cache.getInstance().get("TEST") != null) {
			result = new StringBuilder();
			for (RowPairsSet rowPairsSet : (List<RowPairsSet>) Cache.getInstance().get("TEST"))
				result.append(rowPairsSet.toString() + "<br>");
			mainForm.getEditorPaneCachedTest().setText(result.toString());
		}
	}

	private void refreshTextAreaDecisionRulesBaseOnSysParameters(List<List<Object>> resultObjectListOfObject, boolean withCache) {
		StringBuilder result = new StringBuilder("<b>Decision rules:</b><br>");
		for (List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes : resultObjectListOfObject) {
			buildResultForTextAreaDecisionRules(result, resultConsistedOfRowsSetAndRowsSetForEachAttributes);
		}
		mainForm.getEditorPaneDecisionRules().setText(result.toString());


		if (withCache && Cache.getInstance().get("DECISION_RULES") != null) {
			result = new StringBuilder("<b>Decision rules:</b><br>");
			for (List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes : (List<List<Object>>) Cache.getInstance().get("DECISION_RULES")) {
				buildResultForTextAreaDecisionRules(result, resultConsistedOfRowsSetAndRowsSetForEachAttributes);
			}
			mainForm.getEditorPaneCachedDecisionRules().setText(result.toString());
		}
	}

	private void buildResultForTextAreaDecisionRules(StringBuilder result, List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes) {
		boolean isSysParametersAll = SystemProperties.getDecisionRulesDataRange() == Constants.DecisionRulesDataRange.ALL_DATA,
				isSysParametersCoverageAndDecisionRulesSet = SystemProperties.getDecisionRulesDataRange() == Constants.DecisionRulesDataRange.COVERAGE_AND_DECISION_RULES,
				isSysParametersDecisionRulesSet = SystemProperties.getDecisionRulesDataRange() == Constants.DecisionRulesDataRange.DECISION_RULES;

		if (isSysParametersAll) {
			result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.ROWS_SET.getResultIndex()).toString() + "<br>");
			for (RowsSet rowsSet : (List<RowsSet>) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(
					Constants.StandardDecisionRulesGenerator.ROWS_SET_FOR_EACH_ATTRIBUTE.getResultIndex()))
				result.append((rowsSet).toString() + "<br>");
			result.append("<br>");
		}

		if (isSysParametersAll || isSysParametersCoverageAndDecisionRulesSet) {
			result.append("Coverage: <br>");
			for (RowsSet rowsSet : (List<RowsSet>) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(
					Constants.StandardDecisionRulesGenerator.COVERAGE.getResultIndex()))
				result.append((rowsSet).toString() + "<br>");
			result.append("<br>");
		}

		if (isSysParametersAll || isSysParametersCoverageAndDecisionRulesSet || isSysParametersDecisionRulesSet) {
			result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.DECISION_RULES.getResultIndex()).toString());
			result.append("<br>");
		}
	}

	private void refreshTextFieldDecisionAttributeIndex() {
		mainForm.getTextFieldDecisionAttributeIndex().setText(Integer.toString(SystemProperties.getSystemParameterDecisionAttributeIndex()));
	}

	private void buildAndRefreshViewWithDecisionTable(DefaultTableModel defaultTableModel) {
		mainForm.getDecisionTable().setModel(defaultTableModel);
		new TableFilterHeader(mainForm.getDecisionTable());
		mainForm.getDecisionTableScrollPane().setViewportView(mainForm.getDecisionTable());
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

	private boolean setAndGetCheckBoxShowCacheVisibility(boolean visibility) {
		mainForm.getCheckBoxShowCache().setVisible(visibility);
		return mainForm.getCheckBoxShowCache().isVisible();
	}

	private boolean setAndGetSplitPaneCacheVisibility(boolean visibility) {
		mainForm.getSplitPaneCache().setVisible(visibility);
		return mainForm.getSplitPaneCache().isVisible();
	}

	private boolean validateTableDataLoaded() {
		return mainForm.getDecisionTable().getModel().getRowCount() > 0 && Params.getInstance().get("TABLE_NAME") != null;
	}
}
