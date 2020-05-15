package me.neon.redpoints.controllers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.service.BackupService;
import me.neon.redpoints.utils.ConsoleMessage;
import me.neon.redpoints.utils.ConsoleMessage.MessageLevel;
import me.neon.redpoints.utils.IModule;

public class BackupController implements Controller, IModule {
	
	public boolean temporaryDisablingCommands = false;
	private ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
	
	@Override
	public void init() {
		backupTasking();
	}

	@Override
	public void stop() { backup(); }
	
	public void setTemporaryDisablingCommands(boolean temporaryBackup) {
		this.temporaryDisablingCommands = temporaryBackup;
	}
	
	public boolean isTemporaryDisablingCommands() {
		return this.temporaryDisablingCommands;
	}
	
	public void backup() {
		BackupService bc = RedPoints.getInstance().getModuleForClass(BackupService.class);
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		if (cc.usingMysql()) {
			bc.saveBackupFile(bc.getAccountsForBackupMysql());
			return;
		}
		bc.saveBackupFile(bc.getAccountsForBackupYaml());
	}
	
	public void setBackup(boolean cancelled) {
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		cc.setBackupState(cancelled);
	}
	
	public boolean getBackup() {
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		return cc.getBackupState();
	}
	
	public void backupTasking() {
		if (getBackup()) {
			ConsoleMessage.send(MessageLevel.INFO, "Automatic backup being started...");
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				if (getBackup()) {
					setTemporaryDisablingCommands(true);
					backup();
					setTemporaryDisablingCommands(false);
					ConsoleMessage.send(MessageLevel.INFO, "Automatic backup performed successfully!");
					if (cc.isBackupAlertForPlayers()) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (player.hasPermission("RedPoints.backupalert")) {
								player.sendMessage("§f[§cRedCash§f]§7 Automatic backup performed successfully!");
							}
						}
					}
				}
			}
		}.runTaskTimer(RedPoints.getInstance(), 20L, 20 * 1800);
	}
	
	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
