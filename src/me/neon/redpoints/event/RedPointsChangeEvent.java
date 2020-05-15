package me.neon.redpoints.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class RedPointsChangeEvent extends RedPointsEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	public RedPointsChangeEvent(Player player, UUID uuid, double change) {
		super(player, uuid, change);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	  
	public HandlerList getHandlers() {
		return handlers;	
	}
}
