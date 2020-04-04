package me.neon.redcash;

import org.bukkit.Bukkit;  
import org.bukkit.plugin.java.JavaPlugin;

import me.neon.redcash.commands.CashCommand;
import me.neon.redcash.commands.CashStatsCommand;
import me.neon.redcash.database.Mysql;
import me.neon.redcash.manager.JoinEvent;
import me.neon.redcash.manager.Tasks;
import me.neon.redcash.utils.Configuration;

public class Cash extends JavaPlugin {
    
	public Configuration config = new Configuration("config.yml", this);
	public Configuration messages = new Configuration("messages.yml", this);
	
	public void onEnable() {
		init();
		registerCommands();
		registerListeners();
		Mysql mysql = new Mysql();
		mysql.initialize();
		mysql.createTable();
		Tasks t = new Tasks();
		t.updateToplist();
		t.updateAccountslist();
	}
	
	public void onDisable() {
		new Mysql().closeConnection();
	}
	
	void registerCommands() {
		getCommand("cash").setExecutor(new CashCommand());
		getCommand("cashstats").setExecutor(new CashStatsCommand());
	}
	
	void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
	}
	
	public static Cash getInstance() { return getPlugin(Cash.class); }
	
	void init() {
		config.saveDefaultConfig();
		config.reloadConfig();
		messages.saveDefaultConfig();
		messages.reloadConfig();
	}
}
