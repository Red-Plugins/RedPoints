package me.neon.redcash.database;

import java.sql.*;  

import me.neon.redcash.Cash;

public class Mysql implements Database {
	
	private String url;
	private static Connection connection;
	private static Mysql instance = new Mysql();
	private String tableName = Cash.getInstance().config.getString("Database.Table");
	private String hostAdress = Cash.getInstance().config.getString("Database.Host");
	private String dataBase = Cash.getInstance().config.getString("Database.Database");
	private String userName = Cash.getInstance().config.getString("Database.Username");
	private String password = Cash.getInstance().config.getString("Database.Password");
	private int portAdress = Cash.getInstance().config.getInt("Database.Port");
	
	public static Mysql getInstance() {
		return instance;
	}
	
	@Override
	public void initialize() {
		this.url = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
		try {
			connection = DriverManager.getConnection(this.url, getUser(), getPassword());
			System.out.println("[ReflexCash] Conexão montada com sucesso. (Mysql)");
		} catch (SQLException e) {
			System.out.println("[ReflexCash] FALHA! Erro ao conectar. (Mysql)");
		}
	}

	@Override
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
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
            st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + tableName + "` (`id` INT NOT NULL AUTO_INCREMENT, `user` VARCHAR(24) NULL, `amount` INT NULL,PRIMARY KEY (`id`));");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[ReflexCash] Erro ao criar a tabela. (Mysql)");
        }
    }
	
	@Override
	public Connection getConnection() {
		return connection;
	}
	
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
	public int getPort() {
		return portAdress;
	}
}
