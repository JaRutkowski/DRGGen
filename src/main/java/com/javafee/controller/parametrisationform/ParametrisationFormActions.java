package com.javafee.controller.parametrisationform;

import java.sql.SQLException;
import java.util.function.Consumer;

import javax.ejb.Stateless;
import javax.swing.JOptionPane;

import com.javafee.controller.Actions;
import com.javafee.controller.utils.Common;
import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.Session;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.controller.utils.params.Params;
import com.javafee.forms.mainform.ParametrisationForm;
import com.javafee.forms.mainform.utils.Utils;

@Stateless
public class ParametrisationFormActions implements Actions {
	private ParametrisationForm parametrisationForm;

	public void control() {
		openParametrisationForm();
		initializeComboBoxSetType();
		setComponentsVisibility();
		initializeListeners();
		initializeParameters();

		Consumer setComponentsVisibility = (e) -> setComponentsVisibility();
		Consumer initializeParameters = (e) -> initializeParameters();
		Params.getInstance().add("PARAM_FORM_ACTIONS_COMPONENTS_REFRESH", setComponentsVisibility);
		Params.getInstance().add("PARAM_FORM_ACTIONS_INITIALIZE_PARAMETERS", initializeParameters);
	}

	public void openParametrisationForm() {
		if (parametrisationForm == null || (parametrisationForm != null && !parametrisationForm.getParametrisationFrame().isDisplayable())) {
			parametrisationForm = new ParametrisationForm();
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
		parametrisationForm.getDecisionRulesDataRangePanel().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getRadioButtonShowAllData().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getRadioButtonShowCoverageAndDecisionRulesSetOnly().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getRadioButtonShowDecisionRulesSetOnly().setEnabled(Params.getInstance().contains("TABLE_NAME"));
		parametrisationForm.getBtnAcceptDecisionRulesDataRangePanel().setEnabled(Params.getInstance().contains("TABLE_NAME"));
	}

	private void initializeListeners() {
		parametrisationForm.getBtnConfigureConnection().addActionListener(e -> onClickBtnConfigureConnection());
		parametrisationForm.getBtnAcceptDecisionTableSettingsPanel().addActionListener(e -> onClickBtnAcceptDecisionTableSettingsPanel());
		parametrisationForm.getBtnAcceptDecisionRulesDataRangePanel().addActionListener(e -> onClickBtnAcceptDecisionRulesDataRangePanel());
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
		if (SystemProperties.getSystemParameterDecisionRulesDataRange() != null) {
			switch (SystemProperties.getSystemParameterDecisionRulesDataRange()) {
				case ALL_DATA:
					parametrisationForm.getRadioButtonShowAllData().setSelected(true);
					break;
				case COVERAGE_AND_DECISION_RULES:
					parametrisationForm.getRadioButtonShowCoverageAndDecisionRulesSetOnly().setSelected(true);
					break;
				case DECISION_RULES:
					parametrisationForm.getRadioButtonShowDecisionRulesSetOnly().setSelected(true);
					break;
			}
		} else
			parametrisationForm.getRadioButtonShowAllData().setSelected(true);
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
						SystemProperties.getResourceBundle().getString("optionPane.sysParamDecisionAttrIdxSuccessMessage"),
						JOptionPane.INFORMATION_MESSAGE, null);
				invokeRefreshMainFormDecisionTableAttributes();
				invokeBuildAndRefreshViewOfDecisionTable();
			}
		} else
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("optionPane.validationOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("parametrisationFormActions.trainingAndTestPercentageValuesValidationMessage"),
					JOptionPane.ERROR_MESSAGE, parametrisationForm.getParametrisationFrame());
	}

	private void onClickBtnAcceptDecisionRulesDataRangePanel() {
		if (Utils.displayConfirmDialog(SystemProperties.getResourceBundle().getString("confirmDialog.message"),
				SystemProperties.getResourceBundle().getString("confirmDialog.title")) == JOptionPane.YES_OPTION) {
			Constants.DecisionRulesDataRange decisionRulesDataRange = null;
			if (parametrisationForm.getRadioButtonShowAllData().isSelected())
				decisionRulesDataRange = Constants.DecisionRulesDataRange.ALL_DATA;
			else if (parametrisationForm.getRadioButtonShowCoverageAndDecisionRulesSetOnly().isSelected())
				decisionRulesDataRange = Constants.DecisionRulesDataRange.COVERAGE_AND_DECISION_RULES;
			else if (parametrisationForm.getRadioButtonShowDecisionRulesSetOnly().isSelected())
				decisionRulesDataRange = Constants.DecisionRulesDataRange.DECISION_RULES;
			SystemProperties.setSystemParameterDecisionRulesDataRange(decisionRulesDataRange);
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

	private void invokeRefreshMainFormDecisionTableAttributes() {
		if (Params.getInstance().contains("MAIN_FORM_ACTIONS_REFRESH_DEC_TABLE_ATTR"))
			((Consumer) Params.getInstance().get("MAIN_FORM_ACTIONS_REFRESH_DEC_TABLE_ATTR")).accept(null);
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
