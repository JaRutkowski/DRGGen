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

		private final int resultIndex;

		public static StandardDecisionRulesGenerator getByResultIndex(int resultIndex) {
			return Stream.of(StandardDecisionRulesGenerator.values()).filter(item -> item.getResultIndex() == resultIndex)
					.findFirst().get();
		}
	}

	public enum DecisionRulesDataRange {
		ALL_DATA, COVERAGE_AND_DECISION_RULES, DECISION_RULES
	}

	public String LANGUAGE_RESOURCE_BUNDLE = "messages";
	public String APPLICATION_LANGUAGE = "en";
	public String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
	public boolean DEFAULT_DATA_BASE_CONNECTION_FLAG = false;

	public String CSV_EXTENSION = ".csv";
	public String XLS_EXTENSION = ".xls";
	public String XLSX_EXTENSION = ".xlsx";
}
