package com.javafee.controller.algorithm.datastructure;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RowPair {
	public int first;
	public int second;

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		RowPair other = (RowPair) obj;
		return first == other.first && second == other.second ||
				second == other.first && first == other.second;
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}
}
