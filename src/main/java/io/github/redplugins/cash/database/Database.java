package io.github.redplugins.cash.database;

public interface Database {
	
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
