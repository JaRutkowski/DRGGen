package com.javafee.model.dao;

import java.sql.SQLException;
import java.util.Vector;

public interface BaseDao {
	int createTable(String[] columnNames, String name) throws SQLException;

	int insertData(String[] columnNames, Vector<Vector> data, String name) throws SQLException;
}
