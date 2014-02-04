package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;

import java.awt.Color;
import java.awt.Graphics;
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
	
	public static void drawPixel(BufferedImage dest, int x, int y, Color color) {
		if (VISION_FILL_PIXELS) {
			dest.setRGB(x, y, color.getRGB());
		}
	}
	
	public static void drawTestPixel(BufferedImage dest, int x, int y, Color color) {
			dest.setRGB(x, y, color.getRGB());

	}
	
	public static void drawRect(BufferedImage dest, Rect rect, Color color) {
		if (VISION_DRAW_BOUNDS && rect != null) {
			Graphics g = dest.getGraphics();
			g.setColor(color);
			g.drawRect(rect.minX, rect.minY, rect.width, rect.height);
		}
	}
	
}
