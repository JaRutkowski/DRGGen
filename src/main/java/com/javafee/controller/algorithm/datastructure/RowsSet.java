package com.javafee.controller.algorithm.datastructure;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class RowsSet {
	@Getter
	@Setter
	private Row concernedRow = null;

	@Getter
	@Setter
	private Integer attributeIndex = null;

	@Getter
	private List<Row> rows = new ArrayList<>();

	/**
	 * Adds new row to the set. Removes equals row if <code>distinct</code> is set.
	 *
	 * @param row      row
	 * @param distinct flag that determines redundant rows filtering while adding to row set
	 * @see RowPair
	 */
	public void add(Row row, @NotNull boolean distinct) {
		if (distinct && !contains(row))
			rows.add(row);
		else if (!distinct)
			rows.add(row);
	}

	private boolean contains(Row row) {
		boolean result = false;
		for (Row currRow : rows)
			if (currRow.equals(row)) {
				result = true;
				break;
			}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("A(T,")
				.append(concernedRow != null ? concernedRow.toString() : "r_x")
				.append(attributeIndex != null ? ",f_" + attributeIndex : "")
				.append(") = {");
		int rowPairsSize = rows.size() - 1, pairIndex = 0;
		for (Row row : rows) {
			if (pairIndex != rowPairsSize)
				result.append(row.toString() + ", ");
			else
				result.append(row.toString());
			pairIndex++;
		}
		result.append("}");
		return result.toString();
	}
}
