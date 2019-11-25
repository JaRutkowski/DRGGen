package com.javafee.model.jpa.dao;

import java.sql.SQLException;
import java.util.Vector;

public interface Dao {
	int createTable(String[] columnNames, String name) throws SQLException;

	int insertData(String[] columnNames, Vector<Vector> data, String name) throws SQLException;
}
