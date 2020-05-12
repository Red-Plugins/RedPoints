package me.neon.redcash.service;

import java.io.File;

import me.neon.redcash.RedCash;
import me.neon.redcash.configuration.FileConfiguration;

public class ServiceYaml implements Service, IModule {
	
	private FileConfiguration storageFile;
	private File locale = new File(RedCash.getInstance().getDataFolder() + "/storage");
	private String fileName;
	
	public ServiceYaml(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public void init() {
		createDataFolder();
	}

	@Override
	public void stop() { }
	
	public void createDataFolder() {
		if (!locale.exists()) {
			locale.mkdir();
		}
		createStorageFile();
		getStorageFile().getConfig().set("Accounts", null);
		getStorageFile().saveConfiguration();
	}
	
	public void createStorageFile() {
		storageFile = new FileConfiguration(locale, this.fileName);
	}
	
	public FileConfiguration getStorageFile() {
		if (storageFile == null) storageFile = new FileConfiguration(locale, this.fileName);
		return storageFile;
	}
	
	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
