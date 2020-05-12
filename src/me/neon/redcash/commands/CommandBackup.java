package me.neon.redcash.commands;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.controllers.BackupController;
import me.neon.redcash.utils.Methods;

public class CommandBackup extends AbstractCommand {
	
	public CommandBackup() {
		super(false, "backup");
	}
	
	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		BackupController bc = RedCash.getInstance().getModuleForClass(BackupController.class);
		if (args.length < 1) return ReturnType.FAILURE;
		
		if (args[0].trim().toLowerCase() != "force" && args[0].trim().toLowerCase() != "true" && args[0].trim().toLowerCase() != "false") {
			return ReturnType.SYNTAX_ERROR;
		}
		
		if (!bc.getTemporaryBackup()) {
			sender.sendMessage("§f[§cRedCash§f]§7 Backup em andamento, aguarde!");
			return ReturnType.FAILURE;
		}
		
		if (args[0].trim().toLowerCase() == "force") {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission("RedCash.command.backup")) {
					for (String line : Methods.formatConfigTextList("commandForceBackup")) {
						player.sendMessage(line);
					}
				}
			}
			bc.setBackup(false);
			bc.backup();
			bc.setBackup(true);
		}
		
		if (args[0].trim().toLowerCase() == "true") {
			if (bc.getBackup()) 
				sender.sendMessage(Methods.formatConfigText("commandBackupError", "%state%", "ativado"));
			sender.sendMessage(Methods.formatConfigText("commandBackup", "%state%", "ativado"));
			bc.setBackup(true);
			
		}
		
		if (args[0].trim().toLowerCase() == "false") {
			if (!bc.getBackup())
				sender.sendMessage(Methods.formatConfigText("commandBackupError", "%state%", "desativado"));
			sender.sendMessage(Methods.formatConfigText("commandBackup", "%state%", "desativado"));
			bc.setBackup(false);
		}
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			List<String> options = new ArrayList<String>();
			options.add("force");
			options.add("true");
			options.add("false");
			return options;
		}
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedCash.command.backup";
	}

	@Override
	public String getSyntax() {
		return "/cash backup (force/true/false)";
	}

	@Override
	public String getDescription() {
		return "Funções sobre backup.";
	}
}
