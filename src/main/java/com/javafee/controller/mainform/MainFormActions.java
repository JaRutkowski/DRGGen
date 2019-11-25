package com.javafee.controller.mainform;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.javafee.controller.Actions;
import com.javafee.controller.algorithm.datapreprocessing.inconsistency.InconsistencyGenerator;
import com.javafee.controller.algorithm.datapreprocessing.inconsistency.StandardInconsistencyGenerator;
import com.javafee.controller.algorithm.datastructure.LogicalExpression;
import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowPairsSet;
import com.javafee.controller.algorithm.datastructure.RowsSet;
import com.javafee.controller.algorithm.decisionrules.DecisionRulesGenerator;
import com.javafee.controller.algorithm.decisionrules.InconsistentDataDecisionRulesGenerator;
import com.javafee.controller.algorithm.decisionrules.StandardDecisionRulesGenerator;
import com.javafee.controller.algorithm.exception.AlgorithmException;
import com.javafee.controller.algorithm.measures.StandardDecisionRulesLengthMeasure;
import com.javafee.controller.algorithm.measures.StandardErrorRateMeasure;
import com.javafee.controller.algorithm.measures.StandardNumberOfVariousDecisionRulesMeasure;
import com.javafee.controller.algorithm.measures.StandardQualityMeasure;
import com.javafee.controller.algorithm.measures.StandardSupportMeasure;
import com.javafee.controller.algorithm.process.InconsistencyProcess;
import com.javafee.controller.algorithm.process.VectorProcess;
import com.javafee.controller.algorithm.test.StandardTestGenerator;
import com.javafee.controller.algorithm.test.TestGenerator;
import com.javafee.controller.parametrisationform.ParametrisationFormActions;
import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.SingletonFactory;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.cache.Cache;
import com.javafee.controller.utils.databasemapper.MySQLMapperService;
import com.javafee.controller.utils.jtablemapper.CSVFormatToDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.ExcelFormatToDefaultTableModelMapper;
import com.javafee.controller.utils.jtablemapper.FileToDefaultTableModelMapperService;
import com.javafee.controller.utils.params.Params;
import com.javafee.forms.mainform.MainForm;
import com.javafee.forms.utils.Utils;
import com.javafee.model.foodb.dao.text.FotDBDao;

import lombok.extern.java.Log;
import net.coderazzi.filters.gui.TableFilterHeader;

@Stateless
@Log
public class MainFormActions implements Actions {
	private MainForm mainForm = new MainForm();

	@Inject
	private ParametrisationFormActions parametrisationFormActions;

	@Inject
	private FileToDefaultTableModelMapperService fileToDefaultTableModelMapperService;

	@Inject
	private MySQLMapperService mySQLMapperService;

	private TestGenerator testGenerator = new StandardTestGenerator();
	private DecisionRulesGenerator decisionRulesGenerator = null;
	private InconsistencyGenerator inconsistencyGenerator = new StandardInconsistencyGenerator();
	private StandardQualityMeasure standardQualityMeasure = null;

	private List<List<Object>> decisionRulesGeneratorResult = null;
	private TableFilterHeader decisionTableFilterHeader = null;

	public void control() {
		initializeComboBoxAlgorithm();
		initializeComboBoxSetType();
		initializeComboBoxSetTypeConsistency();
		setComponentsVisibility();
		initializeListeners();

		mainForm.getMainFrame().setVisible(true);
	}

	private void initializeComboBoxAlgorithm() {
		Common.initializeComboBoxAlgorithm(mainForm.getComboBoxAlgorithm());
		onChangeComboBoxAlgorithm();
	}

	private void initializeComboBoxSetType() {
		Common.initializeComboBoxSetType(mainForm.getComboBoxSetType());
	}

