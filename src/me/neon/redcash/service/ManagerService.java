package me.neon.redcash.service;

import java.util.UUID;

import me.neon.redcash.utils.Cash;

public interface ManagerService {
	
	void getTopList();
	boolean hasAccount(UUID uuid);
	void createAccount(Cash cash);
	void setAmount(Cash cash);
	void removeAmount(Cash cash);
	void addAmount(Cash cash);
	int getAmount(UUID uuid);
	void deleteAccount(UUID uuid);
}
