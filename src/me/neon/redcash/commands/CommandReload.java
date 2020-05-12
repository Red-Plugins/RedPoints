package me.neon.redcash.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.controllers.ConfigurationController;
import me.neon.redcash.utils.Methods;

public class CommandReload extends AbstractCommand {

	public CommandReload() {
		super(false, "reload");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		sender.sendMessage(Methods.formatConfigText("commandReload"));
		
		ConfigurationController config = RedCash.getInstance().getModuleForClass(ConfigurationController.class);
		config.getDefaultConfig().reloadConfiguration();
		config.getMessageConfig().reloadConfiguration();
		
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedCash.command.reload";
	}

	@Override
	public String getSyntax() {
		return "/cash reload";
	}

	@Override
	public String getDescription() {
		return "Recarregue todas as configurações.";
	}
}
