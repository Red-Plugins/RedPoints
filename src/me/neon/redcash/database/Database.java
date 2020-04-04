package me.neon.redcash.database;

import java.sql.*;

public interface Database {
	
	Connection getConnection();
	String getTableName();
	String getHost();
	String getDatabase();
	String getUser();
	String getPassword();
	String getUrl();
	void initialize();
	void closeConnection();
	void createTable();
	int getPort();
}
