package me.neon.redpoints.controllers;

import me.neon.redpoints.configuration.FileConfiguration;
import me.neon.redpoints.utils.IModule;

public class ConfigurationController implements Controller, IModule {
	
	private FileConfiguration defaultConfig;
	private FileConfiguration messageConfig;
	private FileConfiguration storageConfig;
	
	@Override
	public void init() {
		defaultConfig = new FileConfiguration();
		messageConfig = new FileConfiguration("messages.yml");
		storageConfig = new FileConfiguration("storage.yml");
	}

	@Override
	public void stop() { }
	
	public boolean getBackupState() {
		return getDefaultConfig().getConfig().getBoolean("backupState");
	}
	
	public boolean isBackupAlertForPlayers() {
		return getDefaultConfig().getConfig().getBoolean("backupAlertForPlayers");
	}
	
	public void setBackupState(boolean cancelled) {
		getDefaultConfig().getConfig().set("backupState", cancelled);
		getDefaultConfig().saveConfiguration();
	}
	
	public boolean usingMysql() {
		return getDefaultConfig().getConfig().getBoolean("Database.Use");
	}
	
	public FileConfiguration getStorageConfig() {
		if (storageConfig == null) storageConfig = new FileConfiguration("storage.yml");
		return storageConfig;
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
	public void starting() { }

	@Override
	public void closing() { }
}
