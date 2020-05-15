package me.neon.redpoints.service;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.controllers.ConfigurationController;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.utils.Cash;
import me.neon.redpoints.utils.Heads;
import me.neon.redpoints.utils.IModule;

public class ManagerYamlService implements IModule {
	
	private ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
	private Heads heads;
	
	public void getLeaderBoardList() {
		heads = new Heads();
		ManagerController mc = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		int i = 0;
		mc.leaderBoardList.clear();
		for (String account : cc.getStorageConfig().getConfig().getConfigurationSection("Accounts").getKeys(false)) {
			i++;
			String name = cc.getStorageConfig().getString("Accounts." + account + ".Name");
			double amount = cc.getStorageConfig().getConfig().getDouble("Accounts." + account + ".Amount");
			Cash cash = new Cash(name, RedPoints.getInstance().translateNameToUUID(name), amount);
			ItemStack head = heads.getHead(cash, i, null);
			mc.leaderBoardList.add(head);
		}
	}
	
	public boolean hasAccount(UUID uuid) {
		return cc.getStorageConfig().getConfig().contains("Accounts." + uuid);
	}
	
	public void createAccount(Cash cash) {
		if (hasAccount(cash.getuuid())) return;
		cc.getStorageConfig().getConfig().set("Accounts." + cash.getuuid() + ".Name", cash.getName().toLowerCase());
		cc.getStorageConfig().getConfig().set("Accounts." + cash.getuuid() + ".Amount", cash.getAmount());
		cc.getStorageConfig().saveConfiguration();
	}
	
	public void setAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cc.getStorageConfig().getConfig().set("Accounts." + cash.getuuid() + ".Amount", cash.getAmount());
		cc.getStorageConfig().saveConfiguration();
	}
	
	public void removeAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cc.getStorageConfig().getConfig().set("Accounts." + cash.getuuid() + ".Amount", getAmount(cash.getuuid()) - cash.getAmount());
		cc.getStorageConfig().saveConfiguration();
	}
	
	public void addAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cc.getStorageConfig().getConfig().set("Accounts." + cash.getuuid() + ".Amount", getAmount(cash.getuuid()) + cash.getAmount());
		cc.getStorageConfig().saveConfiguration();
	}
	
	public double getAmount(UUID uuid) {
		if (!hasAccount(uuid)) return 0.0;
		return cc.getStorageConfig().getConfig().getDouble("Accounts." + uuid + ".Amount");
	}
	
	public void deleteAccount(UUID uuid) {
		if (!hasAccount(uuid)) return;
		cc.getStorageConfig().getConfig().set("Accounts." + uuid, null);
		cc.getStorageConfig().saveConfiguration();
	}

	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
