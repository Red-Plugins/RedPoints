package me.neon.redcash.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command; 
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.Cash;
import me.neon.redcash.manager.CashManager;
import me.neon.redcash.manager.Messages;

public class CashCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(new Messages().getMessage("NoPlayer"));
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			player.sendMessage(new Messages().getMessage("CashMessage2").replace("%amount%", String.valueOf(new CashManager().getAmount(player.getName()))));
			return true;
		}
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("set")) {
				if (args.length < 3) {
					player.sendMessage(new Messages().getMessage("CashSetUsage"));
					return true;
				}
				if (!player.hasPermission(Cash.getInstance().config.getString("PermissionAdminCommand"))) {
					player.sendMessage(new Messages().getMessage("NoPermission"));
					return true;
				}
				Player target = Bukkit.getPlayerExact(args[1]);
				if (target == null) {
					player.sendMessage(new Messages().getMessage("NonExistentPlayer"));
					return true;
				}
				int amount;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					player.sendMessage(new Messages().getMessage("InvalidNumber"));
					return true;
				}
				new CashManager().setAmount(target.getName(), amount);
				player.sendMessage(new Messages().getMessage("CashSetMessage"));
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				if (args.length < 3) {
					player.sendMessage(new Messages().getMessage("CashRemoveUsage"));
					return true;
				}
				if (!player.hasPermission(Cash.getInstance().config.getString("PermissionAdminCommand"))) {
					player.sendMessage(new Messages().getMessage("NoPermission"));
					return true;
				}
				Player target = Bukkit.getPlayerExact(args[1]);
				if (target == null) {
					player.sendMessage(new Messages().getMessage("NonExistentPlayer"));
					return true;
				}
				int amount;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					player.sendMessage(new Messages().getMessage("InvalidNumber"));
					return true;
				}
				new CashManager().removeAmount(target.getName(), amount);
				player.sendMessage(new Messages().getMessage("CashRemoveMessage"));
			}
				
			if (args[0].equalsIgnoreCase("add")) {
				if (args.length < 3) {
					player.sendMessage(new Messages().getMessage("CashAddUsage"));
					return true;
				}
				if (!player.hasPermission(Cash.getInstance().config.getString("PermissionAdminCommand"))) {
					player.sendMessage(new Messages().getMessage("NoPermission"));
					return true;
				}
				Player target = Bukkit.getPlayerExact(args[1]);
				if (target == null) {
					player.sendMessage(new Messages().getMessage("NonExistentPlayer"));
					return true;
				}
				int amount;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					player.sendMessage(new Messages().getMessage("InvalidNumber"));
					return true;
				}
				new CashManager().addAmount(target.getName(), amount);
				player.sendMessage(new Messages().getMessage("CashAddMessage"));
			}
		}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help") || (args[0].equalsIgnoreCase("ajuda"))) {
					for (String message : new Messages().translateColor(Cash.getInstance().messages.getStringList("CashHelpMessage"))) {
						player.sendMessage(message);
					}
					return true;
				}
				Player target = Bukkit.getPlayerExact(args[0]);
				if (target == null) {
					player.sendMessage(new Messages().getMessage("NonExistentPlayer"));
					return true;
				}
				player.sendMessage(new Messages().getMessage("CashMessage").replace("%player%", target.getName()).replace("%amount%", String.valueOf(new CashManager().getAmount(target.getName()))));
			}
		return false;
	}

}
