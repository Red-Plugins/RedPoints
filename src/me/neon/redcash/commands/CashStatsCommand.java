package me.neon.redcash.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.Cash;
import me.neon.redcash.manager.CashManager;
import me.neon.redcash.manager.Messages;

public class CashStatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(new Messages().getMessage("NoPlayer"));
			return true;
		}
		Player player = (Player) sender;
		if (!player.hasPermission(Cash.getInstance().config.getString("PermissionAdminCommand"))) {
			player.sendMessage(new Messages().getMessage("NoPermission"));
			return true;
		}
		if (args.length >= 0) {
			for (String message : Cash.getInstance().messages.getStringList("CashStatsMessage")) {
				player.sendMessage(message.replace("%mysqlstats%", "Ativado").replace("%mysqlaccounts%", String.valueOf(new CashManager().getAccounts())));
			}
			return true;
		}
 		return false;
	}
}
