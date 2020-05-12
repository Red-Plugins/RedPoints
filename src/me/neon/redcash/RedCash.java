package me.neon.redcash;

import java.sql.Connection;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.neon.redcash.commands.CommandAdd;
import me.neon.redcash.commands.CommandBackup;
import me.neon.redcash.commands.CommandCash;
import me.neon.redcash.commands.CommandCreateAccount;
import me.neon.redcash.commands.CommandDeleteAccount;
import me.neon.redcash.commands.CommandDonate;
import me.neon.redcash.commands.CommandRemove;
import me.neon.redcash.commands.CommandReset;
import me.neon.redcash.commands.CommandSet;
import me.neon.redcash.commands.CommandShow;
import me.neon.redcash.commands.CommandTopList;
import me.neon.redcash.commands.manager.CommandManager;
import me.neon.redcash.configuration.FileConfiguration;
import me.neon.redcash.controllers.BackupController;
import me.neon.redcash.controllers.ConfigurationController;
import me.neon.redcash.controllers.StorageController;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.inventory.Inventory;
import me.neon.redcash.listeners.JoinListener;
import me.neon.redcash.listeners.VotifierListener;
import me.neon.redcash.service.BackupService;
import me.neon.redcash.service.IModule;
import me.neon.redcash.service.ManagerMysqlService;
import me.neon.redcash.service.ManagerYamlService;
import me.neon.redcash.utils.Test;
import me.neon.redcash.utils.TestHandler;
import me.neon.redcash.utils.UUIDFetcher;
 
public class RedCash extends JavaPlugin {
	
	private final Map<Class<? extends IModule>, IModule> modules = new HashMap<Class<? extends IModule>, IModule>();
	private ConfigurationController configurationController;
	private StorageController storageController;
	private BackupController backupController;
	private ManagerController managerController;
	private CommandManager commandManager;
	
	public void onEnable() {
		registerModule(ConfigurationController.class, new ConfigurationController());
		registerModule(ManagerMysqlService.class, new ManagerMysqlService());
		registerModule(ManagerYamlService.class, new ManagerYamlService());
		registerModule(ManagerController.class, new ManagerController());
		registerModule(BackupController.class, new BackupController());
		registerModule(BackupService.class, new BackupService());
		configurationController = new ConfigurationController();
		configurationController.init();
		commandManager = new CommandManager(this);
		commandManager.addCommand(new CommandCash())
		.addSubCommand(new CommandAdd())
		.addSubCommand(new CommandRemove())
		.addSubCommand(new CommandSet())
		.addSubCommand(new CommandCreateAccount())
		.addSubCommand(new CommandDeleteAccount())
		.addSubCommand(new CommandDonate())
		.addSubCommand(new CommandTopList())
		.addSubCommand(new CommandReset())
		.addSubCommand(new CommandShow())
		.addSubCommand(new me.neon.redcash.commands.CommandReload())
		.addSubCommand(new CommandBackup());
		Bukkit.getServer().getPluginManager().registerEvents(new Inventory(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new VotifierListener(), this);
		storageController = new StorageController();
		storageController.init();
		managerController = new ManagerController();
		managerController.init();
		backupController = new BackupController();
		backupController.init();
		System.out.println(new Test().get());
		System.out.println(new TestHandler().get());
	}
	
	public void onDisable() {
		managerController.stop();
		backupController.stop();
		storageController.stop();
		List<Class<? extends IModule>> clazzez = new ArrayList<>();
	    clazzez.addAll(this.modules.keySet());
	    for (Class<? extends IModule> clazz : clazzez)
	      deregisterModuleForClass(clazz); 
	}
	
	public CommandManager getCommandManager() { 
		return commandManager; 
	}
	
	public Connection getServiceConnectionMysql() {
		return storageController.getServiceMysql().getConnection();
	}
	
	public String getServiceTableMysql() {
		return storageController.getServiceMysql().getTableName();
	}
	
	public FileConfiguration getServiceFileYaml() {
		return storageController.getServiceYaml().getStorageFile();
	}
	
	public static RedCash getInstance() { return getPlugin(RedCash.class); }
	
	public <T extends IModule> void registerModule(Class<T> clazz, T module) {
	    if (clazz == null) throw new IllegalArgumentException("Class cannot be null"); 
	    if (module == null) throw new IllegalArgumentException("Module cannot be null"); 
	    if (this.modules.containsKey(clazz))
	      getLogger().warning("Overwriting module for class: " + clazz.getName()); 
	    this.modules.put(clazz, (IModule)module);
	    module.starting();
	  }
	
	@SuppressWarnings("unchecked")
	public <T extends IModule> T deregisterModuleForClass(Class<T> clazz) {
	    if (clazz == null) throw new IllegalArgumentException("Class cannot be null"); 
	    IModule iModule = (IModule)clazz.cast(this.modules.get(clazz));
	    if (iModule != null) iModule.closing(); 
	    return (T)iModule;
	}
	
	public <T extends IModule> T getModuleForClass(Class<T> clazz) {
	    return clazz.cast(this.modules.get(clazz));
	}
	
	@SuppressWarnings("deprecation")
	public UUID translateNameToUUID(String name) {
		UUID id = null;
		ConfigurationController config = getModuleForClass(ConfigurationController.class);
		if (name == null) {
			if (config.getDebugUUID()) return id;
		}
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			if (player.getName().equalsIgnoreCase(name)) {
				id = player.getUniqueId();
				break;
			}
		}
		if (id == null && Bukkit.getServer().getOnlineMode()) {
			UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(new String[] {name}));
			try {
				Map<String, UUID> map = fetcher.call();
				for (Map.Entry<String, UUID> entry : map.entrySet()) {
					if (name.equalsIgnoreCase(entry.getKey())) {
						id = entry.getValue();
						break;
					}
				}
			} catch (Exception e) { }
		} else if (id == null && !Bukkit.getServer().getOnlineMode()) {
			id = Bukkit.getServer().getOfflinePlayer(name).getUniqueId();
		}
		return id;
	}
}
