package me.neon.redcash.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceMysql implements Service {
	
	private Connection connection;
	private String host;
	private String password;
	private String username;
	private int port;
	private String database;
	private String tablename;
	
	public ServiceMysql(String host, String password, String username, int port, String database, String tablename) {
		this.host = host;
		this.password = password;
		this.username = username;
		this.port = port;
		this.database = database;
		this.tablename = tablename;
	}
	
	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getTableName() {
		return tablename;
	}

	public int getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	@Override
	public void init() {
		openConnection();
	}

	@Override
	public void stop() {
		closeConnection();
	}
	
	public void createTable() {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTableName() + "` (`id` INT NOT NULL AUTO_INCREMENT, `user` TEXT NOT NULL, `uuid` TEXT NOT NULL, `amount` INT NOT NULL,PRIMARY KEY (`id`));");
			st.executeUpdate();
		} catch (SQLException e) { 
			e.printStackTrace();
			try {
				st.close();
			} catch (SQLException e1) { }
		}
	}
	
	public void openConnection()  {
		try {
			String url = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
			connection = DriverManager.getConnection(url, getUsername(), getPassword());
			createTable();
		} catch (SQLException e) { }
	}
	
	public void closeConnection() {
		if (this.connection != null) {
			try {
				this.connection.close();
				this.connection = null;
			} catch (SQLException e) { }
		}
	}
	
	public Connection getConnection() { return connection; }
}
