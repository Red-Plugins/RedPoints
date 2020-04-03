package io.github.redplugins.cash.database;

import java.sql.*;

import io.github.redplugins.cash.utils.Configuration;

public class Mysql implements Database {
	
	private String url;
	public Connection connection;
	public String tableName = Configuration.config.getYaml().getString("Database.Table");
	private String hostAdress = Configuration.config.getYaml().getString("Database.Host");
	private String dataBase = Configuration.config.getYaml().getString("Database.Database");
	private String userName = Configuration.config.getYaml().getString("Database.Username");
	private String password = Configuration.config.getYaml().getString("Database.Password");
	private int portAdress = Configuration.config.getYaml().getInt("Database.Port");
	
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
	public void initialize() {
		this.url = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(this.url, getUser(), getPassword());
			System.out.println("[ReflexCash] Conexão montada com sucesso. (Mysql)");
			createTable();
		} catch (Exception e) {
			System.out.println("[ReflexCash] FALHA! Erro ao conectar. (Mysql)");
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
				System.out.println("[ReflexCash] FALHA! Erro ao desconectar. (Mysql)");
				e.printStackTrace();
			}
		}
 	}

	@Override
	public void createTable() {
		PreparedStatement st = null;
        try {
            st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + tableName + "` (`id` INT NOT NULL AUTO_INCREMENT, `user` VARCHAR(24) NULL, `amount` INT NULLPRIMARY KEY (`id`));");
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[ReflexCash] Erro ao criar a tabela. (Mysql)");
        }
    }

	@Override
	public int getPort() {
		return portAdress;
	}
}
