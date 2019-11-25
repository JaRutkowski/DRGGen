package com.javafee.model.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.javafee.controller.utils.Constants;

public class JpaUtils {
	private static Connection connection;

	public static void initializeDataBase(String url, String userName, String password) throws ClassNotFoundException, SQLException {
		Class.forName(Constants.DATABASE_DRIVER);
		connection = DriverManager.getConnection(url, userName, password);
	}

	public static ResultSet executeQuery(String query) throws SQLException {
		Statement stmt = connection.createStatement();
		return stmt.executeQuery(query);
	}

	public static int executeUpdate(String query) throws SQLException {
		Statement stmt = connection.createStatement();
		return stmt.executeUpdate(query);
	}

	public static void closeConnection() throws SQLException {
		if (connection != null)
			connection.close();
	}
}
