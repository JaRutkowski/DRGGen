package com.javafee.controller.algorithm.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.NotNull;

import com.javafee.controller.algorithm.datastructure.utils.RowsSetUtils;
import com.javafee.controller.algorithm.process.VectorProcess;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RowsSet {
	@Setter
	private Row concernedRow = null;

	@Setter
	private Integer attributeIndex = null;

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

	/**
	 * Calculate coverage for rows set to the concerned rows set as integer value.
	 *
	 * <p>For mathematically clarification below is the example of the cover value calculations:
	 * <ul>
	 * <li> Consider concerned rows set like <code>A(T, r_1) = {r_2, r_3, r_4, r_5}</code> and the rows set consisted
	 * of <code>A(T, r_1, f_1) = {r_3, r_4, r_5}</code>. </li>
	 * <li> For given data coverage value is 3, because max coverage of the <code>A(T, r_1)</code> is 4 but only 3 of
	 * 4 rows are covered by <code>A(T, r_1, f_1)</code>. The assumption is that <code>r_1</code> from concerned rows
	 * set is equals to <code>r_1</code> in the rows set what means that indexes are the same. The assumption is applied
	 * for all the rows in given sets.</li>
	 * </ul>
	 *
	 * @param concernedRowsSet concerned rows set relative which calculation are made
	 * @return cover value
	 * @see Row
	 */
	public int coverage(@NotNull RowsSet concernedRowsSet) {
		AtomicInteger coverage = new AtomicInteger(0);
		concernedRowsSet.getRows().forEach(row -> {
			if (this.contains(row)) coverage.getAndIncrement();
		});
		return coverage.get();
	}

	public int coverage(RowsSet[] rowsSetCombinationArray) {
		return this.coverage(RowsSetUtils.merge(rowsSetCombinationArray));
	}

	public String getTheMostCommonDecision() {
		return VectorProcess.findTheMostCommonDecision(this);
	}

	public DEGENERATION_TYPE isDegenerated() {
		if (this.rows == null || this.rows.isEmpty())
			return DEGENERATION_TYPE.EMPTY;
		if (VectorProcess.checkIfRowsAreWithSameAttributes(this))
			return DEGENERATION_TYPE.SAME_ROWS;
		if (VectorProcess.checkIfRowsAreWithSameDecisionAttributes(this))
			return DEGENERATION_TYPE.SAME_DECISIONS;
		return DEGENERATION_TYPE.NOT_DEGENERATED;
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

	public enum DEGENERATION_TYPE {
		SAME_ROWS, SAME_DECISIONS, EMPTY, NOT_DEGENERATED
	}
}
