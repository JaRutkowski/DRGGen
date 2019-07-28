package com.javafee.forms.mainform;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.javafee.controller.utils.SystemProperties;

import lombok.Getter;

public class ParametrisationForm {
	@Getter
	private JFrame parametrisationFrame;
	private JPanel parametrisationPanel;

	@Getter
	private JTextField textFieldUrl;
	@Getter
	private JButton btnConfigureConnection;
	@Getter
	private JLabel lblTestConnection;
	@Getter
	private JPasswordField passwordField;
	@Getter
	private JTextField textFieldUser;

	public ParametrisationForm() {
		parametrisationFrame = new JFrame(SystemProperties.getResourceBundle().getString("parametrisationForm.title"));
		parametrisationFrame.setContentPane(parametrisationPanel);
		parametrisationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		parametrisationFrame.pack();
		parametrisationFrame.setVisible(true);
	}
}
