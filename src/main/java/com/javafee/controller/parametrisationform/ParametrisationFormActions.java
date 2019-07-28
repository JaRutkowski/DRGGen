package com.javafee.controller.parametrisationform;

import java.sql.SQLException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.swing.JOptionPane;

import com.javafee.controller.Actions;
import com.javafee.controller.utils.Session;
import com.javafee.controller.utils.SystemProperties;
import com.javafee.forms.mainform.ParametrisationForm;
import com.javafee.forms.mainform.utils.Utils;

@Stateless
public class ParametrisationFormActions implements Actions {
	private ParametrisationForm parametrisationForm;

	@Inject
	private Session session;

	public void control() {
		openParametrisationForm();
		initializeListeners();
	}

	public void openParametrisationForm() {
		if (parametrisationForm == null || (parametrisationForm != null && !parametrisationForm.getParametrisationFrame().isDisplayable())) {
			parametrisationForm = new ParametrisationForm();
		} else {
			parametrisationForm.getParametrisationFrame().toFront();
		}
	}

	private void initializeListeners() {
		parametrisationForm.getBtnConfigureConnection().addActionListener(e -> onClickBtnConfigureConnection());
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
				Utils.displayErrorJOptionPaneAndLogError(SystemProperties.getResourceBundle().getString("errorOptionPaneTitle"),
						e.getMessage(), parametrisationForm.getParametrisationFrame());
				reloadLblTestConnection(true);
			}
		} else {
			Utils.displayOptionPane(SystemProperties.getResourceBundle().getString("validationOptionPaneTitle"),
					SystemProperties.getResourceBundle().getString("parametrisationFormActions.dataBasePropertiesValidationMessage"),
					JOptionPane.ERROR_MESSAGE, parametrisationForm.getParametrisationFrame());
		}
	}

	private void reloadLblTestConnection(boolean isError) {
		//TODO ImageIcon
		String information = isError ? SystemProperties.getResourceBundle().getString("parametrisationFormActions.noDatabaseConnectionInformation") :
				SystemProperties.getResourceBundle().getString("parametrisationFormActions.databaseConnectionInformation");
		parametrisationForm.getLblTestConnection().setText(information);
	}

	private boolean validateDataBaseProperties(String url, String user, String password) {
		return !"".equals(url) && !"".equals(user) && !"".equals(password);
	}
}
