package me.neon.redcash.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.utils.Methods;

public class CommandCash extends AbstractCommand {

	RedCash instance;
	
	public CommandCash() {
		super(false, "cash");
		instance = RedCash.getInstance();
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		sender.sendMessage("");
		sender.sendMessage("§7Versão " + instance.getDescription().getVersion() + " Criado por §c§lRedPlugins");
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
		return "/cash";
	}

	@Override
	public String getDescription() {
		return "Mostra todos os comandos disponíveis.";
	}
}
