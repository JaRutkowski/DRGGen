package com.javafee.model.utils;

import java.util.Vector;

import lombok.Getter;

public class QueryBuilder {
	@Getter
	private StringBuilder createStatement = new StringBuilder("CREATE TABLE `%s` (");
	@Getter
	private StringBuilder insertStatement = new StringBuilder("INSERT INTO `%s`.`%s` (");

	public QueryBuilder addColumnsDefinition(int number) {
		for (int index = 0; index < number; index++) {
			if (index != number - 1)
				createStatement.append(" `%s` VARCHAR(250),");
			else
				createStatement.append(" `%s` VARCHAR(250)");
		}
		return this;
	}

	public QueryBuilder addColumnsName(String[] columnsName) {
		for (int index = 0; index < columnsName.length; index++) {
			if (index != columnsName.length - 1)
				insertStatement.append(" `" + columnsName[index] + "`,");
			else
				insertStatement.append(" `" + columnsName[index] + "`)");
		}
		return this;
	}

	public QueryBuilder addValues(Vector<Vector> rows) {
		insertStatement.append(" VALUES ");
		int rowsCount = 0, valueCount = 0;
		for (Vector row : rows) {
			String[] array = (String[]) row.toArray(new String[row.size()]);
			valueCount = 0;
			for (String value : array) {
				if (valueCount == array.length - 1)
					if (rowsCount == rows.size() - 1)
						insertStatement.append(" '" + value + "');");
					else
						insertStatement.append(" '" + value + "'),");
				else if (valueCount == 0)
					insertStatement.append("('" + value + "',");
				else
					insertStatement.append(" '" + value + "',");
				valueCount++;
			}
			rowsCount++;
		}
		return this;
	}

	public String getPreparedCreateQuery() {
		return createStatement.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;").toString();
	}

	public String getPreparedInsertQuery() {
		return insertStatement.toString();
	}
}
