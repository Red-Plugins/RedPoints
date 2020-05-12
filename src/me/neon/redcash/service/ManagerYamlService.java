package me.neon.redcash.service;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import me.neon.redcash.RedCash;
import me.neon.redcash.configuration.FileConfiguration;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.utils.Cash;
import me.neon.redcash.utils.Heads;

public class ManagerYamlService implements IModule, ManagerService {
	
	private FileConfiguration config = RedCash.getInstance().getServiceFileYaml();
	private Heads heads;
	
	public void getTopList() {
		heads = new Heads();
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		int i = 0;
		for (String account : config.getConfig().getConfigurationSection("Accounts").getKeys(false)) {
			i++;
			String name = config.getString("Accounts." + account + ".Name");
			int amount = config.getConfig().getInt("Accounts." + account + ".Amount");
			Cash cash = new Cash(name, RedCash.getInstance().translateNameToUUID(name), amount);
			ItemStack head = heads.getHead(cash, i, null);
			mc.topList.add(head);
		}
	}
	
	public boolean hasAccount(UUID uuid) {
		return config.getConfig().contains("Accounts." + uuid);
	}
	
	public void createAccount(Cash cash) {
		if (hasAccount(cash.getuuid())) return;
		config.getConfig().set("Accounts." + cash.getuuid() + ".Name", cash.getName().toLowerCase());
		config.getConfig().set("Accounts." + cash.getuuid() + ".Amount", cash.getAmount());
		config.saveConfiguration();
	}
	
	public void setAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		config.getConfig().set("Accounts." + cash.getuuid() + ".Amount", cash.getAmount());
		config.saveConfiguration();
	}
	
	public void removeAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		config.getConfig().set("Accounts." + cash.getuuid() + ".Amount", getAmount(cash.getuuid()) - cash.getAmount());
		config.saveConfiguration();
	}
	
	public void addAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		config.getConfig().set("Accounts." + cash.getuuid() + ".Amount", getAmount(cash.getuuid()) + cash.getAmount());
		config.saveConfiguration();
	}
	
	public int getAmount(UUID uuid) {
		if (!hasAccount(uuid)) return 0;
		return config.getConfig().getInt("Accounts." + uuid + ".Amount");
	}
	
	public void deleteAccount(UUID uuid) {
		if (!hasAccount(uuid)) return;
		config.getConfig().set("Accounts." + uuid, null);
		config.saveConfiguration();
	}

	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
