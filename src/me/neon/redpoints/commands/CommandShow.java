package me.neon.redpoints.commands;

import java.util.List; 

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.commands.manager.AbstractCommand;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.utils.Cash;
import me.neon.redpoints.utils.Methods;

public class CommandShow extends AbstractCommand {
	
	public CommandShow() {
		super(true, "show");
	}
	
	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		ManagerController mc = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		Player player = (Player) sender;
		
		if (!mc.hasAccount(RedPoints.getInstance().translateNameToUUID(sender.getName().toLowerCase()))) {
			sender.sendMessage("§f[§cRedPoints§f] §7You don't have an account, we are creating one for you.");
			Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), 0);
			mc.createAccount(cash);
		}
		
		sender.sendMessage(Methods.formatConfigText("commandShow", "%amount%", String.valueOf(mc.getAmount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase())))));
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.show";
	}

	@Override
	public String getSyntax() {
		return "/redpoints show";
	}

	@Override
	public String getDescription() {
		return "Show you account value.";
	}
}
