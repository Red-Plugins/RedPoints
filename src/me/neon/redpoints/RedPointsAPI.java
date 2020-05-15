package me.neon.redpoints;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.utils.Cash;

public class RedPointsAPI {
	
	private static ManagerController managerController = RedPoints.getInstance().getModuleForClass(ManagerController.class);
	
	public static void createAccount(Player player, double initialAmount) {
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), initialAmount); 
		managerController.createAccount(cash);
	}
	
	public static void createAccount(String playerName, UUID uuid, double initialAmount) {
		Cash cash = new Cash(playerName.toLowerCase(), uuid, initialAmount); 
		managerController.createAccount(cash);
	}
	
	public static void deleteAccount(Player player) {
		managerController.deleteAccount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()));
	}
	
	public static void deleteAccount(UUID uuid) {
		managerController.deleteAccount(uuid);
	}
	
	public static void setAmount(Player player, double amount) {
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), amount);
		managerController.setAmount(cash);
	}
	
	public static void setAmount(String playerName, UUID uuid, double amount) {
		Cash cash = new Cash(playerName.toLowerCase(), uuid, amount);
		managerController.setAmount(cash);
	}
	
	public static void resetAccount(Player player) {
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), 0);
		managerController.resetAccount(cash);
	}
	
	public static void resetAccount(String playerName, UUID uuid) {
		Cash cash = new Cash(playerName.toLowerCase(), uuid, 0);
		managerController.resetAccount(cash);
	}
	
	public static void removeAmount(Player player, double amount) {
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), amount);
		managerController.removeAmount(cash);
	}
	
	public static void removeAmount(String playerName, UUID uuid, double amount) {
		Cash cash = new Cash(playerName.toLowerCase(), uuid, amount);
		managerController.removeAmount(cash);
	}
	
	public static void addAmount(Player player, double amount) {
		Cash cash = new Cash(player.getName().toLowerCase(), RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()), amount);
		managerController.addAmount(cash);
	}
	
	public static void addAmount(String playerName, UUID uuid, double amount) {
		Cash cash = new Cash(playerName.toLowerCase(), uuid, amount);
		managerController.addAmount(cash);
	}
	
	public static double getBalance(Player player) {
		return managerController.getAmount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()));
	}
	
	public static double getBalance(UUID uuid) {
		return managerController.getAmount(uuid);
	}
	
	public static boolean hasAccount(Player player) {
		return managerController.hasAccount(RedPoints.getInstance().translateNameToUUID(player.getName().toLowerCase()));
	}
	
	public static boolean hasAccount(UUID uuid) {
		return managerController.hasAccount(uuid);
	}
}
