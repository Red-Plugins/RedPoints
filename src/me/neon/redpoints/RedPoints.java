package me.neon.redpoints;

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

import me.neon.redpoints.commands.CommandAdd;
import me.neon.redpoints.commands.CommandBackup;
import me.neon.redpoints.commands.CommandCreateAccount;
import me.neon.redpoints.commands.CommandDeleteAccount;
import me.neon.redpoints.commands.CommandDonate;
import me.neon.redpoints.commands.CommandLeaderboard;
import me.neon.redpoints.commands.CommandRedPoints;
import me.neon.redpoints.commands.CommandRemove;
import me.neon.redpoints.commands.CommandReset;
import me.neon.redpoints.commands.CommandSet;
import me.neon.redpoints.commands.CommandShow;
import me.neon.redpoints.commands.manager.CommandManager;
import me.neon.redpoints.controllers.BackupController;
import me.neon.redpoints.controllers.ConfigurationController;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.controllers.StorageController;
import me.neon.redpoints.inventory.Inventory;
import me.neon.redpoints.listeners.JoinListener;
import me.neon.redpoints.metrics.Metrics;
import me.neon.redpoints.service.BackupService;
import me.neon.redpoints.service.ManagerMysqlService;
import me.neon.redpoints.service.ManagerYamlService;
import me.neon.redpoints.utils.ConsoleMessage;
import me.neon.redpoints.utils.ConsoleMessage.MessageLevel;
import me.neon.redpoints.utils.IModule;
import me.neon.redpoints.utils.UUIDFetcher;
 
public class RedPoints extends JavaPlugin {
	
	private final Map<Class<? extends IModule>, IModule> modules = new HashMap<Class<? extends IModule>, IModule>();
	private ConfigurationController configurationController;
	private StorageController storageController;
	private BackupController backupController;
	private ManagerController managerController;
	private CommandManager commandManager;
	
	public void onEnable() {
		registerModule(ConfigurationController.class, new ConfigurationController());
		registerModule(ManagerMysqlService.class, new ManagerMysqlService());
		registerModule(ManagerController.class, new ManagerController());
		registerModule(BackupController.class, new BackupController());
		registerModule(BackupService.class, new BackupService());
		registerModule(ManagerYamlService.class, new ManagerYamlService());
		configurationController = new ConfigurationController();
		configurationController.init();
		commandManager = new CommandManager(this);
		commandManager.addCommand(new CommandRedPoints())
		.addSubCommand(new CommandAdd())
		.addSubCommand(new CommandRemove())
		.addSubCommand(new CommandSet())
		.addSubCommand(new CommandCreateAccount())
		.addSubCommand(new CommandDeleteAccount())
		.addSubCommand(new CommandDonate())
		.addSubCommand(new CommandLeaderboard())
		.addSubCommand(new CommandReset())
		.addSubCommand(new CommandShow())
		.addSubCommand(new me.neon.redpoints.commands.CommandReload())
		.addSubCommand(new CommandBackup());
		Bukkit.getServer().getPluginManager().registerEvents(new Inventory(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		storageController = new StorageController();
		storageController.init();
		managerController = new ManagerController();
		managerController.init();
		backupController = new BackupController();
		backupController.init();
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ConsoleMessage.send(MessageLevel.INFO, "Storage Type: Mysql.");
		} else {
			ConsoleMessage.send(MessageLevel.INFO, "Storage Type: Yaml.");
		}
		Metrics metrics = new Metrics(this);
		metrics.startSubmitting();
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
	
	public static RedPoints getInstance() { return getPlugin(RedPoints.class); }
	
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
	
	public <T extends IModule> T getModuleForClass(Class<T> clazz) { return clazz.cast(this.modules.get(clazz)); }
	
	@SuppressWarnings("deprecation")
	public UUID translateNameToUUID(String name) {
		UUID id = null;
		if (name == null) return id;
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
