package com.javafee.controller.algorithm.datastructure.utils;

import com.javafee.controller.algorithm.datastructure.Row;
import com.javafee.controller.algorithm.datastructure.RowsSet;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RowsSetUtils {
	public RowsSet merge(RowsSet... rowsSets) {
		RowsSet rowsSet = new RowsSet();
		for (RowsSet rSet : rowsSets) {
			for (Row row : rSet.getRows())
				rowsSet.add(row, true);
		}
		return rowsSet;
	}
}
