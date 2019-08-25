package com.javafee.controller.algorithm.datastructure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogicalAttributeValuePair {
	private Integer attributeIndex = null;
	private String value = null;

	@Override
	public String toString() {
		return "f_" + attributeIndex + " = " + value;
	}
}
