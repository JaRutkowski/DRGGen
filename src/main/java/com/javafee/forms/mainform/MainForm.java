package com.javafee.forms.mainform;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.javafee.controller.utils.SystemProperties;
import com.javafee.forms.mainform.utils.Utils;

import lombok.Getter;
import lombok.Setter;

public class MainForm extends JFrame {
	private JFrame mainFrame;
	private JPanel mainPanel;

	private JMenuBar menuBar;
	@Getter
	private JMenu menuData;
	@Getter
	private JMenu menuResearch;
	@Getter
	private JMenu menuParametrisation;
	@Getter
	private JMenuItem menuItemLoadData;
	@Getter
	private JMenuItem menuItemSaveToDatabase;
	@Getter
	private JMenuItem menuItemSettings;


	@Getter
	private JScrollPane decisionTableScrollPane;
	@Setter
	@Getter
	private JTable decisionTable;

	@Getter
	private JTextArea textAreaCachedTest;
	@Getter
	private JTextArea textAreaCachedDecisionRules;


	private JLabel lblDecisionAttributeIndex;
	@Getter
	private JTextField textFieldDecisionAttributeIndex;
	private JLabel lblConditionalAttributes;
	@Getter
	private JTextField textFieldConditionalAttributes;
	private JLabel lblNumberOfConditionalAttributes;
	@Getter
	private JTextField textFieldNumberOfConditionalAttributes;
	private JLabel lblNumberOfRows;
	@Getter
	private JTextField textFieldNumberOfRows;
	private JLabel lblNumberOfColumns;
	@Getter
	private JTextField textFieldNumberOfColumns;

	@Getter
	private JButton btnCheckData;
	@Getter
	private JButton btnGenerateTest;
	@Getter
	private JButton btnGenerateDecisionRules;
	private JLabel lblAlgorithm;
	@Getter
	private JCheckBox checkBoxShowDataParameters;
	@Getter
	private JCheckBox checkBoxShowCache;
	@Getter
	private JPanel dataParametersPanel;
	@Getter
	private JSplitPane splitPaneCache;

	@Getter
	private JButton btnSaveDecisionRules;
	@Getter
	private JButton btnSaveResearch;
	@Getter
	private JComboBox comboBoxAlgorithm;

	@Getter
	private JTextArea textAreaTest;
	@Getter
	private JTextArea textAreaDecisionRules;

	public MainForm() {
		mainFrame = new JFrame(SystemProperties.getResourceBundle().getString("applicationName"));
		mainFrame.setContentPane(mainPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();

		internationalizing();
		setupGraphics();

		mainFrame.setVisible(true);
	}

	private void internationalizing() {
		menuResearch.setText(SystemProperties.getResourceBundle().getString("mainForm.menuResearch"));
		menuParametrisation.setText(SystemProperties.getResourceBundle().getString("mainForm.menuParametrisation"));
		menuItemLoadData.setText(SystemProperties.getResourceBundle().getString("mainForm.menuData.menuItemLoadData"));
		menuItemSaveToDatabase.setText(SystemProperties.getResourceBundle().getString("mainForm.menuData.menuItemSaveToDatabase"));
		menuItemSettings.setText(SystemProperties.getResourceBundle().getString("mainForm.menuParametrisation.menuItemSettings"));

		lblAlgorithm.setText(SystemProperties.getResourceBundle().getString("mainForm.lblAlgorithm"));
		checkBoxShowDataParameters.setText(SystemProperties.getResourceBundle().getString("mainForm.checkBoxShowDataParameters"));
		checkBoxShowCache.setText(SystemProperties.getResourceBundle().getString("mainForm.checkBoxShowCache"));

		lblDecisionAttributeIndex.setText(SystemProperties.getResourceBundle().getString("dataParametersPanel.lblDecisionAttributeIndex"));
		lblConditionalAttributes.setText(SystemProperties.getResourceBundle().getString("dataParametersPanel.lblConditionalAttributes"));
		lblNumberOfConditionalAttributes.setText(SystemProperties.getResourceBundle().getString("dataParametersPanel.lblNumberOfConditionalAttributes"));
		lblNumberOfRows.setText(SystemProperties.getResourceBundle().getString("dataParametersPanel.lblNumberOfRows"));
		lblNumberOfColumns.setText(SystemProperties.getResourceBundle().getString("dataParametersPanel.lblNumberOfColumns"));

		btnCheckData.setText(SystemProperties.getResourceBundle().getString("mainForm.btnCheckData"));
		btnGenerateTest.setText(SystemProperties.getResourceBundle().getString("mainForm.btnGenerateTest"));
		btnGenerateDecisionRules.setText(SystemProperties.getResourceBundle().getString("mainForm.bntGenerateDecisionRules"));

		textAreaTest.setText(SystemProperties.getResourceBundle().getString("mainForm.textAreaTest"));
		textAreaDecisionRules.setText(SystemProperties.getResourceBundle().getString("mainForm.textAreaDecisionRules"));

		btnSaveDecisionRules.setText(SystemProperties.getResourceBundle().getString("mainForm.btnSaveDecisionRules"));
		btnSaveResearch.setText(SystemProperties.getResourceBundle().getString("mainForm.btnSaveResearch"));
		menuData.setText(SystemProperties.getResourceBundle().getString("mainForm.menuData"));
	}

	private void setupGraphics() {
		initializeDecisionTableScrollPaneWithImage();
		btnCheckData.setIcon(Utils.getResourceIcon("btnCheckData-ico.png"));
		btnGenerateDecisionRules.setIcon(Utils.getResourceIcon("btnGenerate-ico.png"));
		btnGenerateTest.setIcon(Utils.getResourceIcon("btnGenerate-ico.png"));
		btnSaveDecisionRules.setIcon(Utils.getResourceIcon("btnSave-ico.png"));
		btnSaveResearch.setIcon(Utils.getResourceIcon("btnSaveResearch-ico.png"));
	}

	private void initializeDecisionTableScrollPaneWithImage() {
		ImageIcon image = new ImageIcon(new ImageIcon(MainForm.class.getResource("/images/data-image.png"))
				.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(image);
		decisionTableScrollPane.setViewportView(label);
	}

	public void pack() {
		mainFrame.pack();
	}
}
