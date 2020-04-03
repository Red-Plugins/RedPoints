package io.github.redplugins.rankup.database;

import java.sql.*;

public class Mysql implements Database {
	
	private String url;
	private Connection connection;
	private String tableName = ""; //Rankup.getInstance().config.getString("Database.Table");
	private String hostAdress = ""; //Rankup.getInstance().config.getString("Database.Host");
	private String dataBase = ""; //Rankup.getInstance().config.getString("Database.Database");
	private String userName = ""; //Rankup.getInstance().config.getString("Database.Username");
	private String password = ""; //Rankup.getInstance().config.getString("Database.Password");
	private int portAdress = 1; //Rankup.getInstance().config.getInt("Database.Port");
	
	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public String getHost() {
		return hostAdress;
	}

	@Override
	public String getDatabase() {
		return dataBase;
	}

	@Override
	public String getUser() {
		return userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void openConnection() {
		this.url = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(this.url, getUser(), getPassword());
			System.out.println("[RedRankup] Conexão montada com sucesso. (Mysql)");
			createTable();
		} catch (Exception e) {
			System.out.println("[RedRankup] FALHA! Erro ao conectar. (Mysql)");
			closeConnection();
		}
	}

	@Override
	public void closeConnection() {
		if (connection != null) {
			try {
				this.connection.close();
				this.connection = null;
			} catch (SQLException e) {
				System.out.println("[RedRankup] FALHA! Erro ao desconectar. (Mysql)");
				e.printStackTrace();
			}
		}
 	}

	@Override
	public void createTable() {
		
	}

	@Override
	public int getPort() {
		return portAdress;
	}
}
