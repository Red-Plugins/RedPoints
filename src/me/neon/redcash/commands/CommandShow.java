package me.neon.redcash.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.utils.Cash;
import me.neon.redcash.utils.Methods;

public class CommandShow extends AbstractCommand {
	
	public CommandShow() {
		super(true, "show");
	}
	
	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		Player player = (Player) sender;
		
		if (!mc.hasAccount(RedCash.getInstance().translateNameToUUID(sender.getName().toLowerCase()))) {
			sender.sendMessage("§f[§cRedCash§f] §7Você não possuí uma conta, estamos criando uma para você.");
			Cash cash = new Cash(player.getName().toLowerCase(), RedCash.getInstance().translateNameToUUID(player.getName().toLowerCase()), 0);
			mc.createAccount(cash);
		}
		
		sender.sendMessage(Methods.formatConfigText("commandShow", "%amount%", String.valueOf(mc.getAmount(RedCash.getInstance().translateNameToUUID(player.getName().toLowerCase())))));
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedCash.command.show";
	}

	@Override
	public String getSyntax() {
		return "/cash show";
	}

	@Override
	public String getDescription() {
		return "Mostre o valor de cash.";
	}
}
