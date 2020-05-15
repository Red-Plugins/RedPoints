package me.neon.redpoints.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.controllers.ConfigurationController;

public class Methods {
	
	public static class CustomDouble {
		
		public static double parseDouble(String amount) {
			double formatedAmount = 0.0;
			try {
				formatedAmount = Double.parseDouble(amount);
			} catch (NumberFormatException e) { }
			return formatedAmount;
		}
	}
	
	private static ConfigurationController config = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
	
	public static List<String> formatConfigTextList(String message) {
		if (message == null) return null;
		List<String> list = new ArrayList<String>();
		List<String> formattedMessage = config.getMessageConfig().getConfig().getStringList(message);
		for (String line : formattedMessage) {
			String message1 = ChatColor.translateAlternateColorCodes('&', formatText(line));
			list.add(message1);
		}
		return list;
	}
	
	public static boolean isStringList(String message) {
		if (config.getMessageConfig().getConfig().isList(message)) return true;
		return false;
	}
	
	public static String formatConfigText(String message) {
		if (message == null) return "";
		String formattedMessage = config.getMessageConfig().getString(message);
		return ChatColor.translateAlternateColorCodes('&', formatText(formattedMessage));
	}
	
	public static String formatConfigText(String message, String oldChar, String newChar) {
		if (message == null) return "";
		String formattedMessage = config.getMessageConfig().getString(message);
		return ChatColor.translateAlternateColorCodes('&', formatText(formattedMessage)).replace(oldChar, newChar);
	}
	
	public static String formatConfigText(String message, String oldChar, String newChar, String oldChar2, String newChar2) {
		if (message == null) return "";
		String formattedMessage = config.getMessageConfig().getString(message);
		return ChatColor.translateAlternateColorCodes('&', formatText(formattedMessage)).replace(oldChar, newChar).replace(oldChar2, newChar2);
	}
	
	public static String formatText(String text) {
        if (text == null || text.equals("")) return "";
        return formatText(text, false);
    }

	public static String formatText(String text, boolean cap) {
        if (text == null || text.equals("")) return "";
        if (cap) text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return ChatColor.translateAlternateColorCodes('&', text);
    }
	
	public static void cleanUp(PreparedStatement preparedStatement) {
		try {
			preparedStatement.close();
		} catch (Exception e) { }
	}
	
	public static void cleanUp(ResultSet resultSet, PreparedStatement preparedStatement) {
		try {
			resultSet.close();
		} catch (Exception e) { }
		try {
			preparedStatement.close();
		} catch (Exception e) { }
	}
}
