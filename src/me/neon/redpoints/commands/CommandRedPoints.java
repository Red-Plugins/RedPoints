package me.neon.redpoints.commands;

import java.util.List; 

import org.bukkit.command.CommandSender;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.commands.manager.AbstractCommand;
import me.neon.redpoints.utils.Methods;

public class CommandRedPoints extends AbstractCommand {

	RedPoints instance;
	
	public CommandRedPoints() {
		super(false, "redpoints");
		instance = RedPoints.getInstance();
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		sender.sendMessage("");
		sender.sendMessage("§7Version " + instance.getDescription().getVersion() + " Created by §c§lRedPlugins");
		sender.sendMessage("");
		for (AbstractCommand command : instance.getCommandManager().getAllCommands()) {
			if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
				sender.sendMessage(Methods.formatText("&f - &c" + command.getSyntax() + "&f - §7" + command.getDescription()));
			}
		}
		sender.sendMessage("");
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return null;
	}

	@Override
	public String getSyntax() {
		return "/redpoints";
	}

	@Override
	public String getDescription() {
		return "Shows all available commands.";
	}
}
