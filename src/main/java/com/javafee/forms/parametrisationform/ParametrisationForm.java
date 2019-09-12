package com.javafee.forms.parametrisationform;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.javafee.controller.utils.SystemProperties;
import com.javafee.forms.utils.DoubleJSpinner;
import com.javafee.forms.utils.Utils;

import lombok.Getter;

public class ParametrisationForm {
	@Getter
	private JFrame parametrisationFrame;
	private JPanel parametrisationPanel;

	private JPanel databaseSettingsPanel;
	@Getter
	private JPanel decisionTableSettingsPanel;
	@Getter
	private JPanel decisionRulesSettingsPanel;
	@Getter
	private JPanel decisionRulesDataRangePanel;

	private JLabel lblDataBaseUrl;
	@Getter
	private JTextField textFieldUrl;
	private JLabel lblUser;
	@Getter
	private JTextField textFieldUser;
	private JLabel lblPassword;
	@Getter
	private JPasswordField passwordField;
	@Getter
	private JLabel lblTestConnection;
	@Getter
	private JButton btnConfigureConnection;

	@Getter
	private JLabel lblDecisionAttributeIndex;
	@Getter
	private JSpinner spinnerDecisionAttributeIndex;
	@Getter
	private JLabel lblSetType;
	@Getter
	private JComboBox comboBoxSetType;
	@Getter
	private JLabel lblTrainingPercentage;
	@Getter
	private DoubleJSpinner doubleSpinnerTrainingPercentage;
	@Getter
	private JLabel lblTestPercentage;
	@Getter
	private DoubleJSpinner doubleSpinnerTestPercentage;
	@Getter
	private JCheckBox checkBoxShuffle;
	@Getter
	private JButton btnAcceptDecisionTableSettingsPanel;

	@Getter
	private JButton btnAcceptDecisionRulesSettingsPanel;
	@Getter
	private JCheckBox checkBoxShowRowsSets;
	@Getter
	private JCheckBox checkBoxShowCoverages;
	@Getter
	private JCheckBox checkBoxShowDecisionRulesSet;
	@Getter
	private JCheckBox checkBoxCalculateDecRulesMeasuresForEachDecRules;
	@Getter
	private JCheckBox checkBoxShowDecRulesGenerationTime;

	public ParametrisationForm() {
		parametrisationFrame = new JFrame(SystemProperties.getResourceBundle().getString("parametrisationForm.title"));
		parametrisationFrame.setContentPane(parametrisationPanel);
		parametrisationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		parametrisationFrame.pack();

		internationalizing();
		setupGraphics();

		parametrisationFrame.setVisible(true);
	}

	private void internationalizing() {
		((TitledBorder) databaseSettingsPanel.getBorder()).setTitle(SystemProperties.getResourceBundle().getString("parametrisationForm.dataBaseSettingsPanelTitle"));
		((TitledBorder) decisionTableSettingsPanel.getBorder()).setTitle(SystemProperties.getResourceBundle().getString("parametrisationForm.decisionTableSettingsPanelTitle"));
		((TitledBorder) decisionRulesSettingsPanel.getBorder()).setTitle(SystemProperties.getResourceBundle().getString("parametrisationForm.decisionRulesPanel"));
		((TitledBorder) decisionRulesDataRangePanel.getBorder()).setTitle(SystemProperties.getResourceBundle().getString("parametrisationForm.decisionRulesDataRangePanel"));

		lblDataBaseUrl.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblUrl"));
		lblUser.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblUser"));
		lblPassword.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblPassword"));
		btnConfigureConnection.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.btnConfigureConnection"));

		lblDecisionAttributeIndex.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblDecisionAttributeIndex"));
		btnAcceptDecisionTableSettingsPanel.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.btnAccept"));
		lblSetType.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblSetType"));
		lblTrainingPercentage.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblTrainingPercentage"));
		lblTestPercentage.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblTestPercentage"));
		checkBoxShuffle.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.checkBoxShuffle"));

		checkBoxShowRowsSets.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.checkBoxShowRowsSets"));
		checkBoxShowCoverages.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.checkBoxShowCoverages"));
		checkBoxShowDecisionRulesSet.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.checkBoxShowDecisionRules"));
		checkBoxCalculateDecRulesMeasuresForEachDecRules.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.checkBoxCalculateDecRulesMeasuresForEachDecRules"));
		checkBoxShowDecRulesGenerationTime.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.checkBoxShowDecRulesGenerationTime"));
		btnAcceptDecisionRulesSettingsPanel.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.btnAccept"));
	}

	private void setupGraphics() {
		btnAcceptDecisionTableSettingsPanel.setIcon(Utils.getResourceIcon("btnAccept-ico.png"));
		btnAcceptDecisionRulesSettingsPanel.setIcon(Utils.getResourceIcon("btnAccept-ico.png"));
		btnConfigureConnection.setIcon(Utils.getResourceIcon("btnConfigureConnection-ico.png"));
	}
}
