package io.github.redplugins.rankup.database;

public interface Database {
	
	String getTableName();
	String getHost();
	String getDatabase();
	String getUser();
	String getPassword();
	String getUrl();
	void openConnection();
	void closeConnection();
	void createTable();
	int getPort();
}
