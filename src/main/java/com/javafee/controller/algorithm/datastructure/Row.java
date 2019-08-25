package com.javafee.controller.algorithm.datastructure;

import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Row {
	int index;

	double value;

	Vector values;

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
