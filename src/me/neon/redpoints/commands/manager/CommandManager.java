package me.neon.redpoints.commands.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager implements CommandExecutor, TabCompleter {

	private final JavaPlugin plugin;
	
	private final HashMap<String, SimpleNestedCommand> commands = new HashMap<String, SimpleNestedCommand>();
	private String message_noConsole = "§cYou must be a player to use this command.";
	private String message_noPerms = "§cYou are not allowed to do this.";
	private String message_noCommand = "§7The command you entered does not exist or is misspelled.";
	private List<String> message_syntaxError = Arrays.asList(
			"§cInvalid syntax.",
			"§7The correct syntax is: §c" + "%syntax%" + "§7."
			);
	
	private boolean allowLooseCommands = false;
	
	public CommandManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void setNoConsoleMessage(String message_noConsole) {
		this.message_noConsole = message_noConsole;
	}
	
	public void setNoPermsMessage(String message_noPerms) {
		this.message_noPerms = message_noPerms;
	}
	
	public void setNoCommandMessage(String message_noCommand) {
		this.message_noCommand = message_noCommand;
	}
	
	public void setSyntaxErrorMessage(List<String> message_syntaxError) {
		this.message_syntaxError = message_syntaxError;
	}
	
	public Set<String> getCommands() {
		return Collections.unmodifiableSet(commands.keySet());
	}
	
	public List<String> getSubCommands(String command) {
		SimpleNestedCommand nested = command == null ? null : commands.get(command.toLowerCase());
		return nested == null ? Collections.emptyList() : nested.children.keySet().stream().collect(Collectors.toList());
	}
	
	public Set<AbstractCommand> getAllCommands() {
		HashSet<AbstractCommand> all = new HashSet<AbstractCommand>();
		commands.values().stream()
			.filter(c -> c.parent != null && !all.contains(c.parent))
			.forEach(c -> {
				all.add(c.parent);
				c.children.values().stream()
				.filter(s -> !all.contains(s))
				.forEach(s -> all.add(s));
			});
		return all;
	}
	
	public CommandManager registerCommandDynamically(String command) {
		CommandManager.registerCommandDynamically(plugin, command, this, this);
		return this;
	}

	public SimpleNestedCommand registerCommandDynamically(AbstractCommand abstractCommand) {
        SimpleNestedCommand nested = new SimpleNestedCommand(abstractCommand);
        abstractCommand.getCommands().stream().forEach(cmd -> {
            CommandManager.registerCommandDynamically(plugin, cmd, this, this);
            commands.put(cmd.toLowerCase(), nested);
            PluginCommand pcmd = plugin.getCommand(cmd);
            if (pcmd != null) {
                pcmd.setExecutor(this);
                pcmd.setTabCompleter(this);
            } else {
                plugin.getLogger().warning("Failed to register command: /" + cmd);
            }
        });
        return nested;
    }

    public SimpleNestedCommand addCommand(AbstractCommand abstractCommand) {
        SimpleNestedCommand nested = new SimpleNestedCommand(abstractCommand);
        abstractCommand.getCommands().stream().forEach(cmd -> {
            commands.put(cmd.toLowerCase(), nested);
            PluginCommand pcmd = plugin.getCommand(cmd);
            if (pcmd != null) {
                pcmd.setExecutor(this);
                pcmd.setTabCompleter(this);
            } else {
                plugin.getLogger().warning("Failed to register command: /" + cmd);
            }
        });
        return nested;
    }

    public CommandManager addCommands(AbstractCommand... abstractCommands) {
        for (AbstractCommand abstractCommand : abstractCommands) {
            addCommand(abstractCommand);
        }
        return this;
    }

    public CommandManager setExecutor(String command) {
        PluginCommand pcmd = command == null ? null : plugin.getCommand(command);
        if (pcmd != null) {
            pcmd.setExecutor(this);
        } else {
            plugin.getLogger().warning("Failed to register command: /" + command);
        }
        return this;
    }

    public CommandManager setUseClosestCommand(boolean bool) {
        allowLooseCommands = bool;
        return this;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        SimpleNestedCommand nested = commands.get(command.getName().toLowerCase());
        if (nested != null) {
            if (args.length != 0 && !nested.children.isEmpty()) {
                String subCmd = getSubCommand(nested, args);
                if (subCmd != null) {
                    AbstractCommand sub = nested.children.get(subCmd);
                    int i = subCmd.indexOf(' ') == -1 ? 1 : 2;
                    String[] newArgs = new String[args.length - i];
                    System.arraycopy(args, i, newArgs, 0, newArgs.length);
                    processRequirements(sub, commandSender, newArgs);
                    return true;
                }
            }
            if (nested.parent != null) {
                processRequirements(nested.parent, commandSender, args);
                return true;
            }
        }
        commandSender.sendMessage(message_noCommand);
        return true;
    }

    private String getSubCommand(SimpleNestedCommand nested, String[] args) {
        String cmd = args[0].toLowerCase();
        if (nested.children.containsKey(cmd)) return cmd;
        String match = null;
        if (args.length >= 2 && nested.children.keySet().stream().anyMatch(k -> k.indexOf(' ') != -1)) {
            for (int len = args.length; len > 1; --len) {
                String cmd2 = String.join(" ", Arrays.copyOf(args, len)).toLowerCase();
                if (nested.children.containsKey(cmd2)) return cmd2;
            }
        }
        if (allowLooseCommands) {
            int count = 0;
            for (String c : nested.children.keySet()) {
                if (c.startsWith(cmd)) {
                    match = c;
                    if (++count > 1) {
                        match = null;
                        break;
                    }
                }
            }
        }
        return match;
    }

    private void processRequirements(AbstractCommand command, CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && command.isNoConsole()) {
            sender.sendMessage(message_noConsole);
            return;
        }
        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
            AbstractCommand.ReturnType returnType = command.runCommand(sender, args);
            if (returnType == AbstractCommand.ReturnType.NEEDS_PLAYER) {
                sender.sendMessage(message_noConsole);
            } else if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                for (String s : message_syntaxError) {
                    sender.sendMessage(s.replace("%syntax%", command.getSyntax()));
                }
            }
            return;
        }
        sender.sendMessage(message_noPerms);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        SimpleNestedCommand nested = commands.get(command.getName().toLowerCase());
        if (nested != null) {
            if (args.length == 0 || nested.children.isEmpty()) {
                return nested.parent != null ? nested.parent.onTab(sender, args) : null;
            }
            final boolean op = sender.isOp();
            final boolean console = !(sender instanceof Player);
            if (args.length == 1) {
                final String arg = args[0].toLowerCase();
                return nested.children.entrySet().stream()
                        .filter(e -> !console || !e.getValue().isNoConsole())
                        .filter(e -> e.getKey().startsWith(arg))
                        .filter(e -> op || e.getValue().getPermissionNode() == null || sender.hasPermission(e.getValue().getPermissionNode()))
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
            } else {
                String subCmd = getSubCommand(nested, args);
                AbstractCommand sub;
                if (subCmd != null && (sub = nested.children.get(subCmd)) != null
                        && (!console || !sub.isNoConsole())
                        && (op || sub.getPermissionNode() == null || sender.hasPermission(sub.getPermissionNode()))) {
                    int i = subCmd.indexOf(' ') == -1 ? 1 : 2;
                    String[] newArgs = new String[args.length - i];
                    System.arraycopy(args, i, newArgs, 0, newArgs.length);
                    return fetchList(sub, newArgs, sender);
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    private List<String> fetchList(AbstractCommand abstractCommand, String[] args, CommandSender sender) {
        List<String> list = abstractCommand.onTab(sender, args);
        if (args.length != 0) {
            String str = args[args.length - 1];
            if (list != null && str != null && str.length() >= 1) {
                try {
                    list.removeIf(s -> !s.toLowerCase().startsWith(str.toLowerCase()));
                } catch (UnsupportedOperationException ignored) {
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
	public static void registerCommandDynamically(Plugin plugin, String command, CommandExecutor executor, TabCompleter tabManager) {
        try {
            Class<?> classCraftServer = Bukkit.getServer().getClass();
            Field fieldCommandMap = classCraftServer.getDeclaredField("commandMap");
            fieldCommandMap.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) fieldCommandMap.get(Bukkit.getServer());
            Constructor<PluginCommand> constructorPluginCommand = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructorPluginCommand.setAccessible(true);
            PluginCommand commandObject = constructorPluginCommand.newInstance(command, plugin);
            commandObject.setExecutor(executor);
            commandObject.setTabCompleter(tabManager);
            Field fieldKnownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            fieldKnownCommands.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) fieldKnownCommands.get(commandMap);
            knownCommands.put(command, commandObject);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
