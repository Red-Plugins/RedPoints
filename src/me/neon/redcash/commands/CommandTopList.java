package me.neon.redcash.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.inventory.ScrollerInventory;

public class CommandTopList extends AbstractCommand {

	public CommandTopList() {
		super(true, "toplist");
	} 

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		Player player = (Player) sender;
		
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		
		new ScrollerInventory(mc.topList, "Top List", player);
		
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedCash.command.toplist";
	}

	@Override
	public String getSyntax() {
		return "/cash toplist";
	}

	@Override
	public String getDescription() {
		return "Veja a lista de melhores.";
	}
}