	private void initializeComboBoxSetTypeConsistency() {
		Common.initializeComboBoxSetTypeConsistency(mainForm.getComboBoxSetTypeConsistency());
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
		mainForm.getMenuItemLoadData().addActionListener(e -> onClickMenuItemLoadData(Utils.displayFileChooserAndGetFile(null)));
		mainForm.getMenuItemSaveToDatabase().addActionListener(e -> onClickMenuItemSaveToDatabase());
		mainForm.getMenuItemSettings().addActionListener(e -> onClickMenuParametrisation());

		mainForm.getComboBoxAlgorithm().addActionListener(e -> onChangeComboBoxAlgorithm());

		mainForm.getCheckBoxShowDataParameters().addActionListener(e -> onClickCheckBoxDataParameters());
		mainForm.getCheckBoxShowCache().addActionListener(e -> onClickCheckBoxShowCache());

		mainForm.getBtnCheckIfInconsistencyExists().addActionListener(e -> onClickBtnCheckIfInconsistencyExists());
		mainForm.getBtnGenerateInconsistency().addActionListener(e -> onClickBtnGenerateInconsistency());
		mainForm.getBtnRetrieveConsistentData().addActionListener(e -> onClickBtnRetrieveConsistentData());
		mainForm.getBtnGenerateInconsistencyReport().addActionListener(e -> onClickBtnGenerateInconsistencyReport());
		mainForm.getBtnGenerateTest().addActionListener(e -> onClickBtnGenerateTest());
		mainForm.getBtnGenerateDecisionRules().addActionListener(e -> onClickBtnGenerateDecisionRules());
		mainForm.getBtnCheckIfDecisionRulesSetConsistent().addActionListener(e -> onClickBtnCheckIfDecisionRulesSetConsistent());
		mainForm.getBtnCalculateDecisionRulesMeasures().addActionListener(e -> onClickBtnCalculateDecisionRulesMeasures());
		mainForm.getBtnSaveResearch().addActionListener(e -> onClickBtnSaveResearch());
	}

