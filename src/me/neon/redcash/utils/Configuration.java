package me.neon.redcash.utils;


import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration {

    private JavaPlugin plugin;
    private String name;
    private File file;
    private YamlConfiguration config;

    public Configuration(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        setName(name);
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        getConfig().options().copyDefaults(true);
    }

    public void saveDefaultConfig() {
        getPlugin().saveResource(getName(), false);
    }

    public void reloadConfig() {
        this.file = new File(getPlugin().getDataFolder(), getName());
        this.config = YamlConfiguration.loadConfiguration(getFile());
    }

    public void deleteConfig() {
        getFile().delete();
    }

    public boolean existConfig() {
        return getFile().exists();
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public List<Integer> getIntList(String path) {
        return getConfig().getIntegerList(path);
    }

    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    public List<Double> getDoubleList(String path) {
        return getConfig().getDoubleList(path);
    }

    public long getLong(String path) {
        return getConfig().getLong(path);
    }

    public List<Long> getLongList(String path) {
        return getConfig().getLongList(path);
    }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public List<?> getList(String path) {
        return getConfig().getList(path);
    }

    public boolean contains(String path) {
        return getConfig().contains(path);
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
    }
}
