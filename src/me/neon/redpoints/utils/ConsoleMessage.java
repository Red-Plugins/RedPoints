package me.neon.redpoints.utils;

public class ConsoleMessage {
	
	public static void send(MessageLevel level, String message) {
		if (message == null || message.isEmpty()) return;
		switch (level) {
			case INFO:
				System.out.println("[RedPoints - INFO] " + message);
				break;
			case SUCCESS:
				System.out.println("[RedPoints - SUCCESS] " + message);
				break;
		}
	}
	
	public enum MessageLevel {INFO, SUCCESS}
}
