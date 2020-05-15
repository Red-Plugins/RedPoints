package me.neon.redpoints.commands;

import java.util.ArrayList; 
import java.util.Arrays;
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
import me.neon.redpoints.utils.Methods.CustomDouble;

public class CommandAdd extends AbstractCommand {
	
	public CommandAdd() {
		super(false, "add");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		if (args.length < 1) return ReturnType.FAILURE;
		
		ManagerController managerController = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		BackupController backupController = RedPoints.getInstance().getModuleForClass(BackupController.class);
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null && !args[0].trim().toLowerCase().equals("all")) return ReturnType.SYNTAX_ERROR;
		
		if (backupController.isTemporaryDisablingCommands()) {
			sender.sendMessage("§f[§cRedPoints§f]§7 Backup in progress, please wait!");
			return ReturnType.FAILURE;
		}
		
		double amount = args.length == 2 ? CustomDouble.parseDouble(args[1]) : 1;
		
		if (args[0].trim().toLowerCase().equals("all")) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(players.getName().toLowerCase()))) {
					Cash cash = new Cash(players.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(players.getName().toLowerCase()), amount);
					managerController.addAmount(cash);
				}
			}
			sender.sendMessage(Methods.formatConfigText("commandAddAll", "%amount%", String.valueOf(amount)));
			return ReturnType.SUCCESS;
		}
		
		if (!managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(player.getName()))) {
			sender.sendMessage("§f[§cRedPoints§f] §7This player does not have an account.");
			return ReturnType.FAILURE;
		}
		
		sender.sendMessage(Methods.formatConfigText("commandAdd", "%amount%", String.valueOf(amount)));
		
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), amount);
		managerController.addAmount(cash);
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			List<String> players = new ArrayList<String>();
			players.add("all");
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				players.add(allPlayers.getName());
			}
			return players;
		}
		if (args.length == 2) {
			return Arrays.asList(new String[] {"1", "2", "3", "4", "5"});
		}
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.add";
	}

	@Override
	public String getSyntax() {
		return "/redpoints add (player) (amount)";
	}

	@Override
	public String getDescription() {
		return "Add a specific value for a player.";
	}
}
