package me.neon.redcash.controllers;

import me.neon.redcash.configuration.FileConfiguration;
import me.neon.redcash.service.IModule;

public class ConfigurationController implements Controller, IModule {
	
	private FileConfiguration defaultConfig;
	private FileConfiguration messageConfig;
	
	@Override
	public void init() {
		defaultConfig = new FileConfiguration();
		messageConfig = new FileConfiguration("messages.yml");
	}

	@Override
	public void stop() { }
	
	public boolean getDebugUUID() {
		return getDefaultConfig().getConfig().getBoolean("debugUUID");
	}
	
	public boolean getBackupState() {
		return getDefaultConfig().getConfig().getBoolean("backupState");
	}
	
	public void setBackupState(boolean cancelled) {
		getDefaultConfig().getConfig().set("backupState", cancelled);
		getDefaultConfig().saveConfiguration();
	}
	
	public boolean getVersionCheckState() {
		return getDefaultConfig().getConfig().getBoolean("versionChecker");
	}
	
	public boolean getUseMysql() {
		return getDefaultConfig().getConfig().getBoolean("Database.Mysql");
	}
	
	public FileConfiguration getDefaultConfig() {
		if (defaultConfig == null) defaultConfig = new FileConfiguration();
		return defaultConfig; 
	}
	
	public FileConfiguration getMessageConfig() {
		if (messageConfig == null) messageConfig = new FileConfiguration("messages.yml");
		return messageConfig; 
	}

	@Override
	public void starting() {
	}

	@Override
	public void closing() {
	}
}
