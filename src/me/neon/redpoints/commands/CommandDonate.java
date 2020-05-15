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

public class CommandDonate extends AbstractCommand {
	
	public CommandDonate() {
		super(true, "donate");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		if (args.length < 1) return ReturnType.FAILURE;
		
		ManagerController managerController = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		BackupController backupController = RedPoints.getInstance().getModuleForClass(BackupController.class);
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null || player == sender) return ReturnType.SYNTAX_ERROR;
		
		if (backupController.isTemporaryDisablingCommands()) {
			sender.sendMessage("§f[§cRedPoints§f]§7 Backup in progress, please wait!");
			return ReturnType.FAILURE;
		}
		
		double amount = args.length == 2 ? CustomDouble.parseDouble(args[1]) : 1;
		
		if (!managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(sender.getName().toLowerCase()))) {
			sender.sendMessage("§f[§cRedPoints§f] §7You dont't have an account, we are creating one for you.");
			Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), 0.0);
			managerController.createAccount(cash);
			return ReturnType.FAILURE;
		}
		
		if (managerController.getAmount(RedPoints.getInstance().translateNameToUUID(sender.getName().toLowerCase())) < amount) {
			sender.sendMessage(Methods.formatConfigText("commandDonateError"));
			return ReturnType.FAILURE;
		}
		
		Cash cashSender = new Cash(sender.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(sender.getName().toLowerCase()), amount);
		managerController.removeAmount(cashSender);
		
		Cash cashPlayer = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), amount);
		managerController.addAmount(cashPlayer);
		
		player.sendMessage(Methods.formatConfigText("commandDonateTarget", "%amount%", String.valueOf(amount), "%player%", player.getName()));
		sender.sendMessage(Methods.formatConfigText("commandDonate", "%amount%", String.valueOf(amount)));
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
		if (args.length == 2) {
			return Arrays.asList(new String[] {"1", "2", "3", "4", "5"});
		}
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.donate";
	}

	@Override
	public String getSyntax() {
		return "/redpoints donate (player) (amount)";
	}

	@Override
	public String getDescription() {
		return "Donate a specified amount.";
	}
}
