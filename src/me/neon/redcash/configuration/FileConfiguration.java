package me.neon.redcash.configuration;

import java.io.File;  
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.neon.redcash.RedCash;

public class FileConfiguration {
	
	private JavaPlugin plugin;
	private String fileName;
	private File file;
	private File directory;
	private YamlConfiguration config;
	private boolean isDefaultDirectory;
	private boolean isDefaultConfiguration;
	
	public FileConfiguration() {
		this.setPlugin(RedCash.getPlugin(RedCash.class));
		this.isDefaultConfiguration = true;
		this.isDefaultDirectory = true;
		if (file == null || config == null) {
			setDefaultFileName();
			saveDefaultConfiguration();
			reloadConfiguration();
		}
	}
	
	public FileConfiguration(String fileName) {
		this.setPlugin(RedCash.getPlugin(RedCash.class));
		this.fileName = fileName;
		this.isDefaultDirectory = true;
		if (file == null || config == null) {
			saveDefaultConfiguration();
			reloadConfiguration();
		}
	}
	
	public FileConfiguration(File directory, String fileName) {
		this.setPlugin(RedCash.getPlugin(RedCash.class));
		this.fileName = fileName;
		this.directory = directory;
		if (file == null || config == null) {
			reloadConfiguration();
		}
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setDefaultFileName() {
		this.fileName = "config.yml";
	}
	
	public File getFile() {
		return file;
	}

	public File getDirectory() {
		return directory;
	}

	public YamlConfiguration getConfig() {
		return config;
	}
	
	public void saveConfiguration() {
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			plugin.getLogger().log(Level.WARNING, "Erro ao salvar está configuration " + getFileName());
		}
	}
	
	public void saveDefaultConfiguration() {
		getPlugin().saveResource(getFileName(), false);
	}
	
	public void reloadConfiguration() {
		if (isDefaultConfiguration) {
			if (isDefaultDirectory) {
				this.file = new File(getPlugin().getDataFolder(), getFileName());
				this.config = YamlConfiguration.loadConfiguration(getFile());
				return;
			}
		}
		if (isDefaultDirectory) {
			this.file = new File(getPlugin().getDataFolder(), getFileName());
			this.config = YamlConfiguration.loadConfiguration(getFile());
			return;
		}
		this.file = new File(getDirectory(), getFileName());
		if (!getFile().exists()) {
			try {
				getFile().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.config = YamlConfiguration.loadConfiguration(getFile());
	}
	
	public void deleteConfiguration() {
		getFile().delete();
	}
	
	public String getString(String path) {
		return getConfig().getString(path); 
	}
	
	public boolean hasConfiguration() {
		if (getFile().exists())
			return true;
		return false;
	}
}
