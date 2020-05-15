package me.neon.redpoints.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.controllers.ConfigurationController;
import me.neon.redpoints.utils.ConsoleMessage;
import me.neon.redpoints.utils.ConsoleMessage.MessageLevel;
import me.neon.redpoints.utils.Methods;

public class MysqlService {
	
	private Connection connection;
	private String host;
	private String password;
	private String username;
	private int port;
	private String database;
	private String tablename;
	
	public MysqlService(String host, String password, String username, int port, String database, String tablename) {
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

	public void init() {
		openConnection();
	}

	public void stop() {
		closeConnection();
	}
	
	public void createTable() {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTableName() + "` (`id` INT NOT NULL AUTO_INCREMENT, `user` TEXT NOT NULL, `uuid` TEXT NOT NULL, `amount` DOUBLE NOT NULL,PRIMARY KEY (`id`));");
			st.executeUpdate();
			Methods.cleanUp(st);
		} catch (SQLException e) { }
	}
	
	public void openConnection()  {
		try {
			String url = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
			connection = DriverManager.getConnection(url, getUsername(), getPassword());
			createTable();
		} catch (SQLException e) { 
			ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
			if (cc.usingMysql()) {
				closeConnection();
				ConsoleMessage.send(MessageLevel.INFO, "It was not possible to connect to Mysql, disabling the plugin...");
				Bukkit.getServer().getPluginManager().disablePlugin(RedPoints.getInstance());
			}
		}
	}
	
	public void closeConnection() {
		if (this.connection != null) {
			try {
				this.connection.close();
				this.connection = null;
			} catch (SQLException e) { ConsoleMessage.send(MessageLevel.INFO, "An error occurred while closing the connection to Mysql."); }
		}
	}
	
	public Connection getConnection() { return connection; }
}
