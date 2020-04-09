package me.neon.redcash.manager;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

import me.neon.redcash.Cash;

public class Messages {
	
	public String getMessage(String path) {
		return translateColor(Cash.getInstance().messages.getString(path));
	}
	
	public String translateColor(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public List<String> translateColor(List<String> strings) {
		return strings.stream().map(this::translateColor).collect(Collectors.toList());
	}
}
