package com.javafee.controller.algorithm.datastructure;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogicalOperator {
	CONJUNCTION("AND"), DISJUNCTION("OR");

	private final String logicalOperatorName;

	public static LogicalOperator getByName(String logicalOperatorName) {
		return Stream.of(LogicalOperator.values()).filter(item -> item.getLogicalOperatorName().equals(logicalOperatorName))
				.findFirst().get();
	}
}
