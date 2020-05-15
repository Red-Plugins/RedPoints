package me.neon.redpoints.controllers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.event.RedPointsChangeEvent;
import me.neon.redpoints.event.RedPointsResetEvent;
import me.neon.redpoints.service.ManagerMysqlService;
import me.neon.redpoints.service.ManagerYamlService;
import me.neon.redpoints.utils.Cash;
import me.neon.redpoints.utils.IModule;
 
public class ManagerController implements Controller, IModule {
	
	public ArrayList<ItemStack> leaderBoardList = new ArrayList<ItemStack>();
	
	@Override
	public void init() {
		updateLeaderBoardListTask();
	}

	@Override
	public void stop() { }
	
	public void getLeaderBoardList() {
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.getLeaderBoardList();
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.getLeaderBoardList();
	}
	
	public void createAccount(Cash cash) {
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.createAccount(cash);
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.createAccount(cash);
	}
	
	public void deleteAccount(UUID uuid) {
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.deleteAccount(uuid);
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.deleteAccount(uuid);
	}
	
	public void setAmount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedPointsChangeEvent event = new RedPointsChangeEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedPoints.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		cash.setAmount(event.getChange());
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.setAmount(cash);
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.setAmount(cash);
	}
	
	public void resetAccount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedPointsResetEvent event = new RedPointsResetEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedPoints.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		cash.setAmount(event.getChange());
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.setAmount(cash);
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.setAmount(cash);
	}
	
	public void removeAmount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedPointsChangeEvent event = new RedPointsChangeEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedPoints.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		cash.setAmount(event.getChange());
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.removeAmount(cash);
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.removeAmount(cash);
	}
	
	public void addAmount(Cash cash) {
		Player playerExact = Bukkit.getPlayerExact(cash.getName());
		RedPointsChangeEvent event = new RedPointsChangeEvent(playerExact, cash.getuuid(), cash.getAmount());
		RedPoints.getInstance().getServer().getPluginManager().callEvent(event);
		cash.setAmount(event.getChange());
		if (event.isCancelled()) return;
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
			account.addAmount(cash);
			return;
		}
		ManagerYamlService account = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		account.addAmount(cash);
	}
	
	public boolean hasAccount(UUID uuid) {
		ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) return account.hasAccount(uuid);
		ManagerYamlService account2 = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		return account2.hasAccount(uuid);
	}
	
	public double getAmount(UUID uuid) {
		ManagerMysqlService account = RedPoints.getInstance().getModuleForClass(ManagerMysqlService.class);
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) return account.getAmount(uuid);
		ManagerYamlService account2 = RedPoints.getInstance().getModuleForClass(ManagerYamlService.class);
		return account2.getAmount(uuid);
	}
	
	public void updateLeaderBoardListTask() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				getLeaderBoardList();
			}
		}.runTaskTimer(RedPoints.getInstance(), 0L, 20 * 300);
	}
	
	@Override
	public void starting() { }

	@Override
	public void closing() { }
}

