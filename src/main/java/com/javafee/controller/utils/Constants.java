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
		ROWS_SETS, COVERAGES, DECISION_RULES
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

	@Getter
	@AllArgsConstructor
	public enum SetTypeConsistency {
		NOT_CHECKED("Not checked"),
		CONSISTENT("Consistent"),
		INCONSISTENT("Inconsistent");

		private final String name;

		public static SetTypeConsistency getByTypeName(String name) {
			return Stream.of(SetTypeConsistency.values()).filter(item -> item.getName().equals(name))
					.findFirst().get();
		}
	}

	@Getter
	@AllArgsConstructor
	public enum GeneralStatusPart {
		READY(SystemProperties.getResourceBundle().getString("mainForm.lblStatus.general.ready"));

		private final String value;

		public static GeneralStatusPart getByValue(String value) {
			return Stream.of(GeneralStatusPart.values()).filter(item -> item.getValue().equals(value))
					.findFirst().get();
		}
	}

	public String LANGUAGE_RESOURCE_BUNDLE = "messages";
	public String APPLICATION_LANGUAGE = "pl";
	public SetType APPLICATION_SET_TYPE = SetType.TRAINING;
	public double APPLICATION_TRAINING_PERCENTAGE = 70;
	public double APPLICATION_TEST_PERCENTAGE = 30;
	public boolean APPLICATION_SHUFFLE = true;
	public SetTypeConsistency APPLICATION_SET_TYPE_CONSISTENCY = SetTypeConsistency.NOT_CHECKED;
	public boolean APPLICATION_SHOW_SET_TYPE_CONSISTENCY_IN_DATA_PARAMETERS_PANEL = true;
	public boolean APPLICATION_REMOVE_FIRST_ATTRIBUTE = true;
	public boolean APPLICATION_RECURSIVE_REMOVAL_ATTRIBUTE = false;
	public boolean APPLICATION_GENERATE_INCONSISTENCY_GENERATION_REPORT_ATTRIBUTE = true;
	public boolean APPLICATION_QUALITY_MEASURE_FOR_EACH_ATTRIBUTE = false;
	public boolean APPLICATION_SHOW_DECISION_RULES_GENERATION_TIME = false;

	public DecisionRulesDataRange APPLICATION_DECISION_RULES_DATA_RANGE = DecisionRulesDataRange.ROWS_SETS;

	public String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
	public boolean DEFAULT_DATA_BASE_CONNECTION_FLAG = false;

	public String CSV_EXTENSION = ".csv";
	public String XLS_EXTENSION = ".xls";
	public String XLSX_EXTENSION = ".xlsx";
}
