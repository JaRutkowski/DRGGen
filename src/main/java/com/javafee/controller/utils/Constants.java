package com.javafee.controller.utils;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
	@Getter
	@AllArgsConstructor
	public enum StandardDecisionRulesGenerator {
		ROWS_SET(0), ROWS_SET_FOR_EACH_ATTRIBUTE(1), COVERAGE(2), DECISION_RULES(3);

		private final int value;

		public static StandardDecisionRulesGenerator getByValue(int value) {
			return Stream.of(StandardDecisionRulesGenerator.values()).filter(item -> item.getValue() == value)
					.findFirst().get();
		}
	}

	public enum DecisionRulesDataRange {
		ALL_DATA, COVERAGE_AND_DECISION_RULES, DECISION_RULES
	}

	@Getter
	@AllArgsConstructor
	public enum SetType {
		TRAINING("Training set"),
		TEST("Test set"),
		VALIDATION("Validation set"),
		FULL("Full set");

		private final String name;

		public static SetType getByTypeName(String name) {
			return Stream.of(SetType.values()).filter(item -> item.getName().equals(name))
					.findFirst().get();
		}
	}

	public String LANGUAGE_RESOURCE_BUNDLE = "messages";
	public String APPLICATION_LANGUAGE = "en";
	public SetType APPLICATION_SET_TYPE = SetType.TRAINING;
	public double APPLICATION_TRAINING_PERCENTAGE = 70;
	public double APPLICATION_TEST_PERCENTAGE = 30;
	public boolean APPLICATION_SHUFFLE = true;
	public DecisionRulesDataRange APPLICATION_DECISION_RULES_DATA_RANGE = DecisionRulesDataRange.ALL_DATA;

	public String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
	public boolean DEFAULT_DATA_BASE_CONNECTION_FLAG = false;

	public String CSV_EXTENSION = ".csv";
	public String XLS_EXTENSION = ".xls";
	public String XLSX_EXTENSION = ".xlsx";
}
