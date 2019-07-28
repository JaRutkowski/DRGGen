package com.javafee.forms.mainform.utils;

import java.awt.*;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

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

	public static void displayErrorJOptionPaneAndLogError(String title, String message, Component component) {
		displayOptionPane(title, message, JOptionPane.ERROR_MESSAGE, component);
		log.severe(message);
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
}
