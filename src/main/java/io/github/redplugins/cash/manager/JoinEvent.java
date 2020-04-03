package io.github.redplugins.cash.manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (new CashManager().containsAccount(event.getPlayer().getName())) {
			new CashManager().createAccount(event.getPlayer().getName());
		}
	}
}
