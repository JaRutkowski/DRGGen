package com.javafee.forms.utils;

import java.awt.*;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import com.javafee.controller.utils.Constants;
import com.javafee.controller.utils.SystemProperties;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

@Log
@UtilityClass
public class Utils {
	public int displayConfirmDialog(String message, String title) {
		Object[] options = {SystemProperties.getResourceBundle().getString("confirmDialog.yes"),
				SystemProperties.getResourceBundle().getString("confirmDialog.no")};
		return JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}

	public static void displayOptionPane(String title, String message, int messageType, Component component) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage("<html>" + message + "</html>");
		optionPane.setMessageType(messageType);
		JDialog dialog = optionPane.createDialog(component, title);
		dialog.setVisible(true);
	}

	public static void displayErrorOptionPane(String title, String message, String tMessage, Component component) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage("<html>" + message + "<hr>" + tMessage + "</html>");
		optionPane.setMessageType(JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog(component, title);
		dialog.setVisible(true);
	}

	public static void displayErrorOptionPane(String title, String message, Component component) {
		displayOptionPane(title, message, JOptionPane.ERROR_MESSAGE, component);
		log.severe(message);
	}

	public static void displayErrorOptionPane(String title, String message, Throwable t, Component component) {
		displayErrorOptionPane(title, message, t.getMessage(), component);
	}

	public File displayFileChooserAndGetFile(String directory) {
		String dir = directory != null ? directory : FileSystemView.getFileSystemView().getHomeDirectory().toString();

		JFileChooser jfc = new JFileChooser(dir);
		jfc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
			}

			public String getDescription() {
				return ".csv";
			}
		});

		File result = null;
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION)
			result = jfc.getSelectedFile();

		return result;
	}

	public String buildStatus(String generalStatusPart) {
		return String.format(SystemProperties.getResourceBundle().getString("mainForm.lblStatus"),
				generalStatusPart,
				"",
				"",
				"",
				"",
				"",
				"");
	}

	public String buildStatus(Constants.GeneralStatusPart generalStatusPart, String pathPart, boolean isConsistent) {
		return String.format(SystemProperties.getResourceBundle().getString("mainForm.lblStatus"),
				generalStatusPart.getValue(),
				Constants.APPLICATION_STATUS_SEPARATOR,
				pathPart != null && !"".equals(pathPart) ? pathPart : "",
				Constants.APPLICATION_STATUS_SEPARATOR,
				isConsistent ?
						SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.consistent") :
						SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.inconsistent"),
				"",
				"");
	}

	public String buildStatus(Constants.GeneralStatusPart generalStatusPart, boolean isConsistent, String currentStatus) {
		return String.format(SystemProperties.getResourceBundle().getString("mainForm.lblStatus"),
				generalStatusPart.getValue(),
				Constants.APPLICATION_STATUS_SEPARATOR,
				currentStatus.split(Constants.APPLICATION_STATUS_SEPARATOR)[Constants.APPLICATION_STATUS_PATH_PART_INDEX].split(" ")[1],
				Constants.APPLICATION_STATUS_SEPARATOR,
				isConsistent ?
						SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.consistent") :
						SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.inconsistent"),
				"",
				"");
	}

	public String buildStatus(Constants.GeneralStatusPart generalStatusPart, String pathPart) {
		return String.format(SystemProperties.getResourceBundle().getString("mainForm.lblStatus"),
				generalStatusPart.getValue(),
				Constants.APPLICATION_STATUS_SEPARATOR,
				pathPart != null && !"".equals(pathPart) ? pathPart : "",
				"",
				"",
				"",
				"");
	}

	public String buildStatus(boolean isInconsistencyInDecisionRulesExist, String currentStatus) {
		currentStatus = currentStatus.replace("<html><p style=\"font-style:italic;color:#328EED\">", "");
		currentStatus = currentStatus.replace("</p></html>", "");
		String[] currentStatusArray = currentStatus.split(Constants.APPLICATION_STATUS_SEPARATOR);
		return String.format(SystemProperties.getResourceBundle().getString("mainForm.lblStatus"),
				currentStatusArray.length > 0 ? currentStatusArray[0] : Constants.GeneralStatusPart.READY.getValue(),
				currentStatusArray.length > 0 && currentStatusArray[1].contains("\\") ? Constants.APPLICATION_STATUS_SEPARATOR : "",
				currentStatusArray.length > 0 && currentStatusArray[1].contains("\\") ?
						currentStatus.split(Constants.APPLICATION_STATUS_SEPARATOR)[Constants.APPLICATION_STATUS_PATH_PART_INDEX].split(" ")[1]
						: "",
				currentStatusArray.length > 1 && currentStatusArray[1].contains(SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.consistent")) ||
						currentStatusArray[1].contains(SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.inconsistent")) ?
						Constants.APPLICATION_STATUS_SEPARATOR : "",
				currentStatusArray.length > 1 && currentStatusArray[1].contains(SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.consistent")) ||
						currentStatusArray[1].contains(SystemProperties.getResourceBundle().getString("mainForm.lblStatus.dataInconsistency.inconsistent")) ?
						currentStatusArray[2] : "",
				Constants.APPLICATION_STATUS_SEPARATOR,
				isInconsistencyInDecisionRulesExist ?
						SystemProperties.getResourceBundle().getString("mainForm.lblStatus.decisionRulesSet.decisionRulesInconsistent") :
						SystemProperties.getResourceBundle().getString("mainForm.lblStatus.decisionRulesSet.decisionRulesConsistent"));
	}

	public Icon getResourceIcon(String resourceName) {
		return new ImageIcon(new ImageIcon(Utils.class.getResource("/images/" + resourceName))
				.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
	}
}
