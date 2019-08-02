package com.javafee.controller.utils;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.javafee.model.JpaUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemProperties {
	@Setter
	@Getter
	private static Integer systemParameterDecisionAttributeIndex = null;

	@Getter
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
			Constants.LANGUAGE_RESOURCE_BUNDLE,
			new Locale(Constants.APPLICATION_LANGUAGE));

	public static void initializeDataBase(String url, String userName, String password) throws SQLException, ClassNotFoundException {
		JpaUtils.initializeDataBase(url, userName, password);
	}

	public Integer getSystemParameterDecisionAttributeIndex(Integer columnCount) {
		return columnCount - 1;
	}
}
