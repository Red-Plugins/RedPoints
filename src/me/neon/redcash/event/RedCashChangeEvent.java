package me.neon.redcash.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class RedCashChangeEvent extends RedCashEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	public RedCashChangeEvent(Player player, UUID uuid, int change) {
		super(player, uuid, change);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	  
	public HandlerList getHandlers() {
		return handlers;	
	}
}
