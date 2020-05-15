package me.neon.redpoints.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class RedPointsResetEvent extends RedPointsEvent {

	private static final HandlerList handlers = new HandlerList();
	
	public RedPointsResetEvent(Player player, UUID uuid, double change) {
		super(player, uuid, 0.0);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
}
