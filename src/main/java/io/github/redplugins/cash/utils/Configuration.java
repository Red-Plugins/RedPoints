package io.github.redplugins.cash.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.redplugins.cash.Cash;

public enum Configuration {
	messages,
	config;
	
	private File file;
	private FileConfiguration configuration;
	
	Configuration() {
		JavaPlugin plugin = Cash.getInstance();
		String fileName = name() + ".yml";
		
		file = new File(plugin.getDataFolder(), fileName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(fileName, false);
		}
		
		configuration = new YamlConfiguration();
		try {
			configuration.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile() {
		return file;
	}
	
	public FileConfiguration getYaml() {
		return configuration;
	}
}
