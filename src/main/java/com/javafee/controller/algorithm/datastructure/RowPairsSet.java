package com.javafee.controller.algorithm.datastructure;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class RowPairsSet {
	@Getter
	@Setter
	private Integer attributeIndex = null;
	@Getter
	@Setter
	private boolean allReductSet = false;
	@Getter
	private List<RowPair> rowPairs = new ArrayList<>();

	/**
	 * Adds new row pair to the set. Removes equals row if <code>distinct</code> is set.
	 *
	 * @param rowPair  pair of rows
	 * @param distinct flag that determines redundant rows filtering while adding to row pairs set
	 * @see RowPair
	 */
	public void add(RowPair rowPair, @NotNull boolean distinct) {
		if (distinct && !contains(rowPair))
			rowPairs.add(rowPair);
		else if (!distinct)
			rowPairs.add(rowPair);
	}

	/**
	 * Checks if given row pairs set is empty.
	 *
	 * @return <code>true</code> if is empty, <code>false</code> if is not empty
	 */
	public boolean isEmpty() {
		return rowPairs.isEmpty();
	}

	/**
	 * Calculates cardinality of given row pairs set.
	 *
	 * @return cardinality value
	 */
	public int cardinality() {
		return rowPairs.size();
	}

	private boolean contains(RowPair rowPair) {
		boolean result = false;
		for (RowPair pair : rowPairs)
			if (pair.equals(rowPair)) {
				result = true;
				break;
			}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = allReductSet ? new StringBuilder("S(T) = {") :
				attributeIndex == null ? new StringBuilder("{") :
						new StringBuilder("S" + attributeIndex.toString() + " = {");
		int rowPairsSize = rowPairs.size() - 1, pairIndex = 0;
		for (RowPair pair : rowPairs) {
			if (pairIndex != rowPairsSize)
				result.append(pair.toString() + ", ");
			else
				result.append(pair.toString());
			pairIndex++;
		}
		result.append("}");
		return result.toString();
	}
}
