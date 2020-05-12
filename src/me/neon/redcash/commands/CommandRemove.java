package me.neon.redcash.commands;

import java.util.ArrayList;  
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.controllers.BackupController;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.utils.Cash;
import me.neon.redcash.utils.Methods;
import me.neon.redcash.utils.Methods.CustomInteger;

public class CommandRemove extends AbstractCommand {
	
	public CommandRemove() {
		super(false, "remove");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		BackupController bc = RedCash.getInstance().getModuleForClass(BackupController.class);
		if (args.length < 1) return ReturnType.FAILURE;
		
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null && !args[0].trim().toLowerCase().equals("all")) return ReturnType.SYNTAX_ERROR;
		
		if (!bc.getTemporaryBackup()) {
			sender.sendMessage("§f[§cRedCash§f]§7 Backup em andamento, aguarde!");
			return ReturnType.FAILURE;
		}
		
		int amount = args.length == 2 ? CustomInteger.parseInt(args[1]) : 1;
		
		if (args[0].trim().toLowerCase().equals("all")) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (mc.hasAccount(RedCash.getInstance().translateNameToUUID(players.getName().toLowerCase()))) {
					Cash cash = new Cash(players.getName().toLowerCase(), RedCash.getInstance().translateNameToUUID(players.getName().toLowerCase()), amount);
					mc.removeAccount(cash);
				}
			}
			return ReturnType.SUCCESS;
		}
		
		if (!mc.hasAccount(RedCash.getInstance().translateNameToUUID(player.getName().toLowerCase()))) {
			sender.sendMessage("§f[§cRedCash§f] §7Este jogador não possuí uma conta.");
			return ReturnType.FAILURE;
		}
		
		if (mc.getAmount(RedCash.getInstance().translateNameToUUID(sender.getName().toLowerCase())) < amount) {
			sender.sendMessage(Methods.formatConfigText("commandRemoveError"));
			return ReturnType.FAILURE;
		}
		
		sender.sendMessage(Methods.formatConfigText("commandRemove", "%amount%", String.valueOf(amount)));
		
		Cash cash = new Cash(player.getName().toLowerCase(), RedCash.getInstance().translateNameToUUID(player.getName().toLowerCase()), amount);
		mc.removeAccount(cash);
		
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
		return "RedCash.command.remove";
	}

	@Override
	public String getSyntax() {
		return "/cash remove (jogador) (quantia)";
	}

	@Override
	public String getDescription() {
		return "Remova um valor específicado.";
	}
}
