package me.neon.redcash.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class RedCashResetEvent extends RedCashEvent {

	private static final HandlerList handlers = new HandlerList();
	
	public RedCashResetEvent(Player player, UUID uuid, int change) {
		super(player, uuid, 0);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
}
