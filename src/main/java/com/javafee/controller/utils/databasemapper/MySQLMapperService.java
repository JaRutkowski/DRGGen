package com.javafee.controller.utils.databasemapper;

import java.sql.SQLException;

import javax.ejb.Stateless;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.javafee.controller.utils.Common;
import com.javafee.model.jpa.dao.Dao;
import com.javafee.model.jpa.dao.JpaDao;

@Stateless
public class MySQLMapperService implements DataBaseMapperStrategy {
	private Dao dao = new JpaDao();

	@Override
	public int map(TableModel inputData, String tableName) throws SQLException {
		DefaultTableModel defaultTableModel = (DefaultTableModel) inputData;
		String[] columnNames = Common.getColumnNamesFromDefaultTableModel(defaultTableModel);
		createTable(columnNames, tableName);
		return insertData(columnNames, defaultTableModel, tableName);
	}

	private int createTable(String[] columnNames, String name) throws SQLException {
		return dao.createTable(columnNames, name);
	}

	private int insertData(String[] columnNames, DefaultTableModel defaultTableModel, String tableName) throws SQLException {
		return dao.insertData(columnNames, defaultTableModel.getDataVector(), tableName);
	}
}
