package com.javafee.model.jpa.dao;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.lang3.ArrayUtils;

import com.javafee.model.jpa.JpaUtils;
import com.javafee.model.jpa.utils.QueryBuilder;

//TODO @Inject - @Named addition
public class JpaDao implements Dao {
	@Override
	public int createTable(String[] columnNames, String name) throws SQLException {
		String preparedQuery = new QueryBuilder().addColumnsDefinition(columnNames.length).getPreparedCreateQuery();
		columnNames = ArrayUtils.insert(0, columnNames, name);
		preparedQuery = String.format(preparedQuery, columnNames);
		return JpaUtils.executeUpdate(preparedQuery);
	}

	@Override
	public int insertData(String[] columnNames, Vector<Vector> data, String name) throws SQLException {
		String preparedQuery = new QueryBuilder().addColumnsName(columnNames).addValues(data).getPreparedInsertQuery();
		preparedQuery = String.format(preparedQuery, "drggen", name);
		return JpaUtils.executeUpdate(preparedQuery);
	}
}
