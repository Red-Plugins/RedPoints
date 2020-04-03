package io.github.redplugins.rankup;

import org.bukkit.plugin.java.JavaPlugin; 

import io.github.redplugins.rankup.database.Mysql;

public final class Rankup extends JavaPlugin {
	
	public static Rankup getInstance() { return getPlugin(Rankup.class); }
	
	private Mysql Mysql = new Mysql();
	
    @Override
    public void onEnable() {
        Mysql.openConnection();
    }

    @Override
    public void onDisable() {
    	
    }
}
