package group2.sdp.pc;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Debug {

	public static boolean DEBUG_LOGGING = true;
	public static boolean VISION_FILL_PIXELS = true;
	public static boolean VISION_DRAW_BOUNDS = true;
	
	public static void log(String message) {
		if (DEBUG_LOGGING) {
			System.out.println(message);
		}
	}
	
	public static void logf(String message, Object... args) {
		if (DEBUG_LOGGING) {
			System.out.printf(message+"\n", args);
		}
	}
	
	public static void drawPixel(boolean passesTest, BufferedImage dest, int x, int y, Color color) {
		if (passesTest) {
			dest.setRGB(x, y, color.getRGB());
		}
	}
	
}
