package com.javafee.controller.mainform;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.decisionrules.DecisionRulesGenerator;
import com.javafee.controller.algorithm.decisionrules.StandardDecisionRulesGenerator;
import com.javafee.controller.algorithm.measures.StandardErrorRateMeasure;
import com.javafee.controller.algorithm.measures.StandardQualityMeasure;
import com.javafee.controller.algorithm.measures.StandardSupportMeasure;
import com.javafee.controller.algorithm.test.StandardTestGenerator;
import com.javafee.controller.algorithm.test.TestGenerator;
import com.javafee.controller.parametrisationform.ParametrisationFormActions;
import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.cache.Cache;
import com.javafee.controller.utils.databasemapper.MySQLMapperService;
import com.javafee.controller.utils.jtablemapper.CSVFormatToDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.ExcelFormatToDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.FileToDefaultTableModelMapperService;
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
	private FileToDefaultTableModelMapperService fileToDefaultTableModelMapperService;

	@Inject
	private MySQLMapperService mySQLMapperService;

	private TestGenerator testGenerator = new StandardTestGenerator();
	private DecisionRulesGenerator greedyDecisionRulesGenerator = new StandardDecisionRulesGenerator();
	private StandardQualityMeasure standardQualityMeasure = null;

	private List<List<Object>> decisionRulesGeneratorResult = null;
	private TableFilterHeader decisionTableFilterHeader = null;

	public void control() {
		initializeComboBoxSetType();
		setComponentsVisibility();
		initializeListeners();
	}

	private void initializeComboBoxSetType() {
		Common.initializeComboBoxSetType(mainForm.getComboBoxSetType());
	}

	private void setComponentsVisibility() {
		setAndGetDataParametersPanelVisibility(mainForm.getCheckBoxShowDataParameters().isSelected());
		setAndGetCheckBoxShowDataParametersVisibility(Params.getInstance().contains("TABLE_NAME"));
		setAndGetCheckBoxShowCacheVisibility(Params.getInstance().contains("TABLE_NAME"));
		setAndGetBtnCalculateDecisionRulesMeasures(Params.getInstance().contains("TABLE_NAME"));
		setAndGetScrollPaneEditorPaneDecisionRulesMeasuresVisibility(Params.getInstance().contains("TABLE_NAME"));
		setAndGetSplitPaneCacheVisibility(mainForm.getCheckBoxShowCache().isSelected());
		setAndGetScrollPaneEditorPaneCachedDecisionRulesMeasuresVisibility(mainForm.getCheckBoxShowCache().isSelected());
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
		mainForm.getBtnCalculateDecisionRulesMeasures().addActionListener(e -> onClickBtnCalculateDecisionRulesMeasures());
	}

	private void onClickMenuItemLoadData() {
		File file = Utils.displayFileChooserAndGetFile(null);
		try {
			if (file != null && file.toString().endsWith(Constants.CSV_EXTENSION))
				fileToDefaultTableModelMapperService = new FileToDefaultTableModelMapperService(new CSVFormatToDefaultTableModelMapper());
			else if (file != null && (file.toString().endsWith(Constants.XLS_EXTENSION) || file.toString().endsWith(Constants.XLSX_EXTENSION)))
				fileToDefaultTableModelMapperService = new FileToDefaultTableModelMapperService(new ExcelFormatToDefaultTableModelMapper());
			else
				throw new InvalidObjectException("Invalid data format");
			DefaultTableModel defaultTableModel = fileToDefaultTableModelMapperService.map(file, SystemProperties.getSystemParameterSetType(),
					SystemProperties.isSystemParameterShuffle(), SystemProperties.getSystemParameterTrainingPercentage(),
					SystemProperties.getSystemParameterTestPercentage());

			buildAndRefreshViewOfDecisionTable(defaultTableModel);
			addTableNameToParams(FilenameUtils.removeExtension(file.getName()));
			setAndGetCheckBoxShowDataParametersVisibility(Params.getInstance().contains("TABLE_NAME"));
			setAndGetCheckBoxShowCacheVisibility(Params.getInstance().contains("TABLE_NAME"));
			fillDataParametersPanel(defaultTableModel);

			SystemProperties.setSystemParameterDecisionAttributeIndex(
					SystemProperties.getSystemParameterDecisionAttributeIndex(((Vector) defaultTableModel.getDataVector().get(0)).size()));
			if (Params.getInstance().contains("PARAM_FORM_ACTIONS_COMPONENTS_REFRESH") && Params.getInstance().contains("PARAM_FORM_ACTIONS_INITIALIZE_PARAMETERS")) {
				((Consumer) Params.getInstance().get("PARAM_FORM_ACTIONS_COMPONENTS_REFRESH")).accept(null);
				((Consumer) Params.getInstance().get("PARAM_FORM_ACTIONS_INITIALIZE_PARAMETERS")).accept(null);
			}
			Consumer fillDataParametersPanel = (e) -> fillDataParametersPanel(fileToDefaultTableModelMapperService.getTableModels().get(SystemProperties.getSystemParameterSetType()));
			Params.getInstance().add("MAIN_FORM_ACTIONS_FILL_DATA_PARAMS_PANEL", fillDataParametersPanel);
			Consumer buildAndRefreshViewOfDecisionTable = (e) -> buildAndRefreshViewOfDecisionTable(fileToDefaultTableModelMapperService.getTableModels().get(SystemProperties.getSystemParameterSetType()));
			Params.getInstance().add("MAIN_FORM_ACTIONS_BUILD_AND_REFRESH_VIEW_OF_DECISION_TABLE", buildAndRefreshViewOfDecisionTable);
		} catch (IOException | InvalidFormatException e) {
			Utils.displayErrorJOptionPaneAndLogError(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"), e.getMessage(), mainForm);
		}
	}

	private void onClickMenuItemSaveToDatabase() {
		if (validateTableDataLoaded()) {
			TableModel decisionTableModel = mainForm.getDecisionTable().getModel();
			mySQLMapperService = new MySQLMapperService();
			String tableName = (String) Params.getInstance().get("TABLE_NAME");
			try {
				mySQLMapperService.map(decisionTableModel, tableName);
			} catch (SQLException e) {
				Utils.displayErrorJOptionPaneAndLogError(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"), e.getMessage(), mainForm);
			}
		} else {
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.validationOptionPaneTitle"),
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
		decisionRulesGeneratorResult = greedyDecisionRulesGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		Cache.getInstance().cache("DECISION_RULES", decisionRulesGeneratorResult);
		refreshTextAreaDecisionRulesBaseOnSysParameters(decisionRulesGeneratorResult, true);
		setAndGetBtnCalculateDecisionRulesMeasures(true);
	}

	private void onClickBtnCalculateDecisionRulesMeasures() {
		List<LogicalExpression> decisionRules = new ArrayList<>();
		for (List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes : decisionRulesGeneratorResult) {
			decisionRules.add((LogicalExpression) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.DECISION_RULES.getValue()));
		}

		StringBuilder result = new StringBuilder();
		standardQualityMeasure = new StandardSupportMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), decisionRules, null);
		result.append("Support: " + ((StandardSupportMeasure) standardQualityMeasure).calculateAverage().toString() + "<br>");
		standardQualityMeasure = new StandardErrorRateMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), decisionRules, null);
		result.append("Error rate: " + ((StandardErrorRateMeasure) standardQualityMeasure).calculateAverage().toString());
		mainForm.getEditorPaneDecisionRulesMeasures().setText(result.toString());

		setAndGetBtnCalculateDecisionRulesMeasures(true);
		setAndGetScrollPaneEditorPaneDecisionRulesMeasuresVisibility(true);
		mainForm.pack();
	}

	private void fillDataParametersPanel(DefaultTableModel defaultTableModel) {
		Vector<Vector> dataVector = defaultTableModel.getDataVector();

		StringBuilder attributes = new StringBuilder();
		int columnIndex = 0;
		for (; columnIndex < defaultTableModel.getColumnCount() - 1; columnIndex++)
			attributes.append(defaultTableModel.getColumnName(columnIndex) + " ");

		refreshTrainingAndTestAttributes();
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
		boolean isSysParametersAll = SystemProperties.getSystemParameterDecisionRulesDataRange() == Constants.DecisionRulesDataRange.ALL_DATA,
				isSysParametersCoverageAndDecisionRulesSet = SystemProperties.getSystemParameterDecisionRulesDataRange() == Constants.DecisionRulesDataRange.COVERAGE_AND_DECISION_RULES,
				isSysParametersDecisionRulesSet = SystemProperties.getSystemParameterDecisionRulesDataRange() == Constants.DecisionRulesDataRange.DECISION_RULES;

		if (isSysParametersAll) {
			result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.ROWS_SET.getValue()).toString() + "<br>");
			for (RowsSet rowsSet : (List<RowsSet>) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(
					Constants.StandardDecisionRulesGenerator.ROWS_SET_FOR_EACH_ATTRIBUTE.getValue()))
				result.append((rowsSet).toString() + "<br>");
			result.append("<br>");
		}

		if (isSysParametersAll || isSysParametersCoverageAndDecisionRulesSet) {
			result.append("Coverage: <br>");
			for (RowsSet rowsSet : (List<RowsSet>) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(
					Constants.StandardDecisionRulesGenerator.COVERAGE.getValue()))
				result.append((rowsSet).toString() + "<br>");
			result.append("<br>");
		}

		if (isSysParametersAll || isSysParametersCoverageAndDecisionRulesSet || isSysParametersDecisionRulesSet) {
			result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.DECISION_RULES.getValue()).toString());
			result.append("<br>");
		}
	}

	private void refreshTrainingAndTestAttributes() {
		mainForm.getComboBoxSetType().setSelectedItem(SystemProperties.getSystemParameterSetType().getName());
		mainForm.getTextFieldTrainingPercentage().setText(Double.toString(SystemProperties.getSystemParameterTrainingPercentage()));
		mainForm.getTextFieldTestPercentage().setText(Double.toString(SystemProperties.getSystemParameterTestPercentage()));
		mainForm.getCheckBoxShowCache().setSelected(SystemProperties.isSystemParameterShuffle());
	}

	private void buildAndRefreshViewOfDecisionTable(DefaultTableModel defaultTableModel) {
		mainForm.getDecisionTable().setModel(defaultTableModel);
		((DefaultTableModel) mainForm.getDecisionTable().getModel()).fireTableDataChanged();
		if (decisionTableFilterHeader == null)
			decisionTableFilterHeader = new TableFilterHeader(mainForm.getDecisionTable());
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

	private boolean setAndGetBtnCalculateDecisionRulesMeasures(boolean visibility) {
		mainForm.getBtnCalculateDecisionRulesMeasures().setVisible(visibility);
		return mainForm.getBtnCalculateDecisionRulesMeasures().isVisible();
	}

	private boolean setAndGetScrollPaneEditorPaneDecisionRulesMeasuresVisibility(boolean visibility) {
		mainForm.getScrollPaneEditorPaneDecisionRulesMeasures().setVisible(visibility);
		return mainForm.getScrollPaneEditorPaneDecisionRulesMeasures().isVisible();
	}

	private boolean setAndGetSplitPaneCacheVisibility(boolean visibility) {
		mainForm.getSplitPaneCache().setVisible(visibility);
		return mainForm.getSplitPaneCache().isVisible();
	}

	private boolean setAndGetScrollPaneEditorPaneCachedDecisionRulesMeasuresVisibility(boolean visibility) {
		mainForm.getScrollPaneEditorPaneCachedDecisionRulesMeasures().setVisible(visibility);
		return mainForm.getScrollPaneEditorPaneCachedDecisionRulesMeasures().isVisible();
	}

	private boolean validateTableDataLoaded() {
		return mainForm.getDecisionTable().getModel().getRowCount() > 0 && Params.getInstance().get("TABLE_NAME") != null;
	}
}
