package com.javafee.controller.algorithm.datastructure;

import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Row {
	@Getter
	@Setter
	int index;

	@Getter
	@Setter
	double value;

	@Getter
	@Setter
	Vector values;

	@Getter
	@Setter
	boolean prettyPrintWithValues = false;

	public Row(int index) {
		this.index = index;
	}

	public Row(int index, Vector values) {
		this.index = index;
		this.values = values;
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		Row other = (Row) obj;
		return index == other.index;
	}

	@Override
	public String toString() {
		return values != null && prettyPrintWithValues ? "r_" + index + " = " + values : "r_" + index;
	}
}
