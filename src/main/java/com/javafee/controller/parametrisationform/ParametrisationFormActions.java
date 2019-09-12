package com.javafee.controller.parametrisationform;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.function.Consumer;

import javax.ejb.Stateless;
import javax.swing.JOptionPane;

import com.javafee.controller.Actions;
import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.Session;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.params.Params;
import com.javafee.forms.parametrisationform.ParametrisationForm;
import com.javafee.forms.utils.Utils;

@Stateless
public class ParametrisationFormActions implements Actions {
	private ParametrisationForm parametrisationForm;

	public void control() {
		if (parametrisationForm == null || (parametrisationForm != null && !parametrisationForm.getParametrisationFrame().isDisplayable())) {
			parametrisationForm = new ParametrisationForm();
			initializeComboBoxSetType();
			setComponentsVisibility();
			initializeListeners();
			initializeParameters();

			Consumer setComponentsVisibility = (e) -> setComponentsVisibility();
			Consumer initializeParameters = (e) -> initializeParameters();
			Params.getInstance().add("PARAM_FORM_ACTIONS_COMPONENTS_REFRESH", setComponentsVisibility);
			Params.getInstance().add("PARAM_FORM_ACTIONS_INITIALIZE_PARAMETERS", initializeParameters);
		} else {
			parametrisationForm.getParametrisationFrame().toFront();
		}
	}

	private void initializeComboBoxSetType() {
		Common.initializeComboBoxSetType(parametrisationForm.getComboBoxSetType());
	}

	private void setComponentsVisibility() {
		parametrisationForm.getDecisionTableSettingsPanel().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getLblDecisionAttributeIndex().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getSpinnerDecisionAttributeIndex().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getLblSetType().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getComboBoxSetType().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getLblTrainingPercentage().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getDoubleSpinnerTrainingPercentage().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getLblTestPercentage().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getDoubleSpinnerTestPercentage().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getCheckBoxShuffle().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getBtnAcceptDecisionTableSettingsPanel().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getDecisionRulesSettingsPanel().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getCheckBoxShowRowsSets().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getCheckBoxShowCoverages().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getCheckBoxShowDecisionRulesSet().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getBtnAcceptDecisionRulesSettingsPanel().setEnabled(Params.getInstance().contains("TABLE_NAME"));
	}

	private void initializeListeners() {
		parametrisationForm.getBtnConfigureConnection().addActionListener(e -> onClickBtnConfigureConnection());
		parametrisationForm.getBtnAcceptDecisionTableSettingsPanel().addActionListener(e -> onClickBtnAcceptDecisionTableSettingsPanel());
		parametrisationForm.getBtnAcceptDecisionRulesSettingsPanel().addActionListener(e -> onClickBtnAcceptDecisionRulesSettingsPanel());
	}

	private void initializeParameters() {
		initializeSystemParameterDecisionTable();
		initializeSystemParameterDecisionRulesDataRange();
	}

	private void initializeSystemParameterDecisionTable() {
		parametrisationForm.getSpinnerDecisionAttributeIndex().setValue(SystemProperties.getSystemParameterDecisionAttributeIndex() != null ?
				SystemProperties.getSystemParameterDecisionAttributeIndex() : 0);
		parametrisationForm.getComboBoxSetType().setSelectedItem(SystemProperties.getSystemParameterSetType().getName());
		parametrisationForm.getDoubleSpinnerTrainingPercentage().setValue(SystemProperties.getSystemParameterTrainingPercentage());
		parametrisationForm.getDoubleSpinnerTestPercentage().setValue(SystemProperties.getSystemParameterTestPercentage());
		parametrisationForm.getCheckBoxShuffle().setSelected(SystemProperties.isSystemParameterShuffle());
	}

	private void initializeSystemParameterDecisionRulesDataRange() {
		if (SystemProperties.getSystemParameterDecisionRulesDataRangeList() != null) {
			SystemProperties.getSystemParameterDecisionRulesDataRangeList().forEach(dataRange -> {
				switch (dataRange) {
					case ROWS_SETS:
						parametrisationForm.getCheckBoxShowRowsSets().setSelected(true);
						break;
					case COVERAGES:
						parametrisationForm.getCheckBoxShowCoverages().setSelected(true);
						break;
					case DECISION_RULES:
						parametrisationForm.getCheckBoxShowDecisionRulesSet().setSelected(true);
						break;
				}
			});
		} else
			parametrisationForm.getCheckBoxShowRowsSets().setSelected(true);
	}

