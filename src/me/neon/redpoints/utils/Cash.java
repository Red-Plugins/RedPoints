package me.neon.redpoints.utils;

import java.util.UUID;

public class Cash {
	
	private UUID uuid;
	private double amount;
	private String name;
	
	public Cash(String name, UUID uuid, double amount) {
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
