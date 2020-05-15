package me.neon.redpoints.commands;

import java.util.List; 

import org.bukkit.command.CommandSender;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.commands.manager.AbstractCommand;
import me.neon.redpoints.controllers.BackupController;
import me.neon.redpoints.controllers.ConfigurationController;
import me.neon.redpoints.utils.Methods;

public class CommandReload extends AbstractCommand {

	public CommandReload() {
		super(false, "reload");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		BackupController backupController = RedPoints.getInstance().getModuleForClass(BackupController.class);
		ConfigurationController config = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		
		if (backupController.isTemporaryDisablingCommands()) {
			sender.sendMessage("§f[§cRedPoints§f]§7 Backup in progress, please wait!");
			return ReturnType.FAILURE;
		}
		
		sender.sendMessage(Methods.formatConfigText("commandReload"));
		config.getDefaultConfig().reloadConfiguration();
		config.getMessageConfig().reloadConfiguration();
		
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.reload";
	}

	@Override
	public String getSyntax() {
		return "/redpoints reload";
	}

	@Override
	public String getDescription() {
		return "Reload all settings.";
	}
}
