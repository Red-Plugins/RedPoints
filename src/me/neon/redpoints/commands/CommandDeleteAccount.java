package me.neon.redpoints.commands;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.commands.manager.AbstractCommand;
import me.neon.redpoints.controllers.BackupController;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.utils.Methods;

public class CommandDeleteAccount extends AbstractCommand {
	
	public CommandDeleteAccount() {
		super(false, "delete");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		if (args.length < 1) return ReturnType.FAILURE;
		
		ManagerController managerController = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		BackupController backupController = RedPoints.getInstance().getModuleForClass(BackupController.class);
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null) return ReturnType.SYNTAX_ERROR;
		
		if (backupController.isTemporaryDisablingCommands()) {
			sender.sendMessage("§f[§cRedPoints§f]§7 Backup in progress, please wait!");
			return ReturnType.FAILURE;
		}
		
		if (!managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()))) {
			sender.sendMessage("§f[§cRedPoints§f] §7This player does not have an account.");
			return ReturnType.FAILURE;
		}
		
		managerController.deleteAccount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()));
		
		sender.sendMessage(Methods.formatConfigText("commandDeleteAccount", "%player%", player.getName()));
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			List<String> players = new ArrayList<String>();
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				players.add(allPlayers.getName());
			}
			return players;
		}
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.delete";
	}

	@Override
	public String getSyntax() {
		return "/redpoints delete (player)";
	}

	@Override
	public String getDescription() {
		return "Delete the account of specified player.";
	}
}
