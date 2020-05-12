package me.neon.redcash.controllers;

import me.neon.redcash.RedCash;
import me.neon.redcash.service.ServiceMysql;
import me.neon.redcash.service.ServiceYaml;

public class StorageController implements Controller {
	
	private ServiceMysql serviceMysql;
	private ServiceYaml serviceYaml;
	private ConfigurationController config = RedCash.getInstance().getModuleForClass(ConfigurationController.class);
	private String host = config.getDefaultConfig().getString("Database.Host");
	private String password = config.getDefaultConfig().getString("Database.Password");
	private String username = config.getDefaultConfig().getString("Database.Username");
	private int port = config.getDefaultConfig().getConfig().getInt("Database.Port");
	private String database = config.getDefaultConfig().getString("Database.Database");
	private String tablename = config.getDefaultConfig().getString("Database.Table");
	
	@Override
	public void init() {
		if (config.getUseMysql()) {
			serviceMysql = new ServiceMysql(host, password, username, port, database, tablename);
			serviceMysql.init();
			return;
		} 
		serviceYaml = new ServiceYaml("storage.yml");
		serviceYaml.init();
	}

	@Override
	public void stop() {
		serviceMysql.stop();
	}
	
	public ServiceMysql getServiceMysql() {
		return serviceMysql;
	}
	
	public ServiceYaml getServiceYaml() {
		return serviceYaml;
	}
}
