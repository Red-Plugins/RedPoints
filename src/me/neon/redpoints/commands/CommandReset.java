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
import me.neon.redpoints.utils.Cash;
import me.neon.redpoints.utils.Methods;

public class CommandReset extends AbstractCommand {
	
	public CommandReset() {
		super(false, "reset");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		if (args.length < 1) return ReturnType.FAILURE;
		
		BackupController backupController = RedPoints.getInstance().getModuleForClass(BackupController.class);
		ManagerController managerController = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null && !args[0].trim().toLowerCase().equals("all")) return ReturnType.SYNTAX_ERROR;
		
		if (backupController.isTemporaryDisablingCommands()) {
			sender.sendMessage("§f[§cRedPoints§f]§7 Backup in progress, please wait!");
			return ReturnType.FAILURE;
		}
		
		if (args[0].trim().toLowerCase().equals("all")) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(players.getName().toLowerCase()))) {
					Cash cash = new Cash(players.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(players.getName().toLowerCase()), 0.0);
					managerController.resetAccount(cash);
				}
			}
			sender.sendMessage(Methods.formatConfigText("commandResetAll"));
			return ReturnType.SUCCESS;
		}
		
		if (!managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()))) {
			sender.sendMessage("§f[§cRedPoints§f] §7This player does not have an account.");
			return ReturnType.FAILURE;
		}
		
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), 0.0);
		managerController.resetAccount(cash);
		
		sender.sendMessage(Methods.formatConfigText("commandReset", "%player%", player.getName()));
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
		return "RedPoints.command.reset";
	}

	@Override
	public String getSyntax() {
		return "/redpoints reset (player)";
	}

	@Override
	public String getDescription() {
		return "Restore the account of the specified player";
	}
}
