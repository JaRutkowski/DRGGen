package com.javafee.controller.utils.databasemapper;

import java.sql.SQLException;

import javax.swing.table.TableModel;

public interface DataBaseMapperStrategy {
	int map(TableModel inputData, String tableName) throws SQLException;
}
