package com.javafee.controller.algorithm.datastructure;

import java.util.Objects;

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof LogicalAttributeValuePair)) return false;
		LogicalAttributeValuePair logicalExpressionObj = (LogicalAttributeValuePair) obj;
		if (logicalExpressionObj.getAttributeIndex() != this.attributeIndex
				&& !logicalExpressionObj.getValue().equals(this.value))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attributeIndex, value);
	}
}
