package me.neon.redcash.utils;

public class ConsoleMessage {
	
	public static void send(MessageLevel level, String message) {
		if (message == null || message.isEmpty()) return;
		switch (level) {
			case INFO:
				System.out.println("[INFO] " + message);
				break;
			case SUCCESS:
				System.out.println("[SUCCES] " + message);
				break;
		}
	}
	
	public enum MessageLevel {INFO, SUCCESS}
}
