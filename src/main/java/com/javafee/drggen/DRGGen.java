package com.javafee.drggen;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.javafee.controller.mainform.MainFormActions;

import lombok.extern.java.Log;

@Log
public class DRGGen {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			MainFormActions mainFormActions = new MainFormActions();
			mainFormActions.control();
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
			log.severe(e.getMessage());
		}
	}
}
