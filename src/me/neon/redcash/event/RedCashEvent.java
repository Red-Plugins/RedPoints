package me.neon.redcash.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RedCashEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private UUID playerId;
	private int change;
	private boolean cancellled;
	
	public RedCashEvent(Player player, UUID uuid, int change) {
		this.player = player;
		this.playerId = uuid;
		this.setChange(change);
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}

	@Override
	public boolean isCancelled() {
		return this.cancellled;
	}

	@Override
	public void setCancelled(boolean cancellled) {
		this.cancellled = cancellled;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}
}
