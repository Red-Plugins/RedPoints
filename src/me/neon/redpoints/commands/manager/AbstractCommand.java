package me.neon.redpoints.commands.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {
	
	private final CommandType commandType;
	private final boolean hasArgs;
	private final List<String> _handledCommands = new ArrayList<>();
	
	protected AbstractCommand(CommandType type, String...command) {
		this._handledCommands.addAll(Arrays.asList(command));
		this.hasArgs = false;
		this.commandType = type;
	}
	
	protected AbstractCommand(CommandType type, boolean hasArgs, String... command) {
		this._handledCommands.addAll(Arrays.asList(command));
		this.hasArgs = hasArgs;
		this.commandType = type;
	}
	
	protected AbstractCommand(boolean noConsole, String... command) {
		this._handledCommands.addAll(Arrays.asList(command));
		this.hasArgs = false;
		this.commandType = noConsole ? CommandType.PLAYER_ONLY : CommandType.CONSOLE_OK;
	}
	
	protected AbstractCommand(boolean noConsole, boolean hasArgs, String... command) {
		this._handledCommands.addAll(Arrays.asList(command));
		this.hasArgs = false;
		this.commandType = noConsole ? CommandType.PLAYER_ONLY : CommandType.CONSOLE_OK;
	}
	
	public final List<String> getCommands() {
		return Collections.unmodifiableList(_handledCommands);
	}
	
	public final void addSubCommand(String command) {
		_handledCommands.add(command);
	}
	
	protected abstract ReturnType runCommand(CommandSender sender, String... args);
	
	protected abstract List<String> onTab(CommandSender sender, String... args);
	
	public abstract String getPermissionNode();
	
	public abstract String getSyntax();
	
	public abstract String getDescription();
	
	public boolean hasArgs() { return hasArgs; }
	
	public boolean isNoConsole() {
		return commandType == CommandType.PLAYER_ONLY;
	}
	
	public static enum ReturnType {SUCCESS, NEEDS_PLAYER, FAILURE, SYNTAX_ERROR};
	public static enum CommandType {PLAYER_ONLY, CONSOLE_OK}
}
