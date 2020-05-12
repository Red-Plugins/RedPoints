package me.neon.redcash.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.neon.redcash.RedCash;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.utils.Cash;

public class JoinListener implements Listener {
	
//	private String PLUGIN_URL = "https://spigotmc.org/resources/";
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		if (mc.hasAccount(RedCash.getInstance().translateNameToUUID(event.getPlayer().getName().toLowerCase()))) return;
		Cash cash = new Cash(event.getPlayer().getName(), RedCash.getInstance().translateNameToUUID(event.getPlayer().getName().toLowerCase()), 0);
		mc.createAccount(cash);
	}
}
