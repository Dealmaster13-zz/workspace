package uk.ac.cam.oda22.core.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oliver
 *
 */
public final class Log {
	
	private static List<Message> log = new ArrayList<Message>();
	
	public static void info(String text) {
		Message message = new InfoMessage(text);
		
		log.add(message);
		
		System.out.println("[INFO] " + text);
	}
	
	public static void warning(String text) {
		Message message = new WarningMessage(text);
		
		log.add(message);
		
		System.out.println("[WARNING] " + text);
	}
	
	public static void error(String text) {
		Message message = new ErrorMessage(text);
		
		log.add(message);
		
		System.out.println("[ERROR] " + text);
	}
	
	public static void debug(String text) {
		Message message = new DebugMessage(text);
		
		log.add(message);
		
		System.out.println("[DEBUG] " + text);
	}
	
}
