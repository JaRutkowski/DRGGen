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
	private Integer systemParameterDecisionAttributeIndex = null;

	@Setter
	@Getter
	private Constants.SetType systemParameterSetType = Constants.APPLICATION_SET_TYPE;

	@Setter
	@Getter
	private double systemParameterTrainingPercentage = Constants.APPLICATION_TRAINING_PERCENTAGE;

	@Setter
	@Getter
	private double systemParameterTestPercentage = Constants.APPLICATION_TEST_PERCENTAGE;

	@Setter
	@Getter
	private boolean systemParameterShuffle = Constants.APPLICATION_SHUFFLE;

	@Setter
	@Getter
	private Constants.DecisionRulesDataRange systemParameterDecisionRulesDataRange = Constants.APPLICATION_DECISION_RULES_DATA_RANGE;

	@Getter
	private ResourceBundle resourceBundle = ResourceBundle.getBundle(
			Constants.LANGUAGE_RESOURCE_BUNDLE,
			new Locale(Constants.APPLICATION_LANGUAGE));

	public void initializeDataBase(String url, String userName, String password) throws SQLException, ClassNotFoundException {
		JpaUtils.initializeDataBase(url, userName, password);
	}

	public Integer getSystemParameterDecisionAttributeIndex(Integer columnCount) {
		return columnCount - 1;
	}
}