	private void onClickBtnConfigureConnection() {
		String url = parametrisationForm.getTextFieldUrl().getText();
		String user = parametrisationForm.getTextFieldUser().getText();
		String password = new String(parametrisationForm.getPasswordField().getPassword());

		if (validateDataBaseProperties(url, user, password)) {
			try {
				SystemProperties.initializeDataBase(url, user, password);
				reloadLblTestConnection(false);
				Session.isDataBaseConnected = true;
			} catch (SQLException | ClassNotFoundException e) {
				Utils.displayErrorJOptionPaneAndLogError(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"),
						e.getMessage(), parametrisationForm.getParametrisationFrame());
				reloadLblTestConnection(true);
			}
		} else
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.validationOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("parametrisationFormActions.dataBasePropertiesValidationMessage"),
					JOptionPane.ERROR_MESSAGE, parametrisationForm.getParametrisationFrame());
	}

	private void onClickBtnAcceptDecisionTableSettingsPanel() {
		if (validateTrainingAndTestPercentageValues(parametrisationForm.getDoubleSpinnerTrainingPercentage().getDouble(),
				parametrisationForm.getDoubleSpinnerTestPercentage().getDouble())) {
			if (Utils.displayConfirmDialog(SystemProperties.getResourceBundle().getString("confirmDialog.message"),
					SystemProperties.getResourceBundle().getString("confirmDialog.title")) == JOptionPane.YES_OPTION) {
				SystemProperties.setSystemParameterDecisionAttributeIndex((Integer) parametrisationForm.getSpinnerDecisionAttributeIndex().getValue());
				SystemProperties.setSystemParameterSetType(Constants.SetType.getByTypeName(parametrisationForm.getComboBoxSetType().getSelectedItem().toString()));
				SystemProperties.setSystemParameterTrainingPercentage(parametrisationForm.getDoubleSpinnerTrainingPercentage().getDouble());
				SystemProperties.setSystemParameterTestPercentage(parametrisationForm.getDoubleSpinnerTestPercentage().getDouble());
				SystemProperties.setSystemParameterShuffle(parametrisationForm.getCheckBoxShuffle().isSelected());
				Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.successTitle"),
						SystemProperties.getResourceBundle().getString("optionPane.sysParamDecisionTableParametersSuccessMessage"),
						JOptionPane.INFORMATION_MESSAGE, null);
				invokeFillDataParametersPanel();
				invokeBuildAndRefreshViewOfDecisionTable();
			}
		} else
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.validationOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("parametrisationFormActions.trainingAndTestPercentageValuesValidationMessage"),
					JOptionPane.ERROR_MESSAGE, parametrisationForm.getParametrisationFrame());
	}

	private void onClickBtnAcceptDecisionRulesSettingsPanel() {
		if (Utils.displayConfirmDialog(SystemProperties.getResourceBundle().getString("confirmDialog.message"),
				SystemProperties.getResourceBundle().getString("confirmDialog.title")) == JOptionPane.YES_OPTION) {
			SystemProperties.setSystemParameterDecisionRulesDataRangeList(new HashSet<>());
			if (parametrisationForm.getCheckBoxShowRowsSets().isSelected())
				SystemProperties.getSystemParameterDecisionRulesDataRangeList().add(Constants.DecisionRulesDataRange.ROWS_SETS);
			if (parametrisationForm.getCheckBoxShowCoverages().isSelected())
				SystemProperties.getSystemParameterDecisionRulesDataRangeList().add(Constants.DecisionRulesDataRange.COVERAGES);
			if (parametrisationForm.getCheckBoxShowDecisionRulesSet().isSelected())
				SystemProperties.getSystemParameterDecisionRulesDataRangeList().add(Constants.DecisionRulesDataRange.DECISION_RULES);
			SystemProperties.setSystemParameterCalculateQualityMeasureForEachDecisionRules(
					parametrisationForm.getCheckBoxCalculateDecRulesMeasuresForEachDecRules().isSelected());
			SystemProperties.setSystemParameterShowDecRulesGenerationTime(
					parametrisationForm.getCheckBoxShowDecRulesGenerationTime().isSelected());
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.successTitle"),
					SystemProperties.getResourceBundle().getString("optionPane.sysParamDecisionRulesDataRangeSuccessMessage"),
					JOptionPane.INFORMATION_MESSAGE, null);
		}
	}

	private void reloadLblTestConnection(boolean isError) {
		//TODO ImageIcon
		String information = isError ? SystemProperties.getResourceBundle().getString("parametrisationFormActions.noDatabaseConnectionInformation") :
				SystemProperties.getResourceBundle().getString("parametrisationFormActions.databaseConnectionInformation");
		parametrisationForm.getLblTestConnection().setText(information);
	}

	private void invokeFillDataParametersPanel() {
		if (Params.getInstance().contains("MAIN_FORM_ACTIONS_FILL_DATA_PARAMS_PANEL"))
			((Consumer) Params.getInstance().get("MAIN_FORM_ACTIONS_FILL_DATA_PARAMS_PANEL")).accept(null);
	}

	private void invokeBuildAndRefreshViewOfDecisionTable() {
		if (Params.getInstance().contains("MAIN_FORM_ACTIONS_BUILD_AND_REFRESH_VIEW_OF_DECISION_TABLE"))
			((Consumer) Params.getInstance().get("MAIN_FORM_ACTIONS_BUILD_AND_REFRESH_VIEW_OF_DECISION_TABLE")).accept(null);
	}

	private boolean validateDataBaseProperties(String url, String user, String password) {
		return !"".equals(url) && !"".equals(user) && !"".equals(password);
	}

	private boolean validateTrainingAndTestPercentageValues(double trainingPercentage, double testPercentage) {
		return trainingPercentage + testPercentage == 100;
	}
}
