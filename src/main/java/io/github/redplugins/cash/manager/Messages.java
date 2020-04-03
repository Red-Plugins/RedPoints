package io.github.redplugins.cash.manager;

import io.github.redplugins.cash.utils.Configuration;

public class Messages {
	
	public String getMessage(String path) {
		return Configuration.messages.getYaml().getString(path);
	}
}
