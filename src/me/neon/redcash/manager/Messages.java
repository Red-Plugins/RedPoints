package me.neon.redcash.manager;

import me.neon.redcash.Cash;

public class Messages {
	
	public String getMessage(String path) {
		return Cash.getInstance().messages.getString(path);
	}
}
