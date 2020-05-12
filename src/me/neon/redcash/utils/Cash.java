package me.neon.redcash.utils;

import java.util.UUID;

public class Cash {
	
	private UUID uuid;
	private int amount;
	private String name;
	
	public Cash(String name, UUID uuid, int amount) {
		this.setuuid(uuid);
		this.setAmount(amount);
		this.setName(name);
	}

	public UUID getuuid() {
		return uuid;
	}

	public void setuuid(UUID uuid) {
		this.uuid = uuid;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
