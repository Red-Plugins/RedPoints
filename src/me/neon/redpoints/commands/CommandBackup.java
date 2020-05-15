package me.neon.redpoints.commands;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.commands.manager.AbstractCommand;
import me.neon.redpoints.controllers.BackupController;
import me.neon.redpoints.utils.Methods;

public class CommandBackup extends AbstractCommand {
	
	public CommandBackup() {
		super(false, "backup");
	}
	
	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		BackupController backupController = RedPoints.getInstance().getModuleForClass(BackupController.class);
		
		if (args.length < 1) return ReturnType.FAILURE;
		
		if (!args[0].trim().toLowerCase().equals("forced") && !args[0].trim().toLowerCase().equals("true") && !args[0].trim().toLowerCase().equals("false")) {
			return ReturnType.SYNTAX_ERROR;
		}
		
		if (backupController.isTemporaryDisablingCommands()) {
			sender.sendMessage("§f[§cRedPoints§f]§7 Backup in progress, please wait!");
			return ReturnType.FAILURE;
		}
		
		if (args[0].trim().toLowerCase().equals("forced")) {
			if (!Methods.isStringList("commandForcedBackup")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Methods.formatConfigText("commandForcedBackup"));
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.hasPermission("RedPoints.command.forcedbackup")) {
						player.sendMessage(Methods.formatConfigText("commandForcedBackup"));
					}
				}
				backupController.setTemporaryDisablingCommands(true);
				backupController.backup();
				backupController.setTemporaryDisablingCommands(false);
				return ReturnType.SUCCESS;
			} else {
				if (!(sender instanceof Player)) {
					for (String line : Methods.formatConfigTextList("commandForcedBackup")) {
						sender.sendMessage(line);
					}
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.hasPermission("RedPoints.command.forcedbackup")) {
						for (String line : Methods.formatConfigTextList("commandForcedBackup")) {
							player.sendMessage(line);
						}
					}
				}
			}
			backupController.setTemporaryDisablingCommands(true);
			backupController.backup();
			backupController.setTemporaryDisablingCommands(false);
			return ReturnType.SUCCESS;
		}
		
		if (args[0].trim().toLowerCase().equals("true")) {
			if (backupController.getBackup()) {
				sender.sendMessage(Methods.formatConfigText("commandBackupError", "%state%", "activated"));
				return ReturnType.FAILURE;
			}
			sender.sendMessage(Methods.formatConfigText("commandBackup", "%state%", "activated"));
			backupController.setBackup(true);
			return ReturnType.SUCCESS;
		}
		
		if (args[0].trim().toLowerCase().equals("false")) {
			if (!backupController.getBackup()) {
				sender.sendMessage(Methods.formatConfigText("commandBackupError", "%state%", "disabled"));
				return ReturnType.FAILURE;
			}
			sender.sendMessage(Methods.formatConfigText("commandBackup", "%state%", "disabled"));
			backupController.setBackup(false);
			return ReturnType.SUCCESS;
		}
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			List<String> options = new ArrayList<String>();
			options.add("forced");
			options.add("true");
			options.add("false");
			return options;
		}
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.backup";
	}

	@Override
	public String getSyntax() {
		return "/redpoints backup (forced/true/false)";
	}

	@Override
	public String getDescription() {
		return "Backup functions.";
	}
}
