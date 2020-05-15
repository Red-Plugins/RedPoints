package me.neon.redpoints.commands;

import java.util.List; 

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.commands.manager.AbstractCommand;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.inventory.ScrollerInventory;

public class CommandLeaderboard extends AbstractCommand {
	
	public CommandLeaderboard() {
		super(true, "leaderboard");
	} 

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		ManagerController managerController = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		 
		Player player = (Player) sender;
		new ScrollerInventory(managerController.leaderBoardList, "§cLeaderBoard", player);
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedPoints.command.leaderboard";
	}

	@Override
	public String getSyntax() {
		return "/redpoints leaderboard";
	}

	@Override
	public String getDescription() {
		return "See the list of best.";
	}
}
