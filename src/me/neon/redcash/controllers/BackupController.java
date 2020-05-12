package me.neon.redcash.controllers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neon.redcash.RedCash;
import me.neon.redcash.service.BackupService;
import me.neon.redcash.service.IModule;
import me.neon.redcash.utils.ConsoleMessage;
import me.neon.redcash.utils.ConsoleMessage.MessageLevel;

public class BackupController implements Controller, IModule {
	
	public boolean temporaryBackup = false;
	
	@Override
	public void init() {
		backupTasking();
	}

	@Override
	public void stop() { backup(); }
	
	public void setTemporaryBackup(boolean temporaryBackup) {
		this.temporaryBackup = temporaryBackup;
	}
	
	public boolean getTemporaryBackup() {
		return this.temporaryBackup;
	}
	
	public void backup() {
		BackupService bc = RedCash.getInstance().getModuleForClass(BackupService.class);
		bc.saveBackupFile(bc.getAccountsForBackup());
	}
	
	public void setBackup(boolean cancelled) {
		ConfigurationController cc = RedCash.getInstance().getModuleForClass(ConfigurationController.class);
		cc.setBackupState(cancelled);
	}
	
	public boolean getBackup() {
		ConfigurationController cc = RedCash.getInstance().getModuleForClass(ConfigurationController.class);
		return cc.getBackupState();
	}
	
	public void backupTasking() {
		if (getBackup()) {
			ConsoleMessage.send(MessageLevel.INFO, "Backup automático sendo inciado...");
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				if (getBackup()) {
					backup();
					ConsoleMessage.send(MessageLevel.INFO, "Backup automático efetuado com sucesso!");
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (player.hasPermission("RedCash.backup.alert")) {
							player.sendMessage("§f[§cRedCash§f]§7 Backup automático efetuado com sucesso!");
						}
					}
				}
			}
		}.runTaskTimer(RedCash.getInstance(), 20L, 20 * 1800);
	}
	
	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
