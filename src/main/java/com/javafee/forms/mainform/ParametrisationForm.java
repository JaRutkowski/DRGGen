package com.javafee.forms.mainform;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.javafee.controller.utils.SystemProperties;
import com.javafee.forms.mainform.utils.Utils;

import lombok.Getter;

public class ParametrisationForm {
	@Getter
	private JFrame parametrisationFrame;
	private JPanel parametrisationPanel;

	private JPanel databaseSettingsPanel;
	@Getter
	private JPanel decisionTableSettingsPanel;

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

	private JLabel lblDecisionAttributeIndex;
	@Getter
	private JButton btnAccept;
	@Getter
	private JSpinner spinnerDecisionAttributeIndex;

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

		lblDataBaseUrl.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblUrl"));
		lblUser.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblUser"));
		lblPassword.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblPassword"));
		btnConfigureConnection.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.btnConfigureConnection"));

		lblDecisionAttributeIndex.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.lblDecisionAttributeIndex"));
		btnAccept.setText(SystemProperties.getResourceBundle().getString("parametrisationForm.btnAccept"));
	}

	private void setupGraphics() {
		btnAccept.setIcon(Utils.getResourceIcon("btnAccept-ico.png"));
		btnConfigureConnection.setIcon(Utils.getResourceIcon("btnConfigureConnection-ico.png"));
	}
}
