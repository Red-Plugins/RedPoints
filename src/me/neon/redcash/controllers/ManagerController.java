package me.neon.redcash.controllers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.neon.redcash.RedCash;
import me.neon.redcash.event.RedCashChangeEvent;
import me.neon.redcash.event.RedCashResetEvent;
import me.neon.redcash.service.IModule;
import me.neon.redcash.service.ManagerMysqlService;
import me.neon.redcash.service.ManagerYamlService;
import me.neon.redcash.utils.Cash;

public class ManagerController implements Controller, IModule {
	
	private ConfigurationController config = RedCash.getInstance().getModuleForClass(ConfigurationController.class);
	public ArrayList<ItemStack> topList;
	
	@Override
	public void init() {
		updateTopListTask();
	}

	@Override
	public void stop() { }
	
	public void getTopList() {
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.getTopList();
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.getTopList();
	}
	
	public void createAccount(Cash cash) {
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.createAccount(cash);
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.createAccount(cash);
	}
	
	public void deleteAccount(UUID uuid) {
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.deleteAccount(uuid);
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.deleteAccount(uuid);
	}
	
	public void setAccount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedCashChangeEvent event = new RedCashChangeEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedCash.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		cash.setAmount(event.getChange());
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.setAmount(cash);
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.setAmount(cash);
	}
	
	public void resetAccount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedCashResetEvent event = new RedCashResetEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedCash.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		cash.setAmount(event.getChange());
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.setAmount(cash);
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.setAmount(cash);
	}
	
	public void removeAccount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedCashChangeEvent event = new RedCashChangeEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedCash.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		cash.setAmount(event.getChange());
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.removeAmount(cash);
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.removeAmount(cash);
	}
	
	public void addAccount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedCashChangeEvent event = new RedCashChangeEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedCash.getInstance().getServer().getPluginManager().callEvent(event);
		cash.setAmount(event.getChange());
		if (event.isCancelled()) return;
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.addAmount(cash);
			return;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		account.addAmount(cash);
	}
	
	public boolean hasAccount(UUID uuid) {
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			if (account.hasAccount(uuid)) return true;
			return false;
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		if (account.hasAccount(uuid)) return true;
		return false;
	}
	
	public int getAmount(UUID uuid) {
		if (config.getUseMysql()) {
			ManagerMysqlService account = RedCash.getInstance().getModuleForClass(ManagerMysqlService.class);
			return account.getAmount(uuid);
		}
		ManagerYamlService account = RedCash.getInstance().getModuleForClass(ManagerYamlService.class);
		return account.getAmount(uuid);
	}
	
	public void updateTopListTask() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				topList = new ArrayList<ItemStack>();
				topList.clear();
				getTopList();
			}
		}.runTaskTimer(RedCash.getInstance(), 0L, 20 * 300);
	}
	
	@Override
	public void starting() {
		
	}

	@Override
	public void closing() {
		
	}
}