	private void onClickMenuItemLoadData(File file) {
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
			refreshLblStatus(Utils.buildStatus(Constants.GeneralStatusPart.READY, file.getAbsolutePath()));

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
			Params.getInstance().add("LOADED_FILE", file);
		} catch (IOException | InvalidFormatException e) {
			Utils.displayErrorOptionPane(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"), e.getMessage(), mainForm);
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
				Utils.displayErrorOptionPane(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"), e.getMessage(), mainForm);
			}
		} else
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.validationOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("optionPane.mainFormActions.tableDataLoadedValidationMessage"),
					JOptionPane.ERROR_MESSAGE, mainForm);
	}

	private void onClickMenuParametrisation() {
		if (parametrisationFormActions == null)
			parametrisationFormActions = new ParametrisationFormActions();
		parametrisationFormActions.control();
	}

	private void onChangeComboBoxAlgorithm() {
		switch (Constants.Algorithm.getByTypeName(mainForm.getComboBoxAlgorithm().getSelectedItem().toString())) {
			case GREEDY_FOR_CONSISTENT_DATA:
				this.decisionRulesGenerator = SingletonFactory.getInstance(StandardDecisionRulesGenerator.class);
				break;
			case GREEDY_FOR_INCONSISTENT_DATA:
				this.decisionRulesGenerator = SingletonFactory.getInstance(InconsistentDataDecisionRulesGenerator.class);
				break;
		}
	}

	private void onClickCheckBoxDataParameters() {
		mainForm.getDataParametersPanel().setVisible(mainForm.getCheckBoxShowDataParameters().isSelected());
	}

	private void onClickCheckBoxShowCache() {
		setAndGetSplitPaneCacheVisibility(mainForm.getCheckBoxShowCache().isSelected());
		mainForm.pack();
	}

	private void onClickBtnCheckIfInconsistencyExists() {
		refreshLblInconsistencyStatus(InconsistencyProcess.checkIfInconsistencyExists(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector()));
	}

	private void onClickBtnGenerateInconsistency() {
		inconsistencyGenerator = new StandardInconsistencyGenerator();
		Vector<Vector> dataVector;
		try {
			dataVector = inconsistencyGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
			reloadViewOfDecisionTableWithDataVectorAndColumnNames(dataVector);
			fillDataParametersPanel(fileToDefaultTableModelMapperService.getTableModels().get(SystemProperties.getSystemParameterSetType()));
		} catch (AlgorithmException e) {
			log.severe(e.getStackTrace()[0].getClassName() + " " + e.getStackTrace()[0].getMethodName());
		}
	}

	private void onClickBtnRetrieveConsistentData() {
		onClickMenuItemLoadData((File) Params.getInstance().get("LOADED_FILE"));
	}

	private void onClickBtnGenerateInconsistencyReport() {
		File loadedFile = (File) Params.getInstance().get("LOADED_FILE");
		Vector<Vector> data = ((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector();
		List<Row> inconsistentRows = new ArrayList<>(VectorProcess.findRowsWithSameAttributesAndVariousDecisionValue(data));
		try {
			FotDBDao fotDBDao = new FotDBDao("inconsistency-report-"
					+ new SimpleDateFormat("ddMMYYYYHHmmss").format(new Date())
					+ "-" + loadedFile.getName() + "-" + Objects.hash(loadedFile));
			fotDBDao.save(loadedFile.getName(), true)
					.save("Inconsistent rows: ", true)
					.saveAll(inconsistentRows, true);
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.successTitle"),
					SystemProperties.getResourceBundle().getString("optionPane.mainFormActions.foDBSavingSuccessMessage"),
					JOptionPane.INFORMATION_MESSAGE, null);
		} catch (IOException e) {
			Utils.displayErrorOptionPane(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneFoDBSavingError"), e, null);
		}
	}

	private void onClickBtnGenerateTest() {
		List<RowPairsSet> rowPairsSetList = testGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		Cache.getInstance().cache("TEST", rowPairsSetList);
		refreshTextAreaTest(rowPairsSetList, true);
	}

	private void onClickBtnGenerateDecisionRules() {
		decisionRulesGeneratorResult = decisionRulesGenerator.generate(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector());
		Cache.getInstance().cache("DECISION_RULES", decisionRulesGeneratorResult);
		refreshTextAreaDecisionRulesBaseOnSysParameters(decisionRulesGeneratorResult, true);
		setAndGetBtnCalculateDecisionRulesMeasures(true);
	}

	private void onClickBtnCheckIfDecisionRulesSetConsistent() {
		mainForm.getLblStatus().setText(Utils.buildStatus(InconsistencyProcess
						.checkIfInconsistencyExists(extractDecisionRulesFromGenerationResultListBasedOnAlgorithm()),
				mainForm.getLblStatus().getText()));
	}

	private void onClickBtnCalculateDecisionRulesMeasures() {
		List<LogicalExpression> decisionRules = extractDecisionRulesFromGenerationResultListBasedOnAlgorithm();

		StringBuilder result = new StringBuilder();
		standardQualityMeasure = new StandardSupportMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), decisionRules, null, true, StandardSupportMeasure.TYPE.ABSOLUTE);
		result.append("Support [MAX] [AVG] [MIN] : " + ((StandardSupportMeasure) standardQualityMeasure).calculateMax().toString() + ", "
				+ ((StandardSupportMeasure) standardQualityMeasure).calculateAverage().toString() + ", "
				+ ((StandardSupportMeasure) standardQualityMeasure).calculateMin().toString() + "<br>");
		standardQualityMeasure = new StandardErrorRateMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), decisionRules, null, true, StandardErrorRateMeasure.TYPE.ABSOLUTE);
		result.append("Error rate [MAX] [AVG] [MIN] : " + ((StandardErrorRateMeasure) standardQualityMeasure).calculateMax().toString() + ", "
				+ ((StandardErrorRateMeasure) standardQualityMeasure).calculateAverage().toString() + ", "
				+ ((StandardErrorRateMeasure) standardQualityMeasure).calculateMin().toString() + "<br>");
		standardQualityMeasure = new StandardDecisionRulesLengthMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), decisionRules, null, true);
		result.append("Length [MAX] [AVG] [MIN] : " + ((StandardDecisionRulesLengthMeasure) standardQualityMeasure).calculateMax().toString() + ", "
				+ ((StandardDecisionRulesLengthMeasure) standardQualityMeasure).calculateAverage().toString() + ", "
				+ ((StandardDecisionRulesLengthMeasure) standardQualityMeasure).calculateMin().toString() + "<br>");
		standardQualityMeasure = new StandardNumberOfVariousDecisionRulesMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), decisionRules, null);
		result.append("No of various decision rules : " + ((StandardNumberOfVariousDecisionRulesMeasure) standardQualityMeasure).calculate().toString() + "<br>");
		mainForm.getEditorPaneDecisionRulesMeasures().setText(result.toString());
		Params.getInstance().add("DECISION_RULES_MEASURES", StringUtils.replace(result.toString(), "<br>", "\n"));

		setAndGetBtnCalculateDecisionRulesMeasures(true);
		setAndGetScrollPaneEditorPaneDecisionRulesMeasuresVisibility(true);
		mainForm.pack();
	}

	private List<LogicalExpression> extractDecisionRulesFromGenerationResultListBasedOnAlgorithm() {
		boolean isGreedyForConsistentDataAlgorithmSelected = Constants.Algorithm.getByTypeName(mainForm.getComboBoxAlgorithm().getSelectedItem().toString())
				== Constants.Algorithm.GREEDY_FOR_CONSISTENT_DATA;

		List<LogicalExpression> decisionRules = new ArrayList<>();
		for (List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes : decisionRulesGeneratorResult)
			decisionRules.add((LogicalExpression) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(isGreedyForConsistentDataAlgorithmSelected ?
					Constants.StandardDecisionRulesGenerator.DECISION_RULES.getValue() :
					Constants.InconsistentDataDecisionRulesGenerator.DECISION_RULES.getValue()));

		return decisionRules;
	}

	private void onClickBtnSaveResearch() {
		List<LogicalExpression> decisionRules = extractDecisionRulesFromGenerationResultListBasedOnAlgorithm();
		try {
			FotDBDao fotDBDao = new FotDBDao("decision-rules-"
					+ new SimpleDateFormat("ddMMYYYYHHmmss").format(new Date())
					+ "-" + Objects.hash(decisionRules));
			fotDBDao
					.save(mainForm.getLblStatus().getText().split(Constants.APPLICATION_STATUS_SEPARATOR)[1].split(" ")[1], true)
					.save(decisionRulesGenerator.getTimeMeasure() / 1000.0 + " s.", true)
					.save(Params.getInstance().get("DECISION_RULES_MEASURES").toString(), true)
					.saveAll(decisionRules, true);
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.successTitle"),
					SystemProperties.getResourceBundle().getString("optionPane.mainFormActions.foDBSavingSuccessMessage"),
					JOptionPane.INFORMATION_MESSAGE, null);
		} catch (IOException e) {
			Utils.displayErrorOptionPane(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneFoDBSavingError"), e, null);
		}
	}

	private void fillDataParametersPanel(DefaultTableModel defaultTableModel) {
		Vector<Vector> dataVector = defaultTableModel.getDataVector();

		StringBuilder attributes = new StringBuilder();
		int columnIndex = 0;
		for (; columnIndex < defaultTableModel.getColumnCount() - 1; columnIndex++)
			attributes.append(defaultTableModel.getColumnName(columnIndex) + " ");

		reloadTrainingAndTestAttributes();
		reloadSetTypeConsistencyAttribute();
		mainForm.getTextFieldDecisionAttributeIndex().setText(Integer.toString(dataVector.get(0).size() - 1));
		mainForm.getTextFieldConditionalAttributes().setText(attributes.toString());
		mainForm.getTextFieldNumberOfDecisionAttributes().setText(
				String.valueOf(Common.getNumberOfUniqueValues(dataVector, dataVector.get(0).size() - 1)));
		mainForm.getTextFieldDecisionAttributesProportion().setText(
				Common.prepareFrequencyForEachValuesInformation(dataVector, dataVector.get(0).size() - 1));
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
		boolean isSysParameterShowDecRulesGenerationTime = SystemProperties.isSystemParameterShowDecRulesGenerationTime();
		StringBuilder result = new StringBuilder("<b>Decision rules:</b><br>");
		for (List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes : resultObjectListOfObject) {
			buildResultForTextAreaDecisionRules(result, resultConsistedOfRowsSetAndRowsSetForEachAttributes);
		}
		if (isSysParameterShowDecRulesGenerationTime) {
			result.append("<br>");
			result.append("Decision rules generation time: ").append(decisionRulesGenerator.getTimeMeasure() / 1000.0).append("s.");
			result.append("<br>");
		}
		mainForm.getEditorPaneDecisionRules().setText(result.toString());


		if (withCache && Cache.getInstance().get("DECISION_RULES") != null) {
			result = new StringBuilder("<b>Decision rules:</b><br>");
			for (List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes : (List<List<Object>>) Cache.getInstance().get("DECISION_RULES")) {
				buildResultForTextAreaDecisionRules(result, resultConsistedOfRowsSetAndRowsSetForEachAttributes);
			}
			if (isSysParameterShowDecRulesGenerationTime) {
				result.append("<br>");
				//TODO decisionRulesGenerator.getTimeMeasure() should be cached value
				result.append("Decision rules generation time: ").append(decisionRulesGenerator.getTimeMeasure() / 1000.0).append("s.");
				result.append("<br>");
			}
			mainForm.getEditorPaneCachedDecisionRules().setText(result.toString());
		}
	}

	private void refreshLblStatus(String builtStatus) {
		mainForm.getLblStatus().setText(builtStatus);
	}

	private void refreshLblInconsistencyStatus(boolean isDataSetInconsistent) {
		if (isDataSetInconsistent) {
			mainForm.getLblStatus().setText(Utils.buildStatus(Constants.GeneralStatusPart.READY, false, mainForm.getLblStatus().getText()));
			mainForm.getLblInconsistencyStatus().setIcon(Utils.getResourceIcon("lblInconsistencyStatus-ico.png"));
		} else {
			mainForm.getLblStatus().setText(Utils.buildStatus(Constants.GeneralStatusPart.READY, true, mainForm.getLblStatus().getText()));
			mainForm.getLblInconsistencyStatus().setIcon(null);
		}
	}

	private void reloadTrainingAndTestAttributes() {
		mainForm.getComboBoxSetType().setSelectedItem(SystemProperties.getSystemParameterSetType().getName());
		mainForm.getTextFieldTrainingPercentage().setText(Double.toString(SystemProperties.getSystemParameterTrainingPercentage()));
		mainForm.getTextFieldTestPercentage().setText(Double.toString(SystemProperties.getSystemParameterTestPercentage()));
		mainForm.getCheckBoxShowCache().setSelected(SystemProperties.isSystemParameterShuffle());
	}

	private void reloadSetTypeConsistencyAttribute() {
		mainForm.getComboBoxSetTypeConsistency().setSelectedItem(SystemProperties.getSystemParameterSetTypeConsistency().getName());
	}

	private void reloadViewOfDecisionTableWithDataVectorAndColumnNames(Vector<Vector> dataVector) {
		Vector columnNames = new Vector();
		List<Long> attributesToRemoveIndexes = Arrays.stream((long[]) Params.getInstance().get("REMOVED_ATTRIBUTE_INDEXES")).
				boxed().collect(Collectors.toList());
		for (int columnNameIndex = 0; columnNameIndex < mainForm.getDecisionTable().getModel().getColumnCount(); columnNameIndex++) {
			if (!attributesToRemoveIndexes.contains(Long.valueOf(columnNameIndex).longValue()))
				columnNames.add(mainForm.getDecisionTable().getModel().getColumnName(columnNameIndex));
		}

		((DefaultTableModel) mainForm.getDecisionTable().getModel()).setDataVector(dataVector, columnNames);
	}

	private void buildResultForTextAreaDecisionRules(StringBuilder result, List<Object> resultConsistedOfRowsSetAndRowsSetForEachAttributes) {
		boolean isSysParametersCalculateQualityMeasureForEachDecisionRules = SystemProperties.isSystemParameterCalculateQualityMeasureForEachDecisionRules();
		boolean isGreedyForConsistentDataAlgorithmSelected = Constants.Algorithm.getByTypeName(mainForm.getComboBoxAlgorithm().getSelectedItem().toString())
				== Constants.Algorithm.GREEDY_FOR_CONSISTENT_DATA;

		if (SystemProperties.getSystemParameterDecisionRulesDataRangeList().contains(Constants.DecisionRulesDataRange.ROWS_SETS)
				&& isGreedyForConsistentDataAlgorithmSelected) {
			result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.ROWS_SET.getValue()).toString() + "<br>");
			for (RowsSet rowsSet : (List<RowsSet>) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(
					Constants.StandardDecisionRulesGenerator.ROWS_SET_FOR_EACH_ATTRIBUTE.getValue()))
				result.append((rowsSet).toString() + "<br>");
			result.append("<br>");
		}

		if (SystemProperties.getSystemParameterDecisionRulesDataRangeList().contains(Constants.DecisionRulesDataRange.COVERAGES)
				&& isGreedyForConsistentDataAlgorithmSelected) {
			result.append("Coverage: <br>");
			for (RowsSet rowsSet : (List<RowsSet>) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(
					Constants.StandardDecisionRulesGenerator.COVERAGE.getValue()))
				result.append((rowsSet).toString() + "<br>");
			result.append("<br>");
		}

		if (SystemProperties.getSystemParameterDecisionRulesDataRangeList().contains(Constants.DecisionRulesDataRange.DECISION_RULES)
				|| !isGreedyForConsistentDataAlgorithmSelected) {
			if (isGreedyForConsistentDataAlgorithmSelected)
				result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.StandardDecisionRulesGenerator.DECISION_RULES.getValue()).toString());
			else
				result.append(resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(Constants.InconsistentDataDecisionRulesGenerator.DECISION_RULES.getValue()).toString());
			result.append("<br>");
			if (isSysParametersCalculateQualityMeasureForEachDecisionRules)
				buildResultConsistedOfCalculatedQualityMeasure(
						(LogicalExpression) resultConsistedOfRowsSetAndRowsSetForEachAttributes.get(isGreedyForConsistentDataAlgorithmSelected ?
								Constants.StandardDecisionRulesGenerator.DECISION_RULES.getValue() :
								Constants.InconsistentDataDecisionRulesGenerator.DECISION_RULES.getValue()),
						result);
		}
	}

	private void buildResultConsistedOfCalculatedQualityMeasure(LogicalExpression decisionRule, StringBuilder result) {
		standardQualityMeasure = new StandardSupportMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), null, decisionRule, false, StandardSupportMeasure.TYPE.ABSOLUTE);
		result.append("Support: " + ((StandardSupportMeasure) standardQualityMeasure).calculate().toString() + "<br>");
		standardQualityMeasure = new StandardErrorRateMeasure(((DefaultTableModel) mainForm.getDecisionTable().getModel()).getDataVector(), null, decisionRule, false, StandardErrorRateMeasure.TYPE.ABSOLUTE);
		result.append("Error rate: " + ((StandardErrorRateMeasure) standardQualityMeasure).calculate().toString() + "<br>");
		result.append("<br>");
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
