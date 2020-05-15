package me.neon.redpoints.listeners;

import java.util.UUID; 

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.utils.Cash;

public class JoinListener implements Listener {
	
	private ManagerController mc = RedPoints.getInstance().getModuleForClass(ManagerController.class);
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		String playerName = event.getPlayer().getName().toLowerCase();
		UUID uuid = RedPoints.getInstance().translateNameToUUID(playerName);
		if (mc.hasAccount(uuid)) return;
		Cash cash = new Cash(playerName, uuid, 0.0);
		mc.createAccount(cash);
	}
}
