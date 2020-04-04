package me.neon.redcash.manager;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import me.neon.redcash.Cash;

public class Tasks {
	
	public static ArrayList<String> top = new ArrayList<String>();
	public static ArrayList<String> accounts = new ArrayList<String>();
	
	public void updateToplist() {
		new BukkitRunnable() {
			@Override
			public void run() {
				top.clear();
				new CashManager().updateTop(top);
			}
		}.runTaskTimer(Cash.getInstance(), 0L, 6000L);
	}
	
	public void updateAccountslist() {
		new BukkitRunnable() {
			@Override
			public void run() {
				accounts.clear();
				new CashManager().getAccounts(accounts);
			}
		}.runTaskTimer(Cash.getInstance(), 0L, 6000L);
	}
}
