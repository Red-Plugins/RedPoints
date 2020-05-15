package me.neon.redpoints.controllers;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.service.MysqlService;

public class StorageController implements Controller {
	
	private MysqlService serviceMysql;
	private ConfigurationController config = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
	private String host = config.getDefaultConfig().getString("Database.Host");
	private String password = config.getDefaultConfig().getString("Database.Password");
	private String username = config.getDefaultConfig().getString("Database.Username");
	private int port = config.getDefaultConfig().getConfig().getInt("Database.Port");
	private String database = config.getDefaultConfig().getString("Database.Database");
	private String tablename = config.getDefaultConfig().getString("Database.Table");
	
	@Override
	public void init() {
		if (!config.usingMysql()) return;
		serviceMysql = new MysqlService(host, password, username, port, database, tablename);
		serviceMysql.init();
	}

	@Override
	public void stop() {
		if (!config.usingMysql()) return;
		serviceMysql.stop();
	}
	
	public MysqlService getServiceMysql() {
		return serviceMysql;
	}
}
